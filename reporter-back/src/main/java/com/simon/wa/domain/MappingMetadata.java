package com.simon.wa.domain;

import java.util.List;

public class MappingMetadata {

	private String itemName;
	private String pathToId;
	private String pathToName;
	private List<String> fieldsToKeep;
	
	public MappingMetadata() {}
	
	public MappingMetadata(String itemName, String pathToId, String pathToName, List<String> fieldsToKeep) {
		this.itemName = itemName;
		this.pathToId = pathToId;
		this.pathToName = pathToName;
		this.fieldsToKeep = fieldsToKeep;
	}
	
	public boolean addField(String name) {
		return this.fieldsToKeep.add(name);
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

	public String getPathToName() {
		return pathToName;
	}

	public void setPathToName(String pathToName) {
		this.pathToName = pathToName;
	}

	public List<String> getFieldsToKeep() {
		return fieldsToKeep;
	}

	public void setFieldsToKeep(List<String> fieldsToKeep) {
		this.fieldsToKeep = fieldsToKeep;
	}
	
}
