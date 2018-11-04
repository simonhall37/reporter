package com.simon.wa.domain.reports.filters;

import java.util.ArrayList;
import java.util.List;

import com.simon.wa.domain.apiobject.ApiObject;

public class FilterMetadata {

	private List<Filterable> filters;
	
	public FilterMetadata() {
		this.setFilters(new ArrayList<>());
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

	public List<Filterable> getFilters() {
		return filters;
	}

	public void setFilters(List<Filterable> filters) {
		this.filters = filters;
	}
	
	
}
