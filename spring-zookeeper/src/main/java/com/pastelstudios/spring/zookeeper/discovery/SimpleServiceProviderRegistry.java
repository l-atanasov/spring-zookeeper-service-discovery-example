package com.pastelstudios.spring.zookeeper.discovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.x.discovery.ServiceProvider;
import org.springframework.beans.factory.InitializingBean;

public class SimpleServiceProviderRegistry implements ServiceProviderRegistry, InitializingBean {

	private List<ServiceProviderDelegate<ServiceMetadata>> serviceProviders = new ArrayList<>();
	private Map<String, ServiceProvider<ServiceMetadata>> registry = new HashMap<>();

	public void setServiceProviders(List<ServiceProviderDelegate<ServiceMetadata>> serviceProviders) {
		this.serviceProviders = serviceProviders;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (ServiceProviderDelegate<ServiceMetadata> serviceProvider : serviceProviders) {
			registry.put(serviceProvider.getServiceName(), serviceProvider);
		}
	}

	@Override
	public ServiceProvider<ServiceMetadata> getProvider(String serviceName) {
		return registry.get(serviceName);
	}
}
