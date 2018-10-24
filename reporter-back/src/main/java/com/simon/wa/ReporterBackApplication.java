package com.simon.wa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200",methods = {RequestMethod.OPTIONS,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@SpringBootApplication
public class ReporterBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReporterBackApplication.class, args);
	}
	
	@GetMapping(value="/message")
	public String getMessage() {
		return "{\"message\":\"backend\"}";
	}
	
}
