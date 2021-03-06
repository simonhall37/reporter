package com.simon.wa.domain.reports.columns;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.simon.wa.domain.apiobject.ApiObject;

@Entity
@Table(name="col_simple")
public class ColumnSimpleValue extends ColumnDefinition {

	public ColumnSimpleValue() {
		super();
	}
	
	public ColumnSimpleValue(String colName, boolean key, ColOutput outputType, int colNum, String inputFieldName) {
		super(colName, key, outputType,colNum);
		this.setColumnType("simple");
		this.addInput("field", inputFieldName);
	}

	@Override
	public Object generateValue(ApiObject input) {
		try {
			return input.getValue((String) getValue("field"), String.class);
		} catch (NullPointerException e) {
			return "";
		}
	}

}
