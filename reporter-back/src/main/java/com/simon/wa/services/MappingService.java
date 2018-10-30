package com.simon.wa.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.apiobject.MappingMetadata;

@Service
public class MappingService {

	private ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(MappingService.class);

	public List<ApiObject> parseObject(String input, MappingMetadata meta) throws JsonParseException, IOException {

		final ObjectNode node = om.readValue(input, ObjectNode.class);
		final List<ApiObject> out = new ArrayList<>();
		final List<String> location = new ArrayList<>();

		Iterator<Entry<String, JsonNode>> rootNode = node.fields();
		ApiObject rootObj = new ApiObject("root");
		out.add(rootObj);
		while (rootNode.hasNext()) {
			
			Entry<String, JsonNode> e = rootNode.next();
			if (e.getKey().equalsIgnoreCase(meta.getItemName()) && e.getValue().isArray()) {
				location.add(meta.getItemName());
				Iterator<JsonNode> targetObj = e.getValue().elements();
				while (targetObj.hasNext()) {		
					Iterator<Entry<String, JsonNode>> innerNodes = targetObj.next().fields();
					checkNodes(innerNodes,meta,location,rootObj);
				}
			}
			else {
				if (meta.getFieldsToKeep().keySet().contains(e.getKey())){
					rootObj.addField(e.getKey(), e.getValue().asText());
				}
				
			}
		}
		
		return out;
	}
	
	private String locToString(List<String> location) {
		StringBuilder sb = new StringBuilder();
		for (String part : location) {
			sb.append(part + ".");
		}
		return sb.toString().substring(0,sb.length()-1);
	}
	
	private void checkNodes(Iterator<Entry<String, JsonNode>> innerNodes, MappingMetadata meta,List<String> location,ApiObject parent) {
		
		ApiObject obj = new ApiObject(locToString(location));
		while (innerNodes.hasNext()) {
			
			Entry<String, JsonNode> pair = innerNodes.next();
//			System.out.println("Checking: " + locToString(location) + "." + pair.getKey() + ": " + pair.getValue().asText());
			
			if (pair.getValue().getNodeType().equals(JsonNodeType.OBJECT)) {
				location.add(pair.getKey());
				checkNodes(pair.getValue().fields(),meta,location,obj);
				location.remove(pair.getKey());
			}
			
			if (meta.getPathToId().equalsIgnoreCase(locToString(location) + "." + pair.getKey()))
				obj.setId(pair.getValue().asLong());
//			else if (meta.getPathToName().equalsIgnoreCase(locToString(location) + "." + pair.getKey()))
//				obj.setName(pair.getValue().asText());
			if (meta.getFieldsToKeep().keySet().contains(locToString(location) + "." + pair.getKey()))
				obj.addField(pair.getKey(), pair.getValue().asText());
		}
		parent.addChild(obj);
	}
	
	
}
