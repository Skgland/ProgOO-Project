package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public interface IJARView extends IJARLinkable, Runnable{

	/**
	 * a vieew should keep itself actively updated
	 * */
	void run();

}
