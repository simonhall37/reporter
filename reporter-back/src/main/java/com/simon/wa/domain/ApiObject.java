package com.simon.wa.domain;

import java.util.HashSet;
import java.util.Set;

public class ApiObject {

	private String name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String fieldName, Class<T> output) {
		for (ApiObjectField field : this.fields) {
			if (field.getKey().equalsIgnoreCase(fieldName)) {
				if (output.getSimpleName().equalsIgnoreCase("integer"))
					return (T) Integer.valueOf(field.getValue());
				else if (output.getSimpleName().equalsIgnoreCase("sring"))
					return (T) field.getValue();
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
