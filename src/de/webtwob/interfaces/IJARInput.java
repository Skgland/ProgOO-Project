package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public interface IJARInput extends IJARLinkable,Runnable{

	/**
	 * an input may be passive therefor run by default does nothing
	 * */
	default void run() {}


}
