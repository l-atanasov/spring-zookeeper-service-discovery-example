package com.pastelstudios.spring.zookeeper.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class CuratorServiceDiscoveryFactoryBean
		implements FactoryBean<ServiceDiscovery<ServiceMetadata>>, InitializingBean, DisposableBean {

	private ServiceDiscovery<ServiceMetadata> serviceDiscovery;
	private CuratorFramework client;
	private String basePath = "/services";
	private InstanceSerializer<ServiceMetadata> serializer = new JsonInstanceSerializer<>(ServiceMetadata.class);

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void setSerializer(InstanceSerializer<ServiceMetadata> serializer) {
		this.serializer = serializer;
	}

	@Override
	public ServiceDiscovery<ServiceMetadata> getObject() throws Exception {
		return serviceDiscovery;
	}

	@Override
	public Class<?> getObjectType() {
		return ServiceDiscovery.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(client);
		Assert.notNull(basePath);
		Assert.notNull(serializer);
		
		serviceDiscovery = ServiceDiscoveryBuilder
				.builder(ServiceMetadata.class)
				.client(client)
				.basePath(basePath)
				.serializer(serializer)
				.build();
		serviceDiscovery.start();
	}

	@Override
	public void destroy() throws Exception {
		if (serviceDiscovery != null) {
			CloseableUtils.closeQuietly(serviceDiscovery);
		}
	}

}
