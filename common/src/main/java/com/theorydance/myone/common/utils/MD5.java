package com.theorydance.myone.common.utils;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

public class MD5 {
    public static String encodeMD5(String text) {
        StringBuffer md5 = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("utf-8"));
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                md5.append(Character.forDigit((digest[i] & 0xF0) >> 4, 16));
                md5.append(Character.forDigit((digest[i] & 0xF), 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5.toString();
    }

    public static String MD5RandomMask(int ww) {
        char hexDigits[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuffer STR = new StringBuffer();
        Random rand = new Random();
        while (STR.length() <= ww) try {
            STR.append(hexDigits[rand.nextInt(57)]);
        } catch (Exception e) {
        }
        return STR.substring(0, ww);
    }
    
    /**
     * 获取32位的UUID，即去掉了中间的横线分隔符
     */
    public static String getUUID32(){
    	return UUID.randomUUID().toString().replace("-", "");
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
