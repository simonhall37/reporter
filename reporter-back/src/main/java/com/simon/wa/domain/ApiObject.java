package com.simon.wa.domain;

import java.util.HashSet;
import java.util.Set;

public class ApiObject {
	
	private String type;
	private long id;
	private Set<ApiObjectField> fields = new HashSet<>();
	private Set<ApiObject> children = new HashSet<>();

	public ApiObject() {
	}

	public ApiObject(String type) {
		this.type = type;
	}

	public boolean addField(String key, String value) {
		return this.fields.add(new ApiObjectField(key, value));
	}

	public boolean addChild(ApiObject child) {
		return this.children.add(child);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String fieldName, Class<T> output) {
		
		if (fieldName.contains(".")) {
			String[] searchArray = fieldName.split("\\.");
			
			for (ApiObject child : this.children) {
				String[] childType = child.getType().split("\\.");
				if (searchArray[0].equalsIgnoreCase(childType[childType.length-1]))
					return child.getValue(searchArray[1], output);
			}
		}
		else {
			for (ApiObjectField field : this.fields) {
				if (field.getKey().equalsIgnoreCase(fieldName)) {
					if (output.getSimpleName().equalsIgnoreCase("integer"))
						return (T) Integer.valueOf(field.getValue());
					else if (output.getSimpleName().equalsIgnoreCase("string"))
						return (T) field.getValue();
					else if (output.getSimpleName().equalsIgnoreCase("double"))
						return (T) Double.valueOf(field.getValue());
				}
			}
		}
		
		return null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<ApiObjectField> getFields() {
		return fields;
	}

	public void setFields(Set<ApiObjectField> fields) {
		this.fields = fields;
	}

	public Set<ApiObject> getChildren() {
		return children;
	}

	public void setChildren(Set<ApiObject> children) {
		this.children = children;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
