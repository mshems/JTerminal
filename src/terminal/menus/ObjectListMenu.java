package terminal.menus;

import terminal.core.Terminal;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ObjectListMenu<E> extends ListMenu<E> {
    private Terminal listener;
    private LinkedList<JLabel> labels;
    private Map<String,  E> itemMap;
    private int selection;

    private Color background = new Color(33,33,33);
    private Color foreground = new Color(245,245,245);
    private Color highlight = new Color(220, 220, 220);

    public ObjectListMenu(Terminal listener, Map<String, E> itemMap, int direction){
        this.listener = listener;
        this.labels = new LinkedList<>();
        this.itemMap = itemMap;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(background);
        this.setForeground(foreground);
        this.setBorder(BorderFactory.createLineBorder(background, 5));
        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }

    private void initHorizontalMenu(){
        JTextArea textArea = new JTextArea();
        textArea.setBackground(background);
        textArea.setForeground(foreground);
        textArea.setText(Terminal.getOutputComponent(listener).getText());
        textArea.setFont(new Font("consolas", Font.PLAIN, listener.getFontSize()));
        textArea.setEditable(false);
        textArea.setFocusable(false);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(background);
        labelPanel.setForeground(foreground);

        makeLabels();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for(JLabel l:labels){
            labelPanel.add(l);
        }
        this.add(textArea);
        this.add(labelPanel);
    }

    private void initVerticalMenu(){
        JTextArea textArea = new JTextArea();
        textArea.setBackground(background);
        textArea.setForeground(foreground);
        textArea.setText(Terminal.getOutputComponent(listener).getText());
        textArea.setFont(new Font("consolas", Font.PLAIN, listener.getFontSize()));
        textArea.setEditable(false);
        textArea.setFocusable(false);

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(background);
        menuPanel.setForeground(foreground);
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(background);
        labelPanel.setForeground(foreground);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        menuPanel.add(labelPanel);

        makeLabels();
        for(JLabel l:labels){
            labelPanel.add(l);
        }
        this.add(textArea);
        this.add(menuPanel);
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
