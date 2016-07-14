package com.pastelstudios.spring.feign;

import feign.Feign.Builder;
import feign.Target;
import feign.Target.HardCodedTarget;

public class BaseUrlTargeter implements Targeter {

	private String baseUrl;

	public BaseUrlTargeter(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public <T> T target(FeignClientFactoryBean factoryBean, Builder feignBuilder, Class<T> targetType) {
		Target<T> target = new HardCodedTarget<>(targetType, baseUrl);
		return feignBuilder.target(target);
	}

}
