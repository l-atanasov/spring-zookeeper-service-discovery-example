package com.pastelstudios.example.helloservice.dependency;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface NameService {

	@RequestMapping(value = "/name-service", method = RequestMethod.GET)
	String sayName(@RequestParam("name") String name);
}
