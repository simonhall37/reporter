package com.simon.wa.domain.reports.columns;

import java.util.HashMap;
import java.util.Map;

import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;

public abstract class ColumnDefinition implements ReportColumn {

	private String columnType;
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
		if (initial instanceof java.lang.String) {
			if (this.outputType.equals(ColOutput.STRING))
				return initial;
			else if (this.outputType.equals(ColOutput.INTEGER))
				return Integer.valueOf((String)initial);
			else if (this.outputType.equals(ColOutput.DOUBLE))
				return Double.valueOf((String)initial);
		}
		else if (initial instanceof java.lang.Integer) {
			if (this.outputType.equals(ColOutput.STRING))
				return String.valueOf(initial);
			else if (this.outputType.equals(ColOutput.INTEGER))
				return initial;
			else if (this.outputType.equals(ColOutput.DOUBLE))
				return ((Integer)initial).doubleValue();
		}
		else if (initial instanceof java.lang.Double) {
			if (this.outputType.equals(ColOutput.STRING))
				return String.valueOf(initial);
			else if (this.outputType.equals(ColOutput.INTEGER))
				return ((Double)initial).intValue();
			else if (this.outputType.equals(ColOutput.DOUBLE))
				return initial;
		}
		return null;
	}


	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String type) {
		this.columnType = type;
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
