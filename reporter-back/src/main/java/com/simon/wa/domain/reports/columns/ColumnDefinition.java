package com.simon.wa.domain.reports.columns;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.simon.wa.domain.Pair;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
		@JsonSubTypes({ 
		  @Type(value = ColumnSimpleValue.class, name = "simple") ,
		  @Type(value = ColumnCombine.class, name = "combine") ,
		  @Type(value = ColumnLookup.class, name = "lookup") ,
		  @Type(value = ColumnWeekNum.class, name = "weeknum")
		})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="column_def")
public abstract class ColumnDefinition implements ReportColumn {

	@Id
	@GeneratedValue
	private long id;
	
	private String columnType;
	private String colName;
	private boolean key;
	private ColOutput outputType;
	
	@OneToMany(targetEntity = Pair.class, cascade=CascadeType.ALL)
	private Set<Pair> inputs;
	
	public ColumnDefinition () {
		this.setInputs(new HashSet<>());
	}
	
	public ColumnDefinition(String colName,boolean key, ColOutput outputType) {
		this();
		this.setColName(colName);
		this.setKey(key);
		this.setOutputType(outputType);
	}
	
	public String getValue(String key) {
		for (Pair p : this.inputs) {
			if (p.getKey().equals(key)) return p.getValue();
		}
		return null;
	}
	
	public void addInput(String key, String value) {
		this.inputs.add(new Pair(key, value));
	}
	
	public abstract Object generateValue(ApiObject input);
	
	public Object outputValue(ApiObject input) {
		Object initial = generateValue(input);
		if (initial instanceof java.lang.String) {
			if (this.outputType.equals(ColOutput.STRING))
				return initial;
			else if (this.outputType.equals(ColOutput.INTEGER))
				return Integer.valueOf((String)initial);
			else if (this.outputType.equals(ColOutput.DOUBLE))
				return Double.valueOf((String)initial);
		}
		else if (initial instanceof java.lang.Integer) {
			if (this.outputType.equals(ColOutput.STRING))
				return String.valueOf(initial);
			else if (this.outputType.equals(ColOutput.INTEGER))
				return initial;
			else if (this.outputType.equals(ColOutput.DOUBLE))
				return ((Integer)initial).doubleValue();
		}
		else if (initial instanceof java.lang.Double) {
			if (this.outputType.equals(ColOutput.STRING))
				return String.valueOf(initial);
			else if (this.outputType.equals(ColOutput.INTEGER))
				return ((Double)initial).intValue();
			else if (this.outputType.equals(ColOutput.DOUBLE))
				return initial;
		}
		return null;
	}


	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String type) {
		this.columnType = type;
	}

	public Set<Pair> getInputs() {
		return inputs;
	}

	public void setInputs(Set<Pair> inputs) {
		this.inputs = inputs;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public ColOutput getOutputType() {
		return outputType;
	}

	public void setOutputType(ColOutput outputType) {
		this.outputType = outputType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	} 

}
