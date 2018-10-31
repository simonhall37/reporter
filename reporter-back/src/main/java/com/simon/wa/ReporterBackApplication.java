package com.simon.wa;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.wa.domain.Lookup;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.apiobject.MappingMetadata;
import com.simon.wa.domain.reports.ReduceOps;
import com.simon.wa.domain.reports.ReportColumn;
import com.simon.wa.domain.reports.ReportingMetadata;
import com.simon.wa.domain.reports.columns.ColOutput;
import com.simon.wa.domain.reports.columns.ColumnMetadata;
import com.simon.wa.domain.reports.columns.ColumnSimpleValue;
import com.simon.wa.domain.reports.filters.FilterMetadata;
import com.simon.wa.domain.reports.filters.FilterNumeric;
import com.simon.wa.domain.reports.filters.FilterTextContains;
import com.simon.wa.domain.reports.filters.Filterable;
import com.simon.wa.domain.reports.filters.NumComp;
import com.simon.wa.services.ApiObjectRepository;
import com.simon.wa.services.ConnService;
import com.simon.wa.services.LookupRepository;
import com.simon.wa.services.ReportService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@SpringBootApplication
public class ReporterBackApplication {

	private static final Logger log = LoggerFactory.getLogger(ReporterBackApplication.class);

	@Autowired
	private LookupRepository lookupRepo;

	@Autowired
	private ConnService connService;

	@Autowired
	private ReportService repService;
	
	@Autowired
	private ApiObjectRepository objRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(ReporterBackApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			loadDummyLookups();
			
			generateUserReport();
			
//			printResponse("users", 2);
//			printResponse("projects", 2);
//			printResponse("time_entries", 50);
			log.info("started");
		};
	}
	
	public void generateUserReport() {
		int userSize = this.objRepo.findByName("users").getSize();
		if (userSize > 0) {
			log.info(userSize + " users found");
//			Filterable filterC = new FilterTextContains("a", "firstname",true);
			Filterable filterN = new FilterNumeric("id", NumComp.BETWEEN, 0, 900);
			List<Filterable> filters = new ArrayList<>();
//			filters.add(filterC);
			filters.add(filterN);
			ReportColumn id = new ColumnSimpleValue("id", false, ColOutput.INTEGER, "id");
			ReportColumn fname = new ColumnSimpleValue("first_name", true, ColOutput.STRING, "firstname");
			ReportColumn lname = new ColumnSimpleValue("last_name", false, ColOutput.STRING, "lastname");
			List<ReportColumn> cols = new ArrayList<>();
			cols.add(id);
			cols.add(fname);
			cols.add(lname);
			
			FilterMetadata fMeta = new FilterMetadata();
			fMeta.setFilters(filters);
			ColumnMetadata cMeta = new ColumnMetadata();
			cMeta.setColumns(cols);
			ReportingMetadata rMeta = new ReportingMetadata("users_report","users",ReduceOps.COUNT);
			rMeta.setFilter(fMeta);
			rMeta.setCols(cMeta);
			List<String> result = this.repService.generateReport(rMeta);
			
			for (String record : result) {
				System.out.println(record);
			}
			
		} else log.warn("Can't generate report as no users stored");
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

	@SuppressWarnings("unused")
	private void printResponse(String type, int limit) {
		final ObjectMapper om = new ObjectMapper();
		MappingMetadata meta = null;
		if (type.equalsIgnoreCase("users")) {
			meta = new MappingMetadata("users", "users.id");
			meta.addField("users.id");
			meta.addField("users.name");
			meta.addField("users.mail");
			meta.addField("users.firstname");
			meta.addField("users.lastname");
			
			List<ApiObject> response = this.connService.getResponse(meta, meta.getUrlParams(),50);
			
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
			meta.addParam("spent_on", "><2018-10-30|2018-10-31");
		}
		
//		try {
//			System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(meta));
//		} catch (JsonProcessingException e) {
//			log.error("Error writing metadata to json",e);
//		}
		
//		@SuppressWarnings("unused")
//		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		List<ApiObject> response = this.connService.getResponse(meta, meta.getUrlParams(),50);
//		log.info(response.size() + " " + meta.getItemName() + " extracted, first " + limit + " ...");
//		response.stream().limit(limit).forEach(o -> {
//			log.info(o.getValue("id", String.class) + "," + o.getValue("project.name",String.class) + "," + o.getValue("spent_on",String.class) + "," + o.getValue("user.name", String.class) + "," + o.getValue("hours", Double.class));
//		});

	}

	@GetMapping(value = "/message")
	public String getMessage() {
		return "{\"message\":\"backend\"}";
	}

}
