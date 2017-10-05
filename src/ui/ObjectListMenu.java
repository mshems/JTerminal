package ui;

import terminal.MenuEvent;
import terminal.Terminal;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ObjectListMenu<E> extends ListMenu<E> {
    private Terminal listener;
    private LinkedList<JLabel> labels;
    private Map<String,  E> itemMap;
    private int selection;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private Color background = new Color(33,33,33);
    private Color foreground = new Color(245,245,245);
    private Color highlight = new Color(220, 220, 220);

    public ObjectListMenu(Terminal listener, Map<String, E> itemMap, int direction){
        this.listener = listener;
        this.labels = new LinkedList<>();
        this.itemMap = itemMap;
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
        for(JLabel l:labels){
            this.add(l);
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
        for(String str:itemMap.keySet()){
            JLabel label = new JLabel(str);
            label.setForeground(foreground);
            label.setBackground(background);
            label.setOpaque(true);
            label.setFont(new Font("consolas", Font.PLAIN, 17));
            labels.add(label);
        }
    }

    public void selectItem(){
        labels.get(selection).setForeground(background);
        labels.get(selection).setBackground(highlight);
    }
    public void selectItem(int n){
        selection = n;
        labels.get(selection).setForeground(background);
        labels.get(selection).setBackground(highlight);
    }
    public void deselectItem(){
        labels.get(selection).setForeground(foreground);
        labels.get(selection).setBackground(background);
    }

    public void fireEvent (MenuEvent e){
        if(listener!=null){
            listener.menuActionPerformed(e);
        }
    }

    public int getNumLabels(){
        return labels.size();
    }

    public int getSelection() {
        return selection;
    }

    public E getSelectedItem(){
        return this.itemMap.get(labels.get(selection).getText());
    }
}
