package de.webtwob.model;

import de.webtwob.interfaces.*;
import de.webtwob.model.menu.BasicMenu;
import de.webtwob.model.menu.BasicMenuEntry;
import de.webtwob.model.menu.InputMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static de.webtwob.interfaces.Mode.GAME;

/**
 * @author Bennet Blessmann
 *         Created on 09.03.2017.
 */
public class BasicMenuModel implements IJARMenuModel {

    /**
     * all inputs added to the model
     */
    private final ArrayList<IJARInput> inputs         = new ArrayList<>();
    /**
     * Stores the menu traversal so we can go back
     */
    private final LinkedList<IMenu>    back           = new LinkedList<>();
    private final IMenu                MAIN_MENU      = new BasicMenu("Main Menu");
    private final IMenu                PAUSE_MENU     = new BasicMenu("Pause Menu");
    private final IMenu                SETTINGS_MENU  = new BasicMenu("Settings");
    private final IMenu                GAME_OVER_MENU = new BasicMenu("Game Over");
    private final InputMenu            INPUTS_MENU    = new InputMenu(this);
    private final IMenuEntry           QUIT           = new BasicMenuEntry(() -> {
        System.exit(0);
    }, "Quit");
    private IJARGameModel game;
    private ModeModel     mode;
    private       boolean    dirty    = false;
    private final IMenuEntry CONTINUE = new BasicMenuEntry(() -> {
        mode.setMode(GAME);
        dirty = true;
    }, "Continue");
    private final IMenuEntry START    = new BasicMenuEntry(() -> {
        game.reset();
        mode.setMode(GAME);
        dirty = true;
    }, "Start");
    /**
     * The current menu
     */
    private volatile IMenu menu;
    /**
     * The currently selected menu item
     */
    private volatile int        selection = 0;
    private final    IMenuEntry BACK      = new BasicMenuEntry(() -> {
        menu = back.pop();
        selection = 0;
        dirty = true;
    }, "Back");
    private final    IMenuEntry GOTO_MAIN = new BasicMenuEntry(() -> {
        menu = MAIN_MENU;
        selection = 0;
        dirty = true;
    }, "Main Menu");

    {
        //setup all menus
        MAIN_MENU.add(START);
        MAIN_MENU.add(SETTINGS_MENU);
        MAIN_MENU.add(QUIT);

        PAUSE_MENU.add(CONTINUE);
        PAUSE_MENU.add(SETTINGS_MENU);
        PAUSE_MENU.add(GOTO_MAIN);

        SETTINGS_MENU.add(INPUTS_MENU);
        SETTINGS_MENU.add(BACK);

        INPUTS_MENU.add(BACK);
        INPUTS_MENU.setAction(() -> {
            back.addFirst(menu);
            selection = 0;
            menu = INPUTS_MENU;
            dirty = true;
        });

        SETTINGS_MENU.setAction(() -> {
            back.addFirst(menu);
            selection = 0;
            menu = SETTINGS_MENU;
            dirty = true;
        });

        GAME_OVER_MENU.add(new BasicMenuEntry(() -> {
        }, "Score") {
            @Override
            public String getValue() {

                return game.getScore() + "";
            }
        });

        GAME_OVER_MENU.add(START);
        GAME_OVER_MENU.add(SETTINGS_MENU);
        GAME_OVER_MENU.add(GOTO_MAIN);

        menu = MAIN_MENU;
    }

    public BasicMenuModel(IJARGameModel ijargm, ModeModel mode) {

        game = ijargm;
        this.mode = mode;
    }
    @Override
    public void doSelect() {

        if (selection >= 0 && selection < menu.size()) {
            final IMenuEntry ime = menu.get(selection);
            //noinspection LawOfDemeter
            ime.executeAction();
        }
    }

    @Override
    public void select(final int i) {

        selection = i;
    }

    @Override
    public void up() {

        selection = (selection + menu.size() - 1) % menu.size();

    }

    @Override
    public void down() {
            selection = (selection + 1) % menu.size();
    }

    @Override
    public List<IMenuEntry> getMenuEntries() {

        return new ArrayList<>(menu.getEntries());
    }

    @Override
    public IMenu getCurrentMenu() {

        return menu;
    }

    @Override
    public int getSelectedIndex() {

        return selection;
    }

    @Override
    public void setInputList(List<IJARInput> inputList) {

        INPUTS_MENU.updateInputs(inputs);
    }
    @Override
    public boolean isDirty() {

        return dirty;
    }
    @Override
    public void clean() {

        dirty = false;
    }
    @Override
    public void setDirty() {
        dirty = true;
    }
    @Override
    public void pause() {
        menu = PAUSE_MENU;
    }
    @Override
    public void gameover() {

        menu = GAME_OVER_MENU;
    }
}
