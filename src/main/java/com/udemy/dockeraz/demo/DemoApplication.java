package com.udemy.dockeraz.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@RequestMapping(value = "/hello")
	public String hello() {
		return "Hello World"; 
	}
	
	@RequestMapping(value = "/hello")
	public String hello() throws Exception {
		return "Hello World"; 
	}
	
	@RequestMapping(value = "/true-false")
	public String hello() {
		if(this.alwaysFalse()){
			return "TRUE";	
		}
		return "FALSE"; 
	}
	
	private boolean alwaysFalse(){
		return false;	
	}
}
