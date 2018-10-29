package com.simon.wa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.wa.domain.ApiObject;
import com.simon.wa.domain.Lookup;
import com.simon.wa.domain.MappingMetadata;
import com.simon.wa.services.ConnService;
import com.simon.wa.services.LookupRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@SpringBootApplication
public class ReporterBackApplication {

	private static final Logger log = LoggerFactory.getLogger(ReporterBackApplication.class);

	@Autowired
	private LookupRepository lookupRepo;

	@Autowired
	private ConnService connService;

	public static void main(String[] args) {
		SpringApplication.run(ReporterBackApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			loadDummyLookups();
//			printResponse("users", 2);
//			printResponse("projects", 2);
			printResponse("time_entries", 50);
			log.info("started");
		};
	}

	private void loadDummyLookups() {

		List<String[]> items = new ArrayList<>();
		items.add(new String[] { "key1", "value1" });
		items.add(new String[] { "key2", "value2" });
		items.add(new String[] { "key3", "value3" });

		Lookup lookup1 = new Lookup("lookup1", items);
		this.lookupRepo.save(lookup1);

		List<String[]> items2 = new ArrayList<>();
		items2.add(new String[] { "key1", "value1" });
		items2.add(new String[] { "key2", "value2" });
		items2.add(new String[] { "key3", "value3" });

		Lookup lookup2 = new Lookup("lookup2", items);
		this.lookupRepo.save(lookup2);

	}

	private void printResponse(String type, int limit) {

		MappingMetadata meta = null;
		if (type.equalsIgnoreCase("users")) {
			meta = new MappingMetadata("users", "users.id");
			meta.addField("users.id");
			meta.addField("users.name");
			meta.addField("users.mail");
			meta.addField("users.firstname");
			meta.addField("users.lastname");
			
		} else if (type.equalsIgnoreCase("projects")) {
			meta = new MappingMetadata("projects", "projects.id");
			meta.addField("projects.id");
			meta.addField("projects.name");
			meta.addField("projects.created_on");
			meta.addField("projects.updated_on");
			meta.addField("projects.parent.id");
			meta.addField("projects.parent.name");
		} else if (type.equalsIgnoreCase("time_entries")) {
			meta = new MappingMetadata("time_entries", "time_entries.id");
			meta.addField("time_entries.id");
			meta.addField("time_entries.created_on");
			meta.addField("time_entries.updated_on");
			meta.addField("time_entries.spent_on");
			meta.addField("time_entries.hours");
			meta.addField("time_entries.project.id");
			meta.addField("time_entries.project.name");
			meta.addField("time_entries.user.id");
			meta.addField("time_entries.user.name");
			meta.addField("time_entries.activity.id");
			meta.addField("time_entries.activity.name");
			meta.addField("time_entries.issue.id");
		}
		
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<ApiObject> response = this.connService.getResponse(meta, new HashMap<>(),10);
		log.info(response.size() + " " + meta.getItemName() + " extracted, first " + limit + " ...");
		response.stream().limit(limit).forEach(o -> {
//			try {
//				log.info(om.writerWithDefaultPrettyPrinter().writeValueAsString(o));
//			} catch (JsonProcessingException e) {
//				log.error("Couldn't print json object from " + o.getType(),e);
//			}
			log.info(o.getValue("id", String.class) + "," + o.getValue("project.name",String.class) + "," + o.getValue("spent_on",String.class) + "," + o.getValue("user.name", String.class) + "," + o.getValue("hours", Double.class));
		});

	}

	@GetMapping(value = "/message")
	public String getMessage() {
		return "{\"message\":\"backend\"}";
	}

}
