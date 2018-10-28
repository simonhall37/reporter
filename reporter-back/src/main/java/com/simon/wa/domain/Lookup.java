package com.simon.wa.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name="lookups")
public class Lookup {

	@Id
	@GeneratedValue
	private long id;
	
	@NotBlank
	private String name;
	
	@OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	private Set<LookupPair> values = new HashSet<>();
	
	public Lookup() {}
	
	public Lookup(String name, List<String[]> items){
		this.name = name;
		for (String[] pair : items) {
			this.values.add(new LookupPair(pair));
		}
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<LookupPair> getValues() {
		return values;
	}
	public void setValues(Set<LookupPair> values) {
		this.values = values;
	}
	
	
}
