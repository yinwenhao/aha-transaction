package com.aha.transaction.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aha.transaction.constants.CompensatorConstants;
import com.aha.transaction.impl.status.StatusChangerImpl;
import com.aha.transaction.impl.status.StatusUpdaterImpl;
import com.aha.transaction.manager.AbstractCompensatorManager;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.status.StatusChanger;

public class CompensatorManagerImpl<T extends CompensateTaskParam, R> extends AbstractCompensatorManager<T, R> {

	private CompensateTaskParamMapper<T> compensateTaskParamMapper;

	private StatusChanger statusChanger = new StatusChangerImpl();

	/**
	 * 回滚时最大失败次数，默认5
	 */
	private int maxFailedTimes = 5;

	/**
	 * 在这个毫秒数之前修改的记录，会被回滚，默认15s
	 */
	private long millisecondsForCompensator = 15000;

	/**
	 * 回滚或重试时，是否跳过失败次数过多的小任务，默认true
	 */
	private boolean skipFalseTasks = true;

	public CompensatorManagerImpl(List<TransactionComponentAndCompensator<T, R>> compensators) {
		setCompensators(compensators);
	}

	/**
	 * 用于获得需要回滚的修改时间
	 * 
	 * @return
	 */
	public Date modifyTime() {
		return new Date(System.currentTimeMillis() - millisecondsForCompensator);
	}

	@Override
	public List<T> getTasks() {
		return compensateTaskParamMapper.getCompensateTaskParams(CompensatorConstants.STATUS_ROLL_BACK,
				getCompensators().size(), maxFailedTimes, modifyTime());
	}

	@Override
	public void skipFalseTasks() {
		if (!skipFalseTasks) {
			return;
		}
		int statusMax = getCompensators().size();
		List<T> tasks = compensateTaskParamMapper.getFalseCompensateTaskParams(CompensatorConstants.STATUS_ROLL_BACK,
				statusMax, maxFailedTimes);
		for (T t : tasks) {
			int newMark;
			int newStatus = t.getStatus();
			if (getMode() == CompensatorConstants.MODE_ROLL_BACK) {
				newMark = 1 << (t.getStatus() - 1);
				newStatus = statusChanger.previousStatus(t.getStatus());
			} else if (getMode() == CompensatorConstants.MODE_RETRY) {
				newMark = 1 << t.getStatus();
				newStatus = statusChanger.nextStatus(t.getStatus());
			} else {
				continue;
			}
			compensateTaskParamMapper.updateStatusFromOldStatusAndResetFailedTimesForSkipFalse(t.getGuid(),
					t.getStatus(), newStatus, t.getMarks(), t.getMarks() | newMark, new Date());
		}
	}

	@Override
	public void init() {
		StatusUpdaterImpl<T> statusUpdater = new StatusUpdaterImpl<T>();
		statusUpdater.setCompensateTaskParamMapper(compensateTaskParamMapper);
		for (int i = 0; i < getCompensators().size(); i++) {
			getCompensators().get(i).setMyStatus(i + 1);
			getCompensators().get(i).setStatusUpdater(statusUpdater);
		}
		if (getMode() == CompensatorConstants.MODE_ROLL_BACK) {
			List<TransactionComponentAndCompensator<T, R>> compensators = new ArrayList<TransactionComponentAndCompensator<T, R>>();
			for (int i = getCompensators().size() - 1; i >= 0; i--) {
				compensators.add(getCompensators().get(i));
			}
			setCompensators(compensators);
		}
	}

	public CompensateTaskParamMapper<T> getCompensateTaskParamMapper() {
		return compensateTaskParamMapper;
	}

	public void setCompensateTaskParamMapper(CompensateTaskParamMapper<T> compensateTaskParamMapper) {
		this.compensateTaskParamMapper = compensateTaskParamMapper;
	}

	public int getMaxFailedTimes() {
		return maxFailedTimes;
	}

	public void setMaxFailedTimes(int maxFailedTimes) {
		this.maxFailedTimes = maxFailedTimes;
	}

	public long getMillisecondsForCompensator() {
		return millisecondsForCompensator;
	}

	public void setMillisecondsForCompensator(long millisecondsForCompensator) {
		this.millisecondsForCompensator = millisecondsForCompensator;
	}

	@Override
	public int insertCompensateTaskParam(T param) {
		return compensateTaskParamMapper.insertCompensateTaskParam(param);
	}

	public boolean isSkipFalseTasks() {
		return skipFalseTasks;
	}

	public void setSkipFalseTasks(boolean skipFalseTasks) {
		this.skipFalseTasks = skipFalseTasks;
	}

}
