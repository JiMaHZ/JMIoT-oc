package com.forward;


import com.huawei.utils.HttpsUtil;
import com.huawei.utils.StreamClosedHttpResponse;
;
/**
 * sand data to thingsboard
 * 
 */
public class SendTelemToTB {
/*****
	public static void main(String args[]) throws Exception {
		HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();       
        String urlTelemetry = "http://122.152.208.51:8080/api/v1/20180327/telemetry";
        String content = "{0104:95}";    
        StreamClosedHttpResponse value = httpsUtil.doPostJsonGetStatusLine(urlTelemetry,content);
        System.out.println("QueryDeviceData, response content:"+value);
        httpsUtil.close();
    	}
 ****/
	public static StreamClosedHttpResponse sendTelemetry(String content,String devId) throws Exception {
		HttpsUtil httpsUtil = new HttpsUtil();
	    //服务器作为客户端向Iot发送请求时需要的认证
		httpsUtil.initSSLConfigForTwoWay();
		
		String urlTelemetry = "http://140.143.23.199:8080/api/v1/"+devId+"/telemetry";    
	    StreamClosedHttpResponse value = httpsUtil.doPostJsonGetStatusLine(urlTelemetry,content);	     
	    System.out.println("QueryDeviceData, response content:"+value);	   
		return httpsUtil.doPostJsonGetStatusLine(urlTelemetry,content);
	}	
}

