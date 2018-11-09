package com.simon.wa.domain.reports.filters;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.simon.wa.domain.apiobject.ApiObject;

@Entity
@Table(name="filter_metadata")
public class FilterMetadata {

	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(targetEntity = FilterDefinition.class, cascade=CascadeType.ALL)
	private Set<FilterDefinition> filters;
	
	public FilterMetadata() {
		this.setFilters(new HashSet<>());
	}
	
	public boolean apply(ApiObject input) {
		for (Filterable f : this.filters) {
			try{
				if (!f.apply(input)) return false;
			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage() + " thrown during filtering " + f.grabDetails());
			}
		}
		return true;
	}

	public Set<FilterDefinition> getFilters() {
		return filters;
	}

	public void setFilters(Set<FilterDefinition> filters) {
		this.filters = filters;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
