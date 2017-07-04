package com.jiangzehui.httpurlconnectiondemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        final HashMap<String,String> map =new HashMap<>();
        map.put("type","top");
        map.put("key","9e05423f7ac6acf6d0dce3425c4ea9fe");



        new Thread() {
            @Override
            public void run() {
                super.run();
                //get("http://v.juhe.cn/toutiao/index?type=&key=9e05423f7ac6acf6d0dce3425c4ea9fe");
                post("http://v.juhe.cn/toutiao/index",map);
                //sendPost("http://v.juhe.cn/toutiao/index","type=&key=9e05423f7ac6acf6d0dce3425c4ea9fe");
            }
        }.start();
    }


    void get(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);//设置连接超时
            connection.setReadTimeout(5000);//设置数据读取超时
            connection.setRequestMethod("GET");//设置请求方式
            connection.setUseCaches(true);//设置是否使用缓存  默认是true
            connection.setRequestProperty("Content-Type", "application/json");//设置请求中的媒体类型信息。
            connection.addRequestProperty("Connection", "Keep-Alive");//设置客户端与服务连接类型

            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.close();
                is.close();
                byte[] byteArray = baos.toByteArray();
                final String result = new String(byteArray);
                Log.e("result=", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(result);
                    }
                });
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void post(String path, HashMap<String, String> paramsMap) {
        try {
            Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
            StringBuilder sb=new StringBuilder();
            int poi=0;
            while (it.hasNext()) {
                if(poi>0){
                    sb.append("&");
                }
                Map.Entry entry = it.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                sb.append(String.format("%s=%s",key,URLEncoder.encode(value,"utf8")));
                poi++;

            }
            String params = sb.toString();
            Log.e("result=", params);
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();

            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);//设置连接超时
            connection.setReadTimeout(5000);//设置数据读取超时
            connection.setRequestMethod("POST");//设置请求方式
            connection.setUseCaches(false);//Post请求不能使用缓存
            connection.setDoOutput(true);//Post请求必须设置允许输出 默认false
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(true);//设置本次连接是否自动处理重定向
            connection.setRequestProperty("accept", "*/*");
            connection.addRequestProperty("Connection", "Keep-Alive");//设置客户端与服务连接类型
            //connection.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.close();
                is.close();
                byte[] byteArray = baos.toByteArray();
                final String result = new String(byteArray);
                Log.e("result=", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(result);
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            Log.e("result=", result);
            final String finalResult = result;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(finalResult);
                }
            });
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
