package com.aha.transaction.impl.status;

import java.util.Date;

import com.aha.transaction.impl.CompensateTaskParamMapper;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.status.StatusUpdater;

public class StatusUpdaterImpl<T extends CompensateTaskParam> implements StatusUpdater {

	private CompensateTaskParamMapper<T> compensateTaskParamMapper;

	@Override
	public void updateStatus(String guid, int newStatus, Date modifyTime) {
		compensateTaskParamMapper.updateStatus(guid, newStatus, modifyTime);
	}

	@Override
	public boolean updateStatusFromOldStatusAndResetFailedTimes(String guid, int oldStatus, int newStatus,
			Date modifyTime) {
		if (compensateTaskParamMapper.updateStatusFromOldStatusAndResetFailedTimes(guid, oldStatus, newStatus,
				modifyTime) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void addFailedTimes(String guid, int addFailedNum, Date modifyTime) {
		compensateTaskParamMapper.addFailedTimes(guid, addFailedNum, modifyTime);
	}

	public CompensateTaskParamMapper<T> getCompensateTaskParamMapper() {
		return compensateTaskParamMapper;
	}

	public void setCompensateTaskParamMapper(CompensateTaskParamMapper<T> compensateTaskParamMapper) {
		this.compensateTaskParamMapper = compensateTaskParamMapper;
	}

}
