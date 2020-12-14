package com.chandra.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chandra.monitoring.model.Tick;
import com.chandra.monitoring.service.TickService;

@RestController
public class TickController {

	@Autowired
	private TickService tickService;
	
	@RequestMapping(value = "/ticks", method = RequestMethod.POST)
	public ResponseEntity<Void> ticks(@RequestBody Tick tick) {
		boolean tickSaved = tickService.save(tick);
		if (tickSaved) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}


}
