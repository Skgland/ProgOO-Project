package de.webtwob.view.swing;

import de.webtwob.interfaces.IMenu;
import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.GridBagConstraints.CENTER;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 */
public class MenuPanel extends JPanel {

    int selected = -1;
    Box titleBox = Box.createHorizontalBox();
    Box contentBox = Box.createVerticalBox();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    JLabel title = new JLabel();
    JPanel content = new JPanel();
    Border butdef = UIManager.getBorder("Button.border");
    Border comp;
    List<JButton> buttonList = new ArrayList<>();

    {
        titleBox.add(title);
        contentBox.add(content);
        contentBox.setAlignmentY(TOP_ALIGNMENT);
        content.setAlignmentY(TOP_ALIGNMENT);
        content.setLayout(new GridBagLayout());
        gridBagConstraints.anchor = CENTER;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        comp = BorderFactory.createCompoundBorder(butdef,BorderFactory.createDashedBorder(null));
    }

    public MenuPanel(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(titleBox);
        add(contentBox);
    }

    public void updateMenu(IMenu im){
        if(im!=null){
        title.setText(im.getText());
        content.removeAll();
        buttonList.clear();
        gridBagConstraints.gridy = 0;
        JButton button;
        for(IMenuEntry iMenuEntry:im.getEntries()){
            gridBagConstraints.gridx = 0;
            button = new JButton(iMenuEntry.getText());
            buttonList.add(button);
            content.add(button,gridBagConstraints);
            button.addActionListener((actionEvent)-> iMenuEntry.executeAction());
            if(iMenuEntry.getValue()!=null){
                gridBagConstraints.gridx = 1;
                content.add(new JTextField(iMenuEntry.getValue()),gridBagConstraints);
            }
            gridBagConstraints.gridy++;
        }
        }else{
            title.setText("Menu is NULL!");
            contentBox.removeAll();
        }
    }

   public void updateSelection(int  i) {
       if(selected>=0&&selected<buttonList.size()){
           buttonList.get(selected).setBorder(butdef);
       }
       if(i>=0&&i<buttonList.size()){
           buttonList.get(i).setBorder(comp);
           selected = i;
        }
    }
}
