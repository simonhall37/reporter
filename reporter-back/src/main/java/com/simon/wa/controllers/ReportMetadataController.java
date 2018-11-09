package com.simon.wa.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.simon.wa.domain.reports.ReportResults;
import com.simon.wa.domain.reports.ReportingMetadata;
import com.simon.wa.exceptions.RestObjectAlreadyExistsException;
import com.simon.wa.exceptions.RestObjectNotFoundException;
import com.simon.wa.services.ReportMetadataRepository;
import com.simon.wa.services.ReportService;

@RestController
@RequestMapping(value = "/api/reports")
@CrossOrigin()
public class ReportMetadataController {

	private static final Logger log = LoggerFactory.getLogger(ReportMetadataController.class);
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ReportMetadataRepository reportRepo;

	@PostMapping(path = "/{name}")
	public ResponseEntity<ReportResults> executeReport(@Valid @RequestBody ReportingMetadata rMeta) {
		List<String> result = this.reportService.generateReport(rMeta);
		ReportResults report = new ReportResults(result);
		return new ResponseEntity<ReportResults>(report, HttpStatus.OK);
	}
	
	@PostMapping(path = "/{name}/csv")
	@ResponseBody
	public String executeReport(@Valid @RequestBody ReportingMetadata rMeta, HttpServletResponse response){
		List<String> result = this.reportService.generateReport(rMeta);
		StringBuilder sb = new StringBuilder();
		for (String line : result) {
			sb.append(line + "\n");
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment; filename=\"" + rMeta.getReportName()+ "\"");
		return sb.toString();
	}
	
	@GetMapping(value = "/{name}")
	public ResponseEntity<ReportingMetadata> getMetadataByName(@PathVariable String name) {
		ReportingMetadata meta = this.reportRepo.findByReportName(name).get();
		if (meta != null)
			return new ResponseEntity<>(meta, HttpStatus.OK);
		else
			throw new RestObjectNotFoundException(log, "reportMetadata", "name", name);
	}
	
	@GetMapping
	public ResponseEntity<List<ReportingMetadata>> getAllReportMetas(){
		List<ReportingMetadata> reports = this.reportRepo.findAll();
		
		if (reports.size()==0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<List<ReportingMetadata>>(reports,HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> postReportMeta(@Valid @RequestBody ReportingMetadata meta, UriComponentsBuilder ucBuilder) {

		if (this.reportRepo.findByReportName(meta.getReportName()).isPresent()) {
			throw new RestObjectAlreadyExistsException(log, "reportmetadata", meta.getReportName());
		}
		ReportingMetadata savedMeta = this.reportRepo.save(meta);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/reports/{name}").buildAndExpand(meta.getReportName()).toUri());
		return new ResponseEntity<>(savedMeta, headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{name}")
	public ResponseEntity<ReportingMetadata> updateMetadata(@PathVariable("name") String name,
			@Valid @RequestBody ReportingMetadata meta) {
		ReportingMetadata oldMeta = this.reportRepo.findByReportName(name).get();
		if (oldMeta != null) {
			oldMeta.setCols(meta.getCols());
			oldMeta.setFilter(meta.getFilter());
			this.reportRepo.save(oldMeta);
			return new ResponseEntity<>(oldMeta, HttpStatus.OK);
		} else
			throw new RestObjectNotFoundException(log, "reportmetadata", "name", name);
	}

	@DeleteMapping("/{name}")
	public ResponseEntity<?> deleteMetadata(@PathVariable("name") String name) {
		ReportingMetadata meta = this.reportRepo.findByReportName(name).get();
		if (meta==null)
			throw new RestObjectNotFoundException(log, "reportmetadata", "name", name);
		else {
			this.reportRepo.delete(meta);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}			
	}
	
}
