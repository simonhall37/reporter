package com.simon.wa.domain.reports;

import com.simon.wa.domain.apiobject.ApiObject;

public class ColumnSimpleValue extends ColumnDefinition {

	public ColumnSimpleValue(String colName, boolean key, ColOutput outputType,String inputFieldName) {
		super(colName,key,outputType);
		this.setType("SIMPLE_VALUE");
		this.addInput("field", inputFieldName);
	}
	
	@Override
	public Object generateValue(ApiObject input) {
		return input.getValue((String)this.getInputs().get("field"), Object.class);
	}

}
