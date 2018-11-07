package com.simon.wa.domain.apiobject;

public class UrlPair {

	private String key;
	private String value;
	
	public UrlPair() {}

	public String getKey() {
		return key;
	}
	
	public UrlPair(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
