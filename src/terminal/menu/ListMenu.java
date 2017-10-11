package terminal.menu;

import terminal.core.QueryEvent;
import terminal.core.Terminal;
import terminal.core.TerminalKeylistener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public abstract class ListMenu<E> extends JPanel {
    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public void fireEvent(QueryEvent e){};

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static synchronized <E> E queryMenu(Terminal terminal, ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        terminal.getInputComponent().removeKeyListener(terminal.getInputComponent().getKeyListeners()[0]);
        terminal.getInputComponent().addKeyListener(menuKeyListener);
        terminal.getInputComponent().unmapArrows();
        terminal.getScrollPane().setViewportView(menu);
        terminal.getScrollPane().getViewport().setViewPosition(new Point(0,terminal.getOutputComponent().getHeight()));
        menu.addKeyListener(menuKeyListener);
        menu.requestFocusInWindow();
        synchronized (terminal) {
            try{
                terminal.wait();
                obj = menu.getSelectedItem();
            }catch (InterruptedException ex){
                //ex.printStackTrace();
            }
        }
        terminal.getFrame().remove(menu);
        terminal.getInputComponent().removeKeyListener(menuKeyListener);
        terminal.getInputComponent().addKeyListener(new TerminalKeylistener(terminal.getInputComponent()));
        terminal.getInputComponent().remapArrows();
        terminal.getScrollPane().setViewportView(terminal.getOutputComponent());
        terminal.getOutputComponent().requestFocusInWindow();
        return obj;
    }

    public static synchronized <E> E queryObjectListMenu(Terminal terminal, Map<String, E> map, int direction){
        ObjectListMenu<E> menu = new ObjectListMenu<E>(terminal, map, direction);
        return queryMenu(terminal, menu);
    }
    public static synchronized String queryStringListMenu(Terminal terminal, String[] strings, int direction){
        StringListMenu menu = new StringListMenu(terminal, strings , direction);
        return queryMenu(terminal,menu);
    }
}
