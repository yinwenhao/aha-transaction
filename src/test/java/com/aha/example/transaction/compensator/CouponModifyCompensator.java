package com.aha.example.transaction.compensator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aha.example.mapper.OrderRecordsModify;
import com.aha.example.transaction.result.MyResult;
import com.aha.transaction.impl.AbstractTransactionComponentAndCompensator;

/**
 * 
 * @author yinwenhao
 *
 */
@Component
public class CouponModifyCompensator extends AbstractTransactionComponentAndCompensator<OrderRecordsModify, MyResult> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected boolean doCompensate(OrderRecordsModify param) {
		logger.debug("CouponModifyCompensator.doCompensate for guid:" + param.getGuid());
		// do something
		return false;
	}

	@Override
	protected MyResult work(OrderRecordsModify param) throws Exception {
		logger.debug("CouponModifyCompensator.work for guid:" + param.getGuid());
		MyResult result = new MyResult();
		result.setSuccess(false);
		return result;
		// do something
	}

}
