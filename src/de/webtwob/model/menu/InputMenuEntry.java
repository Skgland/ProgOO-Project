package de.webtwob.model.menu;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.IMenuEntry;

/**
 * @author Bennet Blessmann
 *         Created on 04.02.2017.
 */
public class InputMenuEntry implements IMenuEntry {

    private final IJARInput in;
    private       boolean   active;
    private       IJARMenuModel model;

    public InputMenuEntry(final IJARInput input) {

        in = input;
    }

    @Override
    public String getText() {

        return (in.isEnabled() ? "Disable" : "Enable") +' '+ in;
    }
    @Override
    public boolean isActive() {

        return active;
    }
    @Override
    public void setActive(final boolean active) {

        this.active = active;
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
    public void setModel(final IJARMenuModel model) {

        this.model = model;
    }
}
