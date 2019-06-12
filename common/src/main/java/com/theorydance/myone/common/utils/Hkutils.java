package com.theorydance.myone.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Hkutils {

	private final static String SECRET = "b8fab40800e548e6801010c965416fba"; 
	private final static String APPKEY = "a704c020";
	
	public static List<String> webUrlList = new ArrayList<>();
    static{ // 网络时间对应的服务器
        webUrlList.add("http://www.baidu.com");
        webUrlList.add("http://www.taobao.com");
        webUrlList.add("http://www.ntsc.ac.cn");
        webUrlList.add("http://www.360.cn");
        webUrlList.add("http://www.beijing-time.org");
        webUrlList.add("http://www.bjtime.cn");
    }
	
	public static void main(String[] args) {
		System.out.println(getCamerasByEncoderUuids("641751095").toString());
	}
	
	/**
	 * 接口4.1.8 <br>
	 * 根据编码设备 UUID 集分页获取监控点【 V2.7】
	 * @param urlStr 以/开始的资源名
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getCamerasByEncoderUuids(String sim){
		JSONObject resultObj = null;
		try {
			String url = "/openapi/service/vss/res/getCamerasByEncoderUuids";
			JSONObject params = new JSONObject();
			params.put("pageNo", 1);
			params.put("pageSize", 1);
			params.put("opUserUuid", "0be83d40695011e7981e0f190ed6d2e7");
			params.put("encoderUuids", sim);
			resultObj = getDataFromInterface(url, params);
			JSONArray array = resultObj.getJSONObject("data").getJSONArray("list");
			if(array.size()>0){
				resultObj = array.getJSONObject(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}
	
	public static long getPublicTimeStamp() {
		for (int i = 0; i < webUrlList.size(); i++) {
            try {
                URL url = new URL(webUrlList.get(i));// 取得资源对象
                URLConnection uc = url.openConnection();// 生成连接对象
                uc.setReadTimeout(500);
                uc.setConnectTimeout(500);
                uc.connect();// 发出连接
                long ld = uc.getDate();// 读取网站日期时间
                return ld;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
	}
	
	private static JSONObject getDataFromInterface(String urlStr,JSONObject params) throws Exception{
		params.put("appkey", APPKEY);
		params.put("time", getPublicTimeStamp());
		
		String ip = "http://139.159.247.230:80";
		String token = MD5(urlStr + params.toString() + SECRET);
		URL url = new URL(ip + urlStr + "?token=" + token);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		byte[] buffer = new byte[1024];
		int len = 0;
		StringBuffer sbuffer = new StringBuffer();
		OutputStream output = conn.getOutputStream();
		output.write(params.toString().getBytes());
		InputStream input = conn.getInputStream();
		while((len=input.read(buffer))!=-1){
			sbuffer.append(new String(buffer,0,len,"UTF-8"));
		}
		input.close();
		return JSONObject.fromObject(sbuffer.toString());
	}
	
	/**
	 * 对字符串md5加密(大写+数字)
	 * @param str 传入要加密的字符串
	 * @return MD5加密后的字符串
	 */
	public static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
