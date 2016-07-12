package com.pastelstudios.spring.zookeeper.feign.codec;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

public class SpringDecoder extends AbstractSpringCodec implements Decoder {

	private List<HttpMessageConverter<?>> messageConverters;
	
	public SpringDecoder() {
		this.messageConverters = defaultMessageConverters();
	}

	public SpringDecoder(List<HttpMessageConverter<?>> messageConverters) {
		this.messageConverters = messageConverters;
	}

	@Override
	public Object decode(final Response response, Type type) throws IOException,
			FeignException {
		if (type instanceof Class || type instanceof ParameterizedType) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(type, this.messageConverters);

			return extractor.extractData(new FeignResponseAdapter(response));
		}
		throw new DecodeException(
				"type is not an instance of Class or ParameterizedType: " + type);
	}

	private class FeignResponseAdapter implements ClientHttpResponse {

		private final Response response;

		private FeignResponseAdapter(Response response) {
			this.response = response;
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return HttpStatus.valueOf(this.response.status());
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return this.response.status();
		}

		@Override
		public String getStatusText() throws IOException {
			return this.response.reason();
		}

		@Override
		public void close() {
			try {
				this.response.body().close();
			}
			catch (IOException ex) {
				// Ignore exception on close...
			}
		}

		@Override
		public InputStream getBody() throws IOException {
			return this.response.body().asInputStream();
		}

		@Override
		public HttpHeaders getHeaders() {
			return FeignUtils.getHttpHeaders(this.response.headers());
		}

	}
}
