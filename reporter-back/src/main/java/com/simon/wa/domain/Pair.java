package com.simon.wa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="metadata_pairs")
public class Pair {

	@Id
	@GeneratedValue
	private long id;
	
	private String key;
	private String value;
	
	public Pair() {}

	public String getKey() {
		return key;
	}
	
	public Pair(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}

	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
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
