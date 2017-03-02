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

    private final List<IMenuEntry> entries = new ArrayList<>();
    private final String name;
    private Runnable     runner;
    private boolean active = true;

    BasicMenu(final String name){
        this.name = name;
    }

    @Override
    public void add(final IMenuEntry entry, final int index) {
            entries.add(index,entry);
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public IMenuEntry get(final int i) {
        return entries.get(i);
    }

    @Override
    public List<IMenuEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public void setAction(final Runnable r) {
        runner = r;
    }

    @Override
    public String getText() {
        return name;
    }
    @Override
    public boolean isActive() {
        return active;
    }
    @Override
    public void setActive(final boolean active) {
        this.active = true;
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
