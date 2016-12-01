package com.aha.transaction.manager;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.aha.transaction.impl.TransactionComponentAndCompensator;

/**
 * 事务补偿管理器
 * 
 * @author yinwenhao
 *
 * @param <T>
 */
public interface CompensatorManager<T, R> {

	public List<T> getTasks();

	public void skipFalseTasks();

	public List<TransactionComponentAndCompensator<T, R>> getCompensators();

	public int getMode();

	public void init();

	public void setPool(ThreadPoolExecutor pool);

	/**
	 * 立即进行一次回滚或重试
	 * 
	 * @param param
	 * @return
	 */
	public boolean compensate(T param);

	/**
	 * 插入一条分布式事务的记录
	 * 
	 * @param param
	 * @return
	 */
	public int insertCompensateTaskParam(T param);

	/**
	 * 进行分布式事务
	 * 
	 * @param param
	 * @return
	 */
	public void doAllTransaction(T param) throws Exception;

}
