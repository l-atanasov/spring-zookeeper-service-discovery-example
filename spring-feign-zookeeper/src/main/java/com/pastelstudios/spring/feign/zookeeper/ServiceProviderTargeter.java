package com.pastelstudios.spring.feign.zookeeper;

import com.pastelstudios.spring.feign.FeignClientFactoryBean;
import com.pastelstudios.spring.feign.Targeter;
import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;
import com.pastelstudios.spring.zookeeper.discovery.ServiceProviderDelegate;

import feign.Feign.Builder;
import feign.Target;

public class ServiceProviderTargeter implements Targeter {

	private ServiceProviderDelegate<ServiceMetadata> serviceProvider;

	public ServiceProviderTargeter(ServiceProviderDelegate<ServiceMetadata> serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	@Override
	public <T> T target(FeignClientFactoryBean factoryBean, Builder feignBuilder, Class<T> targetType) {
		Target<T> target = new ServiceProviderTarget<>(targetType, serviceProvider);
		return feignBuilder.target(target);
	}

}
