package com.simon.wa.domain.reports;

import com.simon.wa.domain.apiobject.ApiObject;

public interface Filterable {

	public boolean apply(ApiObject input);
	
}
