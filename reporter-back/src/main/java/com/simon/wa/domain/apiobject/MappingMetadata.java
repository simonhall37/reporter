package com.simon.wa.domain.apiobject;

import java.util.ArrayList;
import java.util.List;

import com.simon.wa.domain.Pair;

public class MappingMetadata {

	private String itemName;
	private String pathToId;
	private List<String> fieldsToKeep;
	private int size;
	private String lastUpdated;
	private List<Pair> urlParams;
	
	public MappingMetadata() {
		this.urlParams = new ArrayList<>();
	}
	
	public MappingMetadata(String itemName, String pathToId) {
		this();
		this.itemName = itemName;
		this.pathToId = pathToId;
		this.fieldsToKeep = new ArrayList<String>();
	}
	
	public void addParam(String key, String value) {
		this.urlParams.add(new Pair(key, value));
	}
	
	public void addField(String keyAndValue) {
		this.fieldsToKeep.add(keyAndValue);
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

	public List<String> getFieldsToKeep() {
		return fieldsToKeep;
	}

	public void setFieldsToKeep(List<String> fieldsToKeep) {
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

	public List<Pair> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(List<Pair> urlParams) {
		this.urlParams = urlParams;
	}
	
}
