package com.simon.wa.domain.reports.columns;

import java.util.List;

import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;

public class ColumnCombine extends ColumnDefinition implements ReportColumn {

	public ColumnCombine() {
		super();
	}
	
	public ColumnCombine(String colName, boolean key, ColOutput outputType, List<String> inputFieldNames,String delim) {
		super(colName, key, outputType);
		this.setColumnType("combine");
		this.addInput("field", inputFieldNames);
		this.addInput("delim", delim);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object generateValue(ApiObject input) {
		try {
			StringBuilder sb = new StringBuilder();
			String delim = (String)this.getInputs().get("delim");
			for (String field : (List<String>)this.getInputs().get("field")) {
				sb.append(input.getValue(field, String.class) + delim);
			}
			int maxLen = Math.min(delim.length(),sb.length());
			return sb.toString().substring(0, sb.length() - maxLen);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

}
