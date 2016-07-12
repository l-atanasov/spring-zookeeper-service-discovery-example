package com.pastelstudios.spring.zookeeper;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {

	private CuratorFramework curatorFramework = null;
	private String connectString = "localhost:2181";
	private EnsembleProvider ensembleProvider = null;
	private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	private int sessionTimeoutMs = 60 * 1000;
	private int connectionTimeoutMs = 15 * 1000;
	private int waitConnectedTime = 10;
	private TimeUnit waitConnectedTimeUnit = TimeUnit.SECONDS;
	private String namespace = null;

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	public void setSessionTimeoutMs(int sessionTimeoutMs) {
		this.sessionTimeoutMs = sessionTimeoutMs;
	}

	public void setConnectionTimeoutMs(int connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
	}

	public void setWaitConnectedTime(int waitConnectedTime) {
		this.waitConnectedTime = waitConnectedTime;
	}

	public void setWaitConnectedTimeUnit(TimeUnit waitConnectedTimeUnit) {
		this.waitConnectedTimeUnit = waitConnectedTimeUnit;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setEnsembleProvider(EnsembleProvider ensembleProvider) {
		this.ensembleProvider = ensembleProvider;
	}

	@Override
	public CuratorFramework getObject() throws Exception {
		return curatorFramework;
	}

	@Override
	public Class<?> getObjectType() {
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
			.connectionTimeoutMs(connectionTimeoutMs)
			.sessionTimeoutMs(sessionTimeoutMs)
			.retryPolicy(retryPolicy)
			.namespace(namespace);
		if (ensembleProvider != null) {
			builder.ensembleProvider(ensembleProvider);
		} else {
			builder.connectString(connectString);
		}
		curatorFramework = builder.build();
		
		curatorFramework.start();
		boolean isConnected = curatorFramework.blockUntilConnected(waitConnectedTime, waitConnectedTimeUnit);
		if(!isConnected) {
			curatorFramework.close();
			throw new RuntimeException("Could not connect to zookeeper!");
		}
	}

	@Override
	public void destroy() {
		if (curatorFramework != null) {
			CloseableUtils.closeQuietly(curatorFramework);
		}
	}

}
