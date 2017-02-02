package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public interface IJARInput extends IJARLinkable,IJARRunable{

	//inputs are not activly polling by default
	default void start() {}
	default void stop() {}

}
