package com.test;

import java.util.Base64;

public class DecodeTest {
	
	public static void main(String[] args) throws Exception{ 
		
		String base64String="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		char[] base64Chars = base64String.toCharArray();
		
		String rawData1 = "e0gwOTIwOiIifQ==";
		String rawData2 = "SDA5MjA6JEdOUk1DLDA5NDAzNy4wMDAsQSwzMDE1LjgyMDAsTiwxMjAwNy4wMTgyLEUsMC4wMCw4LjExLDMwMDcxOCwsLEEqN0UNCg==";
		//12 34 ab cd 01 0e 21 2b f5 fc 11 11 00 00 a3 ff  EjSrzQEOISv1/BERAACj/w==
		//00 00 a3 7f 6a ff 01 3c 00 07 bc 4a 00 01 00 14  AACjf2r/ATwAB7xKAAEAFA==
		//11 12 3c 6f 02 45 8c 2e ERI8bwJFjC4=
		//a3 a3 a3 a3  o6Ojow==
		//70 7f 80 cH+A
		//gIGCg4SFhoeIiYqLjI2Ojw==
		//JEdOUk1DLDA5NDM1My4wMDAsQSwzMDE1Ljg2MzcsTiwxMjAwNy4wMjk5LEUsMC4wMCwzNTguODYsMjcwNzE4LCwsQSo3OA0K
		
	   Base64.Decoder decoder = Base64.getDecoder();
       
       System.out.println(new String(decoder.decode(rawData1),"UTF-8"));
       System.out.println(new String(decoder.decode(rawData2),"UTF-8"));
   
       
	}

}
