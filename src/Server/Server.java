package Server;

public class Server {
    public static final String HTTP_IP = "http://localhost/jormungandr";
    public static final String IP = "ws://localhost/jormungandr";

    public static final String SOCKET = IP + "/socket";

    public static final String FILE_UPLOAD = HTTP_IP + "/upload";
    public static final String FILE_DOWNLOAD = HTTP_IP + "/download";
    public static final String FILE_DELETE = HTTP_IP + "/delete";
}
