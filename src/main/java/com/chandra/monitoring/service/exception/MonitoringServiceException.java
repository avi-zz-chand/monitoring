package com.chandra.monitoring.service.exception;

public class MonitoringServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public MonitoringServiceException(String errorMessage) {
		super(errorMessage);
	}
}
