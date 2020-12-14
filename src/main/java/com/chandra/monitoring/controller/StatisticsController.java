package com.chandra.monitoring.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chandra.monitoring.model.Statistics;
import com.chandra.monitoring.service.StatisticsService;

@RestController
public class StatisticsController {
	
	@Autowired
	private StatisticsService statsService;

	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
	
	@RequestMapping(value = {"/statistics", "/statistics/{instrument_identifier}"})
	public Statistics statistics(@PathVariable Map<String, String> instrument) {
		
		String instrument_identifier = instrument.get("instrument_identifier");
		logger.info(" Get statistics for instrument :"+instrument_identifier);
		
		return statsService.getInstrumentStatistics(instrument_identifier);
	}

}