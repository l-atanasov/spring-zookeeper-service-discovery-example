package com.pastelstudios.spring.feign.zookeeper;

import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;

import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;
import com.pastelstudios.spring.zookeeper.discovery.ServiceProviderDelegate;

import feign.Target.HardCodedTarget;

class ServiceProviderTarget<T> extends HardCodedTarget<T> {

	private ServiceProvider<ServiceMetadata> serviceProvider;

	public ServiceProviderTarget(Class<T> type, ServiceProviderDelegate<ServiceMetadata> serviceProvider) {
		super(type, serviceProvider.getServiceName());
		this.serviceProvider = serviceProvider;
	}

	@Override
	public String url() {
		String serviceName = super.url();
		ServiceInstance<ServiceMetadata> serviceInstance = null;
		try {
			serviceInstance = serviceProvider.getInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (serviceInstance == null) {
			throw new RuntimeException("There are no service instances for dependency " + serviceName);
		}
		return serviceInstance.buildUriSpec();
	}
}
