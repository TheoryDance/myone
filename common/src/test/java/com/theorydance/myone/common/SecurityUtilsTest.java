package com.theorydance.myone.common;

import org.junit.Test;

import com.theorydance.myone.common.utils.SecurityUtils;

public class SecurityUtilsTest {

	@Test
	public void base64EncodeTest() {
		String sourceStr = "grand_ranfs@163.com";
		String encodeResult = SecurityUtils.base64Encode(sourceStr);
		String decodeResult = SecurityUtils.base64Decode(encodeResult);
		System.out.println("base64加密源字符串："+sourceStr);
		System.out.println("base64加密结果："+encodeResult);
		System.out.println("base64解密结果："+decodeResult);
	}
}
