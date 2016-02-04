package org.dongtian.thrift.balance;

import java.util.List;

public interface LoadBalance {

	
	<T> T select(List<T> invokers);
	
}
