package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class PublishPanel extends JFrame {
    private JButton publishBtn;
    private JPanel panel, upPanel, downPanel;
    private JTextField titleField;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public PublishPanel() {
        initialize();
        addComponent();
        addListener();

        setVisible(true);

    }

    private void initialize() {
        //设置框架的大小标题等
        setSize(UIConst.PUBLISH_FRAME_WIDTH, UIConst.PUBLISH_FRAME_WIDTH);
        setTitle(UIConst.PUBLISH_FRAME_TITLE);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        //设置布局
        panel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setPreferredSize(new Dimension(getWidth(), getHeight()));
        panel.setLayout(new BorderLayout());
        upPanel = new JPanel();
        upPanel.setOpaque(false);
        upPanel.setPreferredSize(new Dimension(getWidth() * 2 / 3, getHeight() * 5 / 6));
        panel.add(upPanel, BorderLayout.CENTER);
        downPanel = new JPanel();
        downPanel.setOpaque(false);
        downPanel.setPreferredSize(new Dimension(getWidth() * 2 / 3, 40));
        panel.add(downPanel, BorderLayout.SOUTH);
    }

    public void addComponent() {
        addTitleField();
        addTextArea();
        addPublishBtn();
    }

    private void addTitleField() {
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(getWidth() * 2 / 3, 60));
        titleField.setBorder(new EmptyBorder(0, 0, 0, 0));
        titleField.setFont(UIConst.PUBLISH_TITLE_FONT);
        titleField.setForeground(Color.GRAY);
        titleField.setText(UIConst.PUBLISH_TITLE_HINT);
        titleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        upPanel.add(titleField);
    }

    private void addTextArea() {
        textArea = new JTextArea();
        textArea.setFont(UIConst.PUBLISH_TEXT_FONT);
        textArea.setText(UIConst.PUBLISH_TEXT_HINT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(getWidth() * 2 / 3, getHeight() * 5 / 6));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        textArea.setCaretPosition(textArea.getText().length());
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));


        upPanel.add(scrollPane);
    }

    private void addPublishBtn() {
        publishBtn = new JButton("发布");
        publishBtn.setFont(UIConst.PUBLISH_TEXT_FONT);
        publishBtn.setForeground(Color.WHITE);
        publishBtn.setBackground(UIConst.OKBTN_COLOR);
        publishBtn.setPreferredSize(new Dimension(getWidth() * 2 / 3, 30));
        publishBtn.setFocusPainted(false);
        downPanel.add(publishBtn);
    }

    private void addListener() {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                panel.requestFocus();
            }
        });

        //对标题框进行默认限制
        titleField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (titleField.getText().equals(UIConst.PUBLISH_TITLE_HINT)) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK);
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (titleField.getText().equals("")) {
                    titleField.setForeground(Color.GRAY);
                    titleField.setText(UIConst.PUBLISH_TITLE_HINT);
                }
            }
        });
        //对文本框进行默认限制
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(UIConst.PUBLISH_TEXT_HINT)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().equals("")) {
                    textArea.setForeground(Color.GRAY);
                    textArea.setText(UIConst.PUBLISH_TEXT_HINT);
                }

            }
        });

        publishBtn.addActionListener(e -> {
            String title = titleField.getText();
            String annText = textArea.getText();
            if (title.isEmpty() || title.equals(UIConst.PUBLISH_TITLE_HINT)) {
                JOptionPane.showMessageDialog(null, "标题不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                titleField.requestFocus();
                System.out.println("发布被点击");
                return;
            }
            if (annText.isEmpty() || annText.equals(UIConst.PUBLISH_TEXT_HINT)) {
                JOptionPane.showMessageDialog(null, "内容不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                textArea.requestFocus();
                return;
            }

            //把信息封装后发送,
            sendAnnToServer(title, annText);
        });

    }

    private void sendAnnToServer(String title, String annText) {
        MyWebSocket webSocket = MainWindows.webSocket;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("annText", annText);
            jsonObject.put("author", MainWindows.userJson.getString("id"));
            jsonObject.put("sid", MainWindows.userJson.getString("sid"));
            jsonObject.put("action", "publish");
        } catch (JSONException e) {
            if (UIConst.DEBUG) {
                e.printStackTrace();
            }
        }
        JSONObject finalJsonObject = jsonObject;
        webSocket.sendToServer(finalJsonObject.toString());
        webSocket.setOnPublishListener((webSocket1, response) -> {
            try {
                String code = response.getString("code");
                switch (code) {
                    case "0": {
                        JOptionPane.showMessageDialog(null, "发送成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                        dispose();
                        break;
                    }
                    default: {
                        JOptionPane.showMessageDialog(null, "发送失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });


    }

    public static void main(String[] args) {
        new PublishPanel();
    }
}
