package com.pastelstudios.spring.zookeeper.discovery.http;

import java.net.URI;

import org.apache.curator.x.discovery.ServiceInstance;

import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;

class ResolvedDependency {

	private URI uri;
	private ServiceInstance<ServiceMetadata> serviceInstance;

	public ResolvedDependency(URI uri, ServiceInstance<ServiceMetadata> serviceInstance) {
		this.uri = uri;
		this.serviceInstance = serviceInstance;
	}

	public URI getUri() {
		return uri;
	}

	public ServiceInstance<ServiceMetadata> getServiceInstance() {
		return serviceInstance;
	}

}
