package UI.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class MyScrollBarUI extends BasicScrollBarUI {
    //TODO 写出自己的滚动条
    @Override
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        super.paintTrack(g, c, trackBounds);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        super.paintThumb(g, c, thumbBounds);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return super.createDecreaseButton(orientation);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return super.createIncreaseButton(orientation);
    }

}
