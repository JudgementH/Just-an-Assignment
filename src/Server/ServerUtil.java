package Server;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * 网络请求接口
 *
 * @author 初始状态0
 * @date 2019/2/26 12:43
 */
public class ServerUtil {
    public static InputStream postRequest(String address, Parameter... parameters) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Parameter parameter : parameters) {
            bodyBuilder.add(parameter.getKey(), parameter.getValue());
        }
        Request request = new Request.Builder().url(address).post(bodyBuilder.build()).build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        if (code != 200) {
            throw new IOException(""+code);
        }
        ResponseBody responseBody = response.body();
        assert responseBody != null;
        return new BufferedInputStream(responseBody.byteStream());
    }

    public static InputStream getRequest(String address, Parameter... parameters) throws IOException {
        OkHttpClient client = new OkHttpClient();
        StringBuilder urlBuilder = new StringBuilder(address);
        urlBuilder.append('?');
        for (Parameter parameter : parameters) {
            urlBuilder.append(URLEncoder.encode(parameter.getKey(), "utf-8"));
            urlBuilder.append('=');
            urlBuilder.append(URLEncoder.encode(parameter.getValue(), "utf-8"));
            urlBuilder.append('&');
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        Request request = new Request.Builder().url(urlBuilder.toString()).get().build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        if (code != 200) {
            throw new IOException(""+code);
        }
        ResponseBody responseBody = response.body();
        assert responseBody != null;
        return new BufferedInputStream(responseBody.byteStream());
    }

    public static String stream2String(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * 传参
     */
    public static class Parameter {
        private String key;
        private Object value;

        public Parameter(String key, Object value) {
            if (key == null || value == null) {
                throw new NullPointerException();
            }
            this.key = key;
            this.value = value;
        }

        private String getKey() {
            return key;
        }

        private String getValue() {
            return String.valueOf(value);
        }
    }

    public static class HttpRequest {
        public final String address;
        public final Parameter[] parameters;

        public HttpRequest(String address, Parameter[] parameters) {
            this.address = address;
            this.parameters = parameters;
        }
    }
}