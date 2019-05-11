package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * 这个是登陆界面
 *
 */
public class Login extends JFrame {
    //分别定义文本框，按钮，标签等；
    private JTextField txtField;
    private JPasswordField pswField;
    private JButton enterBtn, cancelBtn,registerBtn;
    private JPanel upPanel, centerPanel;

    private JLabel logo,hint;

    public Login() {
        initialize();
        addComponent();
        addListener();
        setVisible(true);
    }

    public void initialize() {
        //设置Login的frame
        this.setTitle(UIConst.NAME);
        this.setSize(UIConst.LOGIN_WINDOWS_WIDTH, UIConst.LOGIN_WINDOWS_HEIGHT);
        JPanel panel = getCenterPanel();
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        try {
            Image ico = ImageIO.read(new File("./img/ico.jpg"));
            this.setIconImage(ico);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void addComponent() {
        add(getUpPanel(), BorderLayout.NORTH);
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel getUpPanel() {
        upPanel = new JPanel();
        upPanel.setOpaque(false);
        upPanel.setLayout(new BorderLayout());
        logo = new JLabel("Jormungandr");
        logo.setFont(UIConst.LOGIN_LOGO_FONT);
        logo.setPreferredSize(new Dimension(UIConst.LOGIN_WINDOWS_WIDTH, UIConst.LOGIN_LOGO_HEIGHT));
        logo.setVerticalAlignment(SwingConstants.CENTER);
        logo.setHorizontalAlignment(SwingConstants.CENTER);


        hint = new JLabel();
        hint.setFont(UIConst.LOGIN_HINT_FONT);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        hint.setVerticalAlignment(SwingConstants.CENTER);
        hint.setForeground(Color.RED);
        hint.setVisible(false);

        upPanel.add(logo, BorderLayout.CENTER);
        upPanel.add(hint,BorderLayout.SOUTH);

        return upPanel;
    }

    private JPanel getCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        FlowLayout layout = new FlowLayout();
        layout.setVgap(20);
        centerPanel.setLayout(layout);
        centerPanel.setPreferredSize(new Dimension(UIConst.LOGIN_WINDOWS_WIDTH, 180));

        Dimension dimension = new Dimension(380,40);

        txtField = new JTextField();
        txtField.setPreferredSize(dimension);
        txtField.setFont(UIConst.LOGIN_TEXT_FONT);
        txtField.setForeground(Color.GRAY);

        pswField = new JPasswordField();
        pswField.setPreferredSize(dimension);
        pswField.setFont(UIConst.LOGIN_TEXT_FONT);
        centerPanel.add(txtField);
        centerPanel.add(pswField);


        enterBtn = new JButton("登陆");
        enterBtn.setForeground(Color.WHITE);
        enterBtn.setBackground(UIConst.OKBTN_COLOR);
        enterBtn.setFocusPainted(false);
        enterBtn.setPreferredSize(dimension);

        registerBtn = new JButton("注册");
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(UIConst.OKBTN_COLOR);
        registerBtn.setFocusPainted(false);
        registerBtn.setPreferredSize(dimension);

        cancelBtn = new JButton("退出");
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(dimension);

        centerPanel.add(enterBtn);
        centerPanel.add(registerBtn);
        centerPanel.add(cancelBtn);

        return centerPanel;
    }

    private void addListener() {
        enterBtn.addActionListener(e -> {
            String ID = txtField.getText();
            String passWord = new String(pswField.getPassword());
            if(!ID.matches("[0-9]{12}")){
                hint.setText("请输入正确的账号");
                hint.setVisible(true);
                txtField.requestFocus();
                return;
            }
            if(passWord.isEmpty()){
                hint.setText("请输入密码");
                hint.setVisible(true);
                pswField.requestFocus();
                return;
            }


            /**
             * 下面连接服务器
             */
            connectLogin(ID,passWord);

            hint.setText("~~少女祈祷中~~");
            hint.setVisible(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }


        });

        registerBtn.addActionListener(e -> {
            new RegisterPanel();
        });

        cancelBtn.addActionListener(e -> System.exit(0));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                logo.requestFocus();
            }
        });

        txtField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(txtField.getText().equals(UIConst.LOGIN_TEXT_HINT)){
                    txtField.setText("");
                    txtField.setForeground(Color.BLACK);

                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtField.getText().equals("")){
                    txtField.setForeground(Color.GRAY);
                    txtField.setText(UIConst.LOGIN_TEXT_HINT);
                    if(UIConst.DEBUG){
                        System.out.println("账号框失去聚焦");
                    }

                }

            }
        });

    }

    /**
     * 这个是连接服务器
     * @param id 账号
     * @param password 这个是密码
     */
    private void connectLogin(String id,String password){
        MyWebSocket.connect(new MyWebSocket.OnOpenListener() {
            @Override
            public void onOpen(MyWebSocket webSocket) {
                //第一 处理错误
                webSocket.AddErrorListener(new MyWebSocket.OnErrorListener() {
                    @Override
                    public void onError(MyWebSocket webSocket) {
                        hint.setText("服务器错误");
                        webSocket.close();
                        if(UIConst.DEBUG){
                            System.out.println("WebSocket错误");
                        }
                    }
                });
                //第二 封装一下，准备发送，
                if(!webSocket.sendLogin(id, password, new MyWebSocket.OnLoginListener() {
                    @Override
                    public void OnLogin(MyWebSocket webSocket, JSONObject response) {
                        try {
                             String code = response.getString("code");
                            switch (code){
                                case "0":{ // 成功登陆
                                    //打开新的界面
                                    new MainWindows(response.toString(),webSocket);
                                    //Login关闭
                                    dispose();
                                    break;
                                }
                                case "1":{
                                    hint.setText("账号或密码错误");
                                    hint.setVisible(true);
                                    webSocket.close();
                                    break;
                                }
                                default: {
                                    webSocket.close();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })){
                    hint.setText("无法连接到服务器");
                    hint.setVisible(true);
                    System.out.println();
                }
            }
        },
                new MyWebSocket.OnFailureListener() {
            @Override
            public void OnFailure(MyWebSocket webSocket) {
                if(UIConst.DEBUG)System.out.println("无法连接到服务器");
               // hint.setText("无法连接到服务器");
                hint.setVisible(true);
            }
        });
        if(UIConst.DEBUG){
            System.out.println("connectLogin已经运行结束");
        }
    }
}

