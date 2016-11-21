package com.aha.transaction.worker.factory;

import java.util.ArrayList;
import java.util.List;

import com.aha.transaction.constants.CompensatorConstants;
import com.aha.transaction.impl.TransactionComponentAndCompensator;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.step.Compensator;
import com.aha.transaction.step.TransactionComponent;
import com.aha.transaction.worker.CompensateWorker;
import com.aha.transaction.worker.RetryWorker;

public class WorkerFactory<T extends CompensateTaskParam, K> {

	public Runnable getWorker(int mode, T task, List<TransactionComponentAndCompensator<T, K>> compensators) {
		if (mode == CompensatorConstants.MODE_ROLL_BACK) {
			return new CompensateWorker<T>(task, new ArrayList<Compensator<T>>(compensators));
		} else if (mode == CompensatorConstants.MODE_RETRY) {
			return new RetryWorker<T, K>(task, new ArrayList<TransactionComponent<T, K>>(compensators));
		} else {
			return null;
		}
	}

}
