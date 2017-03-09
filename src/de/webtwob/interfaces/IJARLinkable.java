package de.webtwob.interfaces;

import de.webtwob.model.menu.ModeModel;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public interface IJARLinkable {

    /**
     * link this to a model
     * @param ijarm the Model this will be linked to
     * */
    void linkModel(ModeModel ijarm);

}
