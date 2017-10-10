package terminal.menus;

import javax.swing.*;

public abstract class ListMenu<E> extends JPanel {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public abstract void fireEvent(MenuEvent e);
}
