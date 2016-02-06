package org.dongtian.thrift.zookeeper.impl;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.dongtian.thrift.zookeeper.ChildListener;
import org.dongtian.thrift.zookeeper.StateListener;
import org.dongtian.thrift.zookeeper.support.AbstractZookeeperClient;

public class CuratorZookeeperClient extends AbstractZookeeperClient<CuratorWatcher>{
     private CuratorFramework client;
	
	public CuratorZookeeperClient() {
		        client = CuratorFrameworkFactory.builder()
				.connectString("")
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 3000))
				.build();
		        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
					
					public void stateChanged(CuratorFramework client, ConnectionState state) {
						
						if(ConnectionState.LOST == state) {
							CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
						} else if(ConnectionState.RECONNECTED == state){
							CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
						} else if(ConnectionState.CONNECTED == state) {
							CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
						}
						
					}
				});
		        client.start();
	}

	@Override
	public List<String> addTargetChildListener(String path,
			CuratorWatcher targetChildListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CuratorWatcher createTargetChildListener(String path,
			ChildListener childListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTargetChildListener(String path,
			CuratorWatcher targetChildListener) {
		
		
	}

	@Override
	public void doClose() {
		client.close();
	}

	@Override
	public void createPersistent(String path) {
		try {
			client.create().withMode(CreateMode.PERSISTENT).forPath(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createEphemeral(String path) {
		
		try {
			client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(String path) {
		try {
			client.delete().forPath(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public List<String> getChildren(String path) {
		
		try {
			return this.client.getChildren().forPath(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}

}
