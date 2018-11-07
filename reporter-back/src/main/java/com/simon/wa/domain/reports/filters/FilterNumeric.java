package com.simon.wa.domain.reports.filters;

import com.simon.wa.domain.apiobject.ApiObject;

public class FilterNumeric extends FilterDefinition {
	
	public FilterNumeric() {
		super();
	}
	
	public FilterNumeric(String fieldToCheck, NumComp comp, int lowerValue, int upperValue) {
		this();
		this.addInput("fieldToCheck", fieldToCheck);
		this.addInput("comp", comp);
		this.addInput("lowerValue", lowerValue);
		this.addInput("upperValue", upperValue);
	}
	
	@Override
	public boolean apply(ApiObject input) {
		int val;
		String fieldToCheck = (String) getValue("fieldToCheck");
		int lowerValue = (int) getValue("lowerValue");
		int upperValue = (int) getValue("upperValue");
		NumComp comp = (NumComp) getValue("comp");
		try {
			if (input.getValue(fieldToCheck, String.class)!=null && input.getValue(fieldToCheck, String.class).length()>0) {
				val = Integer.parseInt(input.getValue(fieldToCheck, String.class));
				if (comp.equals(NumComp.EQUAL))
					return val == lowerValue;
				else if (comp.equals(NumComp.GREATER_THAN))
					return val > lowerValue;
				else if (comp.equals(NumComp.LESS_THAN))
					return val < lowerValue;
				else if (comp.equals(NumComp.BETWEEN))
					return (val > lowerValue && val < upperValue);
				else throw new IllegalArgumentException("Can't parse comparison " + comp.toString());
			} else return false;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(input.getValue(fieldToCheck, String.class) + " isn't an integer");
		}
	}

	@Override
	public String grabDetails() {
		return new String("Checking " + getValue("fieldToCheck") + " with parameters (" + getValue("comp") + "," + getValue("upperValue") + "," + getValue("lowerValue") + ")");
	}

}
