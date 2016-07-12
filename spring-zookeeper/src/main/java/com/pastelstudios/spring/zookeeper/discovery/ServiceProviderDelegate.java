package com.pastelstudios.spring.zookeeper.discovery;

import java.io.IOException;
import java.util.Collection;

import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;

public class ServiceProviderDelegate<T> implements ServiceProvider<T> {
	
	private final ServiceProvider<T> serviceProvider;
	private final String serviceName;

	public ServiceProviderDelegate(String serviceName, ServiceProvider<T> serviceProvider) {
		this.serviceProvider = serviceProvider;
		this.serviceName = serviceName;
	}

	@Override
	public void close() throws IOException {
		serviceProvider.close();
	}

	@Override
	public void start() throws Exception {
		serviceProvider.start();
	}

	@Override
	public ServiceInstance<T> getInstance() throws Exception {
		return serviceProvider.getInstance();
	}

	@Override
	public Collection<ServiceInstance<T>> getAllInstances() throws Exception {
		return serviceProvider.getAllInstances();
	}

	@Override
	public void noteError(ServiceInstance<T> instance) {
		serviceProvider.noteError(instance);
	}

	public String getServiceName() {
		return serviceName;
	}

}
