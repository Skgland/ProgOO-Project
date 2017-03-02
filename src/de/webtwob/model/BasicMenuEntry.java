package de.webtwob.model;

import de.webtwob.interfaces.IMenuEntry;

/**
 * @author Bennet Blessmann
 * Created on 02. Feb. 2017.
 */
public class BasicMenuEntry implements IMenuEntry{

    private final Runnable run;
    private final String   name;
    private       boolean  active;

    public BasicMenuEntry(final Runnable r, final String title) {
        run = r;
        name = title;
        active = true;
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
        this.active = active;
    }

    @Override
    public void executeAction() {
        run.run();
    }

    @Override
    public String toString() {
        //noinspection StringConcatenationMissingWhitespace
        return "[Entry]: "+getText()+(getValue()!=null?" : "+getValue():"");
    }
}
