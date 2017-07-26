package de.webtwob.view.swing;

import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.GridBagConstraints.*;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 * <p>
 * This class is there to display to menu of the game in the BasicView
 */
public class MenuPanel extends JPanel {

    private       int                selected     = -1;
    private final GridBagConstraints contentConst = new GridBagConstraints();
    private final GridBagConstraints titleConst   = new GridBagConstraints();
    private final JLabel             title        = new JLabel();
    private final Border             butdef       = UIManager.getBorder("Button.border");
    private final IJARMenuModel menu;
    private final Border        comp;
    private final List<JButton> buttonList = new ArrayList<>();

    {
        /*
         * setup basic layout
         * */
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setAlignmentY(Component.TOP_ALIGNMENT);
        titleConst.anchor = NORTH;
        titleConst.gridx = 0;
        titleConst.gridy = 0;
        titleConst.fill = BOTH;
        titleConst.gridwidth = 2;
        titleConst.gridheight = 1;
        titleConst.weightx = 2;
        titleConst.weighty = 1;

        contentConst.weightx = 1;
        contentConst.insets = new Insets(1, 1, 1, 1);
        contentConst.anchor = CENTER;
        contentConst.gridheight = 1;
        contentConst.gridwidth = 1;
        contentConst.fill = BOTH;
        contentConst.weighty = 2;

        comp = BorderFactory.createCompoundBorder(butdef, BorderFactory.createDashedBorder(null));
    }

    public MenuPanel(final IJARMenuModel menu) {
        this.menu = menu;
        setLayout(new GridBagLayout());
    }

    /**
     * updates the menu so it reflects the changes made to the model
     */
    public void updateMenu() {
        if(menu.isDirty()) {
            removeAll();
            add(title, titleConst);
            if(menu.getMenuEntries() != null) {
                title.setText(menu.getCurrentMenu().getText());
                buttonList.clear();
                contentConst.gridy = 1;
                JButton button;
                for(final IMenuEntry iMenuEntry : menu.getCurrentMenu().getEntries()) {
                    contentConst.gridx = 0;
                    contentConst.gridwidth = iMenuEntry.getValue() == null ? 2 : 1;
                    button = new JButton(iMenuEntry.getText());
                    buttonList.add(button);
                    add(button, contentConst);
                    button.addActionListener((actionEvent) -> {
                        iMenuEntry.executeAction();
                        menu.setDirty();
                    });
                    if(iMenuEntry.getValue() != null) {
                        contentConst.gridx = 1;
                        add(new JTextField(iMenuEntry.getValue()), contentConst);
                    }
                    contentConst.gridy++;
                }
            } else {
                title.setText("Menu is NULL!");
            }

            if(selected >= 0 && selected < buttonList.size()) {
                buttonList.get(selected).setBorder(butdef);
            }
            if(menu.getSelectedIndex() >= 0 && menu.getSelectedIndex() < buttonList.size()) {
                buttonList.get(menu.getSelectedIndex()).setBorder(comp);
                selected = menu.getSelectedIndex();
            }
            menu.clean();
        }
    }

    @Override
    public String toString() {
        return "MenuPanel{" + "selected=" + selected + ", contentConst=" + contentConst + ", titleConst=" +
                       titleConst + ", title=" + title + ", butdef=" + butdef + ", menu=" + menu + ", comp=" + comp +
                       ", buttonList=" + buttonList + '}';
    }
}
