package com.pastelstudios.spring.zookeeper.discovery.http;

import java.io.IOException;

import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;
import com.pastelstudios.spring.zookeeper.discovery.ServiceProviderRegistry;

public class ServiceProviderClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	private final ServiceProviderRegistry serviceProviderRegistry;

	public ServiceProviderClientHttpRequestInterceptor(ServiceProviderRegistry serviceProviderRegistry) {
		this.serviceProviderRegistry = serviceProviderRegistry;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		ClientHttpResponse response = execution.execute(request, body);
		HttpStatus status = response.getStatusCode();
		if (status.is5xxServerError() && request instanceof ServiceInstanceAware) {
			ServiceInstance<ServiceMetadata> serviceInstance = ((ServiceInstanceAware) request).getServiceInstance();
			String serviceName = serviceInstance.getName();
			ServiceProvider<ServiceMetadata> serviceProvider = serviceProviderRegistry.getProvider(serviceName);
			if (serviceProvider != null) {
				serviceProvider.noteError(serviceInstance);
			}
		}
		return response;
	}

}
