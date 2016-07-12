package com.pastelstudios.spring.zookeeper.discovery.http;

import org.apache.curator.x.discovery.ServiceInstance;

import com.pastelstudios.spring.zookeeper.discovery.ServiceMetadata;

interface ServiceInstanceAware {

	ServiceInstance<ServiceMetadata> getServiceInstance();
}
