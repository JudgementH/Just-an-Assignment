package UI.component;

import java.awt.*;

public class MyFlowLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int maxwidth = parent.getWidth() - (insets.left + insets.right);
            int y = insets.top;

            Component[] components = parent.getComponents();
            for (Component component : components) {
                Dimension dimension = component.getPreferredSize();
                component.setBounds(insets.left, y, maxwidth, dimension.height);
                y += dimension.height;
            }
            return new Dimension(parent.getWidth(), y + insets.bottom);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int maxwidth = parent.getWidth() - (insets.left + insets.right);
            int y = insets.top;

            Component[] components = parent.getComponents();
            for (Component component : components) {
                Dimension dimension = component.getPreferredSize();
                component.setBounds(insets.left, y, maxwidth, dimension.height);
                y += dimension.height;
            }
        }
    }
}
