package com.pastelstudios.spring.zookeeper.discovery.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;
import com.pastelstudios.spring.zookeeper.discovery.ServiceProviderRegistry;

public class ServiceProviderClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

	private final ServiceProviderRegistry serviceProviderRegistry;

	public ServiceProviderClientHttpRequestFactory(ServiceProviderRegistry serviceProviderRegistry) {
		super();
		this.serviceProviderRegistry = serviceProviderRegistry;
	}

	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		ResolvedDependency resolvedDependency = resolveDependency(uri);
		ClientHttpRequest delegate = super.createRequest(resolvedDependency.getUri(), httpMethod);
		return new ServiceInstanceClientHttpRequest(delegate, resolvedDependency.getServiceInstance());
	}

	@Override
	public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
		ResolvedDependency resolvedDependency = resolveDependency(uri);
		AsyncClientHttpRequest delegate = super.createAsyncRequest(resolvedDependency.getUri(), httpMethod);
		return new ServiceInstanceAsyncClientHttpRequest(delegate, resolvedDependency.getServiceInstance());
	}

	private ResolvedDependency resolveDependency(URI uri) {
		DependencyUriComponents uriComponents = new DependencyUriComponents(uri);
		String serviceName = uriComponents.getServiceName();
		ServiceProvider<ServiceMetadata> serviceProvider = serviceProviderRegistry.getProvider(serviceName);
		if (serviceProvider == null) {
			throw new RestClientServiceDependencyException("There is no registered provider for dependency " + serviceName);
		}
		ServiceInstance<ServiceMetadata> serviceInstance = null;
		try {
			serviceInstance = serviceProvider.getInstance();
		} catch (Exception e) {
			throw new RestClientServiceDependencyException("Could not get an instance of " + serviceName + " from service provider", e);
		}
		if (serviceInstance == null) {
			throw new RestClientServiceDependencyException("Cannot find an instance for dependency " + serviceName);
		}
		String resolvedUri = serviceInstance.buildUriSpec() + uriComponents.getPath();
		try {
			return new ResolvedDependency(new URI(resolvedUri), serviceInstance);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
}
