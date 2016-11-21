package com.aha.transaction.constants;

public class CompensatorConstants {

	/**
	 * 分布式处理模式——回滚
	 */
	public static final int MODE_ROLL_BACK = 0;

	/**
	 * 分布式处理模式——重试
	 */
	public static final int MODE_RETRY = 1;

	/**
	 * 状态——已回滚
	 */
	public static final int STATUS_ROLL_BACK = -1;

}
