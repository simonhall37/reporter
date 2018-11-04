package com.simon.wa.domain.reports.filters;

import com.simon.wa.domain.apiobject.ApiObject;

public class FilterTextContains implements Filterable {

	private String shouldContain;
	private String fieldToCheck;
	private boolean ignoreCase;

	public FilterTextContains() {

	}
	
	public FilterTextContains(String shouldContain, String fieldToCheck, boolean ignoreCase) {
		this.shouldContain = shouldContain;
		this.fieldToCheck = fieldToCheck;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean apply(ApiObject input) {
		try {
			if (ignoreCase) 
				return input.getValue(fieldToCheck, String.class).contains(shouldContain);
			else return input.getValue(fieldToCheck, String.class).toLowerCase().contains(shouldContain.toLowerCase());
		} catch (NullPointerException e) {
			System.out.println("Filter contains couldn't find field " + this.fieldToCheck);
			return false;
		}
		
	}

	@Override
	public String grabDetails() {
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
