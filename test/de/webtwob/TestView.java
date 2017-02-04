package de.webtwob;

import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public class TestView extends JPanel implements IJARView {

	private Thread userThread;
	private volatile boolean   running = true;
	private          JTextArea label   = new JTextArea("Running Test!");
	private IJARModel model;

	private void keepRunning() {

		while (running) {
			try {
				if (model != null) {
					label.setText("Running Test!:\n" + model.toString());
					label.setMinimumSize(label.getPreferredSize());
				}
				synchronized (this) {
					try {
						wait(10);
					} catch (InterruptedException e) {

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				label.setText("Error!");
			}
		}
	}

	public TestView() {

		add(label);
		label.setEditable(false);
		label.setFocusable(false);
		setMinimumSize(new Dimension(400, 200));
		setVisible(true);
	}

	@Override
	public void linkModel(IJARModel ijarm) {

		model = ijarm;
	}

	@Override
	public void start() {

		running = true;
		if (userThread == null) {
			userThread = new Thread(this::keepRunning);
			userThread.setName("TestView Thread");
			userThread.start();
		}
	}

	@Override
	public void stop() {

		running = false;
		synchronized (this) {
			notifyAll();
		}
		userThread = null;
	}
	@Override
	public void forceUpdate() {

	}
}