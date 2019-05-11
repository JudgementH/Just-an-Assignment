package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import okhttp3.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class VotePage extends JFrame {
    private String author;
    private String sid;
    private String submission_date;
    private String title;

    private JSONObject jsonObject;
    private JSONObject options;
    private JSONObject comments;
    private JSONObject unSendUser;
    private String unSendUserTxt;
    private Dimension dimension;

    private ArrayList<Option> arrayList;

    private int n;

    private JPanel panel;

    public VotePage(String msg) {
        initialize();
        initializeMsg(msg);
        addComponent();
        addScroll();
        setVisible(true);
        if(unSendUserTxt.equals("{}") || unSendUserTxt.equals("")){
            JOptionPane.showMessageDialog(null, "投票完成！", "提示", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void initialize() {
        //设置颜色与布局与大小
        setSize(UIConst.VOTEPAGE_FRAME_WIDTH, UIConst.VOTEPAGE_FRAME_HEIGHT);
        setTitle("投票通知");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panel = new JPanel();
//        panel.setLayout(new MyFlowLayout());
        panel.setPreferredSize(new Dimension(UIConst.VOTEPAGE_DIMENSION_WIDTH, 0));

        n = 0;

    }

    private void initializeMsg(String msg) {
        try {
            jsonObject = new JSONObject(msg);
            author = jsonObject.getString("author");
            sid = jsonObject.getString("sid");
            submission_date = jsonObject.getString("submission_date");
            title = jsonObject.getString("title");
            options = new JSONObject(jsonObject.getString("options"));
            unSendUser = new JSONObject(jsonObject.getString("unSendUser"));
            unSendUserTxt = jsonObject.getString("unSendUser");
            if(jsonObject.getString("comments").equals("{}")){
                comments = new JSONObject();
            }else comments = new JSONObject(jsonObject.getString("comments"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addComponent() {
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panel.setOpaque(false);
        dimension = new Dimension(UIConst.VOTEPAGE_DIMENSION_WIDTH, 50);

        //添加发布人相关信息
        JLabel idAndsid = new JLabel("" + "<html>" + "<b>发布人:</b> " + author + "#" + sid + "<div>" + submission_date + " </div>" + "</html>");
        idAndsid.setFont(UIConst.VOTEPAGE_TITLE_FONT);
        idAndsid.setPreferredSize(dimension);
        panel.add(idAndsid);

        //添加标题相关信息
        JLabel titleLabel = new JLabel("" + "<html><h1><b>" + title + "</b></h1></html>");
        titleLabel.setFont(UIConst.VOTEPAGE_TITLE_FONT);
        titleLabel.setPreferredSize(new Dimension(UIConst.VOTEPAGE_DIMENSION_WIDTH, 100));
        panel.add(titleLabel);

        //添加选项
        addOptions();

        //添加评论两个字
        JLabel comment = new JLabel("<html><b><h1>评论</h1></b></html>");
        comment.setFont(UIConst.VOTEPAGE_TITLE_FONT);
        setPreferredSize(dimension);
        panel.add(comment);

        //添加评论框
        JTextArea commentArea = new JTextArea();
        commentArea.setBorder(BorderFactory.createLineBorder(new Color(0x8CDDFF)));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(UIConst.VOTEPAGE_TEXT_FONT);
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        commentScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        commentScroll.setPreferredSize(new Dimension(UIConst.VOTEPAGE_DIMENSION_WIDTH, 100));
        commentArea.setCaretPosition(commentArea.getText().length());
        comment.requestFocus();
        commentScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(commentScroll);

        //添加发送按钮
        JButton sendBtn = new JButton("发送");
        if(unSendUserTxt.equals("{}") || unSendUserTxt.equals("")){
            sendBtn.setEnabled(false);
        }
        sendBtn.setPreferredSize(dimension);
        sendBtn.setBackground(new Color(0x298CFF));
        sendBtn.setFont(UIConst.VOTEPAGE_TITLE_FONT);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusable(false);
        sendBtn.addActionListener(e -> {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).isClicked()) {
                    try {
                        JSONObject option = options.getJSONObject("" + (i+1));
                        option.put("num", arrayList.get(i).getNewNum());
                        if(UIConst.DEBUG){
                            System.out.println("" + i + "\t" + option.getInt("num"));
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (!commentArea.getText().isEmpty()) {
                try {
                    JSONObject newComment = new JSONObject();
                    newComment.put("number", comments.length()+1);
                    newComment.put("text", commentArea.getText());
                    comments.put("" + (comments.length() + 1), newComment);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            //除名unSendUser
            try {
                JSONObject unSendUser = new JSONObject(jsonObject.getString("unSendUser"));
                Iterator iterator = unSendUser.keys();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    String value = unSendUser.getString(key);
                    if(value.equals(MainWindows.userJson.getString("sid"))){
                        unSendUser.remove(key);
                        jsonObject.put("unSendUser",unSendUser);
                        break;
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            //增加评论
            try {
                JSONObject comments = new JSONObject(jsonObject.getString("comments"));

                JSONObject newComment = new JSONObject();
                newComment.put("number",(comments.length()+1));
                newComment.put("text",commentArea.getText());

                comments.put(""+(comments.length()+1),newComment);
                jsonObject.put("comments",comments);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            //更新票数
            try {
                JSONObject options = new JSONObject(jsonObject.getString("options"));
                for(int i =0;i < arrayList.size();i++){
                    if(arrayList.get(i).isClicked()){
                        JSONObject newOption = options.getJSONObject(""+(i+1));
                        newOption.put("number",arrayList.get(i).getNewNum());
                    }
                }

                jsonObject.put("options",options);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            if(UIConst.DEBUG){
                System.out.println(jsonObject.toString());
            }

            //更新action
            try {
                jsonObject.put("action","updateVote");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            //添加发送人信息
            try {
                jsonObject.put("from",MainWindows.userJson.getString("sid"));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            MainWindows.webSocket.sendToServer(jsonObject.toString());

            MainWindows.webSocket.setOnReturnVoteListener((webSocket, response) -> {
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
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            });

        });
        panel.add(sendBtn);

        //添加评论
        if (!comments.toString().equals("{}")) {
            addComment();
        }

        if(unSendUserTxt.equals("{}") || unSendUserTxt.equals("")){
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    try {
                        jsonObject.put("action","finishVote");
                        MainWindows.webSocket.sendToServer(jsonObject.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }


    }

    private void addOptions() {
        Iterator iterator = options.keys(); //设置迭代器来接收option的key值

        arrayList = new ArrayList<>(); // 用来保存每个选项
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            String key = (String) iterator.next(); //用于获取每个JSON的属性
            try {
                JSONObject opitonJson = options.getJSONObject(key);
                String text = opitonJson.getString("text");
                int num = opitonJson.getInt("number");
                Option option = new Option(i, text, num);
                arrayList.add(option);
                //添加事件
                option.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        for (int j = 0; j < arrayList.size(); j++) {
                            arrayList.get(j).setDefault();
                        }
                        option.setClicked();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                n++;
                panel.add(option);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void addComment() {
        Iterator iterator = comments.keys();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            String key = (String) iterator.next();
            try {
                JSONObject commentJson = comments.getJSONObject(key);
                int number = commentJson.getInt("number");
                String text = commentJson.getString("text");
                Comment comment = new Comment(number, text);
                n++;
                panel.add(comment);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addScroll() {
        panel.setPreferredSize(new Dimension(UIConst.VOTEPAGE_DIMENSION_WIDTH, 400 + 70 * n));

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(panel);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        jScrollPane.setOpaque(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jScrollPane, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
    }

    public class Option extends JPanel {

        private Dimension optionDimension;
        private JLabel textLabel;
        private JLabel numLabel;

        private int i;
        private String text;
        private int num;
        private int newNum;

        public Option(int i, String text, int num) {
            this.i = i;
            this.text = text;
            this.num = num;
            newNum = num;

            optionDimension = new Dimension(UIConst.VOTEPAGE_DIMENSION_WIDTH / 2, 50);

            textLabel = new JLabel("" + i + ". " + text);
            textLabel.setHorizontalAlignment(SwingConstants.LEFT);
            textLabel.setPreferredSize(optionDimension);
            textLabel.setFont(UIConst.VOTEPAGE_TITLE_FONT);

            numLabel = new JLabel(num + "票");
            numLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            numLabel.setPreferredSize(optionDimension);
            numLabel.setFont(UIConst.VOTEPAGE_TITLE_FONT);

            this.setLayout(new GridLayout(1, 2));
            this.add(textLabel);
            this.add(numLabel);
            this.setPreferredSize(dimension);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        }

        public Option getOption() {
            return this;
        }

        public void setClicked() {
            setBackground(new Color(0x0DA5FF));
            newNum++;
            numLabel.setText(newNum + "票");
            numLabel.repaint();
            repaint();

        }

        public boolean isClicked() {
            if (num + 1 == newNum) {
                return true;
            } else return false;
        }

        public void setDefault() {
            newNum = num;
            textLabel.setText("" + i + ". " + text);
            numLabel.setText(num + "票");
            setBackground(null);
        }

        public int getNewNum() {
            return newNum;
        }


    }

    public class Comment extends JPanel {
        private int number;
        private String text;
        private JLabel textLabel;
        private JTextArea textArea;

        public Comment(int number, String text) {
            this.number = number;
            this.text = text;


            textArea = new JTextArea();
            textArea.setOpaque(false);
            textArea.setText("" + number + ": " + text);
            textArea.setFont(UIConst.VOTEPAGE_TITLE_FONT);
            textArea.setPreferredSize(dimension);
            textArea.setMinimumSize(dimension);


            add(textArea);

        }

        public Comment getComment() {
            return this;
        }

    }


    public static void main(String[] args) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("author", "Jojo");
        jsonObject.put("sid", "123456789");
        jsonObject.put("submission_date", "2019.4.24");
        jsonObject.put("title", "这个好厉害啊");

        JSONObject options = new JSONObject();
        for (int i = 0; i < 3; i++) {
            JSONObject option = new JSONObject();
            option.put("text", "这是个选项");
            option.put("number", 5);
            options.put("" + (i + 1), option);
        }

        jsonObject.put("options", options);


        JSONObject comments = new JSONObject();
        for (int i = 0; i < 6; i++) {
            JSONObject comment = new JSONObject();
            comment.put("number", (i + 1));
            comment.put("text", "这个是评论啊");

            comments.put("" + (i + 1), comment);
        }
        jsonObject.put("comments", comments);
        System.out.println(jsonObject.toString());
        new VotePage(jsonObject.toString());
    }

}
