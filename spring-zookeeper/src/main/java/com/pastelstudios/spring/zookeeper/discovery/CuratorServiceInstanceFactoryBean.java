package com.pastelstudios.spring.zookeeper.discovery;

import java.util.HashMap;
import java.util.Map;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.UriSpec;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class CuratorServiceInstanceFactoryBean
		implements FactoryBean<ServiceInstance<ServiceMetadata>>, InitializingBean, DisposableBean {

	private ServiceInstance<ServiceMetadata> serviceInstance = null;
	private ServiceDiscovery<ServiceMetadata> serviceDiscovery = null;
	private UriSpec uriSpec = new UriSpec("{scheme}://{address}:{port}");
	private String serviceName = null;
	private Integer port = null;
	private Integer sslPort = null;
	private Map<String, String> metadata = new HashMap<>();

	public void setServiceDiscovery(ServiceDiscovery<ServiceMetadata> serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}

	public void setUriSpec(UriSpec uriSpec) {
		this.uriSpec = uriSpec;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(serviceDiscovery);
		Assert.notNull(serviceName);
		Assert.notNull(uriSpec);
		Assert.notNull(port);

		ServiceMetadata serviceMetadata = new ServiceMetadata();
		serviceMetadata.setMetadata(metadata);
		
		ServiceInstanceBuilder<ServiceMetadata> builder = ServiceInstance.<ServiceMetadata> builder()
				.uriSpec(uriSpec)
				.name(serviceName)
				.port(port)
				.payload(serviceMetadata);
		if (sslPort != null) {
			builder.sslPort(sslPort);
		}
		serviceInstance = builder.build();
		serviceDiscovery.registerService(serviceInstance);
	}

	@Override
	public void destroy() throws Exception {
		if (serviceInstance != null) {
			serviceDiscovery.unregisterService(serviceInstance);
		}
	}

	@Override
	public ServiceInstance<ServiceMetadata> getObject() throws Exception {
		return serviceInstance;
	}

	@Override
	public Class<?> getObjectType() {
		return ServiceInstance.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
