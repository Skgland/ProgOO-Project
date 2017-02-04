package de.webtwob.model;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IMenuEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann
 *         Created on 03.02.2017.
 */
public class InputMenu extends BasicMenu {

	private final IJARModel model;
	List<IJARInput> inputs = new ArrayList<>();
	List<InputMenuEntry> inputEntries = new ArrayList<>();

	InputMenu(IJARModel m) {

		super("Inputs");
		model = m;
	}

	public void updateInputs(List<IJARInput> inp) {
		inputs = new ArrayList<>(inp);
		inputEntries.clear();
		int active = 0;
		for (IJARInput in : inputs) {
			inputEntries.add(new InputMenuEntry(in));
			if (in.isEnabled()) {
				active++;
			}
		}
		for (InputMenuEntry ime : inputEntries) {
			ime.setActive(active > 1);
			ime.setModel(model);
		}
	}

	@Override
	public int size() {
		return super.size() + inputEntries.size();
	}

	@Override
	public IMenuEntry get(int i) {

		updateActive();
		if (i < inputEntries.size()) {
			return inputEntries.get(i);
		} else {
			return super.get(i - inputEntries.size());
		}
	}
	private void updateActive() {

		int act = 0;
		for (IJARInput ijarInput : inputs) {
			if (ijarInput.isEnabled()) {
				act++;
			}
		}
		for (InputMenuEntry ijarInput : inputEntries) {
			ijarInput.setActive(act > 1);
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

		updateActive();
		List<IMenuEntry> entry = new ArrayList<>(inputEntries);
		return entry;
	}

}
