package de.webtwob.input.action;

import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 * Created on 01. Feb. 2017.
 */
public class PauseAction extends LinkableAction{
    @Override
    public void actionPerformed(final ActionEvent e) {
        model.pause();
    }
}
