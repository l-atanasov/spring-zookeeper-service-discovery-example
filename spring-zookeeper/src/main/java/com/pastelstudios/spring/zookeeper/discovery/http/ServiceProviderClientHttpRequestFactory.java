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
		uri = resolveURI(uri);
		return super.createRequest(uri, httpMethod);
	}

	@Override
	public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
		uri = resolveURI(uri);
		return super.createAsyncRequest(uri, httpMethod);
	}

	private URI resolveURI(URI uri) {
		UriComponents uriComponents = new UriComponents(uri);
		String serviceName = uriComponents.getServiceName();
		ServiceProvider<ServiceMetadata> serviceProvider = serviceProviderRegistry.getProvider(serviceName);
		if (serviceProvider == null) {
			throw new RuntimeException("There is no registered provider for dependency " + serviceName);
		}
		ServiceInstance<ServiceMetadata> serviceInstance = null;
		try {
			serviceInstance = serviceProvider.getInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (serviceInstance == null) {
			throw new RuntimeException("Cannot find an instance for dependency " + serviceName);
		}
		String resolvedUri = serviceInstance.buildUriSpec() + uriComponents.getPath();
		try {
			return new URI(resolvedUri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private static class UriComponents {
		private String serviceName;
		private String path;

		public UriComponents(URI uri) {
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

}
