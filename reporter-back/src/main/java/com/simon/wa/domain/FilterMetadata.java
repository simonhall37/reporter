package com.simon.wa.domain;

import java.util.ArrayList;
import java.util.List;

public class FilterMetadata {

	private List<Filterable> filters;
	
	public FilterMetadata() {
		this.setFilters(new ArrayList<>());
	}
	
	public boolean apply(ApiObject input) {
		for (Filterable f : this.filters) {
			if (!f.apply(input)) return false;
		}
		return true;
	}

	public List<Filterable> getFilters() {
		return filters;
	}

	public void setFilters(List<Filterable> filters) {
		this.filters = filters;
	}
	
	
}
