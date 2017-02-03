package de.webtwob.model;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IMenuEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann
 *         Created on 03.02.2017.
 */
public class InputMenu extends BasicMenu {

	List<IJARInput> inputs;
	volatile int active = 0;

	InputMenu(List<IJARInput> inputs) {

		super("Inputs");
		for (IJARInput in : inputs) {
			if (in.isEnabled()) {
				active++;
			}
		}
		this.inputs = inputs;
	}

	@Override
	public int size() {

		return super.size() + inputs.size();
	}

	@Override
	public IMenuEntry get(int i) {
		List<IMenuEntry> list = getInputEntries();
		if (i < list.size()) {
			return list.get(i);
		} else {
			return super.get(i - list.size());
		}
	}

	@Override
	public List<IMenuEntry> getEntries() {

		List<IMenuEntry> entries = new ArrayList<>();
		entries.addAll(getInputEntries());
		entries.addAll(super.getEntries());
		return entries;
	}

	private List<IMenuEntry> getInputEntries() {

		List<IMenuEntry> entry = new ArrayList<>();

		for (IJARInput in : inputs) {
			//TODO
		}

		return entry;
	}

}
