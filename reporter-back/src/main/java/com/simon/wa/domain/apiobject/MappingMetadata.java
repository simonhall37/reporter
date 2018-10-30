package com.simon.wa.domain.apiobject;

import java.util.HashMap;
import java.util.Map;

public class MappingMetadata {

	private String itemName;
	private String pathToId;
	private Map<String,String> fieldsToKeep;
	private int size;
	private String lastUpdated;
	private Map<String,String> urlParams;
	
	public MappingMetadata() {
		this.urlParams = new HashMap<>();
	}
	
	public MappingMetadata(String itemName, String pathToId) {
		this();
		this.itemName = itemName;
		this.pathToId = pathToId;
		this.fieldsToKeep = new HashMap<String,String>();
	}
	
	public void addParam(String key, String value) {
		this.urlParams.put(key, value);
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Map<String,String> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(Map<String,String> urlParams) {
		this.urlParams = urlParams;
	}
	
}
