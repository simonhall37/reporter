package com.simon.wa.domain.reports;

import java.util.ArrayList;
import java.util.List;

import com.simon.wa.domain.apiobject.ApiObject;

public class ColumnMetadata {

	private List<ReportColumn> columns;
	
	public ColumnMetadata() {
		this.setColumns(new ArrayList<>());
	}
	
	public Object[] getValues(ApiObject input) {
		Object[] out = new Object[columns.size()];
		int index=0;
		for (ReportColumn col : this.columns) {
			out[index++] = col.outputValue(input);
		}
		return out;
	}

	public List<ReportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ReportColumn> columns) {
		this.columns = columns;
	}
	
}
