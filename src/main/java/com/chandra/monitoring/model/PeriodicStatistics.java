package com.chandra.monitoring.model;

public class PeriodicStatistics {

	private long timestamp;
	private double avg;
	private double max;
	private double min;
	private long count;
	private double sum;
	
	
	public PeriodicStatistics(long timestamp, double avg, double max, double min, long count, double sum) {
		super();
		this.timestamp = timestamp;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
		this.sum = sum;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "PeriodicStatistics [timestamp=" + timestamp + ", avg=" + avg + ", max=" + max + ", min=" + min
				+ ", count=" + count + ", sum=" + sum + "]";
	}

	

}
