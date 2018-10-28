package com.simon.wa.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.simon.wa.domain.ApiObject;
import com.simon.wa.domain.MappingMetadata;

@Service
public class MappingService {

	private ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	private static final Logger log = LoggerFactory.getLogger(MappingService.class);

	public List<ApiObject> parseObject(String input, MappingMetadata meta) throws JsonParseException, IOException {

		final ObjectNode node = om.readValue(input, ObjectNode.class);
		final List<ApiObject> out = new ArrayList<>();

		Iterator<Entry<String, JsonNode>> root = node.fields();
		while (root.hasNext()) {
			
			Entry<String, JsonNode> e = root.next();
			if (e.getKey().equalsIgnoreCase(meta.getItemName()) && e.getValue().isArray()) {
				Iterator<JsonNode> targetObj = e.getValue().elements();
				while (targetObj.hasNext()) {
					ApiObject obj = new ApiObject(meta.getItemName());
					Iterator<Entry<String, JsonNode>> innerNodes = targetObj.next().fields();
					while (innerNodes.hasNext()) {
						Entry<String, JsonNode> pair = innerNodes.next();
						if (pair.getKey().equalsIgnoreCase(meta.getPathToId()))
							obj.setId(pair.getValue().asLong());
						else if (pair.getKey().equalsIgnoreCase(meta.getPathToName()))
							obj.setName(pair.getValue().asText());
						if (meta.getFieldsToKeep().contains(pair.getKey()))
							obj.addField(pair.getKey(), pair.getValue().asText());
					}
					out.add(obj);
				}
			}
		}
		
		return out;

	}
}
