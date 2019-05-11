package UI.panel;

import UI.UIConst;
import UI.component.MyButton;
import UI.component.MyButtonListener;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class ToolBarPanel extends JPanel {

    private static ArrayList<MyButton> toolButtons;

    private JLabel logo;
    private MyButton homePageButton;
    private MyButton votePageButton;
    private MyButton sharePageButton;
    private MyButton comPageButton;
    private MyButton idButton;

    private JSONObject jsonObject;

    public ToolBarPanel(String text) {
        initialize();
        userInitialize(text);
        try {
            addButton(jsonObject.getString("id"));
        } catch (JSONException e) {
            if(UIConst.DEBUG){
                e.printStackTrace();
            }
        }
        addListener();
    }

    private void userInitialize(String text) {
        try {
            jsonObject = new JSONObject(text);
        } catch (JSONException e) {
            if(UIConst.DEBUG){
                e.printStackTrace();
            }
        }
    }

    private void initialize() {
        Dimension size = new Dimension(UIConst.MAIN_WOINDOWS_WIDTH, UIConst.TOOLBAR_HEIGHT);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    private void addButton(String id) {
        //设置布局
        JPanel leftP = new JPanel();
        leftP.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftP.setOpaque(false);

        JPanel rightP = new JPanel();
        rightP.setLayout(new BorderLayout());
        rightP.setOpaque(false);
        rightP.setBorder(BorderFactory.createLineBorder(Color.black));
        Dimension rd = new Dimension(UIConst.TOOLBAR_RIGHT_WIDTH, UIConst.TOOLBAR_HEIGHT);
        rightP.setPreferredSize(rd);
        rightP.setBorder(new EmptyBorder(25,0,25,20));

        //添加右侧按钮
        idButton = new MyButton(id, UIConst.TOOLBAR_RIGHT_ID);
        rightP.add(idButton, BorderLayout.CENTER);

        //添加左侧按钮
        Dimension d = new Dimension(100, 100);
        //添加 主界面
        {
            homePageButton = new MyButton("首页", UIConst.TOOLBAR_NORMAL_FONT);
            homePageButton.setPreferredSize(d);
            leftP.add(homePageButton);

        }
        //添加社交界面
        {
            comPageButton = new MyButton("社交", UIConst.TOOLBAR_NORMAL_FONT);
            comPageButton.setPreferredSize(d);
            leftP.add(comPageButton);
        }
        //添加共享界面
        {
            sharePageButton = new MyButton("共享", UIConst.TOOLBAR_NORMAL_FONT);
            sharePageButton.setPreferredSize(d);
            leftP.add(sharePageButton);
        }


//      把按钮添加进数组中
        toolButtons = new ArrayList<MyButton>();
        toolButtons.add(homePageButton);
        toolButtons.add(comPageButton);
        toolButtons.add(sharePageButton);

        this.add(leftP, BorderLayout.CENTER);
        this.add(rightP, BorderLayout.EAST);


    }

    private void addListener() {
        homePageButton.addMouseListener(new MyButtonListener(homePageButton) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //把其他按钮恢复正常状态
                setAllDefault();
                homePageButton.setClicked();

                //设置要显示的东西
                MainWindows.downPanel.removeAll();
                MainWindows.downPanel.add(MainWindows.homePage, BorderLayout.CENTER);
                MainWindows.homePage.refresh();
                MainWindows.downPanel.updateUI();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                //把其他按钮恢复正常状态
                setAllDefault();
                homePageButton.setClicked();

                //设置要显示的东西
                MainWindows.downPanel.removeAll();
                MainWindows.downPanel.add(MainWindows.homePage, BorderLayout.CENTER);
                MainWindows.homePage.refresh();
                MainWindows.downPanel.updateUI();
            }
        });

        comPageButton.addMouseListener(new MyButtonListener(comPageButton) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setAllDefault();
                comPageButton.setClicked();

                MainWindows.downPanel.removeAll();
                MainWindows.downPanel.add(MainWindows.comPage, BorderLayout.CENTER);
                MainWindows.downPanel.updateUI();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setAllDefault();
                comPageButton.setClicked();

                MainWindows.downPanel.removeAll();
                MainWindows.downPanel.add(MainWindows.comPage, BorderLayout.CENTER);
                MainWindows.downPanel.updateUI();
            }
        });

        sharePageButton.addMouseListener(new MyButtonListener(sharePageButton){

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //把其他按钮恢复正常状态
                setAllDefault();
                sharePageButton.setClicked();

                //设置要显示的东西
                MainWindows.downPanel.removeAll();
                MainWindows.downPanel.add(MainWindows.sharePage, BorderLayout.CENTER);
                MainWindows.sharePage.sendRequest();
                MainWindows.downPanel.updateUI();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                //把其他按钮恢复正常状态
                setAllDefault();
                sharePageButton.setClicked();

                //设置要显示的东西
                MainWindows.downPanel.removeAll();
                MainWindows.downPanel.add(MainWindows.sharePage, BorderLayout.CENTER);
                MainWindows.sharePage.sendRequest();
                MainWindows.downPanel.updateUI();
            }
        });

        idButton.addMouseListener(new MyButtonListener(idButton){

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                showIdPopupMenu(idButton,idButton.getX(),idButton.getY()+20);
                idButton.setNormal();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                showIdPopupMenu(idButton,idButton.getX(),idButton.getY()+20);
                idButton.setNormal();
            }
        });


    }

    private void showIdPopupMenu(Component invoker, int x, int y){
        JPopupMenu jPopupMenu = new JPopupMenu();
        try {
            JLabel idMenuItem = new JLabel("用户名: "+jsonObject.getString("id"));
            JLabel sidMenuItem = new JLabel("学号: "+ jsonObject.getString("sid"));
            JLabel emailMenuItem = new JLabel("电子邮箱: "+jsonObject.getString("email"));
            JMenuItem recordMenuItem = new JMenuItem("阅读管理员操作记录");
            JLabel levelMenuItem;
            switch (jsonObject.getString("level")){
                case "0": levelMenuItem = new JLabel("等级: 管理员");break;
                case "1": levelMenuItem = new JLabel("等级: 用  户");break;
                default:levelMenuItem = new JLabel("等级: ???");
            }
            Dimension dimension = new Dimension(200,50);
            idMenuItem.setPreferredSize(dimension);
            sidMenuItem.setPreferredSize(dimension);
            emailMenuItem.setPreferredSize(dimension);
            levelMenuItem.setPreferredSize(dimension);
            recordMenuItem.setPreferredSize(dimension);

            recordMenuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    new RecordPanel();
                }
            });


            /**
             * 把一级菜单添加入jpopupmenu
             */
            jPopupMenu.add(idMenuItem);
            jPopupMenu.add(sidMenuItem);
            jPopupMenu.add(emailMenuItem);
            jPopupMenu.add(levelMenuItem);
            jPopupMenu.addSeparator();
            jPopupMenu.add(recordMenuItem           );


            if(jsonObject.getString("level").equals("0")){
                jPopupMenu.addSeparator();
                JMenuItem publishMenuItem = new JMenuItem("发布公告");
                jPopupMenu.add(publishMenuItem);
                publishMenuItem.setPreferredSize(dimension);
                publishMenuItem.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(UIConst.DEBUG){
                            System.out.println("个人的发布按钮被点击");
                        }
                        new PublishPanel();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if(UIConst.DEBUG){
                            System.out.println("发布按钮被点击");
                        }
                        new PublishPanel();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });

                JMenuItem voteMenuItem = new JMenuItem("发起投票");
                jPopupMenu.add(voteMenuItem);
                voteMenuItem.setPreferredSize(dimension);
                voteMenuItem.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if(UIConst.DEBUG){
                            System.out.println("发起投票按钮被点击");
                        }
                        new VotePanel();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }

            jPopupMenu.show(invoker,x,y);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void setAllDefault() {
        toolButtons.forEach((a) -> {
            a.setNormal();
        });
    }
}

