package terminal.menu;

import terminal.core.QueryEvent;
import terminal.core.JTerminal;
import terminal.core.TerminalIOComponent;
import terminal.core.TerminalKeylistener;

import javax.swing.*;
import java.awt.*;

public abstract class ListMenu<E> extends JPanel {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public void fireEvent(QueryEvent e){};

    Color background = TerminalIOComponent.DEFAULT_THEME[0];
    Color foreground = TerminalIOComponent.DEFAULT_THEME[1];
    Color highlight = TerminalIOComponent.DEFAULT_THEME[3];

    void initLayout(){
        this.setBackground(background);
        this.setForeground(foreground);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(background, 5));
    }

    public static synchronized <E> E queryMenu(JTerminal terminal, ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        terminal.getInputComponent().removeKeyListener(terminal.getInputComponent().getKeyListeners()[0]);
        terminal.getInputComponent().addKeyListener(menuKeyListener);
        terminal.getInputComponent().unmapArrows();
        terminal.getScrollPane().setViewportView(menu);
        terminal.getScrollPane().getViewport().setViewPosition(new Point(0,terminal.getOutputComponent().getHeight()));
        menu.addKeyListener(menuKeyListener);
        menu.requestFocusInWindow();
        try {
            terminal.wait();
            obj = menu.getSelectedItem();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        terminal.getFrame().remove(menu);
        terminal.getInputComponent().removeKeyListener(menuKeyListener);
        terminal.getInputComponent().addKeyListener(new TerminalKeylistener(terminal.getInputComponent()));
        terminal.getInputComponent().remapArrows();
        terminal.getScrollPane().setViewportView(terminal.getOutputComponent());
        terminal.getOutputComponent().requestFocusInWindow();
        return obj;
    }

    /*
    public static synchronized <E> E queryObjectMenu(Terminal terminal, Map<String, E> map, int direction){
        ObjectMenu<E> menu = new ObjectMenu<E>(terminal, map, direction);
        return queryMenu(terminal, menu);
    }
    public static synchronized String queryBasicMenu(Terminal terminal, String[] strings, int direction){
        BasicMenu menu = new BasicMenu(terminal, strings , direction);
        return queryMenu(terminal,menu);
    }
    */
}
