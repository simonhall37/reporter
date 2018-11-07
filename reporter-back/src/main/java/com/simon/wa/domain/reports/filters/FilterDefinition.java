package com.simon.wa.domain.reports.filters;

import java.util.ArrayList;
import java.util.List;

import com.simon.wa.domain.Pair;
import com.simon.wa.domain.apiobject.ApiObject;

public abstract class FilterDefinition implements Filterable {

	private List<Pair> inputs;
	
	public FilterDefinition() {
		this.inputs = new ArrayList<>();
	}
	
	public void addInput(String key, Object value) {
		this.inputs.add(new Pair(key, value));
	}
	
	public abstract boolean apply(ApiObject input);
	
	public abstract String grabDetails();

	public Object getValue(String key) {
		for (Pair p : this.inputs) {
			if (p.getKey().equals(key)) return p.getValue();
		}
		return null;
	}
	
	public List<Pair> getInputs() {
		return inputs;
	}

	public void setInputs(List<Pair> inputs) {
		this.inputs = inputs;
	}
}
