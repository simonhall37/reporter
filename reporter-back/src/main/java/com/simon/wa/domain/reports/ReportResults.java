package com.simon.wa.domain.reports;

import java.util.ArrayList;
import java.util.List;

public class ReportResults {

	private List<ReportResultsRow> rows;
	
	public ReportResults() {
		this.rows = new ArrayList<>();
	}
	
	public ReportResults(List<String> rawRows) {
		this();
		for (String raw : rawRows) {
			this.rows.add(new ReportResultsRow(raw));
		}
	}

	public List<ReportResultsRow> getRows() {
		return rows;
	}

	public void setRows(List<ReportResultsRow> rows) {
		this.rows = rows;
	}
	
}
