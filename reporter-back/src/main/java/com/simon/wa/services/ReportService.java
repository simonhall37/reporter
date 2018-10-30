package com.simon.wa.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simon.wa.domain.ApiObject;
import com.simon.wa.domain.ReportingMetadata;

@Service
public class ReportService {

	@Autowired
	private ApiObjectRepository objRepo;
	
	public List<Object[]> generateReport(ReportingMetadata meta){
		List<Object[]> rows = new ArrayList<Object[]>();
		
		// get input
		List<ApiObject> input = this.objRepo.findObjectsByName(meta.getDataSource());
		if (input == null || input.size() == 0)
			throw new IllegalArgumentException("Datasource doesn't exist or is empty");
		
		
		
		
		
		return rows;
	}
	
}
