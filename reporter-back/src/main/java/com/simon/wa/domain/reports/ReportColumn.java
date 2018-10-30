package com.simon.wa.domain.reports;

import com.simon.wa.domain.apiobject.ApiObject;

public interface ReportColumn {
	
	public Object outputValue(ApiObject input);
	
}
