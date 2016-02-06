package org.dongtian.thrift.zookeeper.support;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.dongtian.thrift.zookeeper.ChildListener;
import org.dongtian.thrift.zookeeper.StateListener;
import org.dongtian.thrift.zookeeper.ZookeeperClient;

public abstract class AbstractZookeeperClient<TargetChildListener>  implements ZookeeperClient{

	
	private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

	private final ConcurrentMap<String, ConcurrentMap<ChildListener, TargetChildListener>> childListeners = new ConcurrentHashMap<String, ConcurrentMap<ChildListener, TargetChildListener>>();

	private volatile boolean closed = false;
	
	public void create(String path,boolean ephemeral) {
		
		int i = path.lastIndexOf('/');
		if(i > 0) {
			create(path.substring(0, i),ephemeral);
		}
		if(ephemeral) {
			createEphemeral(path);
		} else {
			createPersistent(path);
		}
	}

	

	public List<String> addChildListener(String path,
			ChildListener childListener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = this.childListeners.get(path);
		if(listeners == null) {
			childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener,TargetChildListener>());
			listeners = childListeners.get(path);
		}
		
		TargetChildListener targetChildListener = listeners.get(path);
		if(targetChildListener == null) {
			listeners.putIfAbsent(childListener, createTargetChildListener(path,childListener));
			targetChildListener = listeners.get(childListener);
		}
		
		
		return addTargetChildListener(path,targetChildListener);
	}

	public abstract List<String> addTargetChildListener(String path,
			TargetChildListener targetChildListener);


	public abstract TargetChildListener createTargetChildListener(String path,
			ChildListener childListener);



	public void removeChildListener(String path, ChildListener childListener) {
		
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if(listeners != null) {
			TargetChildListener targetChildListener = listeners.remove(childListener);
			if(targetChildListener != null) {
				removeTargetChildListener(path,targetChildListener);
			}
		}
		
	}

	
	public void stateChanged(int state){
		
		for (StateListener stateListener : getSessionListeners()) {
			stateListener.stateChanged(state);
		}
	}
	public abstract void removeTargetChildListener(String path,
			TargetChildListener targetChildListener);



	public void addStateListener(StateListener stateListener) {
		stateListeners.add(stateListener);
	}

	public void removeStateListener(StateListener stateListener) {
		stateListeners.remove(stateListener);
	}

	public Set<StateListener> getSessionListeners() {
		return stateListeners;
	}
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	public void close() {
		
		if(closed) {
			return ;
		}
		closed = true;
		try {
			doClose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public abstract void doClose();
	
	public abstract void createPersistent(String path);

	public abstract void createEphemeral(String path);

	public abstract void delete(String path);

	public abstract List<String> getChildren(String path);

}
