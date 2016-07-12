package com.pastelstudios.spring.zookeeper.discovery.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.concurrent.ListenableFuture;

import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;

class ServiceInstanceAsyncClientHttpRequest implements AsyncClientHttpRequest, ServiceInstanceAware {

	private AsyncClientHttpRequest delegate;
	private ServiceInstance<ServiceMetadata> serviceInstance;

	public ServiceInstanceAsyncClientHttpRequest(AsyncClientHttpRequest delegate, ServiceInstance<ServiceMetadata> serviceInstance) {
		this.delegate = delegate;
		this.serviceInstance = serviceInstance;
	}

	@Override
	public HttpMethod getMethod() {
		return delegate.getMethod();
	}

	@Override
	public URI getURI() {
		return delegate.getURI();
	}

	@Override
	public HttpHeaders getHeaders() {
		return delegate.getHeaders();
	}

	@Override
	public OutputStream getBody() throws IOException {
		return delegate.getBody();
	}

	@Override
	public ListenableFuture<ClientHttpResponse> executeAsync() throws IOException {
		return delegate.executeAsync();
	}

	@Override
	public ServiceInstance<ServiceMetadata> getServiceInstance() {
		return serviceInstance;
	}

}
