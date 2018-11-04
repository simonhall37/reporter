package com.simon.wa.domain.reports.columns;

import java.util.ArrayList;
import java.util.List;

import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;
import com.simon.wa.domain.reports.ReportRow;
import com.simon.wa.services.LookupRepository;

public class ColumnMetadata {

	private List<ReportColumn> columns;
	
	public ColumnMetadata() {
		this.setColumns(new ArrayList<>());
	}
	
	public int numValueCols() {
		int out = 0;
		for (ReportColumn col : this.columns) {
			if (!col.isKey()) out++;
		}
		return out;
	}
	
	public void init(LookupRepository lookupRepo) {
		for (ReportColumn col : this.columns) {
			if (col instanceof ColumnLookup) {
				System.out.println("Must set lookup");
				((ColumnLookup)col).addLookup(lookupRepo);
			}
		}
	}
	
	public ReportRow returnValues(ApiObject input) {
		ReportRow row = new ReportRow(this);
		for (ReportColumn col : this.columns) {
			try{
				row.addValue(col.outputValue(input),col.isKey());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		return row;
	}

	public List<ReportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ReportColumn> columns) {
		this.columns = columns;
	}
	
}
