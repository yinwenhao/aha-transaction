package com.aha.transaction.starter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.aha.transaction.manager.CompensatorManager;
import com.aha.transaction.param.CompensateTaskParam;
import com.aha.transaction.worker.factory.WorkerFactory;

/**
 * 事务补偿定时器总控
 * 
 * @author yinwenhao
 *
 * @param <T>
 */
public abstract class AbstractCompensatorStarter<T extends CompensateTaskParam, R>
		implements Runnable, InitializingBean, CompensatorStarter<T>, DisposableBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 默认扫描间隔
	private static final long PERIOD_MILLISECONDS = 3000;

	/**
	 * 关闭线程池中线程的超时秒数
	 */
	private static final int CLOSE_THREAD_TIMEOUT_SECOND_DEFAULT = 5;

	// 默认线程池大小
	private static final int POOL_SIZE = 2;

	private List<CompensatorManager<T, R>> managerList = new ArrayList<CompensatorManager<T, R>>();

	private int poolSize = POOL_SIZE;

	/**
	 * 扫描间隔
	 */
	private long periodMilliSeconds = PERIOD_MILLISECONDS;

	private ScheduledExecutorService ses;

	private ThreadPoolExecutor pool;

	private WorkerFactory<T, R> workerFactory = new WorkerFactory<T, R>();

	public AbstractCompensatorStarter(List<CompensatorManager<T, R>> managerList) {
		this.managerList = managerList;
	}

	public void addManager(CompensatorManager<T, R> manager) {
		managerList.add(manager);
	}

	@Override
	public void run() {
		try {
			for (CompensatorManager<T, R> manager : managerList) {
				manager.skipFalseTasks();
				for (T task : manager.getTasks()) {
					Runnable worker = workerFactory.getWorker(manager.getMode(), task, manager.getCompensators());
					if (worker == null) {
						// 这个mode没有对应的worker
						break;
					}
					pool.execute(worker);
				}
			}
		} catch (Exception e) {
			logger.error("compensator get all tasks error.", e);
			doGetTasksException(e);
		}
	}

	protected abstract void doGetTasksException(Exception e);

	/**
	 * 开始自动任务，每隔scanMilliSeconds毫秒运行一次
	 */
	public void startCompensateTimer() {
		initPoolAndManager();
		ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(this, 0, periodMilliSeconds, TimeUnit.MILLISECONDS);
		logger.info("start ScheduledExecutorService success.");
	}

	private void initPoolAndManager() {
		pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
		for (CompensatorManager<T, R> manager : managerList) {
			manager.init();
			manager.setPool(pool);
		}
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public long getPeriodMilliSeconds() {
		return periodMilliSeconds;
	}

	public void setPeriodMilliSeconds(long periodMilliSeconds) {
		this.periodMilliSeconds = periodMilliSeconds;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		startCompensateTimer();
	}

	@Override
	public void destroy() throws Exception {
		ses.shutdown();
		if (!ses.awaitTermination(CLOSE_THREAD_TIMEOUT_SECOND_DEFAULT, TimeUnit.SECONDS)) {
			logger.warn("ses: close threads too long... (" + new Date() + " ), use:"
					+ CLOSE_THREAD_TIMEOUT_SECOND_DEFAULT + "s");
		}

		pool.shutdown();
		if (!pool.awaitTermination(CLOSE_THREAD_TIMEOUT_SECOND_DEFAULT, TimeUnit.SECONDS)) {
			logger.warn(
					"pool: close threads... (" + new Date() + " ), use:" + CLOSE_THREAD_TIMEOUT_SECOND_DEFAULT + "s");
		}

		logger.info("stop ScheduledExecutorService success.");
	}

}
