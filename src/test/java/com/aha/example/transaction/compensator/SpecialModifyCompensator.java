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
public class SpecialModifyCompensator extends AbstractTransactionComponentAndCompensator<OrderRecordsModify, Void> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected boolean doCompensate(OrderRecordsModify param) {
		logger.debug("SpecialModifyCompensator.doCompensate for guid:" + param.getGuid());
		// do something
		return false;
	}

	@Override
	protected Void work(OrderRecordsModify param) throws Exception {
		logger.debug("SpecialModifyCompensator.work for guid:" + param.getGuid());
		// throw new Exception();
		return null;
	}

}
