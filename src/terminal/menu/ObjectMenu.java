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
        initLayout();
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
        initLayout();
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
        textArea.setBackground(background);
        textArea.setForeground(foreground);
        textArea.setText(terminal.getOutputComponent().getText());
        textArea.setFont(new Font("consolas", Font.PLAIN, terminal.getFontSize()));
        textArea.setEditable(false);
        textArea.setFocusable(false);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(background);
        labelPanel.setForeground(foreground);

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
        textArea.setText(terminal.getOutputComponent().getText());
        textArea.setFont(new Font("consolas", Font.PLAIN, terminal.getFontSize()));
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

    public void selectItem(int n){
        selection = n;
        labels.get(selection).setForeground(background);
        labels.get(selection).setBackground(highlight);
    }
    public void deselectItem(){
        labels.get(selection).setForeground(foreground);
        labels.get(selection).setBackground(background);
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
