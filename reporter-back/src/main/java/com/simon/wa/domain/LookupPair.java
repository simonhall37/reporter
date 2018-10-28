package com.simon.wa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="lookup_pairs")
public class LookupPair {

	@Id
	@GeneratedValue
	private long id;
	
	@NotBlank
	private String key;
	private String value;
	
	public LookupPair() {}
	
	public LookupPair(String[] pair) {
		this.key = pair[0];
		this.value = pair[1];
	}
	
	public String getKey() {
		return key;
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
