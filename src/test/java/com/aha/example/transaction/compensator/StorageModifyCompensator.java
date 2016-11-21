package com.aha.example.transaction.compensator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aha.example.mapper.OrderRecordsModify;
import com.aha.example.transaction.result.MyResult2;
import com.aha.transaction.impl.AbstractTransactionComponentAndCompensator;

/**
 * 
 * @author yinwenhao
 *
 */
@Component
public class StorageModifyCompensator extends AbstractTransactionComponentAndCompensator<OrderRecordsModify, MyResult2> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected boolean doCompensate(OrderRecordsModify param) {
		logger.debug("StorageModifyCompensator.doCompensate for guid:" + param.getGuid());
		// do something
		return true;
	}

	@Override
	protected MyResult2 work(OrderRecordsModify param) throws Exception {
		logger.debug("StorageModifyCompensator.work for guid:" + param.getGuid());
		return new MyResult2();
		// do something
	}

}
