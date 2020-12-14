package com.chandra.monitoring.service;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chandra.monitoring.model.Tick;
import com.chandra.monitoring.service.exception.MonitoringServiceException;

@Service
public class TickService {
	
    @Autowired
    private ServiceProxy serviceProxy;
    
    private static final Logger logger = LoggerFactory.getLogger(TickService.class);
    
    public boolean save(Tick tick) {
    	boolean response = true;
    	try {
			validate(tick);
			logger.info("Current timestamp: "+ ZonedDateTime.now().toInstant().toEpochMilli());
	    	response = serviceProxy.saveTick(tick);
	    	
		} catch (MonitoringServiceException e) {
			logger.error(e.getMessage());
			response = false;
		}    	
    	return response;
    }

	/*
	 * validate if stock ticker is a valid string.
	 */
	private void validate(Tick tick) throws MonitoringServiceException {		

        if(null == tick) {
        	logger.error("Tick is null");
            throw new MonitoringServiceException("Tick is null");
        } if(null == tick.getTimestamp()) {
        	logger.error("Timestamp is null");
            throw new MonitoringServiceException("Timestamp is null");
        } else if(tick.getPrice() == null || 0 > tick.getPrice()){
        	logger.error("Tick price is incorrect");
            throw new MonitoringServiceException("Tick price is incorrect");
        } 
	}
}
