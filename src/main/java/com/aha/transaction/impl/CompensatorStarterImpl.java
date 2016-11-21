package com.aha.transaction.impl;

import java.util.List;

import com.aha.transaction.manager.CompensatorManager;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.starter.AbstractCompensatorStarter;

public class CompensatorStarterImpl<T extends CompensateTaskParam, R> extends AbstractCompensatorStarter<T, R> {

	public CompensatorStarterImpl(List<CompensatorManager<T, R>> managerList) {
		super(managerList);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGetTasksException(Exception e) {
		// TODO Auto-generated method stub

	}

}
