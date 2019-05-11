package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import UI.component.MyButton;
import UI.component.MyButtonListener;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class VotePanel extends JFrame {
    private JPanel panel, upPanel, downPanel;

    //upPanel
    private JLabel titleLabel, textLabel;
    private JTextField titleField;
    private ArrayList<Option> options;
    private Dimension dimension;
    private MyButton addBtn, subBtn;

    //downPanel
    private JButton cancelBtn, voteBtn;

    public VotePanel() {
        initialize();
        addUpPanel();
        addDownPanel();
        addListener();
        setVisible(true);
    }

    private void initialize() {
        //设置框架的大小标题等
        setSize(UIConst.VOTE_FRAME_WIDTH, UIConst.VOTE_FRAME_HEIGHT);
        setTitle("投票");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        //设置布局
        panel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(getSize());
        upPanel = new JPanel();
        upPanel.setOpaque(false);
        upPanel.setPreferredSize(new Dimension(UIConst.VOTE_FRAME_WIDTH, 0));
        panel.add(upPanel, BorderLayout.CENTER);

        downPanel = new JPanel();
        downPanel.setOpaque(false);
        panel.add(downPanel, BorderLayout.SOUTH);
    }

    private void addUpPanel() {
        //设置上方的布局
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        upPanel.setLayout(flowLayout);

        dimension = new Dimension(450, 50);

        titleLabel = new JLabel("投票标题");
        titleLabel.setFont(UIConst.VOTE_TITLE_FONT);
        titleLabel.setPreferredSize(dimension);
        upPanel.add(titleLabel);

        titleField = new JTextField();
        titleField.setPreferredSize(dimension);
        titleField.setFont(UIConst.VOTE_TITLE_FONT);
        titleField.setForeground(Color.GRAY);
        upPanel.add(titleField);

        textLabel = new JLabel("投票选项");
        textLabel.setFont(UIConst.VOTE_TITLE_FONT);
        textLabel.setPreferredSize(dimension);
        upPanel.add(textLabel);

        options = new ArrayList<>();
        options.add(new Option());
        upPanel.add(options.get(0));

        Dimension addAndsub = new Dimension(225, 50);

        addBtn = new MyButton("+ 添加选项", UIConst.VOTE_TITLE_FONT);
        addBtn.setPreferredSize(addAndsub);
        upPanel.add(addBtn);

        subBtn = new MyButton("- 减少选项", UIConst.VOTE_TITLE_FONT);
        subBtn.setPreferredSize(addAndsub);
        upPanel.add(subBtn);


        //TODO 单选与多选
    }

    private void addDownPanel() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
        downPanel.setLayout(flowLayout);

        cancelBtn = new JButton("取消");
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBackground(Color.red);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setFont(UIConst.VOTE_TITLE_FONT);
        downPanel.add(cancelBtn);

        //TODO 点击发起后，直接向每个人发出弹窗，提醒投票。
        voteBtn = new JButton("发起投票");
        voteBtn.setFocusPainted(false);
        voteBtn.setBackground(Color.BLUE);
        voteBtn.setForeground(Color.WHITE);
        voteBtn.setPreferredSize(new Dimension(150, 40));
        voteBtn.setFont(UIConst.VOTE_TITLE_FONT);
        downPanel.add(voteBtn);


    }

    private void addListener() {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                panel.requestFocus();
            }
        });

        titleField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (titleField.getText().equals("请填写标题")) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (titleField.getText().equals("")) {
                    titleField.setText("请填写标题");
                    titleField.setForeground(Color.GRAY);
                }

            }
        });

        addBtn.addMouseListener(new MyButtonListener(addBtn) {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                if(options.size() > 4){
                    JOptionPane.showMessageDialog(null,"那是不可能的！","提示",JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                options.add(new Option());
                upPanel.remove(addBtn);
                upPanel.remove(subBtn);
                upPanel.add(options.get(options.size() - 1));
                upPanel.add(addBtn);
                upPanel.add(subBtn);

                upPanel.updateUI();
                addBtn.setNormal();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                addBtn.setClicked();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addBtn.setNormal();
            }

        });

        subBtn.addMouseListener(new MyButtonListener(subBtn) {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                if (options.size() <= 1) {
                    JOptionPane.showMessageDialog(null, "已经不能再增加了！", "提示", JOptionPane.PLAIN_MESSAGE);
                    subBtn.setNormal();
                    return;
                }
                upPanel.remove(options.get(options.size() - 1));
                options.remove(options.get(options.size() - 1));
                upPanel.updateUI();
                upPanel.revalidate();
                subBtn.setNormal();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                subBtn.setClicked();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                subBtn.setNormal();
            }
        });

        voteBtn.addActionListener(e -> {
            String title;
            ArrayList<String> optionText = new ArrayList<>();
            if (titleField.getText().equals("") || titleField.getText().equals("请填写标题")) {
                JOptionPane.showMessageDialog(null, "标题不能为空", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).getText().equals("") || options.get(i).getText().equals("请填写选项内容")) {
                    JOptionPane.showMessageDialog(null, "选项内容不能为空", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }else{
                    optionText.add(options.get(i).getText());
                }
            }

            title = titleField.getText();

            sendVoteToServer(title,optionText);

        });

        cancelBtn.addActionListener(e -> dispose());

    }
    private void sendVoteToServer(String title,ArrayList options){
        MyWebSocket myWebSocket = MainWindows.webSocket;

        //建立option的json对象;
        JSONObject optionsJson = new JSONObject();
        for(int i = 0; i < options.size();i++){
            try {
                JSONObject optionJson =new JSONObject();
                optionJson.put("text",options.get(i));
                optionJson.put("number",0);

                optionsJson.put(""+(i+1),optionJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject commentJson = new JSONObject();

        //创建发送的json对象
        JSONObject sendVoteJson = null;
        try {
            //得到发送人的信息;
            JSONObject userJson = MainWindows.userJson;
            String id = userJson.getString("id");
            String sid = userJson.getString("sid");
            sendVoteJson = new JSONObject();
            sendVoteJson.put("title",title);
            sendVoteJson.put("author",id);
            sendVoteJson.put("sid",sid);
            sendVoteJson.put("options",optionsJson);
            sendVoteJson.put("comments",commentJson);
            sendVoteJson.put("action","onVotePanel");
        } catch (JSONException e){
            e.printStackTrace();
        }

        myWebSocket.sendToServer(sendVoteJson.toString());

        myWebSocket.setOnVotePanelListener((webSocket, response) -> {
            try {
                String code = response.getString("code");
                switch (code){
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

    public class Option extends JTextField {

        public Option(String text) {
            super(text);
            setFont(UIConst.VOTE_OPTION_FONT);
            setPreferredSize(dimension);
            setForeground(Color.GRAY);

            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals("请填写选项内容")) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().equals("")) {
                        setText("请填写选项内容");
                        setForeground(Color.GRAY);
                    }
                }
            });
        }

        public Option() {
            super("请填写选项内容");
            setFont(UIConst.VOTE_OPTION_FONT);
            setPreferredSize(dimension);
            setForeground(Color.GRAY);

            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals("请填写选项内容")) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().equals("")) {
                        setText("请填写选项内容");
                        setForeground(Color.GRAY);
                    }
                }
            });

        }

    }

    public static void main(String[] args) {
        new VotePanel();
    }
}
