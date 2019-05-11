package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import okhttp3.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class RegisterPanel extends JFrame {
    JPanel panel;
    JLabel title, hint;
    Dimension dimension;

    JTextField IDText, nameText, emailText, pswText, id, admin;
    JButton registerBtn, cancelBtn;
    JCheckBox accept;

    String level;


    public RegisterPanel() {
        initialize();
        addComponent();
        addListener();
        setVisible(true);
    }

    private void initialize() {
        setTitle("创建账号");
        setSize(UIConst.REGISTER_WINDOWS_WIDTH, UIConst.REGISTER_WINDOWS_HEIGHT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        panel = (JPanel) getContentPane();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(20);
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(-20, 0, -10, 0));

        dimension = new Dimension(600, 40);
    }

    private void addComponent() {
        panel.add(getTitleLabel());
        panel.add(getHintLabel());
        panel.add(getIDText());
        panel.add(getId());
        panel.add(getNameText());
        panel.add(getEmailText());
        panel.add(getPswText());
        panel.add(getAdmin());
        panel.add(getAcceptBox());
        panel.add(getRegisterBtn());
        panel.add(getCancelBtn());
    }

    private JLabel getHintLabel() {
        hint = new JLabel();
        hint.setFont(UIConst.LOGIN_HINT_FONT);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        hint.setVerticalAlignment(SwingConstants.CENTER);
        hint.setForeground(Color.RED);
        hint.setVisible(false);
        return hint;
    }

    private JLabel getTitleLabel() {
        title = new JLabel("创建账号");
        title.setPreferredSize(dimension);
        title.setOpaque(false);
        title.setPreferredSize(new Dimension(UIConst.REGISTER_WINDOWS_WIDTH, 160));
        title.setFont(UIConst.LOGIN_LOGO_FONT);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return title;
    }

    private JTextField getIDText() {
        IDText = new JTextField();
        IDText.setPreferredSize(dimension);
        IDText.setFont(UIConst.LOGIN_TEXT_FONT);
        IDText.setForeground(Color.GRAY);

        return IDText;
    }

    private JTextField getNameText() {
        nameText = new JTextField(UIConst.REGISTER_NAME_HINT);
        nameText.setPreferredSize(dimension);
        nameText.setFont(UIConst.LOGIN_TEXT_FONT);
        nameText.setForeground(Color.GRAY);

        return nameText;
    }

    private JTextField getEmailText() {
        emailText = new JTextField(UIConst.REGISTER_EMAIL_HINT);
        emailText.setPreferredSize(dimension);
        emailText.setFont(UIConst.LOGIN_TEXT_FONT);
        emailText.setForeground(Color.GRAY);

        return emailText;
    }

    private JTextField getId() {
        id = new JTextField(UIConst.REGISTER_ID_HINT);
        id.setPreferredSize(dimension);
        id.setFont(UIConst.LOGIN_TEXT_FONT);
        id.setForeground(Color.GRAY);

        return id;
    }

    private JTextField getPswText() {

        pswText = new JTextField(UIConst.REGISTER_PSW_HINT);
        pswText.setPreferredSize(dimension);
        pswText.setFont(UIConst.LOGIN_TEXT_FONT);
        pswText.setForeground(Color.GRAY);

        return pswText;
    }

    private JTextField getAdmin() {

        admin = new JTextField(UIConst.REGISTER_ADMIN_HINT);
        admin.setPreferredSize(dimension);
        admin.setFont(UIConst.LOGIN_TEXT_FONT);
        admin.setForeground(Color.GRAY);

        return admin;
    }

    private JCheckBox getAcceptBox() {
        accept = new JCheckBox("我准备好要冒险了");
        accept.setPreferredSize(dimension);
        accept.setFont(UIConst.LOGIN_TEXT_FONT);
        accept.setForeground(Color.BLACK);
        return accept;
    }

    private JButton getRegisterBtn() {
        registerBtn = new JButton("注册");
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(UIConst.OKBTN_COLOR);
        registerBtn.setPreferredSize(dimension);
        registerBtn.setFocusPainted(false);
        registerBtn.setEnabled(false);

        return registerBtn;
    }

    private JButton getCancelBtn() {

        cancelBtn = new JButton("退出");
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setPreferredSize(dimension);
        cancelBtn.setFocusPainted(false);
        return cancelBtn;
    }

    private void addListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                title.requestFocus();
            }
        });
        //对文本提示监听
        IDText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (IDText.getText().equals(UIConst.REGISTER_SID_HINT)) {
                    IDText.setText("");
                    IDText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (IDText.getText().equals("")) {
                    IDText.setForeground(Color.GRAY);
                    IDText.setText(UIConst.REGISTER_SID_HINT);
                }
            }
        });
        id.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (id.getText().equals(UIConst.REGISTER_ID_HINT)) {
                    id.setText("");
                    id.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (id.getText().equals("")) {
                    id.setForeground(Color.GRAY);
                    id.setText(UIConst.REGISTER_ID_HINT);
                }
            }
        });
        nameText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameText.getText().equals(UIConst.REGISTER_NAME_HINT)) {
                    nameText.setText("");
                    nameText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameText.getText().equals("")) {
                    nameText.setForeground(Color.GRAY);
                    nameText.setText(UIConst.REGISTER_NAME_HINT);
                }
            }
        });
        emailText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailText.getText().equals(UIConst.REGISTER_EMAIL_HINT)) {
                    emailText.setText("");
                    emailText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailText.getText().equals("")) {
                    emailText.setForeground(Color.GRAY);
                    emailText.setText(UIConst.REGISTER_EMAIL_HINT);
                }
            }
        });
        pswText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (pswText.getText().equals(UIConst.REGISTER_PSW_HINT)) {
                    pswText.setText("");
                    pswText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (pswText.getText().equals("")) {
                    pswText.setForeground(Color.GRAY);
                    pswText.setText(UIConst.REGISTER_PSW_HINT);
                }
            }
        });
        admin.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (admin.getText().equals(UIConst.REGISTER_ADMIN_HINT)) {
                    admin.setText("");
                    admin.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (admin.getText().equals("")) {
                    admin.setForeground(Color.GRAY);
                    admin.setText(UIConst.REGISTER_ADMIN_HINT);
                }
            }
        });
        accept.addChangeListener(e -> {
            if (accept.isSelected()) {
                registerBtn.setEnabled(true);
            } else registerBtn.setEnabled(false);
        });

        registerBtn.addActionListener(e -> {
            String sid = IDText.getText();
            String id = this.id.getText();
            String name = nameText.getText();
            String email = emailText.getText();
            String password = pswText.getText();
            String level;
            if (admin.getText().equals(UIConst.ADMIN_KEY)) level = "0";
            else level = "1";

            if (!sid.matches("[0-9]{12}")) {
                hint.setText("请输入正确的账号(学号)");
                hint.setVisible(true);
                IDText.requestFocus();
                return;
            }
            if(id.isEmpty()){
                hint.setText("昵称(id)不能为空");
                hint.setVisible(true);
                this.id.requestFocus();
                return;
            }
            if(name.isEmpty()){
                hint.setText("姓名不能为空");
                hint.setVisible(true);
                nameText.requestFocus();
                return;
            }
            if(password.isEmpty()){
                hint.setText("密码不能为空");
                hint.setVisible(true);
                pswText.requestFocus();
                return;
            }
            if(!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                hint.setText("请输入正确的邮箱");
                hint.setVisible(true);
                emailText.requestFocus();
                return;
            }
            if(!admin.getText().equals(UIConst.REGISTER_ADMIN_HINT)){
                if(!admin.getText().equals(UIConst.ADMIN_KEY)){
                    /**
                     * 若确定flag为0;
                     * 若取消flag为1
                     */
                    int flag = JOptionPane.showConfirmDialog(null,"激活码错误，是否还要继续？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(flag == 1){
                        return;
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "激活码正确,管理员权限启动！", "提示", JOptionPane.PLAIN_MESSAGE);
                    System.out.println("激活码正确");
                }

            }

            System.out.println(sid);
            System.out.println(id);
            System.out.println(name);
            System.out.println(email);
            System.out.println(password);
            System.out.println(level);

            connReg(sid, id, name, password, level, email);

            hint.setText("~~少女祈祷中~~");
            hint.setVisible(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });

        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }

    public void connReg(String sid, String id, String name, String password, String level, String email) {
        MyWebSocket.connect(new MyWebSocket.OnOpenListener() {
                                @Override
                                public void onOpen(MyWebSocket webSocket) {
                                    webSocket.AddErrorListener(webSocket1 -> {
                                        hint.setText("服务器连接中断");
                                        hint.setVisible(true);

                                        webSocket.close();
                                    });

                                    JSONObject registerJson = new JSONObject();
                                    try {
                                        registerJson.put("action", "register");
                                        registerJson.put("id", id);
                                        registerJson.put("password", password);
                                        registerJson.put("name", name);
                                        registerJson.put("sid", sid);
                                        registerJson.put("email", email);
                                        registerJson.put("level", level);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (!webSocket.sendRegisterToServer(registerJson.toString(), new MyWebSocket.OnRegisterListener() {
                                        @Override
                                        public void OnRegister(MyWebSocket webSocket, JSONObject response) {
                                            try {
                                                String code = response.getString("code");
                                                switch (code) {
                                                    case "0": {
                                                        JOptionPane.showMessageDialog(null, "注册成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                                                        title.requestFocus();
                                                        accept.setSelected(false);
                                                        if (UIConst.DEBUG) {
                                                            System.out.println("注册成功");
                                                        }
                                                        webSocket.close();
                                                        break;
                                                    }
                                                    case "1": {
                                                        JOptionPane.showMessageDialog(null, "账号已经存在！", "提示", JOptionPane.PLAIN_MESSAGE);
                                                        title.requestFocus();
                                                        accept.setSelected(false);
                                                        if (UIConst.DEBUG) {
                                                            System.out.println("账号已经存在");
                                                        }
                                                        webSocket.close();
                                                        break;
                                                    }
                                                    default: {
                                                        JOptionPane.showMessageDialog(null, "注册异常错误！", "提示", JOptionPane.PLAIN_MESSAGE);
                                                        title.requestFocus();
                                                        accept.setSelected(false);
                                                        if (UIConst.DEBUG) {
                                                            System.out.println("注册异常错误");
                                                        }
                                                    }
                                                }

                                                hint.setVisible(false);
                                            } catch (JSONException e) {
                                                if(UIConst.DEBUG){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    })) {
                                        if (UIConst.DEBUG) {
                                            System.err.println("发送失败");
                                            hint.setText("无法连接到服务器");
                                            hint.setVisible(true);
                                            webSocket.close();
                                        }
                                    }
                                }
                            },
                new MyWebSocket.OnFailureListener() {
                    @Override
                    public void OnFailure(MyWebSocket webSocket) {
                        if (UIConst.DEBUG) {
                            System.out.println("注册连接已经断开");
                        }
                        hint.setVisible(false);
                        webSocket.close();
                    }
                });
    }
}
