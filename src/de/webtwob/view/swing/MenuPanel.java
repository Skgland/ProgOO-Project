package de.webtwob.view.swing;

import de.webtwob.interfaces.IMenu;
import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NORTH;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 */
public class MenuPanel extends JPanel {

    private       int                selected     = -1;
    private final GridBagConstraints contentConst = new GridBagConstraints();
    private final GridBagConstraints titleConst   = new GridBagConstraints();
    private final JLabel             title        = new JLabel();
    private final Border             butdef       = UIManager.getBorder("Button.border");
    private final Border comp;
    private final List<JButton> buttonList = new ArrayList<>();

    {
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setAlignmentY(JLabel.TOP_ALIGNMENT);
        titleConst.anchor = NORTH;
        titleConst.gridx = 0;
        titleConst.gridy = 0;
        titleConst.fill = BOTH;
        titleConst.gridwidth = 2;
        titleConst.gridheight = 1;
        titleConst.weightx = 2;
        titleConst.weighty = 1;

        contentConst.weightx = 1;
        contentConst.insets = new Insets(1,1,1,1);
        contentConst.anchor = CENTER;
        contentConst.gridheight = 1;
        contentConst.gridwidth = 1;
        contentConst.fill = BOTH;
        contentConst.weighty = 2;

        comp = BorderFactory.createCompoundBorder(butdef, BorderFactory.createDashedBorder(null));
    }

    public MenuPanel() {
        setLayout(new GridBagLayout());
    }

    public void updateMenu(final IMenu im) {
        removeAll();
        add(title,titleConst);
        if (im != null) {
            title.setText(im.getText());
            buttonList.clear();
            contentConst.gridy = 1;
            JButton button;
            for (final IMenuEntry iMenuEntry : im.getEntries()) {
                contentConst.gridx = 0;
                contentConst.gridwidth = iMenuEntry.getValue()==null?2:1;
                button = new JButton(iMenuEntry.getText());
                buttonList.add(button);
                add(button, contentConst);
                button.addActionListener((actionEvent) -> iMenuEntry.executeAction());
                if (iMenuEntry.getValue() != null) {
                    contentConst.gridx = 1;
                    add(new JTextField(iMenuEntry.getValue()), contentConst);
                }
                contentConst.gridy++;
            }
        } else {
            title.setText("Menu is NULL!");
        }
    }

    public void updateSelection(final int i) {

        if (selected >= 0 && selected < buttonList.size()) {
            buttonList.get(selected).setBorder(butdef);
        }
        if (i >= 0 && i < buttonList.size()) {
            buttonList.get(i).setBorder(comp);
            selected = i;
        }
    }
}
