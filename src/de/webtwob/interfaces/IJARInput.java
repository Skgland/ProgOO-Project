package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public interface IJARInput extends IJARLinkable, IJARRunable {

	/**
	 * @return a meue entry that enables/disables
	 * the current input based on it's current mode
	 */
	default IMenuEntry getMenuEntry() {

		return new IMenuEntry() {
			@Override
			public String getText() {

				return isEnabled() ? "Enable" : "Disable" + " Input: " + toString();
			}

			@Override
			public boolean isActive() {

				return true;
			}

			@Override
			public void executeAction() {

				setEnabled(!isEnabled());
			}
		};
	}

	void setEnabled(boolean b);

	boolean isEnabled();

	//inputs are not activly polling by default
	default void start() {

	}

	default void stop() {

	}

}
