package com.pastelstudios.spring.zookeeper.discovery;

import org.apache.curator.x.discovery.ServiceProvider;

public interface ServiceProviderRegistry {

	ServiceProvider<ServiceMetadata> getProvider(String serviceName);
}
