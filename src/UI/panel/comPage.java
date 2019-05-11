package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import UI.component.MyButton;
import UI.component.MyButtonListener;
import online.cszt0.cmt.util.FileDialog;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;

public class comPage extends JPanel {

    private JPanel leftPanel, upPanel, downPanel;
    private JScrollPane scrollPane;
    private JLabel groupChat, chat;
    private JTextPane upText, downText;
    private JButton emotion, picture,draw;

    private Dimension labelDimension;
    private Dimension buttonDimension;

    private MyButton group;

    private ArrayList<User> userArrayList;


    public comPage() {
        //TODO 刷新用户面板
        initialize();
        addComponent();
        addListener();

        group.setClicked();
    }

    private void initialize() {
        //设置颜色与布局
        setOpaque(false);
        setLayout(new BorderLayout());

        userArrayList = new ArrayList<>();
    }

    private void addComponent() {
        addLeftPanel();
        add(getRightPanel("", "", ""), BorderLayout.CENTER);

    }

    private void addLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(UIConst.COMPAGE_LEFT_WIDTH, 0));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setHgap(5);
        layout.setVgap(5);
        leftPanel.setLayout(layout);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(leftPanel);
        scrollPane.setBounds(0, 0, UIConst.COMPAGE_LEFT_WIDTH, 200);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.WEST);

        //添加群聊标签
        labelDimension = new Dimension(UIConst.COMPAGE_LEFT_WIDTH, UIConst.COMPAGE_LEFTLABEL_HEIGHT);
        groupChat = new JLabel("群组");
        groupChat.setPreferredSize(labelDimension);
        groupChat.setBorder(new EmptyBorder(10, 20, 10, 0));
        groupChat.setFont(UIConst.COMPAGE_LEFTLABLE_FONT);
        leftPanel.add(groupChat);

        //添加群聊按钮
        buttonDimension = new Dimension(UIConst.COMPAGE_LEFT_WIDTH, UIConst.COMPAGE_LEFTLABEL_HEIGHT);
        group = new MyButton("班级群聊", UIConst.COMPAGE_LEFT_FONT);
        group.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        group.setPreferredSize(buttonDimension);
        leftPanel.add(group);

        //添加聊天标签
        chat = new JLabel("聊天");
        chat.setPreferredSize(labelDimension);
        chat.setBorder(new EmptyBorder(10, 20, 10, 0));
        chat.setFont(UIConst.COMPAGE_LEFTLABLE_FONT);
        leftPanel.add(chat);

        //添加聊天用户按钮
        addFriendsList();


    }

    private void addFriendsList() {
        //接收全用户名单
        MyWebSocket webSocket = MainWindows.webSocket;
        JSONObject sendJson = new JSONObject();
        try {
            sendJson.put("action", "getUser");
            sendJson.put("sid", MainWindows.userJson.getString("sid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.sendToServer(sendJson.toString());

        webSocket.setOnUserListListener((webSocket1, response) -> {
            try {
                JSONObject userList = response.getJSONObject("userList");

                Iterator iterator = userList.keys();
                while (iterator.hasNext()) {
                    JSONObject userJson = userList.getJSONObject((String) iterator.next());
                    String id = userJson.getString("id");
                    String sid = userJson.getString("sid");
                    String online = userJson.getString("online");

                    User user = new User(id, sid, online);
                    userArrayList.add(user);


                    addListToPanel();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

    private void addListToPanel() {
        for (int i = 0; i < userArrayList.size(); i++) {
            leftPanel.add(userArrayList.get(i).button);
        }
        leftPanel.setPreferredSize(new Dimension(UIConst.COMPAGE_LEFT_WIDTH, UIConst.COMPAGE_LEFTLABEL_HEIGHT * userArrayList.size()));
        leftPanel.updateUI();
    }

    private JPanel getRightPanel(String from, String to, String msg) {
        //设置布局
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);

        //上方
        upPanel = new JPanel();
        upText = new JTextPane();
        upText.setFont(UIConst.COMPAGE_CHAT_FONT);
        upText.setEditable(false);
        JScrollPane upScroll = new JScrollPane(upText);
        upScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        rightPanel.add(upScroll, BorderLayout.CENTER);

        //下方
        downPanel = new JPanel();
        downPanel.setLayout(new GridBagLayout());
        downPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        GridBagConstraints constraints = new GridBagConstraints();

        //添加工具条
        JPanel toolBar = new JPanel();
        toolBar.setBorder(new EmptyBorder(0, 10, 0, 0));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 100;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.BOTH;
        downPanel.add(toolBar, constraints);
        //向工具条上添加表情按钮
        emotion = new JButton(UIConst.COMPAGE_TOOLBAR_FACE);
        emotion.setBorder(null);
        emotion.setPreferredSize(new Dimension(32, 32));
        //添加发送图片按钮
        picture = new JButton(UIConst.COMPAGE_TOOLBAR_PICTURE);
        picture.setBorder(null);
        //添加即时绘图按钮
        draw = new JButton(UIConst.COMPAGe_TOOLBAR_DRAW);
        draw.setBorder(null);
//        toolBar.add(emotion);
        toolBar.add(picture);
        toolBar.add(draw);
        emotion.setContentAreaFilled(false);
        picture.setContentAreaFilled(false);
        draw.setContentAreaFilled(false);

        //设置下方的对话框
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 100;
        constraints.insets = new Insets(0, 10, 10, 10);
        constraints.weightx = 100;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        downText = new JTextPane();
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");
        StyleConstants.setFontSize(simpleAttributeSet, 17);
        downText.setCharacterAttributes(simpleAttributeSet, true);
        downText.setFont(UIConst.COMPAGE_CHAT_FONT);
        JScrollPane downScroll = new JScrollPane(downText);
        downPanel.add(downScroll, constraints);

        rightPanel.add(downPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private void addListener() {
        picture.addActionListener(e -> {
            online.cszt0.cmt.util.FileDialog fileDialog = FileDialog.getPlatInstance();
            fileDialog.addFileExtensionFilter("图片选择", "jpg", "png", "gif");
            String filePath = fileDialog.showOpenDialog();
            File picture = new File(filePath);
            //插入图片
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");
                StyleConstants.setFontSize(simpleAttributeSet, 17);
                String id = MainWindows.userJson.getString("id");
                StyleConstants.setForeground(simpleAttributeSet, new Color(0x4B89E2));
                String userID = "" + id + "\t" + dateFormat.format(new Date());
                upText.getDocument().insertString(upText.getDocument().getLength(), userID + "\n", simpleAttributeSet);

                ImageIcon image = new ImageIcon(ImageIO.read(picture));
                upText.setCaretPosition(upText.getDocument().getLength()); // 设置插入位置
                upText.insertIcon(image);
                upText.getDocument().insertString(upText.getDocument().getLength(), "\n", simpleAttributeSet);



                if (filePath != null) {
                    sendImage(filePath,userID);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        });

        draw.addActionListener(e->{
            if (group.isClicked()) {
                new PaintFrame(true,"group");
            } else {
                for (int i = 0; i < userArrayList.size(); i++) {
                    if (userArrayList.get(i).button.isClicked()) {
                        String sid = userArrayList.get(i).sid;
                        new PaintFrame(true,""+sid);
                    }
                }
            }
        });

        group.addMouseListener(new MyButtonListener(group));

        downText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    if (!downText.getText().isEmpty()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                        StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");
                        StyleConstants.setFontSize(simpleAttributeSet, 17);
                        try {
                            String id = MainWindows.userJson.getString("id");
                            StyleConstants.setForeground(simpleAttributeSet, new Color(0x4B89E2));
                            String userID = "" + id + "\t" + dateFormat.format(new Date());
                            upText.getDocument().insertString(upText.getDocument().getLength(), userID + "\n", simpleAttributeSet);

                            StyleConstants.setAlignment(simpleAttributeSet, StyleConstants.ALIGN_LEFT);
                            StyleConstants.setForeground(simpleAttributeSet, Color.BLACK);
                            StyleConstants.setLeftIndent(simpleAttributeSet, 20f);
                            String text = downText.getText();
                            upText.getDocument().insertString(upText.getDocument().getLength(), text + "\n", simpleAttributeSet);

                            String allText = upText.getText();
                            String sendText = userID + "\n" + text + "\n";
                            if (group.isClicked()) {
                                sendToGroup(sendText, allText);
                            } else {
                                for (int i = 0; i < userArrayList.size(); i++) {
                                    if (userArrayList.get(i).button.isClicked()) {
                                        sendTo(sendText, allText, userArrayList.get(i).sid);
                                    }
                                }
                            }
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        downText.setText(null);

                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        group.addMouseListener(new MyButtonListener(group) {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                //TODO 向服务器发送请求，获得所有的聊天记录并加载聊天记录

                //清除屏幕
                upText.setText(null);
                setAllDefault();

            }
        });

        MainWindows.webSocket.setOnChatListener(new MyWebSocket.OnChatListener() {
            @Override
            public void OnGroupChat(MyWebSocket webSocket, JSONObject response) {
                try {
                    String sendText = response.getString("sendText");
                    //添加到聊天面板上
                    if (group.isClicked()) {
                        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                        StyleConstants.setAlignment(simpleAttributeSet, StyleConstants.ALIGN_LEFT);
                        StyleConstants.setForeground(simpleAttributeSet, Color.BLACK);
                        StyleConstants.setFontSize(simpleAttributeSet, 17);
                        StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");
                        upText.getDocument().insertString(upText.getDocument().getLength(), sendText, simpleAttributeSet);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnChatTo(MyWebSocket webSocket, JSONObject response) {
                try {
                    String sendText = response.getString("sendText");
                    String sendTo = response.getString("sendTo");
                    String from = response.getString("from");
                    //添加到聊天面板上
                    for (int i = 0; i < userArrayList.size(); i++) {
                        if (userArrayList.get(i).sid.equals(from) && userArrayList.get(i).button.isClicked()) {
                            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                            StyleConstants.setAlignment(simpleAttributeSet, StyleConstants.ALIGN_LEFT);
                            StyleConstants.setForeground(simpleAttributeSet, Color.BLACK);
                            StyleConstants.setFontSize(simpleAttributeSet, 17);
                            StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");
                            upText.getDocument().insertString(upText.getDocument().getLength(), sendText, simpleAttributeSet);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });

        MainWindows.webSocket.setOnChatImageListener(new MyWebSocket.OnChatImageListener() {
            @Override
            public void OnChatImage(MyWebSocket webSocket, JSONObject response) {
                try {
                    String sendText = response.getString("sendText");
                    String allText = response.getString("allText");
                    String sendTo = response.getString("sendTo");
                    String from = response.getString("from");
                    String fromAuthor = response.getString("fromAuthor");

                    //解析保存图片
                    String fileName= new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date(System.currentTimeMillis()));
                    fileName = fileName + ".jpg";//extension, you can change it.

                    File file = new File("D:/data/img/"+MainWindows.userJson.getString("sid"),fileName);
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] decode = Base64.getDecoder().decode(sendText);
                    fileOutputStream.write(decode);
                    fileOutputStream.close();

                    ImageIcon imageIcon = new ImageIcon(ImageIO.read(file));
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");
                    StyleConstants.setFontSize(simpleAttributeSet, 17);
                    StyleConstants.setForeground(simpleAttributeSet,Color.BLACK);
                    if(sendTo.equals("group") && group.isClicked()){
                        upText.getDocument().insertString(upText.getDocument().getLength(), fromAuthor, simpleAttributeSet);
                        upText.getDocument().insertString(upText.getDocument().getLength(), "\n", simpleAttributeSet);

                        upText.setCaretPosition(upText.getDocument().getLength()); // 设置插入位置
                        upText.insertIcon(imageIcon); // 插入图片

                        upText.getDocument().insertString(upText.getDocument().getLength(), "\n", simpleAttributeSet);
                    }else {
                        for (int i = 0; i < userArrayList.size(); i++) {
                            if (userArrayList.get(i).sid.equals(from) && userArrayList.get(i).button.isClicked()) {
                                upText.getDocument().insertString(upText.getDocument().getLength(), fromAuthor, simpleAttributeSet);
                                upText.getDocument().insertString(upText.getDocument().getLength(), "\n", simpleAttributeSet);

                                upText.setCaretPosition(upText.getDocument().getLength()); // 设置插入位置
                                upText.insertIcon(imageIcon);
                                upText.getDocument().insertString(upText.getDocument().getLength(), "\n", simpleAttributeSet);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });

        MainWindows.webSocket.setOnOpenPaintListener(new MyWebSocket.OnOpenPaintListener() {
            @Override
            public void OnOpen(MyWebSocket webSocket, JSONObject response) {
                new PaintFrame(false,"null");
            }
        });
    }

    public void sendToGroup(String sendText, String allText) {
        MyWebSocket myWebSocket = MainWindows.webSocket;
        JSONObject sendJSON = new JSONObject();
        try {
            sendJSON.put("action", "chat");
            sendJSON.put("sendTo", "group");
            sendJSON.put("sendText", sendText);
            sendJSON.put("allText", allText);
            sendJSON.put("from", MainWindows.userJson.getString("sid"));

            myWebSocket.sendToServer(sendJSON.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendTo(String sendText, String allText, String someone) {
        MyWebSocket myWebSocket = MainWindows.webSocket;
        JSONObject sendJSON = new JSONObject();
        try {
            sendJSON.put("action", "chat");
            sendJSON.put("sendTo", someone);
            sendJSON.put("sendText", sendText);
            sendJSON.put("allText", allText);
            sendJSON.put("from", MainWindows.userJson.getString("sid"));

            myWebSocket.sendToServer(sendJSON.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(String filePath,String fromAuthor) {
        //把图片转化为Byte数组
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filePath));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);
            String allText = upText.getText();
            String sendText = new String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
            MyWebSocket myWebSocket = MainWindows.webSocket;
            JSONObject sendJSON = new JSONObject();
            sendJSON.put("action", "chatImage");
            sendJSON.put("sendText", sendText);
            sendJSON.put("allText", allText);
            sendJSON.put("from", MainWindows.userJson.getString("sid"));
            sendJSON.put("fromAuthor",fromAuthor);

            if (group.isClicked()) {
                sendJSON.put("sendTo", "group");
                myWebSocket.sendToServer(sendJSON.toString());
            } else {
                for (int i = 0; i < userArrayList.size(); i++) {
                    if (userArrayList.get(i).button.isClicked()) {
                        String sid = userArrayList.get(i).sid;
                        sendJSON.put("sendTo",sid);
                        myWebSocket.sendToServer(sendJSON.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAllDefault() {
        for (int i = 0; i < userArrayList.size(); i++) {
            userArrayList.get(i).button.setNormal();
        }
    }

    public void setGroupDefault() {
        group.setNormal();
    }

    public class User {
        private MyButton button;

        private String sid;
        private String id;
        private String online;

        public User(String id, String sid, String online) {
            this.id = id;
            this.sid = sid;
            this.online = online;

            button = new MyButton(id, UIConst.COMPAGE_LEFT_FONT);
            button.setPreferredSize(buttonDimension);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK));


            addUserListener();
        }

        private void addUserListener() {
            button.addMouseListener(new MyButtonListener(button) {

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    setOtherDefault();

                    //TODO 用户的点击事件
                    upText.setText(null);
                    downText.setText(null);

                }
            });
        }

        public void setOtherDefault() {
            for (int i = 0; i < userArrayList.size(); i++) {
                if (userArrayList.get(i).sid != sid) {
                    userArrayList.get(i).button.setNormal();
                }
            }
            setGroupDefault();
        }


    }

}
