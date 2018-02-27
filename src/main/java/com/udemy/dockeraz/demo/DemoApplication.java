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
	public String hello() throws Exception {
		return "Hello World"; 
	}
	
	@RequestMapping(value = "/hello3")
	public String hello3() throws Exception {
		return "Hello World"; 
	}
	
	@RequestMapping(value = "/hello2")
	public String hello2() throws Exception {
		return "Hello World"; 
	}
	
	@RequestMapping(value = "/hello1")
	public String hello1() throws Exception {
		return "Hello World"; 
	}

}
