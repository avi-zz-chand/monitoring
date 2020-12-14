package com.chandra.monitoring.service;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.chandra.monitoring.model.Statistics;
import com.chandra.monitoring.model.Tick;


@SpringBootTest
public class TickServiceTest {

    @Autowired
    private TickService tickService;
    
    @Autowired
    private StatisticsService statsService;

    //Tick(String instrument, double price, long timestamp)
    
    @Test
    public void testWhenTimestampGreaterThanWindowSize() {
    	long timpStamp = ZonedDateTime.now().toInstant().toEpochMilli() - 61000;
        boolean response = tickService.save(new Tick("ABC", 120.0, timpStamp));
        Assert.isTrue(BooleanUtils.isFalse(response), "Tick is older than 60 secs");
    }
    
    @Test
    public void testWhenTimestampWithinWindowSize() {
    	long timpStamp = ZonedDateTime.now().toInstant().toEpochMilli() - 50000;
        boolean response = tickService.save(new Tick("CDE", 120.0, timpStamp));
        Assert.isTrue(response, "Tick is older than 60 secs");
    }

    @Test
    public void testStatsWithMultipleTicks() {
    	
        tickService.save(new Tick("IBM.N", 123.0, ZonedDateTime.now().toInstant().toEpochMilli() - 40000));
        tickService.save(new Tick("IBM.N", 122.0, ZonedDateTime.now().toInstant().toEpochMilli() - 50000));
        tickService.save(new Tick("IBM.N", 121.0, ZonedDateTime.now().toInstant().toEpochMilli() - 61000));
        tickService.save(new Tick("IBM.N", 120.0, ZonedDateTime.now().toInstant().toEpochMilli() - 70000));
        
        Statistics results = statsService.getInstrumentStatistics("IBM.N");
        System.out.println(results);
        Assert.isTrue(results.getCount() == 2, "Two ticks are older than 60 secs");

    }
    
    @Test
    public void testStatsWithMultipleInstrumentTicks() {
    	
        tickService.save(new Tick("GOOG", 123.0, ZonedDateTime.now().toInstant().toEpochMilli() - 40000));
        tickService.save(new Tick("GOOG", 122.0, ZonedDateTime.now().toInstant().toEpochMilli() - 50000));
        tickService.save(new Tick("GOOG", 121.0, ZonedDateTime.now().toInstant().toEpochMilli() - 61000));
        tickService.save(new Tick("GOOG", 120.0, ZonedDateTime.now().toInstant().toEpochMilli() - 70000));
        tickService.save(new Tick("AMZN", 123.0, ZonedDateTime.now().toInstant().toEpochMilli() - 40000));
        tickService.save(new Tick("AMZN", 122.0, ZonedDateTime.now().toInstant().toEpochMilli() - 50000));
        
        Statistics resultsIBM = statsService.getInstrumentStatistics("GOOG");
        Statistics resultsAMZN = statsService.getInstrumentStatistics("AMZN");

        Assert.isTrue(resultsIBM.getCount() == resultsAMZN.getCount(), "Two tics are older than 60 secs");

    }
    
    @Test
    public void testAggregateStatsWithMultipleInstrumentTicks() {
    	
        tickService.save(new Tick("GOOG", 123.0, ZonedDateTime.now().toInstant().toEpochMilli() - 40000));
        tickService.save(new Tick("GOOG", 122.0, ZonedDateTime.now().toInstant().toEpochMilli() - 50000));
        tickService.save(new Tick("GOOG", 121.0, ZonedDateTime.now().toInstant().toEpochMilli() - 61000));
        tickService.save(new Tick("GOOG", 120.0, ZonedDateTime.now().toInstant().toEpochMilli() - 70000));
        tickService.save(new Tick("AMZN", 123.0, ZonedDateTime.now().toInstant().toEpochMilli() - 40000));
        tickService.save(new Tick("AMZN", 122.0, ZonedDateTime.now().toInstant().toEpochMilli() - 50000));
        
        Statistics results = statsService.getInstrumentStatistics(null);
        Assert.isTrue(results.getCount() == 4, "Two tics are older than 60 secs");

    }

 }