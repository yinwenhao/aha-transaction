package com.aha.transaction.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aha.transaction.param.CompensateTaskParam;

public interface CompensateTaskParamMapper<T extends CompensateTaskParam> {

	/**
	 * 插入一条CompensateTaskParam
	 * 
	 * @param param
	 * @return
	 */
	public int insertCompensateTaskParam(T param);

	/**
	 * 查出status在statusMin和statusMax之间的CompensateTaskParam
	 * 
	 * @param statusMin
	 * @param statusMax
	 * @param failedTimes
	 * @param modifyTime
	 * @return
	 */
	public List<T> getCompensateTaskParams(@Param("statusMin") int statusMin, @Param("statusMax") int statusMax,
			@Param("failedTimes") int failedTimes, @Param("modifyTime") Date modifyTime);

	/**
	 * 查出status在statusMin和statusMax之间的失败过多次数的CompensateTaskParam
	 * 
	 * @param statusMin
	 * @param statusMax
	 * @param failedTimes
	 * @return
	 */
	public List<T> getFalseCompensateTaskParams(@Param("statusMin") int statusMin, @Param("statusMax") int statusMax,
			@Param("failedTimes") int failedTimes);

	/**
	 * 将status更新为newStatus
	 * 
	 * @param guid
	 * @param newStatus
	 * @param modifyTime
	 */
	public void updateStatus(@Param("guid") String guid, @Param("newStatus") int newStatus,
			@Param("modifyTime") Date modifyTime);

	/**
	 * 从oldStatus更新为newStatus，并设置失败次数为0
	 * 
	 * @param guid
	 * @param oldStatus
	 * @param newStatus
	 * @param modifyTime
	 * @return
	 */
	public int updateStatusFromOldStatusAndResetFailedTimes(@Param("guid") String guid,
			@Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus, @Param("modifyTime") Date modifyTime);

	/**
	 * 从oldStatus更新为newStatus，从oldMarks更新为newMarks，并设置失败次数为0
	 * 
	 * @param guid
	 * @param oldStatus
	 * @param newStatus
	 * @param oldMarks
	 * @param newMarks
	 * @param modifyTime
	 * @return
	 */
	public int updateStatusFromOldStatusAndResetFailedTimesForSkipFalse(@Param("guid") String guid,
			@Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus, @Param("oldMarks") int oldMarks,
			@Param("newMarks") int newMarks, @Param("modifyTime") Date modifyTime);

	/**
	 * 增加失败次数
	 * 
	 * @param guid
	 * @param addFailedNum
	 * @param modifyTime
	 */
	public void addFailedTimes(@Param("guid") String guid, @Param("addFailedNum") int addFailedNum,
			@Param("modifyTime") Date modifyTime);

}
