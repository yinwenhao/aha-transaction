package com.aha.transaction.manager;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.aha.transaction.constants.CompensatorConstants;
import com.aha.transaction.impl.TransactionComponentAndCompensator;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.worker.factory.WorkerFactory;

public abstract class AbstractCompensatorManager<T extends CompensateTaskParam, R> implements CompensatorManager<T, R> {

	private List<TransactionComponentAndCompensator<T, R>> compensators = null;

	private WorkerFactory<T, R> workerFactory = new WorkerFactory<T, R>();

	private ThreadPoolExecutor pool;

	private int mode = CompensatorConstants.MODE_ROLL_BACK;

	@Override
	public boolean compensate(T t) {
		Runnable worker = workerFactory.getWorker(getMode(), t, getCompensators());
		if (worker == null) {
			// 这个mode没有对应的worker
			return false;
		}
		pool.execute(worker);
		return true;
	}

	protected void setCompensators(List<TransactionComponentAndCompensator<T, R>> compensators) {
		this.compensators = compensators;
	}

	@Override
	public List<TransactionComponentAndCompensator<T, R>> getCompensators() {
		return compensators;
	}

	@Override
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public ThreadPoolExecutor getPool() {
		return pool;
	}

	@Override
	public void setPool(ThreadPoolExecutor pool) {
		this.pool = pool;
	}

}
