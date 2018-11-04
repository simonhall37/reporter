package com.simon.wa.domain.reports.columns;

import com.simon.wa.domain.apiobject.ApiObject;

public class ColumnSimpleValue extends ColumnDefinition {

	public ColumnSimpleValue() {
		super();
	}
	
	public ColumnSimpleValue(String colName, boolean key, ColOutput outputType, String inputFieldName) {
		super(colName, key, outputType);
		this.setColumnType("simple");
		this.addInput("field", inputFieldName);
	}

	@Override
	public Object generateValue(ApiObject input) {
		try {
			return input.getValue((String) this.getInputs().get("field"), String.class);
		} catch (NullPointerException e) {
			System.out.println("Couldn't find field " + this.getInputs().get("inputFieldName"));
			return "";
		}
	}

}
