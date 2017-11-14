package terminal.optional.menu;

import terminal.core.JTerminal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MenuBuilder{
    int direction = ListMenu.HORIZONTAL;

    public MenuBuilder setDirection(int direction){
        this.direction = direction;
        return this;
    }

    public BasicMenu buildMenu(JTerminal terminal, String[] labels){
        return new BasicMenu(terminal, labels, this.direction);
    }

    public <E> ObjectMenu<E> buildMenu(JTerminal terminal, Map<String, E> map){
        return new ObjectMenu<>(terminal, map, this.direction);
    }
    public <E> ObjectMenu<E> buildMenu(JTerminal terminal, Collection<E> list, LabelFactory<E> labelFactory){
        return new ObjectMenu<>(terminal, list, this.direction, labelFactory);
    }
}
