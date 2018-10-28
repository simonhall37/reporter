package com.simon.wa.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;


@Repository
public class ConnService {

	private final String URL = "https://issues.webanywhere.co.uk";
	private final String FORMAT = "json";
	private final String tokenKey = "X-Redmine-API-Key";
	private String apikey = "";
	private RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(ConnService.class);

	@Autowired
	private RestTemplateBuilder builder;

	private void findKey() {
		this.apikey = System.getenv("RM_APIKEY");
		if (this.apikey.length() == 0) {
			log.error("Could not get API KEY from environment var");
		}
	}

	public String buildUrl(String type, List<String> params,int limit) {

		StringBuilder url = new StringBuilder(this.URL + "/" + type + "." + this.FORMAT + "?");
		for (String entry : params) {
			url.append(entry + "&");
		}
		url.append("limit=" + limit);

		return url.toString();
	}

	public List<String> getResponse(String type, List<String> params) {

		if (this.apikey.length() == 0)
			findKey();
		if (this.restTemplate == null)
			this.restTemplate = builder.build();
		
		String urlString = buildUrl(type, params,1);
		
		@SuppressWarnings("serial")
		String initialResponse = restTemplate.exchange(urlString, HttpMethod.GET,
				new HttpEntity<String>(new HttpHeaders() {
					{
						set(tokenKey, apikey);
					}
				}), String.class).getBody();
		
		
//		@SuppressWarnings("serial")
//		HttpEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET,
//				new HttpEntity<String>(new HttpHeaders() {
//					{
//						set(tokenKey, apikey);
//					}
//				}), String.class);

		return new ArrayList<>();

	}
	
	private int getCount(String initialResponse) {
		int count = -1;
		try {
			count = Integer.parseInt(initialResponse.substring(initialResponse.lastIndexOf("total_count")+13, initialResponse.lastIndexOf("offset")-2).trim());
			return count;
		} catch (NumberFormatException e) {
			log.error("Couldn't parse total count, received " + count,e);
		}
		return count;
	}
	

}
