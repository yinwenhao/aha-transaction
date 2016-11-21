package com.aha.transaction.worker;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aha.transaction.step.Compensator;

public class CompensateWorker<K> implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<Compensator<K>> compensators;

	private K task;

	public CompensateWorker(K task, List<Compensator<K>> compensators) {
		this.task = task;
		this.compensators = compensators;
	}

	@Override
	public void run() {
		try {
			for (Compensator<K> c : compensators) {
				if (!c.compensate(task)) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("compensator worker error.", e);
		}
	}

}
