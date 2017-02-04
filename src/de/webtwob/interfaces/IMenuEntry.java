package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public interface IMenuEntry {

	String getText();
	boolean isActive();
	void setActive(boolean b);
	void executeAction();
	default String getValue(){return null;}
	default void setValue(String update){}

}
