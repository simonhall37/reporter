package com.simon.wa.domain.reports.columns;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;
import com.simon.wa.domain.reports.ReportRow;
import com.simon.wa.services.LookupRepository;

@Entity
@Table(name="col_metadata")
public class ColumnMetadata {

	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(targetEntity = ColumnDefinition.class, cascade=CascadeType.ALL)
	private Set<ColumnDefinition> columns;
	
	public ColumnMetadata() {
		this.setColumns(new HashSet<>());
	}
	
	public int numValueCols() {
		int out = 0;
		for (ColumnDefinition col : this.columns) {
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
		for (ColumnDefinition col : this.columns) {
			try{
				row.addValue(col.outputValue(input),col.isKey());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		return row;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public Set<ColumnDefinition> getColumns() {
		return columns;
	}

	public void setColumns(Set<ColumnDefinition> columns) {
		this.columns = columns;
	}
	
}
