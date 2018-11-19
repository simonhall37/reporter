package com.simon.wa.domain.reports.filters;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.simon.wa.domain.apiobject.ApiObject;

@Entity
@Table(name="filter_contains")
public class FilterTextContains extends FilterDefinition {

	public FilterTextContains() {
		super();
	}
	
	public FilterTextContains(String shouldContain, String fieldToCheck, boolean ignoreCase) {
		this.addInput("shouldContain", shouldContain);
		this.addInput("fieldToCheck", fieldToCheck);
		this.addInput("ignoreCase", String.valueOf(ignoreCase));
	}

	@Override
	public boolean apply(ApiObject input) {
		String shouldContain = (String) getValue("shouldContain");
		String fieldToCheck = (String) getValue("fieldToCheck");
		Boolean ignoreCase = Boolean.parseBoolean(getValue("ignoreCase"));
		try {
			if (ignoreCase) 
				return input.getValue(fieldToCheck, String.class).contains(shouldContain);
			else return input.getValue(fieldToCheck, String.class).toLowerCase().contains(shouldContain.toLowerCase());
		} catch (NullPointerException e) {
			return false;
		}
		
	}

	@Override
	public String grabDetails() {
		return this.getClass().getSimpleName() + ": " + getValue("shouldContain") + " and " + getValue("fieldToCheck");
	}
	
}
