package com.jieweifu.plugins.barcode.http;

import android.content.Intent;
import android.util.Log;

import com.jieweifu.plugins.barcode.Util.NoSSLv3SocketFactory;
import com.jieweifu.plugins.barcode.bean.ArgsBean;
import com.jieweifu.plugins.barcode.bean.HttpInfoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by panx on 2017/3/8.
 */
public class HttpUtil {
    /**
     * 从网络获取json数据,(String byte[})
     *
     * @param httpInfoBean
     * @return
     */
    public static String get(HttpInfoBean httpInfoBean, ResultCallback<String> callback) {
        try {
            URL url = new URL(httpInfoBean.getUrl());
            //打开连接
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setSSLSocketFactory(new NoSSLv3SocketFactory());
            urlConnection.setRequestProperty("Authorization", httpInfoBean.getHeaders().getAuthorization());
            urlConnection.setRequestProperty("Content-Type", httpInfoBean.getHeaders().getContentType());
            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                System.out.println(baos.toString());
                baos.close();
                urlConnection.disconnect();
                callback.onResponse(baos.toString(), "");
                return baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e, null);
        }

        return null;
    }
    //获取其他页面的数据

    /**
     * POST请求获取数据
     */
    public static String post(HttpInfoBean httpInfoBean, ResultCallback<String> callback) {
        URL url = null;
        try {
            url = new URL(httpInfoBean.getUrl());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(httpInfoBean.getConfig().getTimeout());
            connection.setReadTimeout(30000);
            connection.setDoOutput(true);// 是否输入参数
            connection.setDoInput(true);
            connection.setSSLSocketFactory(new NoSSLv3SocketFactory());

            connection.setRequestProperty("Authorization", httpInfoBean.getHeaders().getAuthorization());
            connection.setRequestProperty("Content-Type", httpInfoBean.getHeaders().getContentType());
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(httpInfoBean.getData());
            out.flush();
            out.close();
            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            callback.onResponse(sb.toString(), null);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e, null);
        }
        return null;
    }
}
