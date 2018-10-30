package com.simon.wa.domain.reports;

import java.util.List;

public class ReportingMetadata {

	private String dataSource;
	private String reportName;
	private ReduceOps reductionType;
	private List<ColumnMetadata> cols;
	private FilterMetadata filter;
	
	public ReportingMetadata() {}

	public ReportingMetadata(String reportName, String dataSource, ReduceOps reductionType) {
		this.reportName = reportName;
		this.dataSource = dataSource;
		this.reductionType = reductionType;
	}
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public ReduceOps getReductionType() {
		return reductionType;
	}

	public void setReductionType(ReduceOps reductionType) {
		this.reductionType = reductionType;
	}

	public List<ColumnMetadata> getCols() {
		return cols;
	}

	public void setCols(List<ColumnMetadata> cols) {
		this.cols = cols;
	}

	public FilterMetadata getFilter() {
		return filter;
	}

	public void setFilter(FilterMetadata filter) {
		this.filter = filter;
	}
	
}