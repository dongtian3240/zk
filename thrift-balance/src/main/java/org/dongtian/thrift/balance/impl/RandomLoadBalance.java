package org.dongtian.thrift.balance.impl;

import java.util.List;
import java.util.Random;

import org.dongtian.thrift.balance.support.AbstractLoadBalance;

/***
 * 随机负载均衡算法
 * @author gaoyuandong
 * @mailto 466862016@qq.com
 * @date   2016年2月4日 下午3:33:13
 */
public class RandomLoadBalance extends AbstractLoadBalance {

	private Random random = new Random();
	@Override
	protected <T> T doSelect(List<T> invokers) {
		int length = invokers.size();
		int totalWeight = 0;
		boolean sameWeight = true;
		
		for(int i = 0; i <length ;i++) {
			
			int weight = getWeight(invokers.get(i));
			totalWeight +=weight;
			if(i >0 && weight >0 && !sameWeight && weight != getWeight(invokers.get(i-1))) {
				sameWeight = false;
			}
		}
		
		if(!sameWeight && totalWeight >0) {
			int offset = random.nextInt(totalWeight);
			
			for (int i = 0; i < length; i++) {
				offset -= getWeight(invokers.get(i));
				if(offset <= 0) {
					return invokers.get(i);
				}
			}
		}
		return invokers.get(random.nextInt(length));
	}

}
