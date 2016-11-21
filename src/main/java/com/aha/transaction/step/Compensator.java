package com.aha.transaction.step;

import com.aha.transaction.status.StatusUpdater;

/**
 * 事务补偿接口
 * 
 * @author yinwenhao
 *
 * @param <T>
 */
public interface Compensator<T> {

	public boolean compensate(T param);

	public void setMyStatus(int myStatus);

	public void setStatusUpdater(StatusUpdater statusUpdater);

}
