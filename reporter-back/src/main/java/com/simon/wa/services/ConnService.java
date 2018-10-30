package com.simon.wa.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.apiobject.MappingMetadata;


@Repository
public class ConnService {

	private final String URL = "https://issues.webanywhere.co.uk";
	private final String FORMAT = "json";
	private final int LIMIT = 100;
	private final String tokenKey = "X-Redmine-API-Key";
	private String apikey = "";
	private RestTemplate restTemplate;
	
	@Autowired
	private MappingService mapService;

	private static final Logger log = LoggerFactory.getLogger(ConnService.class);

	@Autowired
	private RestTemplateBuilder builder;

	private void findKey() {
		this.apikey = System.getenv("RM_APIKEY");
		if (this.apikey.length() == 0) {
			log.error("Could not get API KEY from environment var");
		}
	}

	public String buildUrl(String type, Map<String,String> params,int limit) {

		StringBuilder url = new StringBuilder(this.URL + "/" + type + "." + this.FORMAT + "?");
		for (Entry<String,String> entry : params.entrySet()) {
			url.append(entry.getKey() + "=" + entry.getValue() + "&");
		}
		url.append("limit=" + limit);

		return url.toString();
	}

	public List<ApiObject> getResponse(MappingMetadata meta, Map<String,String> params,int hardLimit) {

		List<ApiObject> out = new ArrayList<>();
		if (this.apikey.length() == 0)
			findKey();
		if (this.restTemplate == null)
			this.restTemplate = builder.build();

		String initialResponse = getResponseAsString(buildUrl(meta.getItemName(), params,1));
		
		int total_count = this.getTotalCount(initialResponse);
		int iterations = ((Double)Math.ceil(total_count/this.LIMIT)).intValue();
		log.info("Total Count: " + total_count + ", need " + (iterations+1) + " iterations");
		
		for (int i = 0; i<=Math.min(iterations,hardLimit);i++) {
			try {
				params.put("offset", String.valueOf(i*this.LIMIT));
				List<ApiObject> temp = this.mapService.parseObject(getResponseAsString(buildUrl(meta.getItemName(), params,this.LIMIT)), meta);
				int count = 0;
				for (ApiObject child : temp.get(0).getChildren()) {
					out.add(child);
					count++;
				}
				log.info("Extracted " + count + " from " + this.URL + params);
			} catch (IOException e) {
				log.error("Error extracting objects",e);
			}
		}
		params.remove("offset");
		meta.setSize(out.size());
		meta.setLastUpdated(LocalDateTime.now().toString());
		return out;

	}
	
	private String getResponseAsString(String urlString) {
		return restTemplate.exchange(urlString, HttpMethod.GET,
				new HttpEntity<String>(new HttpHeaders() {
					private static final long serialVersionUID = 4484749640306401240L;
					{
						set(tokenKey, apikey);
					}
				}), String.class).getBody();
	}
	
	private Integer getTotalCount(String input) {
		MappingMetadata countMeta = new MappingMetadata("","total_count");
		countMeta.addField("total_count");
		try {
			List<ApiObject> c = this.mapService.parseObject(input,countMeta);
			if (c.size() == 1) {
				try{
					return c.get(0).getValue("total_count",Integer.class);
				} catch (NumberFormatException e) {
					log.error("Couldn't cast count to integer",e);
				} 
			} else {
				throw new IllegalArgumentException(String.valueOf(c.size()));
			}
		} catch (IOException e) {
			log.error("IO Exception during search for total_count",e);
		} catch (IllegalArgumentException e) {
			log.error("Expecting 1 ApiObject but received " + e.getMessage(),e);
		}
		return null;
	}

}
