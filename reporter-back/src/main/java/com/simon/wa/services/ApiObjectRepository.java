package com.simon.wa.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simon.wa.domain.ApiObject;
import com.simon.wa.domain.MappingMetadata;

@Service
public class ApiObjectRepository {

	private Map<String,List<ApiObject>> cache = new HashMap<>();
	@Autowired
	private ConnService connService;
	private static final Logger log = LoggerFactory.getLogger(ApiObjectRepository.class);
	
	public int save(MappingMetadata meta) {
		
		if (this.cache.containsKey(meta.getItemName()))
			log.warn("Overwriting cache for " + meta.getItemName());
		this.cache.put(meta.getItemName(), this.connService.getResponse(meta, new HashMap<>(), 5000));
		return this.cache.get(meta.getItemName()).size();
		
	}
	
	public List<ApiObject> findByName(String name){
		return this.cache.get(name);
	}
	
//	public Map<String,Integer> getStats(){
//		return this.cache.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue().size()).collect(Collectors.toMap((String i) -> i., valueMapper))
//	}
	
}
