package de.webtwob.model;

import de.webtwob.interfaces.IMenu;
import de.webtwob.interfaces.IMenuEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann
 *         Created on 03.02.2017.
 */
public class BasicMenu implements IMenu {

	private List<IMenuEntry> entries = new ArrayList<>();
	private final String name;
	private Runnable     runner;

	BasicMenu(String name){
		this.name = name;
	}

	@Override
	public void add(IMenuEntry entry, int index) {
			entries.add(index,entry);
	}

	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public IMenuEntry get(int i) {
		return entries.get(i);
	}

	@Override
	public List<IMenuEntry> getEntries() {
		return new ArrayList<>(entries);
	}

	@Override
	public void setAction(Runnable r) {
		runner = r;
	}

	@Override
	public String getText() {
		return name;
	}
	@Override
	public boolean isActive() {
		return true;
	}
	@Override
	public void executeAction() {
		runner.run();
	}

	@Override
	public String toString() {

		return "[Menu]: "+name;
	}
}
