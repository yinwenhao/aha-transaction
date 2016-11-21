package com.aha.transaction.status;

import java.util.Date;

public interface StatusUpdater {

	/**
	 * 将status更新为newStatus
	 * 
	 * @param guid
	 * @param newStatus
	 * @param modifyTime
	 */
	public void updateStatus(String guid, int newStatus, Date modifyTime);

	/**
	 * 从oldStatus更新为newStatus，并设置失败次数为0
	 * 
	 * @param guid
	 * @param oldStatus
	 * @param newStatus
	 * @param modifyTime
	 * @return
	 */
	public boolean updateStatusFromOldStatusAndResetFailedTimes(String guid, int oldStatus, int newStatus,
			Date modifyTime);

	/**
	 * 增加失败次数
	 * 
	 * @param guid
	 * @param addFailedNum
	 * @param modifyTime
	 */
	public void addFailedTimes(String guid, int addFailedNum, Date modifyTime);

}
