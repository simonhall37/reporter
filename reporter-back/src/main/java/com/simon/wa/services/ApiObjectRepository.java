package com.simon.wa.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.apiobject.MappingMetadata;
import com.simon.wa.domain.apiobject.Pair;

@Service
public class ApiObjectRepository {

	private Map<MappingMetadata,List<ApiObject>> cache = new HashMap<>();
	@Autowired
	private ConnService connService;
	@Value(value="${cache.output.dir}")
	private String OUT_DIR;
	private boolean initialized = false;
	private final ObjectMapper om = new ObjectMapper();

	private static final Logger log = LoggerFactory.getLogger(ApiObjectRepository.class);
	
	public ApiObjectRepository() {
		this.cache = new HashMap<>();
	}
	
	public void init() {
		log.info(OUT_DIR);
		loadAllFromDisk();
		this.initialized = true;
		log.info("Read " + this.cache.size() + " from disk");
	}
	
	public Set<MappingMetadata> findAllMetadata(){
		if (!initialized)
			init();
		return this.cache.keySet();
	}
	
	public boolean delete(String nameToCheck) {
		if (!initialized)
			init();
		
		boolean removed = false;
		Iterator<MappingMetadata> names = this.cache.keySet().iterator();
		while (names.hasNext()) {
			MappingMetadata meta =  names.next();
			if (nameToCheck.equalsIgnoreCase(meta.getItemName())) {
				this.cache.remove(meta);
				removed=true;
				break;
			}
		}
		if (removed)
			if (removeFromDisk(nameToCheck)) {
				return true;
			} else {
				log.error(nameToCheck + " removed from cache but not disk");
			}
		return false;
	}
	
	public MappingMetadata save(MappingMetadata meta, boolean runQuery) {
		if (!initialized)
			init();
		
		if (delete(meta.getItemName()))
			log.warn("Removing " + meta.getItemName() + " from in memory cache and disk");
		List<ApiObject> toInsert = new ArrayList<>(); 
		if (runQuery)
			toInsert = this.connService.getResponse(meta, meta.getUrlParams(), 2000);
		this.cache.put(meta, toInsert);
		if (!writeToDisk(meta.getItemName())) {
			log.warn(meta.getItemName() + " - can't write to disk");
		}
		return meta;
		
	}
	
	public MappingMetadata findByName(String name){
		if (!initialized)
			init();
		
		for (MappingMetadata meta : this.cache.keySet()) {
			if (meta.getItemName().equalsIgnoreCase(name))
				return meta;
		}
		return null;
	}
	
	public List<ApiObject> findObjectsByName(String name){
		if (!initialized)
			init();
		
		for (MappingMetadata meta : this.cache.keySet()) {
			if (meta.getItemName().equalsIgnoreCase(name))
				return this.cache.get(meta);
		}
		return null;
	}
	
	public Map<String,Integer> getStats(){
		if (!initialized)
			init();
		
		Map<String,Integer> out = new HashMap<>();
		this.cache.entrySet().stream().forEach(e -> out.put(e.getKey().getItemName(), e.getValue().size()));
		return out;
	}
	
	public void loadAllFromDisk() {
		File outFile = null;
		try{
			outFile = new File(OUT_DIR);
		} catch (NullPointerException e) {
			log.error("Cache disk path is null",e);
			return;
		}
		for (File cacheFile : outFile.listFiles()) {
			log.info("Trying to get " + cacheFile.getName() + " from disk");
			readFromDisk(cacheFile.getName());
		}
	}
	
	private boolean readFromDisk(String cacheName) {

		Pair toRead = null;
		File outFile = new File(this.OUT_DIR + "/" + cacheName);
		try {
			toRead = om.readValue(outFile, Pair.class);
			this.cache.put(toRead.getKey(),toRead.getValue());
			return true;
		} catch (IOException e) {
			log.error("Could not read json to disk: " + e.getMessage() + " (" + e.getCause() + ")");
		}
		return false;
	}
	
	private boolean writeToDisk(String cacheName) {

		File outFile = new File(this.OUT_DIR + "/" + cacheName + ".json");
		try {
			for (Map.Entry<MappingMetadata,List<ApiObject>> entry : this.cache.entrySet()) {
				if (entry.getKey().getItemName().equalsIgnoreCase(cacheName)) {
					Pair pair = new Pair(entry.getKey(),entry.getValue());
					om.writerWithDefaultPrettyPrinter().writeValue(outFile, pair);
					return true;
				}
			}
		} catch (IOException e) {
			log.error("Could not write json to disk: " + e.getMessage() + " (" + e.getCause() + ")");
		}
		return false;
	}
	
	private boolean removeFromDisk(String cacheName) {
		File outFile = new File(this.OUT_DIR + "/" + cacheName + ".json");
		if (outFile.exists()) {
			try{
				return outFile.delete();
			} catch (SecurityException e) {
				log.error("Couldn't delete " + outFile.getName() + " from disk - permission denied",e);
				return false;
			}
		}
		else {
			log.warn(cacheName + " - doesn't exist!");
			return false;
		}
	}
	
}
