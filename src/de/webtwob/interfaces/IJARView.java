package de.webtwob.interfaces;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public interface IJARView extends IJARLinkable, IJARRunable{

	/**
	 * causes the view to update all components
	 * */
	void forceUpdate();

}
