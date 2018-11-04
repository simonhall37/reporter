package com.simon.wa.domain.reports;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.columns.ColumnCombine;
import com.simon.wa.domain.reports.columns.ColumnLookup;
import com.simon.wa.domain.reports.columns.ColumnSimpleValue;
import com.simon.wa.domain.reports.columns.ColumnWeekNum;

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
		@JsonSubTypes({ 
		  @Type(value = ColumnSimpleValue.class, name = "simple") ,
		  @Type(value = ColumnCombine.class, name = "combine") ,
		  @Type(value = ColumnLookup.class, name = "lookup") ,
		  @Type(value = ColumnWeekNum.class, name = "weeknum")
		})
public interface ReportColumn {
	
	public Object outputValue(ApiObject input);
	
	public boolean isKey();
	
}
