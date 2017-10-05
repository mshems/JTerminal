package ui;

import terminal.MenuEvent;

import javax.swing.*;

public abstract class ListMenu<E> extends JPanel {
    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public abstract void fireEvent(MenuEvent e);
}
