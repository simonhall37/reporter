package com.simon.wa.domain.reports;

import java.util.List;

import com.simon.wa.domain.reports.columns.ColumnMetadata;
import com.simon.wa.domain.reports.filters.FilterMetadata;

public class ReportingMetadata {

	private String dataSource;
	private String reportName;
	private ReduceOps reductionType;
	private ColumnMetadata cols;
	private FilterMetadata filter;
	
	public ReportingMetadata() {}

	public ReportingMetadata(String reportName, String dataSource, ReduceOps reductionType) {
		this.reportName = reportName;
		this.dataSource = dataSource;
		this.reductionType = reductionType;
	}
	
	public String reduce(ReportKey key, List<ReportRow> values) {
		
		Double[] runningTotal = new Double[this.cols.numValueCols()];	
		for (int i=0;i<runningTotal.length;i++) {
			runningTotal[i] = 0d;
		}
		for (ReportRow r : values) {
			int index = 0;
			for (Object o : r.getValues()) {
				if (reductionType.equals(ReduceOps.COUNT))
					runningTotal[index] = runningTotal[index++]+1;
				else if (reductionType.equals(ReduceOps.SUM)) {
					runningTotal[index] = runningTotal[index++] + (double)o;
				}
			}
		}
		
		StringBuilder sb = new StringBuilder(key + ",");
		for (double i : runningTotal) {
			sb.append(i+",");
		}
		return sb.toString().substring(0,sb.length()-1);
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

	public ColumnMetadata getCols() {
		return cols;
	}

	public void setCols(ColumnMetadata cols) {
		this.cols = cols;
	}

	public FilterMetadata getFilter() {
		return filter;
	}

	public void setFilter(FilterMetadata filter) {
		this.filter = filter;
	}
	
}
