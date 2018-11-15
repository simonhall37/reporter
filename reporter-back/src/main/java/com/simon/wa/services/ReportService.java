package com.simon.wa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.reports.ReportKey;
import com.simon.wa.domain.reports.ReportRow;
import com.simon.wa.domain.reports.ReportingMetadata;

@Service
public class ReportService {

	@Autowired
	private ApiObjectRepository objRepo;
	@Autowired
	private LookupRepository lookupRepo;
	@Autowired
	private CsvService csvService;
	
	private static final Logger log = LoggerFactory.getLogger(ReportService.class);

	public List<String> generateReport(ReportingMetadata meta) {
		
		// validation
		if (!validate(meta, "ReportMeta")) return null;
		if (!validate(meta.getFilter(), "Filter")) return null;
		if (!validate(meta.getCols(), "ColMeta")) return null;
		
		// get input
		List<ApiObject> input = this.objRepo.findObjectsByName(meta.getDataSource());
		if (input == null || input.size() == 0)
			throw new IllegalArgumentException("Datasource doesn't exist or is empty");

		log.info("Initial " + meta.getReportName() + " data contains " + input.size() + " objects");
		
		// filter
		List<ApiObject> filtered = input.stream().filter(o -> 
			{
				try{
					return meta.getFilter().apply(o);
				} catch (IllegalArgumentException e) {
					ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					try {
						System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(o));
					} catch (JsonProcessingException e1) {
						e1.printStackTrace();
					}
					throw new IllegalArgumentException(e.getMessage());
				}
			}
		).collect(Collectors.toList());

		log.info("Filtered " + meta.getReportName() + " data contains " + filtered.size() + " objects");
		meta.getCols().init(this.lookupRepo);
		
		// get values
		Map<ReportKey, List<ReportRow>> outputRows = filtered.stream().map(o -> 
				{
					try {
						return meta.getCols().returnValues(o);
					} catch (IllegalArgumentException e) {
						ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						try {
							System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(o));
						} catch (JsonProcessingException e1) {
							e1.printStackTrace();
						}
						throw new IllegalArgumentException(e.getMessage());
					}
				}
			).collect(Collectors.groupingBy(row -> row.getKeys(),TreeMap::new,Collectors.toCollection(ArrayList::new)));
		
		List<String> finalOutput = outputRows.entrySet().stream().map(e -> meta.reduce(e.getKey(),e.getValue(),csvService)).collect(Collectors.toList());
		return finalOutput;
	}
	
	private boolean validate(Object o,String name) {
		if (o==null) {
			log.error(name + " is null!");
			return false;
		}
		else return true;
	}

}
