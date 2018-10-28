package com.simon.wa;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simon.wa.domain.ApiObject;
import com.simon.wa.domain.Lookup;
import com.simon.wa.domain.MappingMetadata;
import com.simon.wa.services.ConnService;
import com.simon.wa.services.LookupRepository;
import com.simon.wa.services.MappingService;

@RestController
@CrossOrigin(origins = "http://localhost:4200",methods = {RequestMethod.OPTIONS,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@SpringBootApplication
public class ReporterBackApplication {

	private static final Logger log = LoggerFactory.getLogger(ReporterBackApplication.class);
	
	@Autowired
	private LookupRepository lookupRepo;
	
	@Autowired
	private ConnService connService;
	@Autowired
	private MappingService mapService;
	
	public static void main(String[] args) {
		SpringApplication.run(ReporterBackApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			loadDummyLookups();
			printResponse("users");
			log.info("started");
		};
	}
	
	private void loadDummyLookups() {
		
		List<String[]> items = new ArrayList<>();
		items.add(new String[] {"key1","value1"});
		items.add(new String[] {"key2","value2"});
		items.add(new String[] {"key3","value3"});
		
		Lookup lookup1 = new Lookup("lookup1",items);
		this.lookupRepo.save(lookup1);
		
		List<String[]> items2 = new ArrayList<>();
		items2.add(new String[] {"key1","value1"});
		items2.add(new String[] {"key2","value2"});
		items2.add(new String[] {"key3","value3"});
		
		Lookup lookup2 = new Lookup("lookup2",items);
		this.lookupRepo.save(lookup2);

	}
	
	private void printResponse(String type) {
		
		List<String> fields = new ArrayList<>();
		fields.add("mail");
		fields.add("firstname");
		fields.add("lastname");
		MappingMetadata user = new MappingMetadata("users","id","login",fields);
		List<String> response = this.connService.getResponse(user.getItemName(), new ArrayList<>());
		
//		try {
//			List<ApiObject> objs = this.mapService.parseObject(response, user);
//			objs.stream().forEach(o -> log.info(o.getId() + ": " + o.getName()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

	@GetMapping(value="/message")
	public String getMessage() {
		return "{\"message\":\"backend\"}";
	}
	
}
