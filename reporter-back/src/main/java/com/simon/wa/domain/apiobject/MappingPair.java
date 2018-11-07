package com.simon.wa.domain.apiobject;

import java.util.List;

public class MappingPair {

	private MappingMetadata key;
	private List<ApiObject> value;
	
	public MappingPair() {
	}
	
	public MappingPair(MappingMetadata key,List<ApiObject> value) {
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
