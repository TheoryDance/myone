package com.theorydance.myone.common;

import org.junit.Test;

import com.theorydance.myone.common.utils.MD5;

public class MD5Test {

	@Test
	public void encodeMD5Test() {
		System.out.println(MD5.encodeMD5("grand_ranfs@023"));
		System.out.println(MD5.md5("grand_ranfs@023"));
	}
	
}
