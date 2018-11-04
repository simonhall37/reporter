package com.simon.wa.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simon.wa.domain.reports.ReportingMetadata;
import com.simon.wa.services.ReportService;

@RestController
@RequestMapping(value = "/api/report")
@CrossOrigin()
public class ReportMetadataController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ReportMetadataController.class);
	
	@Autowired
	private ReportService reportService;

	@PostMapping
	public ResponseEntity<String> postReportMeta(@Valid @RequestBody ReportingMetadata rMeta) {
		
		List<String> result = this.reportService.generateReport(rMeta);
		StringBuilder sb = new StringBuilder();
		for (String line : result) {
			sb.append(line + "\n");
		}
		
		return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
	}
	
}
