package com.simon.wa.domain.reports.filters;

import com.simon.wa.domain.apiobject.ApiObject;

public class FilterNumeric implements Filterable {

	private final String fieldToCheck;
	private final NumComp comp;
	private final int lowerValue;
	private final int upperValue;
	
	public FilterNumeric(String fieldToCheck, NumComp comp, int lowerValue, int upperValue) {
		this.fieldToCheck = fieldToCheck;
		this.comp = comp;
		this.lowerValue = lowerValue;
		this.upperValue  = upperValue;
	}
	
	@Override
	public boolean apply(ApiObject input) {
		int val;
		try {
			if (input.getValue(fieldToCheck, String.class)!=null && input.getValue(fieldToCheck, String.class).length()>0) {
				val = Integer.parseInt(input.getValue(fieldToCheck, String.class));
				if (comp.equals(NumComp.EQUAL))
					return val == this.lowerValue;
				else if (comp.equals(NumComp.GREATER_THAN))
					return val > this.lowerValue;
				else if (comp.equals(NumComp.LESS_THAN))
					return val < this.lowerValue;
				else if (comp.equals(NumComp.BETWEEN))
					return (val > this.lowerValue && val < this.upperValue);
				else throw new IllegalArgumentException("Can't parse comparison " + this.comp.toString());
			} else return false;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(input.getValue(fieldToCheck, String.class) + " isn't an integer");
		}
	}

	@Override
	public String getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFieldToCheck() {
		return fieldToCheck;
	}

	public NumComp getComp() {
		return comp;
	}

}
