package org.dongtian.thrift.balance.support;

import java.util.List;

import org.dongtian.thrift.balance.LoadBalance;

/***
 * 负载均衡算法抽象类
 * @author gaoyuandong
 * @mailto 466862016@qq.com
 * @date   2016年2月4日 下午3:32:11
 */
public abstract class AbstractLoadBalance implements LoadBalance {

	public <T> T select(List<T> invokers) {
		
		if(invokers == null || invokers.size() == 0) {
			return null;
		}
		
		if(invokers.size() == 1) return invokers.get(0);
		
		return doSelect(invokers);
	}

	protected <T> int getWeight(T invoker) {
		int weight =1;
		return weight;
	}
	protected abstract  <T> T doSelect(List<T> invokers);
}
