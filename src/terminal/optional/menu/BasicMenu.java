package terminal.optional.menu;

import terminal.core.event.QueryEvent;
import terminal.core.JTerminal;

import javax.swing.*;
import java.awt.*;

public class BasicMenu extends ListMenu<String> {
    private JTerminal terminal;
    private String[] strings;
    private JLabel[] labels;
    private int selection;


    BasicMenu(JTerminal term, String[] strings, int direction){
        this.terminal = term;
        this.labels = new JLabel[strings.length];
        this.strings = strings;
        initLayout(terminal.getTheme());
        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }

    private void initHorizontalMenu(){
        JTextArea textArea = terminal.getOutputComponent();
        Font f = terminal.getTheme().font;
        textArea.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
        labelPanel.setBorder(BorderFactory.createLineBorder(terminal.getTheme().foregroundColor,1));

        makeLabels();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (JLabel l:labels) {
            labelPanel.add(l);
        }
        this.add(textArea);
        this.add(labelPanel);
    }

    private void initVerticalMenu(){
        JTextArea textArea = terminal.getOutputComponent();
        Font f = terminal.getTheme().font;
        textArea.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(terminal.getTheme().backgroundColor);
        menuPanel.setForeground(terminal.getTheme().foregroundColor);
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
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
            label.setBackground(terminal.getTheme().backgroundColor);
            label.setForeground(terminal.getTheme().foregroundColor);
            label.setOpaque(true);
            Font f = terminal.getTheme().font;
            label.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
            labels[i] = label;
        }
    }
    @Override
    public void selectItem(int n){
        selection = n;
        labels[selection].setForeground(terminal.getTheme().backgroundColor);
        labels[selection].setBackground(terminal.getTheme().highlightColor);
    }
    @Override
    public void deselectItem(){
        labels[selection].setBackground(terminal.getTheme().backgroundColor);
        labels[selection].setForeground(terminal.getTheme().foregroundColor);
    }

    @Override
    public int getNumLabels() {
        return strings.length;
    }

    @Override
    public void fireEvent (QueryEvent e){
        if(terminal !=null){
            terminal.queryActionPerformed(e);
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
