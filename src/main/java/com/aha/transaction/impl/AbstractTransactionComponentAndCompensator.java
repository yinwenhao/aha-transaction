package com.aha.transaction.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aha.transaction.constants.CompensatorConstants;
import com.aha.transaction.impl.status.StatusChangerImpl;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.status.StatusChanger;
import com.aha.transaction.status.StatusUpdater;

public abstract class AbstractTransactionComponentAndCompensator<T extends CompensateTaskParam, R>
		implements TransactionComponentAndCompensator<T, R> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private StatusUpdater statusUpdater;

	private StatusChanger statusChanger = new StatusChangerImpl();

	private int myStatus;

	public void setMyStatus(int myStatus) {
		this.myStatus = myStatus;
	}

	@Override
	public boolean compensate(T param) {
		if (param.getStatus() != myStatus) {
			return true;
		}
		long time = 0;
		if (log.isDebugEnabled()) {
			log.debug("compensate start for guid:" + param.getGuid() + ",status:" + param.getStatus());
			time = System.currentTimeMillis();
		}
		boolean result = compensateWork(param);
		if (log.isDebugEnabled()) {
			log.debug("compensate end for guid:" + param.getGuid() + ",status:" + param.getStatus() + ",result:"
					+ result + ",spend time:" + (System.currentTimeMillis() - time) + "ms");
		}
		return result;
	}

	private boolean compensateWork(T param) {
		boolean result = false;
		try {
			result = doCompensate(param);
		} catch (Exception e) {
			log.warn("Compensator error.", e);
		}
		if (result) {
			int newStatus = statusChanger.previousStatus(myStatus);
			if (newStatus == 0) {
				// 如果是初始化，修改为已回滚
				newStatus = CompensatorConstants.STATUS_ROLL_BACK;
			}
			if (newStatus != param.getStatus()) {
				statusUpdater.updateStatusFromOldStatusAndResetFailedTimes(param.getGuid(), param.getStatus(),
						newStatus, new Date());
				param.setStatus(newStatus);
			}
		} else {
			statusUpdater.addFailedTimes(param.getGuid(), 1, new Date());
			log.warn("Compensator failed.");
		}
		return result;
	}

	/**
	 * 进行一步补偿
	 * 
	 * @param param
	 * @return
	 */
	abstract protected boolean doCompensate(T param);

	private R workWork(T param) throws Exception {
		R r = null;
		try {
			r = work(param);
		} catch (Exception e) {
			statusUpdater.addFailedTimes(param.getGuid(), 1, new Date());
			log.warn("work failed.");
			throw e;
		}
		int newStatus = myStatus;
		if (newStatus != param.getStatus()) {
			statusUpdater.updateStatus(param.getGuid(), newStatus, new Date());
			param.setStatus(newStatus);
		}
		return r;
	}

	@Override
	public R doWork(T param) throws Exception {
		if (param.getStatus() != statusChanger.previousStatus(myStatus)) {
			return null;
		}
		long time = 0;
		if (log.isDebugEnabled()) {
			log.debug("one transaction start for guid:" + param.getGuid() + ",status:" + param.getStatus());
			time = System.currentTimeMillis();
		}
		R r = null;
		try {
			r = workWork(param);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("one transaction end failed for guid:" + param.getGuid() + ",status:" + param.getStatus()
						+ ",spend time:" + (System.currentTimeMillis() - time) + "ms");
			}
			log.warn("one transaction failed.", e);
			throw e;
		}
		if (log.isDebugEnabled()) {
			log.debug("one transaction end success for guid:" + param.getGuid() + ",status:" + param.getStatus()
					+ ",spend time:" + (System.currentTimeMillis() - time) + "ms");
		}
		return r;
	}

	/**
	 * 进行一步事务
	 * 
	 * @param param
	 * @throws Exception
	 */
	abstract protected R work(T param) throws Exception;

	public StatusUpdater getStatusUpdater() {
		return statusUpdater;
	}

	@Override
	public void setStatusUpdater(StatusUpdater statusUpdater) {
		this.statusUpdater = statusUpdater;
	}

}
