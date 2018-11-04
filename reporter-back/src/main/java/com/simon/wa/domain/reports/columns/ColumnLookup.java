package com.simon.wa.domain.reports.columns;

import java.util.NoSuchElementException;

import com.simon.wa.domain.Lookup;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportColumn;
import com.simon.wa.services.LookupRepository;

public class ColumnLookup extends ColumnDefinition implements ReportColumn {
	
	private Lookup lookup;
	
	public ColumnLookup() {
		super();
	}
	
	public ColumnLookup(String colName, boolean key, ColOutput outputType, String inputFieldName,String lookupName, String defaultValue) {
		super(colName, key, outputType);
		this.setColumnType("combine");
		this.addInput("field", inputFieldName);
		this.addInput("lookup", lookupName);
		this.addInput("default", defaultValue);
	}
	
	public void addLookup(LookupRepository lookupRepo) {
		this.lookup = lookupRepo.findByName((String)this.getInputs().get("lookup")).get();
		System.out.println("Lookup has " + this.lookup.getValues().size());
	}
	
	@Override
	public Object generateValue(ApiObject input) {
		try {
			String original = input.getValue((String) this.getInputs().get("field"), String.class);
			String lookedUpValue = this.lookup.findValue(original);
			if (lookedUpValue == null) 
				return this.getInputs().get("default");
			else 
				return lookedUpValue;
		} catch (NullPointerException e) {
//			throw new IllegalArgumentException(e.getMessage());
			return "";
		} catch (NoSuchElementException e){
			throw new IllegalArgumentException("Couldn't find lookup with name " + this.getInputs().get("lookup"));
		}
	}

}
