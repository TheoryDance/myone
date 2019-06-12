package com.theorydance.myone.common;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class WSFileUploadClientTest {
 
    public static String BOUNDARY = "----------" + System.currentTimeMillis();
 
    /**
     * 向服务器发送post请求
     * @param serverUrl URL地址
     * @return 服务器返回结果
     * @param formText 文本表单
     * @throws Exception 
     * 
     */
    public static String sendHttpPostRequest(String serverUrl, String[] filePaths, Map<String, String> formText) throws Exception {
        // 向服务器发送post请求
        URL url = null;
        try {
            url = new URL(serverUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
 
        // 发送POST请求头信息
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(200000);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
 
        StringBuilder message = new StringBuilder();//存放请求信息
        // 链接服务器获得输出流
        try (OutputStream out = new DataOutputStream(connection.getOutputStream())) {
            // 第一部分：传递文本表单
            for (String key : formText.keySet()) {
                message.append("--").append(BOUNDARY).append("\r\n").append("Content-Disposition: form-data; name=\"")
                        .append(key + "\"").append("\r\n").append("\r\n").append(formText.get(key)).append("\r\n");
            }
            // 写入文本表单信息
            String boundaryMessage1 = message.toString();
            out.write(boundaryMessage1.getBytes("utf-8"));
         // 第二部分： 循环读取上传文件读取个文件
            for (int i = 0; i < filePaths.length; i++) {
                File file = new File(filePaths[i]);
                if (!file.exists()) {
                    throw new Exception("文件不存在或路径错误!");
                }
                message.delete(0, message.length());
                message.append("--");
                message.append(BOUNDARY);
                message.append("\r\n");
                message.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
                message.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] head = message.toString().getBytes("utf-8");
 
                // 输出文件表头
                out.write(head);
                // 文件正文部分
                // 把文件已流文件的方式 推入到url中
                try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 写入两个文件之间得分隔符，如果没有两个文件内容会被写在一个文件中
                out.write("\r\n".getBytes("utf-8"));
            }
            // 结尾部分
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            return e1.getMessage();
        }
        // 4. 从服务器获得回答的内容
        String strLine = "";
        String strResponse = "";
        try (InputStream responseIO = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseIO));) {
 
            while ((strLine = reader.readLine()) != null) {
                strResponse += strLine + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return strResponse;
    }
 
    public static void main(String[] args) {
    	JSONObject obj = new JSONObject();
		obj.put("descript", "test file to MultipartFile");
		String jsonInfo = obj.toString();
		String code = "version_1.2";
		String key = "app_package";
    	
        //map用于存放input数据key对应name   value对应map的value
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        map.put("key", key);
        map.put("fileJson", jsonInfo);
		
        String specUrl = "http://139.159.252.39:9080/data-center/upload/file";
        try {
            //这里的String[] 数组用于存放文件路径集合
            String response = WSFileUploadClientTest.sendHttpPostRequest(specUrl,new String[] {"d:/grand_souce.apk"},map);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}