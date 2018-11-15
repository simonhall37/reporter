package com.simon.wa.domain.reports;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.simon.wa.domain.reports.columns.ColumnDefinition;
import com.simon.wa.domain.reports.columns.ColumnMetadata;
import com.simon.wa.domain.reports.filters.FilterMetadata;
import com.simon.wa.services.CsvService;

@Entity
@Table(name="report_metadata")
public class ReportingMetadata {

	@Id
	@GeneratedValue
	private long id;
	
	private String dataSource;
	private String reportName;
	private ReduceOps reductionType;
	@OneToOne(targetEntity=ColumnMetadata.class, cascade=CascadeType.ALL)
	private ColumnMetadata cols;
	@OneToOne(targetEntity=FilterMetadata.class, cascade=CascadeType.ALL)
	private FilterMetadata filter;
	
	public ReportingMetadata() {}

	public ReportingMetadata(String reportName, String dataSource, ReduceOps reductionType) {
		this.reportName = reportName;
		this.dataSource = dataSource;
		this.reductionType = reductionType;
	}
	
	public String reduce(ReportKey key, List<ReportRow> values, CsvService csvService) {
		
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
		
		StringBuilder sb = new StringBuilder(csvService.toCsvLine(key.getValues()) + ",");
		sb.append(csvService.toCsvLine(runningTotal));
		return sb.toString();
	}
	
	public String generateHeader(CsvService csvService) {
		String[] colNames = new String[this.getCols().getColumns().size()];
		int index = 0;
		for (ColumnDefinition col : this.getCols().getColumns()) {
			colNames[index++] = col.getColName();
		}
		return csvService.toCsvLine(colNames);
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
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
