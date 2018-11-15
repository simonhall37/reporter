package com.simon.wa.domain.reports.columns;

import java.util.NoSuchElementException;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simon.wa.domain.Lookup;
import com.simon.wa.domain.apiobject.ApiObject;

import com.simon.wa.services.LookupRepository;

@Entity
@Table(name="col_lookup")
public class ColumnLookup extends ColumnDefinition {
	
	@JsonIgnore
	@Transient
	private Lookup lookup;
	
	public ColumnLookup() {
		super();
	}
	
	public ColumnLookup(String colName, boolean key, ColOutput outputType,int colNum, String inputFieldName,String lookupName, String defaultValue) {
		super(colName, key, outputType, colNum);
		this.setColumnType("lookup");
		this.addInput("field", inputFieldName);
		this.addInput("lookup", lookupName);
		this.addInput("default", defaultValue);
	}
	
	public void addLookup(LookupRepository lookupRepo) {
		this.lookup = lookupRepo.findByName((String)getValue("lookup")).get();
		System.out.println("Lookup has " + this.lookup.getValues().size());
	}
	
	@Override
	public Object generateValue(ApiObject input) {
		try {
			String original = input.getValue((String) getValue("field"), String.class);
			String lookedUpValue = this.lookup.findValue(original);
			if (lookedUpValue == null) 
				return getValue("default");
			else 
				return lookedUpValue;
		} catch (NullPointerException e) {
			return "";
		} catch (NoSuchElementException e){
			throw new IllegalArgumentException("Couldn't find lookup with name " + getValue("lookup"));
		}
	}

}
