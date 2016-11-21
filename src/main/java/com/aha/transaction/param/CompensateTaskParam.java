package com.aha.transaction.param;

import java.util.Date;

/**
 * 事务补偿——参数
 * 
 * @author yinwenhao
 *
 */
public class CompensateTaskParam {

	private String guid;

	private int status;

	private int marks;

	private Date modifyTime;

	private int failedTimes;

	public CompensateTaskParam() {
	}

	/**
	 * 一般用这个构造函数
	 * 
	 * @param guid
	 */
	public CompensateTaskParam(String guid) {
		this.guid = guid;
		this.modifyTime = new Date();
	}

	public CompensateTaskParam(String guid, Date modifyTime) {
		this.guid = guid;
		this.modifyTime = modifyTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public int getFailedTimes() {
		return failedTimes;
	}

	public void setFailedTimes(int failedTimes) {
		this.failedTimes = failedTimes;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

}
