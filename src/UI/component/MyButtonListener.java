package UI.component;

import UI.UIConst;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyButtonListener implements MouseListener {
    private MyButton button;


    public MyButtonListener(MyButton button){
        this.button = button;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(button.getForeground() == UIConst.OPAQUE_L2){
            button.setForeground(UIConst.OPAQUE_L3);
            button.setOpaque(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(button.getForeground() == UIConst.OPAQUE_L2){
            button.setForeground(UIConst.OPAQUE_L3);
            button.setOpaque(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(button.getForeground() == UIConst.OPAQUE_L1){
            button.setForeground(UIConst.OPAQUE_L2);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(button.getForeground() == UIConst.OPAQUE_L2){
            button.setForeground(UIConst.OPAQUE_L1);
        }
    }
}
