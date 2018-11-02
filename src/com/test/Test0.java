package com.test;

import com.huawei.utils.HttpsUtil;
import com.huawei.utils.StreamClosedHttpResponse;
;
/**
 * sand data to thingsboard
 * 
 */
public class Test0 {

	public static void main(String args[]) throws Exception {
		
		/**
		File file = new File(Test.class.getClassLoader().getResource("ca.jks").getPath());
		String Path = file.getAbsolutePath();
		System.out.println(Path);///Users/mac/eclipse-workspace/SecondSpringMVCProj/ca.jks
		
		
		/***
		HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();
        
        String urlTelemetry = "http://140.143.23.199:8080/api/v1/12345678901234567890/telemetry";
        String content = "{0108:95}";
        
        StreamClosedHttpResponse value = httpsUtil.doPostJsonGetStatusLine(urlTelemetry,content);
     ****/
       // System.out.println("QueryDeviceData, response content:"+value);
      
		//String path = this.getServletContext().getRealPath("ca.jks");
    	}
    	
	public static StreamClosedHttpResponse sendTelemetry(String content) throws Exception {
		HttpsUtil httpsUtil = new HttpsUtil();
		httpsUtil.initSSLConfigForTwoWay();
		
		 String urlTelemetry = "http://140.143.23.199:8080/api/v1/12345678901234567890/telemetry";
	    
	     StreamClosedHttpResponse value = httpsUtil.doPostJsonGetStatusLine(urlTelemetry,content);
	     
	     System.out.println("QueryDeviceData, response content:"+value);
	   
		return httpsUtil.doPostJsonGetStatusLine(urlTelemetry,content);
	
	}
	
	
}


