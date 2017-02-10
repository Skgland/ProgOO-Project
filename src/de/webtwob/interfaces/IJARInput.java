package de.webtwob.interfaces;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public interface IJARInput extends IJARLinkable, IJARRunable {

	void setEnabled(boolean enable);

	boolean isEnabled();

	//inputs are not actively polling by default
	default void start() {

	}

	default void stop() {

	}

}
