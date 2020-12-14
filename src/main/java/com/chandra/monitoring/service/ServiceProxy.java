package com.chandra.monitoring.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.chandra.monitoring.model.PeriodicStatistics;
import com.chandra.monitoring.model.Statistics;
import com.chandra.monitoring.model.Tick;

@Component
public class ServiceProxy {

	private static final int MONITORING_WINDOW = 60000;

	// collections that maintains aggregate stats
	private static Map<Integer, Map<String, PeriodicStatistics>> statsCircularBuckets = new ConcurrentHashMap<>();

	/**
	 * Method to store tick in concurrent collection.
	 * @param tick
	 * @return
	 */

	public boolean saveTick(Tick tick) {

		long currentTime = System.currentTimeMillis();
		if (currentTime - tick.getTimestamp() > MONITORING_WINDOW) { // if tick is older than 60 seconds
			return false;
		}

		int second = LocalDateTime.ofInstant(Instant.ofEpochMilli(tick.getTimestamp()), ZoneId.systemDefault())
				.getSecond();

		statsCircularBuckets.compute(second, (secondKey, perSecondStatistics) -> {

			if (null == perSecondStatistics) {
				perSecondStatistics = new ConcurrentHashMap<String, PeriodicStatistics>();
				perSecondStatistics.put(tick.getInstrument(), new PeriodicStatistics(tick.getTimestamp(),tick.getPrice(), tick.getPrice(), tick.getPrice(), 1, tick.getPrice()));
				return perSecondStatistics;

			} else if (null == perSecondStatistics.get(tick.getInstrument())) {
				perSecondStatistics.put(tick.getInstrument(), new PeriodicStatistics(tick.getTimestamp(),tick.getPrice(), tick.getPrice(), tick.getPrice(), 1, tick.getPrice()));
				return perSecondStatistics;

			} else if (perSecondStatistics.get(tick.getInstrument()) != null
					&& currentTime - perSecondStatistics.get(tick.getInstrument()).getTimestamp() > MONITORING_WINDOW) {
				perSecondStatistics.put(tick.getInstrument(), new PeriodicStatistics(tick.getTimestamp(),tick.getPrice(), tick.getPrice(), tick.getPrice(), 1, tick.getPrice()));
				return perSecondStatistics;

			} else {
				PeriodicStatistics tickerStats = perSecondStatistics.get(tick.getInstrument());
				tickerStats.setSum(tickerStats.getSum() + tick.getPrice());
				if (tick.getPrice() > tickerStats.getMax())
					tickerStats.setMax(tick.getPrice());
				if (tick.getPrice() < tickerStats.getMin())
					tickerStats.setMin(tick.getPrice());
				tickerStats.setCount(tickerStats.getCount() + 1);
				return perSecondStatistics;
			}

		});

		return true;
	}

	/**
	 * Method to calculate aggregate statistics for a specific instrument.
	 * 
	 * @param ticker
	 * @return PeriodicStatistics
	 */

	private PeriodicStatistics getInstrumentAggregates(String ticker) {

		long currentTime = System.currentTimeMillis();
		PeriodicStatistics results = null;

		for (Map.Entry<Integer, Map<String, PeriodicStatistics>> second : statsCircularBuckets.entrySet()) {

			Map<String, PeriodicStatistics> tickerStatsMap = second.getValue();
			PeriodicStatistics tickerStats = tickerStatsMap.get(ticker);

			if (tickerStats != null && (currentTime - tickerStats.getTimestamp() <= MONITORING_WINDOW)) {

				if (results != null) {
					results.setTimestamp(tickerStats.getTimestamp());
					results.setSum(results.getSum() + tickerStats.getSum());
					if (tickerStats.getMax() > results.getMax())
						results.setMax(tickerStats.getMax());
					if (tickerStats.getMin() < results.getMin())
						results.setMin(tickerStats.getMin());
					results.setCount(results.getCount() + tickerStats.getCount());
					results.setAvg(results.getSum() / results.getCount());
				} else {
					results = new PeriodicStatistics(tickerStats.getTimestamp(), tickerStats.getAvg(), tickerStats.getMax(),
							tickerStats.getMin(), tickerStats.getCount(), tickerStats.getSum());
				}
			}
		}

		return results != null ? results : new PeriodicStatistics(currentTime, 0, 0, 0, 0, 0);

	}

	/**
	 * Method to calculate aggregate stats across all the entries in bucket falling
	 * within 60 secs window.
	 * 
	 * @return
	 */

	private PeriodicStatistics getAggregates() {

		long currentTime = System.currentTimeMillis();
		PeriodicStatistics results = null;

		for (Map.Entry<Integer, Map<String, PeriodicStatistics>> second : statsCircularBuckets.entrySet()) {

			Map<String, PeriodicStatistics> tickerStatsMap = second.getValue();

			for (Map.Entry<String, PeriodicStatistics> instrument : tickerStatsMap.entrySet()) {

				PeriodicStatistics tickerStats = instrument.getValue();

				if (currentTime - tickerStats.getTimestamp() <= MONITORING_WINDOW) {

					if (results != null) {
						results.setTimestamp(tickerStats.getTimestamp());
						results.setSum(results.getSum() + tickerStats.getSum());
						if (tickerStats.getMax() > results.getMax())
							results.setMax(tickerStats.getMax());
						if (tickerStats.getMin() < results.getMin())
							results.setMin(tickerStats.getMin());
						results.setCount(results.getCount() + tickerStats.getCount());
						results.setAvg(results.getSum() / results.getCount());
					} else {

						results = new PeriodicStatistics(tickerStats.getTimestamp(), tickerStats.getAvg(), tickerStats.getMax(),
								tickerStats.getMin(), tickerStats.getCount(), tickerStats.getSum());
					}

				}
			}

		}

		return results != null ? results : new PeriodicStatistics(currentTime, 0, 0, 0, 0, 0);

	}

	/**
	 * Compute and get tick statistics.
	 * 
	 * @param instrument
	 * @return
	 */

	public Statistics getStatistics(String instrument) {
		PeriodicStatistics tickStats = null;
		Statistics tickStatistics = null;

		if (instrument != null) {
			tickStats = getInstrumentAggregates(instrument);

		} else {
			tickStats = getAggregates();
		}

		if (null == tickStats || System.currentTimeMillis() - tickStats.getTimestamp() > MONITORING_WINDOW) {
			// tick statistics outside monitoring window
			return new Statistics(0, 0, 0, 0);
		} else {
			tickStatistics = new Statistics();
			tickStatistics.setAvg(tickStats.getAvg());
			tickStatistics.setMin(tickStats.getMin());
			tickStatistics.setMax(tickStats.getMax());
			tickStatistics.setCount(tickStats.getCount());
		}

		return tickStatistics;

	}

}
