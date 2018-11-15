package com.simon.wa.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	
	public String toCsvLine(Object[] input) {
		StringBuilder sb = new StringBuilder();
		
		for (Object o : input) {
			if (o instanceof Double) {
				sb.append(round((double)o,2));
			} else if (o instanceof Integer) {
				sb.append(o);
			} else if (o instanceof String) {
				sb.append("\""+ ((String)o).replaceAll("\"", "").trim() + "\"");
			} else throw new IllegalArgumentException("Don't recognise " + o + " (" + o.getClass().getSimpleName() + ")");
			sb.append(",");
		}
		
		return sb.toString().substring(0,Math.max(0, sb.length()-1));
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException("Can't round double to " + places);
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}
