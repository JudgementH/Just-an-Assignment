package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class PaintFrame extends JFrame {
    //TODO 写画图
    private Pen blackPen;
    private JPanel mainPanel;
    private boolean editable;
    private String sendTo;

    public PaintFrame(Boolean editable, String sendTo) {
        this.editable = editable;
        if (editable) {
            this.sendTo = sendTo;
        }else this.sendTo = "null";
        initialize();
        addListener();
    }

    private void initialize() {
        setSize(UIConst.PAINTFRAME_WIDTH, UIConst.PAINTFRAME_HEIGHT);
        setTitle(UIConst.PAINTFRAME_NAME);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        mainPanel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBackground(Color.WHITE);
        blackPen = new Pen(Color.BLACK);

        if (editable) {
            try {
                //发送消息让所有人都打开这个面板，但不能编辑
                JSONObject reqJSON = new JSONObject();
                reqJSON.put("action", "openPaint");
                reqJSON.put("from",MainWindows.userJson.getString("sid"));
                reqJSON.put("sendTo",sendTo);

                MainWindows.webSocket.sendToServer(reqJSON.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addListener() {
        if(editable){

            mainPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    blackPen.setPressed(true);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    blackPen.setPressed(false);
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            mainPanel.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    blackPen.setX1(e.getX());
                    blackPen.setY1(e.getY() + 30);
                    if (editable) {
                        MyWebSocket webSocket = MainWindows.webSocket;
                        JSONObject reqJSON = new JSONObject();
                        try {
                            reqJSON.put("action", "paint");
                            reqJSON.put("sendTo",sendTo);
                            reqJSON.put("x", e.getX());
                            reqJSON.put("y", e.getY() + 30);
                            reqJSON.put("from",MainWindows.userJson.getString("sid"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        webSocket.sendToServer(reqJSON.toString());
                    }
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });

        }
        MainWindows.webSocket.setOnPaintListener(new MyWebSocket.OnPaintListener() {
            @Override
            public void OnPaint(MyWebSocket webSocket, JSONObject response) {
                if (true) {
                    try {
                        int x = response.getInt("x");
                        int y = response.getInt("y");
                        blackPen.setX1(x);
                        blackPen.setY1(y);
                        repaint();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void paint(Graphics g) {
        blackPen.setGraphics2D((Graphics2D) g);
        blackPen.getGraphics2D().fillOval(blackPen.getX1(), blackPen.getY1(), 8, 8);
    }

    public class Pen {
        private int x1;
        private int y1;
        private int x2;
        private int y2;
        private Color color;
        private boolean pressed;
        private Graphics2D graphics2D;
        private int[] point;

        public Pen(Color color) {
            this.color = color;
            pressed = false;
        }

        public Graphics2D getGraphics2D() {
            return graphics2D;
        }

        public int getX1() {
            return x1;
        }

        public void setX1(int x1) {
            this.x1 = x1;
        }

        public int getY1() {
            return y1;
        }

        public void setY1(int y1) {
            this.y1 = y1;
        }

        public int getX2() {
            return x2;
        }

        public void setX2(int x2) {
            this.x2 = x2;
        }

        public int getY2() {
            return y2;
        }

        public void setY2(int y2) {
            this.y2 = y2;
        }

        public void setGraphics2D(Graphics2D graphics2D) {
            this.graphics2D = graphics2D;
        }


        public boolean isPressed() {
            return pressed;
        }

        public void setPressed(boolean pressed) {
            this.pressed = pressed;
        }

    }

    public static void main(String[] args) {
        new PaintFrame(true, "");
    }
}
