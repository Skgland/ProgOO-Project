package de.webtwob.model;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IMenuEntry;

/**
 * @author Bennet Blessmann
 *         Created on 04.02.2017.
 */
public class InputMenuEntry implements IMenuEntry {

	private IJARInput in;
	private boolean   active;
	private IJARModel model;

	public InputMenuEntry(IJARInput input) {

		in = input;
	}

	@Override
	public String getText() {

		return (in.isEnabled() ? "Disable " : "Enable ") + in;
	}
	@Override
	public boolean isActive() {

		return active;
	}
	@Override
	public void setActive(boolean b) {

		active = b;
	}

	@Override
	public void executeAction() {

		if (!in.isEnabled() || active) {
			in.setEnabled(!in.isEnabled());
			model.updateViews();
		}
	}

	@Override
	public String toString() {

		return getText();
	}
	public void setModel(IJARModel model) {

		this.model = model;
	}
}