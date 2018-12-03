package com.simon.wa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReporterBackApplication {

	private static final Logger log = LoggerFactory.getLogger(ReporterBackApplication.class);
	
	@Autowired
	private InitialLoader loader;
	
	@Value( "${load.admin.user}" )
	private boolean loadUser;
	
	@Value( "${load.dummy.report}" )
	private boolean loadReport;
	
	@Value( "${load.dummy.lookup}" )
	private boolean loadLookup;
	
	public static void main(String[] args) {
		SpringApplication.run(ReporterBackApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			
			if (loadUser)
				loader.loadDefaultUser();
			
			if (loadLookup)
				loader.loadDummyLookups();
			
			if (loadReport)
				loader.loadDummyReport();

			log.info("started");
		};
	}

}
