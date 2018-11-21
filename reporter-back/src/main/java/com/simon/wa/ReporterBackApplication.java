package com.simon.wa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simon.wa.auth.User;
import com.simon.wa.auth.UserRepository;
import com.simon.wa.domain.Lookup;
import com.simon.wa.domain.apiobject.ApiObject;
import com.simon.wa.domain.apiobject.MappingMetadata;
import com.simon.wa.domain.reports.ReduceOps;
import com.simon.wa.domain.reports.ReportingMetadata;
import com.simon.wa.domain.reports.columns.ColOutput;
import com.simon.wa.domain.reports.columns.ColumnCombine;
import com.simon.wa.domain.reports.columns.ColumnDefinition;
import com.simon.wa.domain.reports.columns.ColumnLookup;
import com.simon.wa.domain.reports.columns.ColumnMetadata;
import com.simon.wa.domain.reports.columns.ColumnSimpleValue;
import com.simon.wa.domain.reports.filters.FilterDefinition;
import com.simon.wa.domain.reports.filters.FilterMetadata;
import com.simon.wa.domain.reports.filters.FilterNumeric;
import com.simon.wa.domain.reports.filters.FilterTextContains;
import com.simon.wa.domain.reports.filters.NumComp;
import com.simon.wa.services.ApiObjectRepository;
import com.simon.wa.services.ConnService;
import com.simon.wa.services.CsvService;
import com.simon.wa.services.LookupRepository;
import com.simon.wa.services.ReportMetadataRepository;

@SpringBootApplication
public class ReporterBackApplication {

	private static final Logger log = LoggerFactory.getLogger(ReporterBackApplication.class);

	@Autowired
	private LookupRepository lookupRepo;

	@Autowired
	private ConnService connService;
	
	@Autowired
	private ApiObjectRepository objRepo;
	
	@Autowired
	private CsvService csvService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ReportMetadataRepository reportRepo;
	
	ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	public static void main(String[] args) {
		SpringApplication.run(ReporterBackApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			
			loadDefaultUser();
			
			loadDummyLookups();
			
			loadDummyReport();
			
			log.info("started");
		};
	}
	
	private void loadDefaultUser() {
		this.userRepo.save(new User("admin","pathfinder",System.getenv("RM_APIKEY")));
	}
	
	private void loadDummyReport() {
		int userSize = this.objRepo.findByName("users").getSize();
		if (userSize > 0) {
			log.info(userSize + " users found");
			FilterDefinition filterC = new FilterTextContains("as", "firstname",true);
			FilterDefinition filterN = new FilterNumeric("id", NumComp.BETWEEN, 0, 900);
			Set<FilterDefinition> filters = new HashSet<>();
			filters.add(filterC);
			filters.add(filterN);
			ColumnDefinition id = new ColumnSimpleValue("id", true, ColOutput.INTEGER,2, "id");
			ColumnDefinition team = new ColumnLookup("team", true, ColOutput.STRING,1, "id", "teams", "Other");
			List<String> fields = new ArrayList<String>();
			fields.add("firstname");
			fields.add("lastname");
			ColumnDefinition bname = new ColumnCombine("full_name", true, ColOutput.STRING,3, fields, ".");
			SortedSet<ColumnDefinition> cols = new TreeSet<>();
			cols.add(id);
			cols.add(team);
			cols.add(bname);
			
			FilterMetadata fMeta = new FilterMetadata();
			fMeta.setFilters(filters);
			ColumnMetadata cMeta = new ColumnMetadata();
			cMeta.setColumns(cols);
			ReportingMetadata rMeta = new ReportingMetadata("users_report","users",ReduceOps.SUM);
			rMeta.setFilter(fMeta);
			rMeta.setCols(cMeta);
			
			this.reportRepo.save(rMeta);
			
		} else log.warn("Can't generate report as no users stored");
	}

	private void loadDummyLookups() {

		String resPath = "C:\\Users\\Simon\\git\\reporter\\reporter-back\\src\\main\\resources\\";
		try {
			this.lookupRepo.save(getLookupFromFile(resPath, "teams", ".csv"));
		} catch (IOException e) {
			log.error("Couldn't read file from system " + resPath + "teams.csv");
		}

	}
	
	private Lookup getLookupFromFile(String path, String name, String extension) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path + name + extension));
		String contents = new String(encoded,  StandardCharsets.UTF_8);
		return this.csvService.lookupFromString(name, contents);
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
		
		try {
			System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(meta));
		} catch (JsonProcessingException e) {
			log.error("Error writing metadata to json",e);
		}

	}

}
