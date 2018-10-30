package com.simon.wa.controllers;

import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.simon.wa.domain.MappingMetadata;
import com.simon.wa.exceptions.RestObjectAlreadyExistsException;
import com.simon.wa.exceptions.RestObjectNotFoundException;
import com.simon.wa.services.ApiObjectRepository;

@RestController
@RequestMapping(value = "/api/apiobjects")
@CrossOrigin()
public class MappingMetadataController {

	@Autowired
	private ApiObjectRepository objRepo;

	private static final Logger log = LoggerFactory.getLogger(MappingMetadataController.class);

	@GetMapping
	public ResponseEntity<Set<MappingMetadata>> getAllMetadata() {
		Set<MappingMetadata> meta = this.objRepo.findAllMetadata();
		if (meta.size() == 0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<Set<MappingMetadata>>(meta, HttpStatus.OK);
	}

	@GetMapping(value = "/{name}")
	public ResponseEntity<MappingMetadata> getMetadataByName(@PathVariable String name) {
		MappingMetadata meta = this.objRepo.findByName(name);
		if (meta != null)
			return new ResponseEntity<>(meta, HttpStatus.OK);
		else
			throw new RestObjectNotFoundException(log, "mappingmetadata", "name", name);
	}

	@PostMapping
	public ResponseEntity<?> postLookup(@Valid @RequestBody MappingMetadata meta, UriComponentsBuilder ucBuilder) {

		if (this.objRepo.findByName(meta.getItemName()) != null) {
			throw new RestObjectAlreadyExistsException(log, "mappingmetadata", meta.getItemName());
		}
		MappingMetadata savedMeta = this.objRepo.save(meta, true);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/lookups/{name}").buildAndExpand(meta.getItemName()).toUri());
		return new ResponseEntity<>(savedMeta, headers, HttpStatus.CREATED);
	}

	@PutMapping("/{name}")
	public ResponseEntity<MappingMetadata> updateMetadata(@PathVariable("name") String name,
			@Valid @RequestBody MappingMetadata meta) {
		MappingMetadata oldMeta = this.objRepo.findByName(name);
		if (oldMeta != null) {
			oldMeta.setFieldsToKeep(meta.getFieldsToKeep());
			oldMeta.setPathToId(meta.getPathToId());
			this.objRepo.save(oldMeta, true);
			return new ResponseEntity<>(oldMeta, HttpStatus.OK);
		} else
			throw new RestObjectNotFoundException(log, "mappingmetadata", "name", name);
	}

	@DeleteMapping("/{name}")
	public ResponseEntity<?> deleteMetadata(@PathVariable("name") String name) {
		MappingMetadata meta = this.objRepo.findByName(name);
		if (meta==null)
			throw new RestObjectNotFoundException(log, "mappingmetadata", "name", name);
		else {
			boolean deleted = this.objRepo.delete(name);
			if (deleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}

}
