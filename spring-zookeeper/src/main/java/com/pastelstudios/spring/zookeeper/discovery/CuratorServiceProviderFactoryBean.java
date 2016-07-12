package com.pastelstudios.spring.zookeeper.discovery;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.DownInstancePolicy;
import org.apache.curator.x.discovery.InstanceFilter;
import org.apache.curator.x.discovery.ProviderStrategy;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.ServiceProviderBuilder;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class CuratorServiceProviderFactoryBean
		implements FactoryBean<ServiceProvider<ServiceMetadata>>, InitializingBean, DisposableBean {

	private ServiceProvider<ServiceMetadata> serviceProvider = null;
	private ServiceDiscovery<ServiceMetadata> serviceDiscovery = null;
	private String serviceName = null;
	private ProviderStrategy<ServiceMetadata> providerStrategy = new RoundRobinStrategy<>();
	private DownInstancePolicy downInstancePolicy = new DownInstancePolicy();
	private List<InstanceFilter<ServiceMetadata>> instanceFilters = new ArrayList<>();

	public void setServiceDiscovery(ServiceDiscovery<ServiceMetadata> serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setProviderStrategy(ProviderStrategy<ServiceMetadata> providerStrategy) {
		this.providerStrategy = providerStrategy;
	}

	public void setDownInstancePolicy(DownInstancePolicy downInstancePolicy) {
		this.downInstancePolicy = downInstancePolicy;
	}

	public void setInstanceFilters(List<InstanceFilter<ServiceMetadata>> instanceFilters) {
		this.instanceFilters = instanceFilters;
	}

	@Override
	public void destroy() throws Exception {
		if (serviceProvider != null) {
			CloseableUtils.closeQuietly(serviceProvider);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(serviceDiscovery);
		Assert.notNull(serviceName);

		ServiceProviderBuilder<ServiceMetadata> builder = serviceDiscovery.serviceProviderBuilder()
				.serviceName(serviceName)
				.providerStrategy(providerStrategy)
				.downInstancePolicy(downInstancePolicy);

		if (instanceFilters != null) {
			for (InstanceFilter<ServiceMetadata> instanceFilter : instanceFilters) {
				builder.additionalFilter(instanceFilter);
			}
		}

		ServiceProvider<ServiceMetadata> wrappedServiceProvider = builder.build();
		serviceProvider = new ServiceProviderDelegate<>(serviceName, wrappedServiceProvider);
		serviceProvider.start();
	}

	@Override
	public ServiceProvider<ServiceMetadata> getObject() throws Exception {
		return serviceProvider;
	}

	@Override
	public Class<?> getObjectType() {
		return ServiceProvider.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
