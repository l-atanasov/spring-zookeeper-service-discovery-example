package com.pastelstudios.spring.feign;

import feign.Feign;

public interface Targeter {

	<T> T target(FeignClientFactoryBean factoryBean, Feign.Builder feignBuilder, Class<T> targetType);
}
