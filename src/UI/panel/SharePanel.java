package UI.panel;

import Server.MyWebSocket;
import Server.Server;
import Server.ServerUtil;
import UI.UIConst;
import UI.component.MyButton;
import Util.FileUtil;
import Util.StreamUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import online.cszt0.cmt.util.FileDialog;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;

public class SharePanel extends JPanel {

    private JPanel upPanel, centerPanel, mainPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton upLoadButton, refreshButton;
    private ArrayList<File> fileArrayList;

    private Dimension picDimension;

    //TODO 上传人的名字
    public SharePanel() {
        initialize();
        addButtonPanel();
        sendRequest();
        addMainComponent();
        addListener();
    }

    private void initialize() {
        setOpaque(false);
        setLayout(new BorderLayout());

        picDimension = new Dimension(64, 64);
        fileArrayList = new ArrayList<>();
    }

    private void addButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Dimension buttonDimension = new Dimension(200, 50);

        upLoadButton = new JButton("上传文件");
        upLoadButton.setFont(UIConst.SHAREPAGE_BOLDFONT);
        upLoadButton.setForeground(Color.WHITE);
        upLoadButton.setBackground(Color.GRAY);
        upLoadButton.setFocusPainted(false);
        upLoadButton.setPreferredSize(buttonDimension);
        buttonPanel.add(upLoadButton);

        refreshButton = new JButton("刷新");
        refreshButton.setFont(UIConst.SHAREPAGE_BOLDFONT);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(Color.GRAY);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(buttonDimension);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    public void sendRequest() {
        try {
            //发送请求
            MyWebSocket webSocket = MainWindows.webSocket;
            JSONObject sendJSON = new JSONObject();
            sendJSON.put("action", "getFile");

            webSocket.sendToServer(sendJSON.toString());

            //设置接收
            MainWindows.webSocket.setOnFileShareListener(new MyWebSocket.OnFileShareListener() {
                @Override
                public void OnGetFile(MyWebSocket webSocket, JSONObject response) {
                    removeFile();
                    JSONObject allFileJSON = response;
                    allFileJSON.remove("action");
                    /**
                     * 把文件组成一个大的JSON
                     * fileJSON{
                     *     1:{
                     *         filename:文件名称
                     *         submissionDate:创建时间
                     *     }
                     * }
                     */

                    Iterator iterator = allFileJSON.keys();
                    while (iterator.hasNext()) {
                        try {
                            String key = (String) iterator.next();
                            JSONObject fileJSON = allFileJSON.getJSONObject(key);
                            String filename = fileJSON.getString("filename");
                            String submissionDate = fileJSON.getString("submissionDate");
                            fileArrayList.add(new File(filename, submissionDate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    addFile();
                    updateUI();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMainComponent() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 50, 100, 50));

        //设置上方的面板
        upPanel = new JPanel();
        upPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        upPanel.setLayout(new BorderLayout());

        JLabel picAndName = new JLabel("文件名称");
        picAndName.setBorder(new EmptyBorder(0, 10, 0, 0));
        picAndName.setFont(UIConst.SHAREPAGE_FONT);
        upPanel.add(picAndName, BorderLayout.WEST);

        JLabel comment = new JLabel("", SwingConstants.CENTER);
        comment.setFont(UIConst.SHAREPAGE_FONT);
        upPanel.add(comment, BorderLayout.CENTER);

        JLabel date = new JLabel("上传时间");
        date.setBorder(new EmptyBorder(0, 0, 0, 10));
        date.setFont(UIConst.SHAREPAGE_FONT);
        upPanel.add(date, BorderLayout.EAST);

        mainPanel.add(upPanel, BorderLayout.NORTH);

        //设置中间的面板
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        scrollPane = new JScrollPane(centerPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JScrollPane jScrollPane = new JScrollPane(mainPanel);
        add(jScrollPane, BorderLayout.CENTER);
    }

    private void addListener() {
        //TODO 处理重名问题
        upLoadButton.addActionListener(e -> {
            online.cszt0.cmt.util.FileDialog fileDialog = FileDialog.getPlatInstance();
            String filePath = fileDialog.showOpenDialog();
            if (filePath != null) {
                try {
                    java.io.File file = new java.io.File(filePath);
                    String filename = file.getName();
                    //读取文件并变为byte数组
                    byte[] bytes = FileUtil.getBytes(filePath);
                    String fileText = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);

                    //获取上传时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String submissionDate = dateFormat.format(new Date());

                    JSONObject sendJSON = new JSONObject();
                    sendJSON.put("fileText", fileText);
                    sendJSON.put("upId", MainWindows.userJson.getString("id"));
                    sendJSON.put("upSid", MainWindows.userJson.getString("sid"));
                    sendJSON.put("filename", filename);
                    sendJSON.put("submissionDate", submissionDate);

                    //传送并接收
                    InputStream is = ServerUtil.postRequest(Server.FILE_UPLOAD, new ServerUtil.Parameter("sendJSON", sendJSON));

                    JSONObject resp = new JSONObject(StreamUtil.getString(is));
                    String code = resp.getString("code");
                    if (UIConst.DEBUG) {
                        System.out.println(resp);
                    }
                    switch (code) {
                        case "0": {
                            JOptionPane.showMessageDialog(null, "上传成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                            sendRequest();
                            break;
                        }
                        default: {
                            JOptionPane.showMessageDialog(null, "发送失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                        }
                    }

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });

        refreshButton.addActionListener(e -> {
            sendRequest();
        });
    }

    public void removeFile() {
        fileArrayList.clear();
        centerPanel.removeAll();
    }

    public void addFile() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 100;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 5, 0);
        for (int i = 0; i < fileArrayList.size(); i++) {
            constraints.gridy = i;
            centerPanel.add(fileArrayList.get(i), constraints);
        }

    }

    public class File extends JPanel {
        private String filename;
        private String submissionDate;
        private String comment;
        private JButton picButton;
        private MyButton textButton;

        public File(String filename, String submissionDate) {
            this.filename = filename;
            this.submissionDate = submissionDate;
            comment = "";

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JPanel picAndText = new JPanel(new BorderLayout());
            picButton = new JButton(UIConst.SHAREPAGE_FILE);
            picButton.setBorder(null);
            picButton.setPreferredSize(picDimension);
            picButton.setContentAreaFilled(false);
            textButton = new MyButton(filename, UIConst.SHAREPAGE_FONT);
            textButton.setClicked();
            picAndText.add(picButton, BorderLayout.WEST);
            picAndText.add(textButton, BorderLayout.CENTER);
            add(picAndText, BorderLayout.WEST);

            JLabel commentLabel = new JLabel(comment);
            add(commentLabel, BorderLayout.CENTER);


            JLabel dateLabel = new JLabel("" + submissionDate);
            dateLabel.setFont(UIConst.SHAREPAGE_FONT);
            add(dateLabel, BorderLayout.EAST);

            addFileListener();
        }

        public void addFileListener() {
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    showFilePopupMenu(e.getComponent(), e.getX(), e.getY());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //TODO 可以变颜色
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //TODO 可以变颜色
                }
            });
        }

        public void showFilePopupMenu(Component invoker, int x, int y) {
            JPopupMenu jPopupMenu = new JPopupMenu();
            Dimension itemDimension = new Dimension(150, 50);

            JMenuItem downLoadItem = new JMenuItem("下载文件");
            downLoadItem.setPreferredSize(itemDimension);
            downLoadItem.setFont(UIConst.SHAREPAGE_MENU_FONT);
            //TODO 给downLoadItem添加监听
            downLoadItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    online.cszt0.cmt.util.FileDialog fileDialog = FileDialog.getPlatInstance();
                    String filePath = fileDialog.showSaveDialog();
                    String filename = getFilename();
                    try {
                        JSONObject reqJSON = new JSONObject();
                        reqJSON.put("desPath", filePath);
                        reqJSON.put("filename", filename);
                        //连接服务器，发送相应的文件到前端
                        InputStream inputStream = ServerUtil.getRequest(Server.FILE_DOWNLOAD, new ServerUtil.Parameter("reqJSON", reqJSON));
                        JSONObject respJSON = new JSONObject(StreamUtil.getString(inputStream));
                        String fileText = respJSON.getString("fileText");
                        String code = respJSON.getString("code");
                        if (UIConst.DEBUG) {
                            System.out.println("目标路径:" + filePath);
                            System.out.println("接收到文件:" + fileText);
                        }
                        if (code.equals("0")) {
                            //base64解码
                            byte[] bytes = Base64.getDecoder().decode(fileText.replace("\r\n", ""));
                            //把byte数组转化为文件并保存
                            FileUtil.getFile(bytes, filePath, filename);
                            JOptionPane.showMessageDialog(null, "下载成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                        } else JOptionPane.showMessageDialog(null, "下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            jPopupMenu.add(downLoadItem);
            jPopupMenu.addSeparator();

            JMenuItem deleteItem = new JMenuItem("删除");
            deleteItem.setPreferredSize(itemDimension);
            deleteItem.setFont(UIConst.SHAREPAGE_MENU_FONT);
            deleteItem.addActionListener(e -> {
                //获取删除文件的名称
                try {
                    String filename = getFilename();
                    JSONObject reqJson = new JSONObject();
                    reqJson.put("action", "deleteFile");
                    reqJson.put("filename", filename);
                    InputStream inputStream = ServerUtil.postRequest(Server.FILE_DELETE, new ServerUtil.Parameter("reqJson", reqJson));
                    JSONObject respJSON = new JSONObject(StreamUtil.getString(inputStream));
                    String code = respJSON.getString("code");
                    switch (code) {
                        case "0":{
                            JOptionPane.showMessageDialog(null, "删除成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                            sendRequest();
                            break;
                        }
                        case "1":{
                            JOptionPane.showMessageDialog(null, "删除失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                            break;
                        }
                        default:{
                            JOptionPane.showMessageDialog(null, "服务器错误！", "提示", JOptionPane.PLAIN_MESSAGE);
                        }

                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            jPopupMenu.add(deleteItem);


//            JMenuItem renameItem = new JMenuItem("重命名");
//            renameItem.setPreferredSize(itemDimension);
//            renameItem.setFont(UIConst.SHAREPAGE_MENU_FONT);
//            jPopupMenu.add(renameItem);
//
//            JMenuItem recommentItem = new JMenuItem("更改注释");
//            recommentItem.setPreferredSize(itemDimension);
//            recommentItem.setFont(UIConst.SHAREPAGE_MENU_FONT);
//            jPopupMenu.add(recommentItem);
//

            jPopupMenu.show(invoker, x, y);
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getSubmissionDate() {
            return submissionDate;
        }

        public void setSubmissionDate(String submissionDate) {
            this.submissionDate = submissionDate;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

    }

}
