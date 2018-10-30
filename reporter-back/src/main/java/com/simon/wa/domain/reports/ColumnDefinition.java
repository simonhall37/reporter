package com.simon.wa.domain.reports;

import java.util.HashMap;
import java.util.Map;

import com.simon.wa.domain.apiobject.ApiObject;

public abstract class ColumnDefinition implements ReportColumn {

	private String type;
	private String colName;
	private boolean key;
	private Map<String,Object> inputs;
	private ColOutput outputType;
	
	public ColumnDefinition () {
		this.setInputs(new HashMap<>());
	}
	
	public ColumnDefinition(String colName,boolean key, ColOutput outputType) {
		this();
		this.setColName(colName);
		this.setKey(key);
		this.setOutputType(outputType);
	}
	
	public void addInput(String key, Object value) {
		this.inputs.put(key, value);
	}
	
	public abstract Object generateValue(ApiObject input);
	
	@Override
	public Object outputValue(ApiObject input) {
		Object initial = generateValue(input);
		if (this.outputType.equals(ColOutput.STRING))
			return (String)initial;
		else if (this.outputType.equals(ColOutput.INTEGER))
			return (Integer)initial;
		else if (this.outputType.equals(ColOutput.DOUBLE))
			return (Double)initial;
		return null;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String,Object> getInputs() {
		return inputs;
	}

	public void setInputs(Map<String,Object> inputs) {
		this.inputs = inputs;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public ColOutput getOutputType() {
		return outputType;
	}

	public void setOutputType(ColOutput outputType) {
		this.outputType = outputType;
	} 

}
