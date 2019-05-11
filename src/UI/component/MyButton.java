package UI.component;

import UI.UIConst;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyButton extends JLabel {
    private String name;


    private Font normal;



    public MyButton(String name,Font normal){
        super(name,JLabel.CENTER);

        this.normal = normal;
        this.name = name;
        setFont(normal);
        setForeground(UIConst.OPAQUE_L1);
        initialize();
    }

    private void initialize(){
        setBorder(new EmptyBorder(0,0,0,0));
        setOpaque(false);
    }

    public void setNormal(){
        setForeground(UIConst.OPAQUE_L1);
    }

    public void setClicked(){
        setForeground(UIConst.OPAQUE_L3);
    }

    public boolean isClicked(){
        if(getForeground() == UIConst.OPAQUE_L3){
            return true;
        }else return false;
    }

}
