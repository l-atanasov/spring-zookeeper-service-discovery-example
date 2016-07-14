package com.pastelstudios.spring.feign;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.pastelstudios.spring.feign.codec.SpringDecoder;
import com.pastelstudios.spring.feign.codec.SpringEncoder;
import com.pastelstudios.spring.feign.contract.SpringMvcContract;

import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.Request.Options;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

public class FeignClientFactoryBean implements FactoryBean<Object>, InitializingBean {

	private Object client = null;
	private Class<?> type = null;
	private Logger logger = null;
	private Logger.Level logLevel = null;
	private Encoder encoder = new SpringEncoder();
	private Decoder decoder = new SpringDecoder();
	private ErrorDecoder errorDecoder = null;
	private Contract contract = new SpringMvcContract();
	private boolean decode404 = false;
	private Options options = null;
	private List<RequestInterceptor> requestInterceptors = new ArrayList<>();
	private Retryer retryer = null;
	private InvocationHandlerFactory invocationHandlerFactory = null;
	private Targeter targeter = null;

	public void setType(Class<?> type) {
		this.type = type;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setLogLevel(Logger.Level logLevel) {
		this.logLevel = logLevel;
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	public void setDecoder(Decoder decoder) {
		this.decoder = decoder;
	}

	public void setErrorDecoder(ErrorDecoder errorDecoder) {
		this.errorDecoder = errorDecoder;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setDecode404(boolean decode404) {
		this.decode404 = decode404;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public void setRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
		this.requestInterceptors = requestInterceptors;
	}

	public void setRetryer(Retryer retryer) {
		this.retryer = retryer;
	}

	public void setInvocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
		this.invocationHandlerFactory = invocationHandlerFactory;
	}

	public void setTargeter(Targeter targeter) {
		this.targeter = targeter;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(type);

		Feign.Builder builder = Feign.builder();

		if (logger != null) {
			builder.logger(logger);
		}
		if (logLevel != null) {
			builder.logLevel(logLevel);
		}
		if (encoder != null) {
			builder.encoder(encoder);
		}
		if (decoder != null) {
			builder.decoder(decoder);
		}
		if (errorDecoder != null) {
			builder.errorDecoder(errorDecoder);
		}
		if (contract != null) {
			builder.contract(contract);
		}
		if (decode404) {
			builder.decode404();
		}
		if (options != null) {
			builder.options(options);
		}
		if (requestInterceptors != null) {
			builder.requestInterceptors(requestInterceptors);
		}
		if (retryer != null) {
			builder.retryer(retryer);
		}
		if (invocationHandlerFactory != null) {
			builder.invocationHandlerFactory(invocationHandlerFactory);
		}

		client = targeter.target(this, builder, type);
	}

	@Override
	public Object getObject() throws Exception {
		return client;
	}

	@Override
	public Class<?> getObjectType() {
		return type;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
