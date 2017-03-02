package de.webtwob.view.lighthouse;

import de.cau.infprogoo.lighthouse.LighthouseNetwork;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;

import java.awt.*;
import java.io.IOException;

/**
 * @author Bennet Blessmann
 *         Created on 10.02.2017.
 */
public class LightHouseView implements IJARView {

	private final String address;
	private final int    port;
	private final byte[] windows = new byte[1176];
	private LighthouseNetwork lhn;
	private Thread            updateThread;
	private boolean           run;
	private IJARModel         model;
	private final Runnable updateLoop = this::update;

	public LightHouseView() {

		this("localhost", 8000);
	}

	public LightHouseView(String address, int port) {

		if (0 > port || port > 65535) {
			throw new IllegalArgumentException("Port has to be in the Interval [0,65535] but was " + port + "!");
		}
		this.address = address;
		this.port = port;
	}
	@Override
	public void linkModel(final IJARModel ijarm) {

		model = ijarm;
	}
	@Override
	public void forceUpdate() {

		synchronized (updateLoop) {
			updateLoop.notifyAll();
		}
	}
	private void connect() {

		try {
			lhn = new LighthouseNetwork(address, port);
			lhn.connect();
		} catch (final IOException e) {
			run = false;
			updateThread = null;
			System.err.println("Couldn't connect to LightHouse!");
		}
	}

	private void send(byte[] windows) {

		assert windows.length == 1176 : "Unexpected window count: " + windows.length;
		try {
			lhn.send(windows);
		} catch (final IOException e) {
			System.err.println("Failed to send data to LightHouse! Dropping frame!");
		}
	}

	private void update() {

		connect();
		while (run) {

			//set background
			for (byte x = 0; x < 28; x++) {
				for (byte y = 0; y < 14; y++) {
					setWindowColor(x, y, Color.BLUE);
				}
			}

			final byte player_y     = (byte) model.getPlayerY();
			final byte player_y_top = (byte) (model.getPlayerY() + model.getPlayerHeight());

			//set floor
			for (byte x = 0; x < 28; x++) {
				setWindowColor(x, (byte) 12, Color.GRAY);
			}

			//TODO draw hurdles
			Rectangle[] rects = model.getHurdles();
			byte        size  = (byte) rects.length;
			for (byte i = 0; i < size && i < 28; i++) {
				if (rects[i] != null) {
					for (byte x = 0; x < rects[i].getWidth(); x++) {
						for (byte y = 0; y < rects[i].getHeight(); y++) {
							setWindowColor((byte) (i + x), (byte) (11 - rects[i].getY() - y), Color.CYAN);
						}
					}
				}
			}

			//paint player
			for (byte y = player_y; y < player_y_top; y++) {
				setWindowColor((byte) 1, (byte) (11 - y), Color.YELLOW);
			}

			send(windows);
			synchronized (updateLoop) {
				try {
					updateLoop.wait(10);
				} catch (final InterruptedException ignore) {
				}
			}
		}
	}

	private void setWindowColor(final byte x, final byte y, final Color color) {

		assert x >= 0 && x < 28 : "Window X-Coordinate ot of bounds was:" + x;
		assert y >= 0 && y < 14 : "Window Y-Coordinate ot of bounds was:" + y;

		windows[coordToWinNumber(x, y)] = (byte) color.getRed();
		windows[coordToWinNumber(x, y) + 1] = (byte) color.getGreen();
		windows[coordToWinNumber(x, y) + 2] = (byte) color.getBlue();
	}

	private short coordToWinNumber(byte x, byte y) {

		assert x >= 0 && x < 28 : "Window X-Coordinate ot of bounds was:" + x;
		assert y >= 0 && y < 14 : "Window Y-Coordinate ot of bounds was:" + y;

		return (short) (x * 3 + y * 28 * 3);
	}

	@Override
	public void start() {

		if (!run) {
			synchronized (updateLoop) {
				if (!run) {
					run = true;
					updateThread = new Thread(updateLoop);
					updateThread.start();
				}
			}
		}
	}

	@Override
	public void stop() {

		if (run) {
			synchronized (updateLoop) {
				if (run) {
					run = false;
					try {
						updateThread.join();
						updateThread = null;
					} catch (final InterruptedException ignore) {
					}
				}
			}
		}
	}
}
