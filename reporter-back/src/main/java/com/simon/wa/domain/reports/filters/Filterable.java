package com.simon.wa.domain.reports.filters;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.simon.wa.domain.apiobject.ApiObject;

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
		@JsonSubTypes({ 
		  @Type(value = FilterNumeric.class, name = "numeric"), 
		  @Type(value = FilterTextContains.class, name = "contains") 
		})
public interface Filterable {

	public boolean apply(ApiObject input);
	
	public String grabDetails();
	
}
