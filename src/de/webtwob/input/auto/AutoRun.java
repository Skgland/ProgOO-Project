package de.webtwob.input.auto;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import java.awt.*;

/**
 * Created by BB20101997 on 17. Feb. 2017.
 */
public class AutoRun implements IJARInput {

	private boolean enabled = false;
	private IJARModel model;
	private boolean   running;
	private final Runnable run = this::marathon;
	private Thread runner;

	@Override
	public void linkModel(IJARModel ijarm) {

		if (enabled && model == null && ijarm != null) {
			synchronized (run) {
				run.notifyAll();
			}
		}
		model = ijarm;
	}
	private void marathon() {

		while (running) {
			if (model != null && enabled) {
				Rectangle[] rectangles = model.getHurdles();
				boolean     jump       = false;
				boolean     sneak      = false;
				if (rectangles[1] != null && rectangles[1].getY() != 0) {
					sneak = true;
				}
				inner:
				for (int i = 2; i < 7; i++) {
					if (rectangles[i] != null) {
						if (rectangles[i].getY() == 0) {
							if (!sneak) {
								jump = true;
							}
						} else {
							sneak = true;
						}
						break inner;
					}
				}

				if (sneak != model.isSneaking()) {
					model.setSneaking(sneak);
				}
				if (jump) {
					model.jump();
				}
			} else {
				synchronized (run) {
					try {
						run.wait();
					} catch (final InterruptedException ignore) {
					}
				}
			}
		}
	}
	@Override
	public String toString() {

		return "[Auto Run]";
	}	@Override
	public void setEnabled(boolean enable) {

		if (!enabled && enable && model != null) {
			synchronized (run) {
				run.notifyAll();
			}
		}
		enabled = enable;
	}

	@Override
	public boolean isEnabled() {

		return enabled;
	}

	@Override
	public void start() {

		if (runner == null) {
			synchronized (run) {
				if (runner == null) {
					running = true;
					runner = new Thread(run);
					runner.start();
				}
			}
		}
	}

	@Override
	public void stop() {
		if (runner != null) {
			synchronized (run) {
				if (runner != null) {
					running = false;
					try {
						runner.join();
					} catch (final InterruptedException ignore) {
					}
					runner = null;
				}
			}
		}
	}

}
