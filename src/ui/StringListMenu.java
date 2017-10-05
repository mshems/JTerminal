package ui;

import terminal.MenuEvent;
import terminal.Terminal;

import javax.swing.*;
import java.awt.*;

public class StringListMenu extends ListMenu {
    private Terminal listener;
    private String[] strings;
    private JLabel[] labels;
    private int selection;

    private Color background = new Color(33,33,33);
    private Color foreground = new Color(245,245,245);
    private Color highlight = new Color(220, 220, 220);

    public StringListMenu(Terminal listener, String[] strings){
        this.listener = listener;
        this.labels = new JLabel[strings.length];
        this.strings = strings;
        this.setAlignmentY(Component.TOP_ALIGNMENT);
        initHorizontalMenu();
        selectItem(0);
    }

    public StringListMenu(Terminal listener, String[] strings, int direction){
        this.listener = listener;
        this.labels = new JLabel[strings.length];
        this.strings = strings;
        this.setAlignmentY(Component.TOP_ALIGNMENT);
        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }

    private void initHorizontalMenu(){
        this.setBorder(BorderFactory.createLineBorder(background, 5));
        this.setBackground(background);
        this.setForeground(foreground);
        makeLabels();
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        for(int i=0; i<labels.length; i++){
            this.add(labels[i]);
            /*if(i<labels.length-1) {
                JLabel divider  = new JLabel("|");
                divider.setForeground(foreground);
                this.add(divider);
            }*/
        }
    }

    private void initVerticalMenu(){
        this.setBorder(BorderFactory.createLineBorder(background, 5));
        this.setBackground(background);
        this.setForeground(foreground);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        makeLabels();
        for(JLabel l:labels){
            this.add(l);
        }
    }

    private void makeLabels(){
        for(int i=0; i<strings.length; i++){
            JLabel label = new JLabel(strings[i]);
            label.setForeground(foreground);
            label.setBackground(background);
            label.setOpaque(true);
            label.setFont(new Font("consolas", Font.PLAIN, 17));
            labels[i] = label;
        }
    }
    public void selectItem(){
        labels[selection].setForeground(background);
        labels[selection].setBackground(highlight);
    }
    public void selectItem(int n){
        selection = n;
        labels[selection].setForeground(background);
        labels[selection].setBackground(highlight);
    }
    public void deselectItem(){
        labels[selection].setForeground(foreground);
        labels[selection].setBackground(background);
    }

    @Override
    public int getNumLabels() {
        return strings.length;
    }

    public void fireEvent (MenuEvent e){
        if(listener!=null){
            listener.menuActionPerformed(e);
        }
    }

    public String getSelectedItem(){
        return this.strings[selection];
    }

    @Override
    public int getSelection() {
        return selection;
    }
}
