package Util;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

    public static String getString(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bs = new byte[1024];
        int len = -1;
        while (true) {
            if (!((len = inputStream.read(bs)) != -1)) break;
            stringBuffer.append(new String(bs, 0, len));
        }
        return stringBuffer.toString();
    }
}
