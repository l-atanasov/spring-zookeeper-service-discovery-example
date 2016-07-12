package com.pastelstudios.spring.zookeeper.discovery.http;

import java.net.URI;

class DependencyUriComponents {

	private String serviceName;
	private String path;

	public DependencyUriComponents(URI uri) {
		String fullUri = uri.toString();
		int indexOfSlash = fullUri.indexOf("/");
		if(indexOfSlash != -1) {
			serviceName = fullUri.substring(0, indexOfSlash);
			path = fullUri.substring(indexOfSlash);
		} else {
			serviceName = fullUri;
			path = "";
		}
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getPath() {
		return path;
	}
}
