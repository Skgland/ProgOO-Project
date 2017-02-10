package de.webtwob.view.lighthouse;

import de.cau.infprogoo.lighthouse.LighthouseNetwork;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;

/**
 * @author Bennet Blessmann
 *         Created on 10.02.2017.
 */
public class LightHouseView implements IJARView {

	private LighthouseNetwork lhn;
	private IJARModel model;
	private String address = "localhost";
	private short port = 8000;

	public LightHouseView(String address,int port){
		if(0>port||port>65535){
			throw new IllegalArgumentException("Port has to be in the Interval [0,65535] but was "+port+"!");
		}
	}

	@Override
	public void linkModel(final IJARModel ijarm) {
		model = ijarm;
	}
	@Override
	public void forceUpdate() {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}
}
