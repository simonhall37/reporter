package com.simon.wa.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.simon.wa.domain.Lookup;
import com.simon.wa.exceptions.RestObjectAlreadyExistsException;
import com.simon.wa.exceptions.RestObjectNotFoundException;
import com.simon.wa.services.LookupRepository;

@RestController
@RequestMapping(value = "/api/lookups")
@CrossOrigin()
public class LookupController {

	private static final Logger log = LoggerFactory.getLogger(LookupController.class);
	
	private final LookupRepository lookupRepo;
	
	public LookupController(LookupRepository lookupRepo){
		this.lookupRepo = lookupRepo;
	}
	
	@GetMapping
	public ResponseEntity<List<Lookup>> getAllLookups(){
		log.info("Get all lookups request");
		List<Lookup> lookups = this.lookupRepo.findAll();
		
		if (lookups.size()==0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<List<Lookup>>(lookups,HttpStatus.OK);
	}
	
	@GetMapping(value="/names")
	public ResponseEntity<List<String>> getAllLookupNames(){
		List<String> lookups = this.lookupRepo.findAll().stream().map(l -> l.getName()).collect(Collectors.toList());
		
		if (lookups.size()==0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<List<String>>(lookups,HttpStatus.OK);
	}
	
	@GetMapping(value="/{name}")
	public ResponseEntity<Lookup> getLookupByName(@PathVariable String name){
		return lookupRepo.findByName(name).map(lookup -> new ResponseEntity<>(lookup, HttpStatus.OK))
				.orElseThrow(() -> new RestObjectNotFoundException(log, "lookup", "name", name));
	}
	
	@PostMapping
	public ResponseEntity<?> postLookup(@Valid @RequestBody Lookup lookup, UriComponentsBuilder ucBuilder) {

		if (this.lookupRepo.findByName(lookup.getName()).isPresent())
			throw new RestObjectAlreadyExistsException(log, "lookup" ,lookup.getName());

		Lookup savedLookup = this.lookupRepo.save(lookup);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/lookups/{name}").buildAndExpand(savedLookup.getName()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{name}")
	public ResponseEntity<Lookup> updateLookup(@PathVariable("name") String name, @Valid @RequestBody Lookup lookup) {
		return this.lookupRepo.findByName(name).map(lookupToUpdate -> {
			lookupToUpdate.setValues(lookup.getValues());
			this.lookupRepo.save(lookupToUpdate);
			return new ResponseEntity<>(lookupToUpdate, HttpStatus.OK);
		}).orElseThrow(() -> new RestObjectNotFoundException(log, "lookup", "name", name));
	}

	@DeleteMapping("/{name}")
	public ResponseEntity<?> deleteLookup(@PathVariable("name") String name) {
		return this.lookupRepo.findByName(name).map(lookup -> {
			lookupRepo.delete(lookup);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new RestObjectNotFoundException(log, "lookup", "name", name));
	}
}
