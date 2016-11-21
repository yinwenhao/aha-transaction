package com.aha.transaction.impl.status;

import com.aha.transaction.status.StatusChanger;

public class StatusChangerImpl implements StatusChanger {

	@Override
	public int nextStatus(int status) {
		return status + 1;
	}

	@Override
	public int previousStatus(int status) {
		return status - 1;
	}

}
