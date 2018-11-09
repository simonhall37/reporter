package com.simon.wa.domain.reports.columns;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.simon.wa.domain.apiobject.ApiObject;

@Entity
@Table(name="col_combine")
public class ColumnCombine extends ColumnDefinition {

	public ColumnCombine() {
		super();
	}
	
	public ColumnCombine(String colName, boolean key, ColOutput outputType, List<String> inputFieldNames,String delim) {
		super(colName, key, outputType);
		this.setColumnType("combine");
		String fields = Arrays.toString(inputFieldNames.toArray());
		fields = fields.substring(Math.min(fields.length(), 1), Math.max(0, fields.length()-1));
		this.addInput("field", fields);
		this.addInput("delim", delim);
	}

	@Override
	public Object generateValue(ApiObject input) {
		try {
			StringBuilder sb = new StringBuilder();
			String delim = (String)getValue("delim");
			String[] in = getValue("field").split(",");
			List<String> fields = Arrays.asList(in);
			
			for (String field : fields) {
				sb.append(input.getValue(field.trim(), String.class) + delim);
			}
			int maxLen = Math.min(delim.length(),sb.length());
			return sb.toString().substring(0, sb.length() - maxLen);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

}
