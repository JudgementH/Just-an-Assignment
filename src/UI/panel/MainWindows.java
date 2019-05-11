package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


public class MainWindows extends JFrame {
    //用户相关信息
    public static JSONObject userJson;
    public static MyWebSocket webSocket;

    public static JPanel mainPanel;
    public static JPanel upPanel;
    public static JPanel downPanel;

    public static HomePage homePage;
    public static VotePage votePage;
    public static UI.panel.comPage comPage;
    public static SharePanel sharePage;
    private ToolBarPanel toolBarPanel;

    public MainWindows() {
        initialize();
        repaint();
    }
    public MainWindows(String text,MyWebSocket webSocket) {
        userInitialize(text,webSocket);
        initialize();
        addListener();
        loadVote();

        repaint();
    }
    private void userInitialize(String text,MyWebSocket webSocket){
        try {
            userJson = new JSONObject(text);
            this.webSocket = webSocket;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        //设置框架相关
        setTitle(UIConst.NAME);
        setSize(UIConst.MAIN_WOINDOWS_WIDTH, UIConst.MAIN_WOINDOWS_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        try {
            Image ico = ImageIO.read(new File("./img/ico.jpg"));
            this.setIconImage(ico);
        } catch (Exception e) {
            System.out.println(e);
        }

        //设置布局与面板
//        mainPanel = new JPanel(){
//            @Override
//            public void paintComponent(Graphics g) {
//                ImageIcon icon = new ImageIcon("E:\\picture\\Pixiv\\wenwen.jpg");// 003.jpg是测试图片在项目的根目录下
//                g.drawImage(icon.getImage(),0,0 , getSize().width, getSize().height, this);// 图片会自动缩放
////    g.drawImage(icon.getImage(), x, y,this);//图片不会自动缩放
//            }
//        };

        mainPanel = new JPanel();
        this.getContentPane().add(mainPanel);
        BorderLayout layout = new BorderLayout();
        layout.setHgap(0);
        layout.setVgap(0);
        mainPanel.setBorder(new EmptyBorder(-5, 0, -5, 0));
        mainPanel.setLayout(layout);
        //上面的面板
        upPanel = new JPanel();
        upPanel.setLayout(new BorderLayout());
        upPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        upPanel.setOpaque(false);
        mainPanel.add(upPanel, BorderLayout.NORTH);
        //下面的面板
        downPanel = new JPanel();
        downPanel.setLayout(new BorderLayout());
        downPanel.setOpaque(false);
        mainPanel.add(downPanel, BorderLayout.CENTER);

        //创建并添加工具条
        toolBarPanel = new ToolBarPanel(userJson.toString());
        upPanel.add(toolBarPanel,BorderLayout.CENTER);

        //创建其他界面
        homePage = new HomePage();
        comPage = new comPage();
        sharePage = new SharePanel();

    }

    private void addListener(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                webSocket.close();
            }
        });
    }

    //TODO 可以改进一下
    public void loadVote(){
        MyWebSocket myWebSocket = MainWindows.webSocket;
        JSONObject jsonObject = new JSONObject();//用于发射信息
        try {
            jsonObject.put("action","readVote");
            jsonObject.put("sid",MainWindows.userJson.getString("sid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myWebSocket.sendToServer(jsonObject.toString());


//        myWebSocket.setOnVoteListener(new MyWebSocket.OnVoteListener() {
//            @Override
//            public void OnVote(MyWebSocket webSocket, JSONObject response) {
//
//                new VotePage(response.toString());
//
//            }
//        });

        myWebSocket.setOnReadVoteListener(new MyWebSocket.OnReadVoteListener() {
            @Override
            public void OnReadVote(MyWebSocket webSocket, JSONObject response) {
                try {
                    String next = response.getString("next");
                    if(userJson.getString("sid").equals(next)){
                        new VotePage(response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }



}
