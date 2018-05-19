package me.shadorc.shadbot.utils.executor;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.shadorc.shadbot.utils.Utils;
import me.shadorc.shadbot.utils.embed.log.LogUtils;

public class ScheduledWrappedExecutor extends ScheduledThreadPoolExecutor {

	public ScheduledWrappedExecutor(int corePoolSize, String threadName) {
		super(corePoolSize, Utils.createDaemonThreadFactory(threadName));
		this.setRemoveOnCancelPolicy(true);
	}

	public ScheduledWrappedExecutor(String name) {
		this(1, name);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return super.scheduleAtFixedRate(this.wrapRunnable(command), initialDelay, period, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return super.scheduleWithFixedDelay(this.wrapRunnable(command), initialDelay, delay, unit);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return super.schedule(this.wrapRunnable(command), delay, unit);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(this.wrapRunnable(task));
	}

	private Runnable wrapRunnable(Runnable command) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					command.run();
				} catch (Exception err) {
					LogUtils.error(err, String.format("{%s} An unknown exception occurred while running a scheduled task.",
							ScheduledWrappedExecutor.class.getSimpleName()));
					throw new RuntimeException(err);
				}
			}
		};
	}

}