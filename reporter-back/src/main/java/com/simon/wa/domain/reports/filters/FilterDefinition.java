package com.simon.wa.domain.reports.filters;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.simon.wa.domain.Pair;
import com.simon.wa.domain.apiobject.ApiObject;

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
		@JsonSubTypes({ 
		  @Type(value = FilterNumeric.class, name = "numeric"), 
		  @Type(value = FilterTextContains.class, name = "contains") 
		})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="filter_def")
public abstract class FilterDefinition implements Filterable {

	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(targetEntity=Pair.class, cascade=CascadeType.ALL)
	private Set<Pair> inputs;
	
	public FilterDefinition() {
		this.inputs = new HashSet<>();
	}
	
	public void addInput(String key, String value) {
		this.inputs.add(new Pair(key, value));
	}
	
	public abstract boolean apply(ApiObject input);
	
	public abstract String grabDetails();

	public String getValue(String key) {
		for (Pair p : this.inputs) {
			if (p.getKey().equals(key)) return p.getValue();
		}
		return null;
	}
	
	public Set<Pair> getInputs() {
		return inputs;
	}

	public void setInputs(Set<Pair> inputs) {
		this.inputs = inputs;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
