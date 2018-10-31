package com.simon.wa.domain.reports.columns;

import com.simon.wa.domain.apiobject.ApiObject;

public class ColumnSimpleValue extends ColumnDefinition {

	public ColumnSimpleValue(String colName, boolean key, ColOutput outputType,String inputFieldName) {
		super(colName,key,outputType);
		this.setType("SIMPLE_VALUE");
		this.addInput("field", inputFieldName);
	}
	
	@Override
	public Object generateValue(ApiObject input) {
		try{
			return input.getValue((String)this.getInputs().get("field"), String.class);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

}
