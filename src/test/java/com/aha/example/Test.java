package com.aha.example;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aha.example.mapper.OrderRecordsModify;
import com.aha.example.transaction.compensator.CouponModifyCompensator;
import com.aha.example.transaction.compensator.ScheduleModifyCompensator;
import com.aha.example.transaction.compensator.SpecialModifyCompensator;
import com.aha.example.transaction.compensator.StorageModifyCompensator;
import com.aha.example.transaction.result.MyResult;
import com.aha.example.transaction.result.MyResult2;
import com.aha.transaction.manager.CompensatorManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@Transactional
public class Test {

	@Autowired
	private CouponModifyCompensator couponModifyCompensator;

	@Autowired
	private SpecialModifyCompensator specialModifyCompensator;

	@Autowired
	private StorageModifyCompensator storageModifyCompensator;

	@Autowired
	private ScheduleModifyCompensator scheduleModifyCompensator;

	@Autowired
	@Qualifier("compensatorManager")
	private CompensatorManager<OrderRecordsModify, MyResult> compensatorManager;

	@org.junit.Test
	public void test() throws Exception {
		OrderRecordsModify orm = new OrderRecordsModify("1qqqqqqqqqq");
		compensatorManager.insertCompensateTaskParam(orm);
		MyResult r = couponModifyCompensator.doWork(orm);
		if (!r.isSuccess()) {
			// do something
		}
		specialModifyCompensator.doWork(orm);
		MyResult2 r2 = storageModifyCompensator.doWork(orm);
		if (!r2.isSuccess()) {
			// 立即进行一次回滚
			compensatorManager.compensate(orm);
			return;
		}
		scheduleModifyCompensator.doWork(orm);
	}

}
