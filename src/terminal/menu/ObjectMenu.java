package terminal.menu;

import terminal.core.QueryEvent;
import terminal.core.JTerminal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectMenu<E> extends ListMenu<E> {
    private JTerminal terminal;
    private LinkedList<JLabel> labels;
    private Map<String,  E> itemMap;
    private int selection;

    ObjectMenu(JTerminal term, List<E> objects, int direction, LabelFactory<E> labelFactory){
        this.terminal = term;
        this.labels = new LinkedList<>();
        itemMap = new LinkedHashMap<String, E>();
        initLayout(terminal.getTheme());
        for(E o:objects){
            itemMap.put(labelFactory.toLabel(o), o);
        }
        makeLabels();
        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }


    ObjectMenu(JTerminal term, Map<String, E> itemMap, int direction){
        this.terminal = term;
        this.labels = new LinkedList<>();
        this.itemMap = itemMap;
        initLayout(terminal.getTheme());
        makeLabels();
        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }

    private void initHorizontalMenu(){
        JTextArea textArea = new JTextArea();
        textArea.setBackground(terminal.getTheme().backgroundColor);
        textArea.setForeground(terminal.getTheme().foregroundColor);
        textArea.setText(terminal.getOutputComponent().getText());
        Font f = terminal.getTheme().font;
        textArea.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
        textArea.setEditable(false);
        textArea.setFocusable(false);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);

        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for(JLabel l:labels){
            labelPanel.add(l);
        }
        this.add(textArea);
        this.add(labelPanel);
    }

    private void initVerticalMenu(){
        JTextArea textArea = new JTextArea();
        textArea.setBackground(terminal.getTheme().backgroundColor);
        textArea.setForeground(terminal.getTheme().foregroundColor);
        textArea.setText(terminal.getOutputComponent().getText());
        Font f = terminal.getTheme().font;
        textArea.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
        textArea.setEditable(false);
        textArea.setFocusable(false);

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(terminal.getTheme().backgroundColor);
        menuPanel.setForeground(terminal.getTheme().foregroundColor);
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        menuPanel.add(labelPanel);

        for(JLabel l:labels){
            labelPanel.add(l);
        }
        this.add(textArea);
        this.add(menuPanel);
    }

    private void makeLabels(){
        for(String str:itemMap.keySet()){
            JLabel label = new JLabel(str);
            label.setBackground(terminal.getTheme().backgroundColor);
            label.setForeground(terminal.getTheme().foregroundColor);
            label.setOpaque(true);
            Font f = terminal.getTheme().font;
            label.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
            labels.add(label);
        }
    }

    public void selectItem(int n){
        selection = n;
        labels.get(selection).setForeground(terminal.getTheme().backgroundColor);
        labels.get(selection).setBackground(terminal.getTheme().highlightColor);
    }
    public void deselectItem(){
        labels.get(selection).setBackground(terminal.getTheme().backgroundColor);
        labels.get(selection).setForeground(terminal.getTheme().foregroundColor);
    }

    public void fireEvent (QueryEvent e){
        if(terminal !=null){
            terminal.queryActionPerformed(e);
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
