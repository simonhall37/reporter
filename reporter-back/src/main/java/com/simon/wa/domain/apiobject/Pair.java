package com.simon.wa.domain.apiobject;

import java.util.List;

public class Pair {

	private MappingMetadata key;
	private List<ApiObject> value;
	
	public Pair() {
	}
	
	public Pair(MappingMetadata key,List<ApiObject> value) {
		this.key = key;
		this.value = value;
	}
	
	public MappingMetadata getKey() {
		return this.key;
	}

	public List<ApiObject> getValue() {
		return this.value;
	}

	public void setKey(MappingMetadata meta) {
		this.key = meta;
	}
	
	public void setValue(List<ApiObject> arg0) {
		this.value = arg0;
	}

}
