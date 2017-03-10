package de.webtwob;

import de.webtwob.interfaces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann
 *         Created on 02. Feb. 2017.
 */
public class TestModel implements IJARMenuModel, IJARGameModel {

    public boolean jump;
    public boolean sneak;
    private Mode mode = Mode.GAME;
    @Override
    public boolean cycle() {

        return true;
    }    @Override
    public long getTime() {

        return -1;
    }
    @Override
    public void jump() {

        if (mode == Mode.GAME) {
            System.out.println("Jump");
            jump = true;
        }
    }
    @Override
    public void doSelect() {

        if (mode == Mode.MENU) {
            System.out.println("Selected");
        }
    }    @Override
    public long getScore() {

        return -1;
    }
    @Override
    public void select(final int i) {

    }    @Override
    public void reset() {

    }
    @Override
    public void up() {

        if (mode == Mode.MENU) {
            System.out.println("UP");
        }
    }
    @Override
    public void down() {

        if (mode == Mode.MENU) {
            System.out.println("DOWN");
        }

    }
    @Override
    public List<IMenuEntry> getMenuEntries() {

        return new ArrayList<>();
    }
    @Override
    public IMenu getCurrentMenu() {

        return null;
    }
    @Override
    public int getSelectedIndex() {

        return 0;
    }    @Override
    public void setSneaking(final boolean sneak) {

        this.sneak = sneak;
        if (mode == Mode.GAME) {
            System.out.println(sneak ? "Sneaking" : "Not Sneaking");
        }
    }
    @Override
    public void setInputList(final List<IJARInput> inputList) {

    }
    @Override
    public boolean isDirty() {

        return false;
    }    @Override
    public boolean isSneaking() {

        return sneak && mode == Mode.GAME;
    }
    @Override
    public void clean() {

    }
    @Override
    public void setDirty() {

    }
    @Override
    public void pause() {

        if (mode == Mode.GAME) {
            mode = Mode.MENU;
            System.out.println("Pause");
        } else {
            mode = Mode.GAME;
            System.out.println("Unpause");
        }
    }    @Override
    public double getPlayerY() {

        return 0;
    }
    @Override
    public void gameOver() {

    }
    @SuppressWarnings("HardcodedLineSeparator")
    @Override
    public String toString() {

        return "[TestModel]:\n" +
                "\t[Sneaking]: " + sneak + "\n";
    }



    @Override
    public double getPlayerHeight() {

        return 0;
    }


    @Override
    public Rectangle[] getHurdles() {

        return new Rectangle[0];
    }



}