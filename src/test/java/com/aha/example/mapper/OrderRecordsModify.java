package com.aha.example.mapper;

import com.aha.transaction.param.CompensateTaskParam;

/**
 * 修改订单事务补偿幂等需要的请求记录
 * 
 * @author yinwenhao
 *
 */
public class OrderRecordsModify extends CompensateTaskParam {

	public OrderRecordsModify(String guid) {
		super(guid);
	}

}
