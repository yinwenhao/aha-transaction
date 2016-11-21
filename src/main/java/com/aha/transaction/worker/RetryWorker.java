package com.aha.transaction.worker;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aha.transaction.step.TransactionComponent;

public class RetryWorker<K, L> implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<TransactionComponent<K, L>> compensators;

	private K task;

	public RetryWorker(K task, List<TransactionComponent<K, L>> compensators) {
		this.task = task;
		this.compensators = compensators;
	}

	@Override
	public void run() {
		try {
			for (TransactionComponent<K, L> c : compensators) {
				c.doWork(task);
			}
		} catch (Exception e) {
			logger.error("retry worker error.", e);
		}
	}

}
