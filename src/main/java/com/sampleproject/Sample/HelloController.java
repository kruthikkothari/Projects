package com.sampleproject.Sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello World!";
	}
	
	@GetMapping("/greet/{name}")
	public String greet(String name) {
		return "Hello, " + name + "! Welcome to our application.";
	}
}
