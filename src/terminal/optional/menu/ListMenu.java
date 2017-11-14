package terminal.optional.menu;

import terminal.core.*;
import terminal.core.event.QueryEvent;
import terminal.core.theme.Theme;
import terminal.core.theme.ThemedComponent;

import javax.swing.*;
import java.awt.*;

public abstract class ListMenu<E> extends JPanel implements ThemedComponent {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public abstract void fireEvent(QueryEvent e);

    void initLayout(Theme theme){
        this.applyTheme(theme);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public static synchronized <E> E queryMenu(JTerminal terminal, ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        terminal.getInputComponent().removeKeyListener(terminal.getInputComponent().getKeyListeners()[0]);
        terminal.getInputComponent().addKeyListener(menuKeyListener);
        terminal.getInputComponent().setEditable(false);
        terminal.getScrollPane().setViewportView(menu);
        terminal.getScrollPane().getViewport().setViewPosition(new Point(0,terminal.getOutputComponent().getHeight()));
        menu.addKeyListener(menuKeyListener);
        menu.requestFocusInWindow();
        try {
            terminal.wait();
            obj = menu.getSelectedItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        terminal.getFrame().remove(menu);
        terminal.getInputComponent().removeKeyListener(menuKeyListener);
        terminal.getInputComponent().addKeyListener(new JTerminalKeylistener(terminal.getInputComponent()));
        terminal.getInputComponent().setEditable(true);
        terminal.getScrollPane().setViewportView(terminal.getOutputComponent());
        terminal.getOutputComponent().requestFocusInWindow();
        return obj;
    }

    @Override
    public void applyTheme(Theme theme){
        this.setBackground(theme.backgroundColor);
        this.setForeground(theme.foregroundColor);
    }
}
