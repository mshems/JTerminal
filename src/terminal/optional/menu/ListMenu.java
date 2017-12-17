package terminal.optional.menu;

import terminal.core.*;
import terminal.core.event.QueryEvent;
import terminal.core.theme.Theme;
import terminal.core.theme.ThemedComponent;
import terminal.optional.menu.*;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class for all list-based menus
 * @param <E> type of entries in the menu
 */
public abstract class ListMenu<E> extends JPanel implements ThemedComponent {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    protected JTerminal terminal;
    protected String menuTitle;
    boolean cancelled = false;

    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public abstract void fireEvent(QueryEvent e);

    ListMenu(JTerminal t){
            this.terminal=t;
    }

    void initLayout(Theme theme){
        this.applyTheme(theme);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Displays a menu on a JTerminal and returns the user's selection.
     * @param menu the menu to be displayed
     * @param <E> the type of the items in the menu
     * @return the user's selection from the menu
     */
    public static synchronized <E> E queryMenu(ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        if(menu.menuTitle!=null) menu.terminal.out.println(menu.menuTitle);
        //terminal.getInputComponent().removeKeyListener(terminal.getInputComponent().getKeyListeners()[0]);
        menu.terminal.getInputComponent().addKeyListener(menuKeyListener);
        menu.terminal.getInputComponent().setEditable(false);
        menu.terminal.getScrollPaneView().add(menu);
        menu.terminal.getScrollPaneView().revalidate();
        menu.terminal.getFrame().revalidate();
        menu.terminal.getScrollPane().repaint();
        menu.terminal.getScrollPane().getViewport().setViewPosition(new Point(0, menu.terminal.getScrollPane().getHeight()));
        menu.requestFocusInWindow();
        menu.addKeyListener(menuKeyListener);
        try {
            menu.terminal.wait();
            obj = menu.getSelectedItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        menu.terminal.getInputComponent().removeKeyListener(menuKeyListener);
        //terminal.getInputComponent().addKeyListener(new JTerminalKeylistener(terminal.getInputComponent()));
        menu.terminal.getInputComponent().setEditable(true);
        menu.terminal.getScrollPaneView().remove(menu);
        menu.terminal.getFrame().revalidate();
        menu.terminal.getScrollPane().repaint();
        menu.terminal.getScrollPane().getViewport().setViewPosition(new Point(0, menu.terminal.getScrollPane().getHeight()));
        menu.terminal.getInputComponent().requestFocusInWindow();
        return obj;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(terminal.getOutputComponent().getPreferredSize().width, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(terminal.getOutputComponent().getPreferredSize().width, super.getPreferredSize().height);
    }

    @Override
    public void applyTheme(Theme theme){
        this.setBackground(theme.backgroundColor);
        this.setForeground(theme.foregroundColor);
    }

    public void setMenuTitle(String title){
        this.menuTitle = title;
    }
}
