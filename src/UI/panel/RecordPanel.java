package UI.panel;

import Server.MyWebSocket;
import UI.UIConst;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RecordPanel extends JFrame {

    private JPanel mainPanel,centerPanel;
    private JLabel titleLabel;
    private JScrollPane jScrollPane;

    private ArrayList<Record> recordArrayList;


    public RecordPanel(){
        initialize();
        addComponent();
        sendToServer();
        setVisible(true);
    }
    private void initialize() {
        setSize(UIConst.RECORDRANEL_WIDTH,UIConst.RECORDPANEL_HEIGHT);
        setTitle(UIConst.RECORDPANEL_NAME);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel = (JPanel) getContentPane();
        mainPanel.setLayout(new BorderLayout());

        recordArrayList = new ArrayList<>();
    }

    private void sendToServer(){
        try {
            JSONObject reqJSON = new JSONObject();
            reqJSON.put("action","record");
            MainWindows.webSocket.sendToServer(reqJSON.toString());

            MainWindows.webSocket.setOnRecordListener(new MyWebSocket.OnRecordListener() {
                @Override
                public void OnRecord(MyWebSocket webSocket, JSONObject response) {
                    response.remove("action");
                    Iterator iterator = response.keys();
                    while (iterator.hasNext()){
                        try {
                            String key = (String) iterator.next();
                            JSONObject logJson = response.getJSONObject(key);
                            String who = logJson.getString("author");
                            String what = logJson.getString("operate");
                            String when = logJson.getString("date");
                            System.out.println(logJson.toString());
                            recordArrayList.add(new Record(who,what,when));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    addRecord();
                    centerPanel.updateUI();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addComponent() {
        titleLabel = new JLabel("管理员操作记录");
        titleLabel.setFont(UIConst.RECORDPAGE_TITLE_FONT);
        mainPanel.add(titleLabel,BorderLayout.NORTH);


        centerPanel = new JPanel();
        centerPanel.setBorder(new EmptyBorder(0,50,100,50));
        centerPanel.setLayout(new GridBagLayout());
        jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(centerPanel);
        mainPanel.add(jScrollPane,BorderLayout.CENTER);

    }

    public void addRecord(){
        Collections.reverse(recordArrayList);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 100;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 5, 0);
        constraints.ipadx = 5;
        constraints.ipady = 5;
        for (int i = 0; i < recordArrayList.size(); i++) {
            constraints.gridy = i;
            centerPanel.add(recordArrayList.get(i), constraints);
        }
    }

    public class Record extends JPanel{
        private String who;
        private String what;
        private String when;
        public Record(String who,String what,String when){
            this.who = who;
            this.what = what;
            this.when = when;

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel whoLabel = new JLabel("<html><b>"+who+"</b></html>");
            whoLabel.setFont(UIConst.RECORDPAGE_TEXT_FONT);
            add(whoLabel,BorderLayout.WEST);

            JLabel whatLabel = new JLabel(what);
            whatLabel.setFont(UIConst.RECORDPAGE_TEXT_FONT);
            add(whatLabel, BorderLayout.CENTER);


            JLabel whenLabel = new JLabel("" + when);
            whenLabel.setFont(UIConst.RECORDPAGE_TEXT_FONT);
            add(whenLabel, BorderLayout.EAST);

        }
    }

}
