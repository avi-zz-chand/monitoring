package com.chandra.monitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chandra.monitoring.model.Statistics;

@Service
public class StatisticsService {
	
	@Autowired
    private ServiceProxy serviceProxy;
	
	public Statistics getInstrumentStatistics(String instrument) {
		
		return serviceProxy.getStatistics(instrument);
		
	}
	

}
