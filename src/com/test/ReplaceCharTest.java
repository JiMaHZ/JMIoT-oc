package com.test;


public class ReplaceCharTest {
	
	public static void main(String args[]) throws Exception{
		String s = "v1/devices/me/rpc/request/13";
		System.out.println(s);
		s = s.replaceAll("request", "response");
		System.out.println(s);
	}

}
