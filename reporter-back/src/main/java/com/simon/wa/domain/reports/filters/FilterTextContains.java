package com.simon.wa.domain.reports.filters;

import com.simon.wa.domain.apiobject.ApiObject;

public class FilterTextContains implements Filterable {

	private final String shouldContain;
	private final String fieldToCheck;
	private final boolean ignoreCase;

	public FilterTextContains(String shouldContain, String fieldToCheck, boolean ignoreCase) {
		this.shouldContain = shouldContain;
		this.fieldToCheck = fieldToCheck;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean apply(ApiObject input) {
		if (ignoreCase) 
			return input.getValue(fieldToCheck, String.class).contains(shouldContain);
		else return input.getValue(fieldToCheck, String.class).toLowerCase().contains(shouldContain.toLowerCase());
	}

	@Override
	public String getDetails() {
		return this.getClass().getSimpleName() + ": " + this.shouldContain + " and " + this.fieldToCheck;
	}
	
	public String getShouldContain() {
		return shouldContain;
	}

	public String getFieldToCheck() {
		return fieldToCheck;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

}
