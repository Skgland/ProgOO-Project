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

	private LighthouseNetwork lhn;
	private Thread updateThread;
	private final Runnable updateLoop = this::update;
	private boolean run;
	private IJARModel model;
	private final String address;
	private final int port;

	public LightHouseView(){
		this("localhost",8000);
	}

	public LightHouseView(String address,int port){
		if(0>port||port>65535){
			throw new IllegalArgumentException("Port has to be in the Interval [0,65535] but was "+port+"!");
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
		synchronized (updateLoop){
			updateLoop.notifyAll();
		}
	}

	final byte[] windows = new byte[1176];

	private void update(){

		try {
			lhn = new LighthouseNetwork(address,port);
			lhn.connect();
		} catch (final IOException e) {
			run = false;
			updateThread = null;
			System.err.println("Couldn't connect to LightHouse!");
		}
		while(run){
			for(byte x = 0;x<28;x++){
				for(byte y = 0 ;y<14;y++){
					//set background
					setWindowColor(x, y, Color.BLUE);
				}
			}

			byte player_y = (byte) model.getPlayerY();
			byte player_y_top = (byte) (model.getPlayerY()+model.getPlayerHeight());

			for(byte x = 0;x<28;x++){
				//set floor
				setWindowColor(x, (byte) 12, Color.GRAY);
			}

			for(byte y = player_y;y<player_y_top;y++){
				setWindowColor((byte) 1, (byte) (11-y), Color.YELLOW);
			}

			try {
				lhn.send(windows);
			} catch (final IOException e) {
				System.err.println("Failed to send data to LightHouse!");
			}
			synchronized (updateLoop){
				try {
					updateLoop.wait(10);
				} catch (final InterruptedException ignore) {
				}
			}
		}
	}

	private void setWindowColor(final byte x, final byte y, final Color color){
		windows[coordToWinNumber(x,y)] = (byte)color.getRed();
		windows[coordToWinNumber(x,y)+1] = (byte)color.getGreen();
		windows[coordToWinNumber(x,y)+2] = (byte)color.getBlue();
	}

	private short coordToWinNumber(byte x, byte y){
		return (short) (x*3+y*28*3);
	}

	@Override
	public void start() {
		if(!run)
		synchronized (updateLoop){
			if(!run){
				run = true;
				updateThread = new Thread(updateLoop);
				updateThread.start();
			}
		}
	}

	@Override
	public void stop() {
		if(run)
			synchronized (updateLoop){
				if(run){
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
