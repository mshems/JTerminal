package terminal.optional.menu;

public class MenuReturnObject<E> {
    public E returnObject;
    public int modifiers = 0;

    public MenuReturnObject(E o){
        returnObject = o;
    }

    public MenuReturnObject(E o, int m){
        returnObject = o;
        modifiers = m;
    }
}
