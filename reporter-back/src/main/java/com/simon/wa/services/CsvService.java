package com.simon.wa.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simon.wa.domain.Lookup;

@Service
public class CsvService {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CsvService.class);
	
	public Lookup lookupFromString(String name, String input) {
		Lookup out = new Lookup();
		out.setName(name);
		String[] lines = input.split(System.getProperty("line.separator"));
		for (String line : lines) {
			String[] cols = line.split(",");
			out.addValue(cols);
		}
		
		return out;
	}
	
}
