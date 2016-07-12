package com.pastelstudios.spring.zookeeper.discovery.http;

import org.springframework.web.client.RestClientException;

public class RestClientServiceDependencyException extends RestClientException {

	private static final long serialVersionUID = -2413067908785218043L;

	public RestClientServiceDependencyException(String msg) {
		super(msg);
	}

	public RestClientServiceDependencyException(String msg, Throwable ex) {
		super(msg, ex);
	}
	
	
}
