package terminal.optional.menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuKeyListener implements KeyListener {
    private ListMenu menu;

    MenuKeyListener(ListMenu menu){
        super();
        this.menu = menu;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if(menu.getSelection()>0) {
                menu.deselectItem();
                menu.selectItem(menu.getSelection()-1);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if(menu.getSelection()<menu.getNumLabels()-1) {
                menu.deselectItem();
                menu.selectItem(menu.getSelection()+1);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(menu.getSelection()>0) {
                menu.deselectItem();
                menu.selectItem(menu.getSelection()-1);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(menu.getSelection()<menu.getNumLabels()-1) {
                menu.deselectItem();
                menu.selectItem(menu.getSelection()+1);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            int mod =0;
            if(e.isShiftDown()) mod = ListMenu.SHIFT;
            if(e.isControlDown()) mod = ListMenu.CTRL;
            if(e.isShiftDown() && e.isControlDown()) mod = ListMenu.CTRL_SHIFT;
            menu.fireEvent(new MenuQueryEvent(this, mod));
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            int mod=0;
            menu.cancelled = true;
            menu.fireEvent(new MenuQueryEvent(this, mod, true));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
