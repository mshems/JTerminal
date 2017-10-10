package terminal.menus;

import terminal.core.Terminal;

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

    public StringListMenu(Terminal listener, String[] strings, int direction){
        this.listener = listener;
        this.labels = new JLabel[strings.length];
        this.strings = strings;
        this.setBackground(background);
        this.setForeground(foreground);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
        labelPanel.setBorder(BorderFactory.createLineBorder(foreground,1));

        makeLabels();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (JLabel l:labels) {
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
        for(int i=0; i<strings.length; i++){
            JLabel label = new JLabel(strings[i]);
            label.setForeground(foreground);
            label.setBackground(background);
            label.setOpaque(true);
            label.setFont(new Font("consolas", Font.PLAIN, listener.getFontSize()));
            labels[i] = label;
        }
    }
    @Override
    public void selectItem(int n){
        selection = n;
        labels[selection].setForeground(background);
        labels[selection].setBackground(highlight);
    }
    @Override
    public void deselectItem(){
        labels[selection].setForeground(foreground);
        labels[selection].setBackground(background);
    }

    @Override
    public int getNumLabels() {
        return strings.length;
    }

    @Override
    public void fireEvent (MenuEvent e){
        if(listener!=null){
            listener.menuActionPerformed(e);
        }
    }

    @Override
    public String getSelectedItem(){
        return this.strings[selection];
    }

    @Override
    public int getSelection() {
        return selection;
    }
}
