package UI.panel;


import Server.MyWebSocket;
import UI.UIConst;
import UI.component.MyButton;
import UI.component.MyButtonListener;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class HomePage extends JPanel {
    private JPanel leftPanel;
    private JPanel rightPanel;
    //右边通知的面板
    private JPanel upPanel, downPanel;
    private JScrollPane scrollPane;
    private ArrayList<Announcement> announcements;


    public HomePage() {
        initialize();
        addComponent();
    }

    private void initialize() {
        //设置颜色与布局
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        announcements = new ArrayList<Announcement>();
    }

    private void addComponent() {
        add(getLeftPanel(), BorderLayout.WEST);
        add(getRightPanel("默认标题", "默认内容"), BorderLayout.CENTER);
        addToAnnouncements();
    }

    private JScrollPane getLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(UIConst.HOMEPAGE_LEFT_WIDTH, 0));
//        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setHgap(0);
        layout.setVgap(0);
        leftPanel.setLayout(layout);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(leftPanel);
        scrollPane.setBounds(0, 0, UIConst.HOMEPAGE_LEFT_WIDTH, 200);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JPanel getRightPanel(String title, String text) {
        //设置布局
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
//        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(960, HEIGHT));

        //设置上面
        upPanel = new JPanel();
        upPanel.setLayout(new GridLayout());
        upPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        upPanel.setOpaque(false);
        //上面的标题

        JLabel titleLabel = new JLabel(title);
        formatTitleLabel(titleLabel);
        upPanel.add(titleLabel);
        rightPanel.add(upPanel, BorderLayout.NORTH);


        //设置下面
        downPanel = new JPanel();
        downPanel.setLayout(new GridLayout(1, 1));
        downPanel.setOpaque(false);
        rightPanel.add(downPanel, BorderLayout.CENTER);
        //下面的文本

        JLabel textLabel = new JLabel(text);
        formatTextLabel(textLabel);
        downPanel.add(textLabel);

        return rightPanel;
    }

    public void addToAnnouncements() {
        MyWebSocket myWebSocket = MainWindows.webSocket;
        JSONObject jsonObject = new JSONObject();//用于发射
        try {
            jsonObject.put("action", "announcement");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myWebSocket.sendToServer(jsonObject.toString());
        myWebSocket.setOnAnnListener(new MyWebSocket.OnAnnListener() {
            @Override
            public void OnAnn(MyWebSocket webSocket, JSONObject response) {
                announcements.clear();
                leftPanel.removeAll();
                try {
                    response.remove("action"); //改造
                    Iterator iterator = response.keys();//接收
                    while(iterator.hasNext()){
                        String key = (String) iterator.next();
                        JSONObject obj = response.getJSONObject(key);
                        String no = obj.getString("no");
                        String author = obj.getString("author");
                        String sid = obj.getString("sid");
                        String title = obj.getString("title");
                        String text = obj.getString("text");
                        String submission_time = obj.getString("submission_time");

                        String trueTile = "<html>"+author+"\t"+submission_time+"<br>"+"<h1><b>"+title+"</b></h1>"+"</html>";
                        announcements.add(new Announcement(new MyButton(trueTile, UIConst.HOMEPAGE_LEFT_FONT), title, text));
                        addAnnouncementsToPanel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addAnnouncementsToPanel() {
        Collections.reverse(announcements);
        for (int i = 0; i < announcements.size(); i++) {
            leftPanel.add(announcements.get(i).btn);
        }
        leftPanel.setPreferredSize(new Dimension(UIConst.HOMEPAGE_LEFT_WIDTH, UIConst.HOMEPAGE_LEFT_BUTTON_HEIGHT * announcements.size()));
        leftPanel.updateUI();
    }

    public void refresh(){
        MyWebSocket myWebSocket = MainWindows.webSocket;
        JSONObject jsonObject = new JSONObject();//用于发射
        try {
            jsonObject.put("action", "announcement");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myWebSocket.sendToServer(jsonObject.toString());
    }

    public void formatTitleLabel(JLabel titleLabel) {
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleLabel.setPreferredSize(new Dimension(upPanel.getWidth(), 100));
        titleLabel.setFont(UIConst.HOMEPAGE_TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    public void formatTextLabel(JLabel textLabel) {
        textLabel.setPreferredSize(new Dimension(960, 600));
        textLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textLabel.setFont(UIConst.HOMEPAGE_TEXT_FONT);
        textLabel.setHorizontalAlignment(JLabel.LEFT);
        textLabel.setVerticalAlignment(JLabel.TOP);
    }

    //点击按钮之后，把其他按钮恢复成原来的状态
    //Announcement接口callback,在点击后，把全部都设置为初始状态

    //定义一个内部类来统一管理通知的相关属性如按钮，标题，内容，以方便以后大量生成通知
    public class Announcement {
        private MyButton btn;

        private String title;
        private String text;

        private JLabel titleLabel;
        private JLabel textLabel;

        public Announcement(MyButton btn, String title, String text) {
            this.btn = btn;
            this.title = title;
            this.text = text;

            btn.setPreferredSize(new Dimension(UIConst.HOMEPAGE_LEFT_WIDTH, UIConst.HOMEPAGE_LEFT_BUTTON_HEIGHT));
            btn.setFont(UIConst.HOMEPAGE_LEFT_FONT);
            btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            titleLabel = new JLabel(title);
            formatTitleLabel(titleLabel);
            textLabel = new JLabel(text);
            formatTextLabel(textLabel);

            addListener();
        }

        public Announcement getAnnouncement() {
            return this;
        }

        public String getTitle(){return title;}

        public void addListener() {
            btn.addMouseListener(new MyButtonListener(btn) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    setAllDefault();
                    btn.setForeground(UIConst.OPAQUE_L3);

                    //刷新标题
                    upPanel.removeAll();
                    upPanel.add(titleLabel);
                    upPanel.updateUI();

                    //刷新内容
                    downPanel.removeAll();
                    downPanel.add(textLabel);
                    downPanel.updateUI();

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    setAllDefault();
                    btn.setForeground(UIConst.OPAQUE_L3);

                    //刷新标题
                    upPanel.removeAll();
                    upPanel.add(titleLabel);
                    upPanel.updateUI();

                    //刷新内容
                    downPanel.removeAll();
                    downPanel.add(textLabel);
                    downPanel.updateUI();
                }
            });

        }

        private void setAllDefault() {
            announcements.forEach((a) -> {
                a.btn.setNormal();
                a.btn.getListeners(MyButtonListener.class);
            });
        }

    }
}
