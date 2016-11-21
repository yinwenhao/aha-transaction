package com.aha.transaction.status;

public interface StatusChanger {

	public int nextStatus(int status);

	public int previousStatus(int status);

}
