package com.pastelstudios.example.helloservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.pastelstudios.example.helloservice.dependency.NameService;

@RestController
public class HelloController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private NameService nameService;

	@RequestMapping(method = RequestMethod.GET, params = {"client=restTemplate"})
	public String sayHelloWithRestTemplate(@RequestParam("name") String name) throws Exception {
		try {
			String response = restTemplate.getForObject("NameService/name-service/?name=" + name, String.class);
			return "Hello, " + response;
		} catch (Exception e) {
			System.out.println("RestTemplate exception");
			return "Error";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, params = {"client=feign"})
	public String sayHelloWithFeignClient(@RequestParam("name") String name) throws Exception {
		try {
			String response = nameService.sayName(name);
			return "Hello, " + response;
		} catch (Exception e) {
			System.out.println("NameService exception");
			return "Error";
		}
	}
}
