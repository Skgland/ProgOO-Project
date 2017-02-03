package de.webtwob.interfaces;

import java.util.List;

/**
 * @author Bennet Blessmann
 *         Created on 03.02.2017.
 */
public interface IMenu extends IMenuEntry {

	void add(IMenuEntry entry,int index);

	default void add(IMenuEntry entry){
		add(entry,size());
	}

	int size();
	IMenuEntry get(int i);

	List<IMenuEntry> getEntries();

	void setAction(Runnable r);

}
