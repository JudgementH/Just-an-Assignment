package Server;

import UI.UIConst;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class MyWebSocket extends WebSocketListener {
    private OkHttpClient okHttpClient;

    private WebSocket socket;
    private OnOpenListener onOpenListener;
    private OnFailureListener onFailureListener;
    private OnErrorListener onErrorListener;

    private OnLoginListener onLoginListener;
    private OnRegisterListener onRegisterListener;
    private OnPublishListener onPublishLIstener;
    private OnAnnListener onAnnListener;
    private OnVotePanelListener onVotePanelListener;
    private OnReadVoteListener onReadVoteListener;
    private OnVoteListener onVoteListener;
    private OnReturnVoteListener onReturnVoteListener;
    private OnUserListListener onUserListListener;
    private OnChatListener onChatListener;
    private OnChatImageListener onChatImageListener;
    private OnFileShareListener onFileShareListener;
    private OnPaintListener onPaintListener;
    private OnOpenPaintListener onOpenPaintListener;
    private OnRecordListener onRecordListener;

    public static MyWebSocket connect(OnOpenListener onOpen, OnFailureListener onFailure) {
        MyWebSocket myWebSocket = new MyWebSocket();
        myWebSocket.okHttpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(Server.SOCKET).build();
        myWebSocket.socket = myWebSocket.okHttpClient.newWebSocket(request, myWebSocket);

        myWebSocket.onOpenListener = onOpen;
        myWebSocket.onFailureListener = onFailure;

        return myWebSocket;
    }

    public boolean sendLogin(String sid, String password, OnLoginListener onLoginListener) {
        if (onLoginListener != null) {
            this.onLoginListener = onLoginListener;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "login");
            jsonObject.put("sid", sid);
            jsonObject.put("password", password);
            if (UIConst.DEBUG) {
                System.out.println("In the sendLogin " + jsonObject);
            }
            return socket.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendRegisterToServer(String text, OnRegisterListener onRegisterListener) {
        if (onRegisterListener != null) {
            this.onRegisterListener = onRegisterListener;
        }
        try {
            JSONObject jsonObject = new JSONObject(text);
            if (UIConst.DEBUG) {
                System.out.println("In the sendRegister: " + jsonObject);
            }
            return socket.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendToServer(String text) {
        if (UIConst.DEBUG) {
            System.out.println("In the sendToServer: " + text);
        }
        return socket.send(text);

    }

    public void AddErrorListener(OnErrorListener errorListener) {
        this.onErrorListener = errorListener;
    }

    public void setOnPublishListener(OnPublishListener onPublishLIstener) { this.onPublishLIstener = onPublishLIstener; }

    public void setOnAnnListener(OnAnnListener onAnnListener) {
        this.onAnnListener = onAnnListener;
    }

    public void setOnVotePanelListener(OnVotePanelListener onVotePanelListener) { this.onVotePanelListener = onVotePanelListener; }

    public void setOnReadVoteListener(OnReadVoteListener onReadVoteListener){this.onReadVoteListener = onReadVoteListener;}

    public void setOnVoteListener(OnVoteListener onVoteListener) {
        this.onVoteListener = onVoteListener;
    }

    public void setOnReturnVoteListener(OnReturnVoteListener onReturnVoteListener) { this.onReturnVoteListener = onReturnVoteListener; }

    public void setOnUserListListener(OnUserListListener onUserListListener) { this.onUserListListener = onUserListListener; }

    public void setOnChatListener(OnChatListener onChatListener) {
        this.onChatListener = onChatListener;
    }

    public void setOnChatImageListener(OnChatImageListener onChatImageListener) { this.onChatImageListener = onChatImageListener; }

    public void setOnFileShareListener(OnFileShareListener onFileShareListener){this.onFileShareListener = onFileShareListener;}

    public void setOnOpenPaintListener(OnOpenPaintListener onOpenPaintListener){this.onOpenPaintListener = onOpenPaintListener;}

    public void setOnPaintListener(OnPaintListener onPaintListener){this.onPaintListener = onPaintListener;}

    public void setOnRecordListener(OnRecordListener onRecordListener){this.onRecordListener = onRecordListener;}


    public void close() {
        socket.close(0, "bye");
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        if (onOpenListener != null) {
            onOpenListener.onOpen(this);
            if (UIConst.DEBUG) {
                System.out.println("建立连接");
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (UIConst.DEBUG) {
            System.out.println("In the onMessage: " + text);
        }
        try {
            JSONObject jsonObject = new JSONObject(text);
            String action = jsonObject.optString("action", null);
            if (action == null) {
                System.out.println("Socket closed" + jsonObject);
                onErrorListener.onError(this);
                return;
            }
            switch (action) {
                case "login": {
                    if (onLoginListener != null) {
                        onLoginListener.OnLogin(this, jsonObject);
                    }
                    break;
                }
                case "register": {
                    if (onRegisterListener != null) {
                        onRegisterListener.OnRegister(this, jsonObject);
                    }
                    break;
                }
                case "publish": {
                    if (onPublishLIstener != null) {
                        onPublishLIstener.OnPublish(this, jsonObject);
                    }
                    break;
                }
                case "announcement": {
                    if (onAnnListener != null) {
                        onAnnListener.OnAnn(this, jsonObject);
                    }
                    break;
                }
                case "onVotePanel": {
                    if (onVotePanelListener != null) {
                        onVotePanelListener.OnVotePanel(this, jsonObject);
                    }
                    break;
                }
                case "readVote":{
                    if(onReadVoteListener != null){
                        onReadVoteListener.OnReadVote(this,jsonObject);
                    }
                    break;
                }
                case "onVotePage": {
                    if (onVoteListener != null) {
                        onVoteListener.OnVote(this, jsonObject);
                    }
                    break;
                }
                case "returnVote": {
                    if (onReturnVoteListener != null) {
                        onReturnVoteListener.OnReturnVote(this, jsonObject);
                    }
                    break;
                }
                case "getUserList": {
                    if (onUserListListener != null) {
                        onUserListListener.OnUserList(this, jsonObject);
                    }
                    break;
                }
                case "chat": {
                    if (onChatListener != null) {
                        String sendTo = jsonObject.getString("sendTo");
                        if (sendTo.equals("group")) {
                            onChatListener.OnGroupChat(this, jsonObject);
                        } else onChatListener.OnChatTo(this, jsonObject);
                    }
                    break;
                }
                case "chatImage": {
                    if (onChatImageListener != null) {
                        onChatImageListener.OnChatImage(this, jsonObject);
                    }
                    break;
                }
                case "getFile":{
                    if(onFileShareListener!=null){
                        onFileShareListener.OnGetFile(this,jsonObject);
                    }
                    break;
                }
                case "openPaint":{
                    if(onOpenPaintListener != null){
                        onOpenPaintListener.OnOpen(this,jsonObject);
                    }
                    break;
                }
                case "paint":{
                    if(onPaintListener != null){
                        onPaintListener.OnPaint(this,jsonObject);
                    }
                    break;
                }
                case "record":{
                    if(onRecordListener != null){
                        onRecordListener.OnRecord(this, jsonObject);
                    }
                    break;
                }
                case "error": {
                    System.out.println("In the onMessage 行为错误");
                    break;
                }
                default:
                    System.out.println("action 没有发回");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        t.printStackTrace();
        if (onFailureListener != null) {
            onFailureListener.OnFailure(this);
            if (UIConst.DEBUG) {
                System.out.println("连接已中断了");
            }
        }
    }

    @Override
    public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        if (UIConst.DEBUG) {
            System.out.println("连接关闭了");
        }
    }

    public interface OnLoginListener {
        void OnLogin(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnRegisterListener {
        void OnRegister(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnPublishListener {
        void OnPublish(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnAnnListener {
        void OnAnn(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnVotePanelListener {
        void OnVotePanel(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnReadVoteListener{
        void OnReadVote(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnVoteListener {
        void OnVote(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnReturnVoteListener {
        void OnReturnVote(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnUserListListener {
        void OnUserList(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnChatListener {
        void OnGroupChat(MyWebSocket webSocket, JSONObject response);

        void OnChatTo(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnChatImageListener {
        void OnChatImage(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnFileShareListener {
        void OnGetFile(MyWebSocket webSocket, JSONObject response);
    }

    public interface  OnOpenPaintListener{
        void OnOpen(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnPaintListener{
        void OnPaint(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnRecordListener{
        void OnRecord(MyWebSocket webSocket, JSONObject response);
    }

    public interface OnErrorListener {
        void onError(MyWebSocket webSocket);
    }

    public interface OnOpenListener {
        void onOpen(MyWebSocket webSocket);
    }

    public interface OnFailureListener {
        void OnFailure(MyWebSocket webSocket);
    }


}
