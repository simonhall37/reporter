package com.simon.wa.domain;

import java.util.HashMap;
import java.util.Map;

public class MappingMetadata {

	private String itemName;
	private String pathToId;
	private Map<String,String> fieldsToKeep;
	
	public MappingMetadata() {}
	
	public MappingMetadata(String itemName, String pathToId) {
		this.itemName = itemName;
		this.pathToId = pathToId;
		this.fieldsToKeep = new HashMap<String,String>();
	}
	
	public void addField(String keyAndValue) {
		this.fieldsToKeep.put(keyAndValue, keyAndValue);
	}
	
	public void addField(String key,String value) {
		this.fieldsToKeep.put(key,value);
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPathToId() {
		return pathToId;
	}

	public void setPathToId(String pathToId) {
		this.pathToId = pathToId;
	}

	public Map<String,String> getFieldsToKeep() {
		return fieldsToKeep;
	}

	public void setFieldsToKeep(Map<String,String> fieldsToKeep) {
		this.fieldsToKeep = fieldsToKeep;
	}
	
}
