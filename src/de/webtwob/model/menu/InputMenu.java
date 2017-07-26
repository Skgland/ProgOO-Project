package de.webtwob.model.menu;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.IMenuEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann Created on 03.02.2017.
 *         <p>
 *         This class implements the menu where IJARInputs can be disabled
 */
public class InputMenu extends BasicMenu {

    private final IJARMenuModel model;
    private       List<IJARInput>      inputs       = new ArrayList<>();
    private final List<InputMenuEntry> inputEntries = new ArrayList<>();

    public InputMenu(final IJARMenuModel model) {
        super("Inputs");
        this.model = model;
    }

    public void updateInputs(final List<IJARInput> inp) {
        inputs = new ArrayList<>(inp);
        inputEntries.clear();
        int active = 0;
        for(final IJARInput in : inputs) {
            inputEntries.add(new InputMenuEntry(in));
            if(in.isEnabled()) {
                active++;
            }
        }
        for(final InputMenuEntry ime : inputEntries) {
            ime.setActive(active > 1);
        }
        model.setDirty();
    }

    @Override
    public int size() {
        return super.size() + inputEntries.size();
    }

    @Override
    public IMenuEntry get(final int i) {

        updateActive();
        if(i < inputEntries.size()) {
            return inputEntries.get(i);
        } else {
            return super.get(i - inputEntries.size());
        }
    }

    @Override
    public List<IMenuEntry> getEntries() {

        final List<IMenuEntry> entries = new ArrayList<>();
        entries.addAll(getInputEntries());
        entries.addAll(super.getEntries());
        return entries;
    }

    @Override
    public String toString() {
        return "InputMenu{" + "model=" + model + ", inputs=" + inputs + ", inputEntries=" + inputEntries + '}';
    }

    private void updateActive() {

        int act = 0;
        for(final IJARInput ijarInput : inputs) {
            if(ijarInput.isEnabled()) {
                act++;
            }
        }
        for(final InputMenuEntry ijarInput : inputEntries) {
            ijarInput.setActive(act > 1);
        }
    }

    private List<IMenuEntry> getInputEntries() {
        updateActive();
        return new ArrayList<>(inputEntries);
    }
}
