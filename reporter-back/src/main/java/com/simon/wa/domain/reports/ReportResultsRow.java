package com.simon.wa.domain.reports;

public class ReportResultsRow {

	private String[] fields;

	public ReportResultsRow() {}
	
	public ReportResultsRow(String raw) {
		this.fields = raw.split(",");
	}
	
	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
	
}
