package de.webtwob.input.action;

import de.webtwob.interfaces.IJARLinkable;

import javax.swing.*;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 *
 * A LinkableAction is an Action that can be linked to a model
 */
public abstract class LinkableAction extends AbstractAction implements IJARLinkable {

    volatile IJARModel model;

    LinkableAction(){
        //until linked this action is disabled
        setEnabled(false);
    }

    @Override
    public void linkModel(final IJARModel ijarm) {
        model = ijarm;
        //if it's an actual model this Action can be activated
        setEnabled(model!=null);
    }
}
