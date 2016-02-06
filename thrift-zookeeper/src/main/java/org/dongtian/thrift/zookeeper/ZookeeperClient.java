package org.dongtian.thrift.zookeeper;

import java.util.List;

public interface ZookeeperClient {

	//新建目录
	void create(String path,boolean ephemeral);
	
	//删除目录
	void delete(String path);
	
	List<String> getChildren(String path);

	List<String> addChildListener(String path,ChildListener childListener);
	
	void removeChildListener(String path,ChildListener childListener);
	
	void addStateListener(StateListener stateListener);
	
	void removeStateListener(StateListener stateListener);
	
	boolean isConnected();
	
	void close();
	
	
}
