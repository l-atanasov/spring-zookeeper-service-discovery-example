package com.pastelstudios.spring.zookeeper.discovery;

import java.util.HashMap;
import java.util.Map;

public class ServiceMetadata {

	private Map<String, String> metadata = new HashMap<>();

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

}
