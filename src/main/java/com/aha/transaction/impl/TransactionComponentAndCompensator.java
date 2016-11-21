package com.aha.transaction.impl;

import com.aha.transaction.step.Compensator;
import com.aha.transaction.step.TransactionComponent;

public interface TransactionComponentAndCompensator<T, R> extends TransactionComponent<T, R>, Compensator<T> {

}
