package com.aha.transaction.step;

/**
 * 一步事务的执行器
 * 
 * @author yinwenhao
 *
 * @param <T>
 */
public interface TransactionComponent<T, R> {

	public R doWork(T param) throws Exception;

}
