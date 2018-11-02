package com.test;
import java.io.File;  
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;  
import org.json.JSONObject;  
import org.json.JSONTokener;

import com.forward.SendTelemToTB;
import com.huawei.utils.JsonUtil;

import mqtt.Client;
import mqtt.Server;
import silentdoer.web.controller.Base64codeToInt;

public class JsonTest {
	public static void main(String[] args) throws Exception {  
		String json="{\r\n" + 
				"\"notifyType\": \"deviceDataChanged\",\r\n" + 
				"\"requestId\": \"672781764\",\r\n" + 
				"\"timestamp\":\"2018793156\",\r\n" + 
				"\"deviceId\":\"e89e4f55-4611-403c-8\",\r\n" + 
				"\"gatewayId\":\"2c6bd53b-d7c1-4b56-a2fa-d4454dcf9ed2\",\r\n" + 
				"\"service\": {\r\n" + 
				"               \"serviceType\":\"conn\",\r\n" + 
				"               \"serviceId\":\"b\",\r\n" + 
				"               \"data\":{\r\n" + 
				"               	        \"length\":\"75\",\r\n" + 
				"               	        \"rawdata\":\"SDA5MjAkR05STUMsMDcxMDUyLjAwMCxBLDMwMTUuODUwNSxOLDEyMDA3LjAyOTEsRSwwLjAwLDAuMDAsMzAwNzE4LCwsQSo3RA0K\"\r\n" + 
				"                      },\r\n" + 
				"               \"eventTime\":\"20180713T033819Z\"\r\n" + 
				"            }\r\n" + 
				"}";
        //JSONTokener jsonTokener = new JSONTokener(new FileReader(new File("json.txt"))); 
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject pushData = new JSONObject(jsonTokener);
        //JSONArray jsonArray = new JSONArray(jsonTokener);//获取整个json文件的内容，因为最外层是数组，所以用JSONArray来构造  
        System.out.println(pushData);  
        //Client mqttSubscribe = new Client();
        //Server mqttPushData = new Server();
        
        //得到notifyType,deviceId
        String notifyType = pushData.getString("notifyType");
        System.out.println(notifyType);
        String deviceId = pushData.getString("deviceId");
        System.out.println(deviceId);
        String accessToken;
        if(deviceId.length() > 20) {
        	accessToken = deviceId.substring(0, 20);
        }else accessToken = deviceId;
        
       // mqttSubscribe.setClientId(accessToken);
        //mqttSubscribe.start();
        //mqttPushData.setUserName(accessToken);
        //mqttPushData.setClientId(accessToken);
        
       
        //得到service
        JSONObject service = pushData.getJSONObject("service");
        System.out.println(service);
        
        //d得到data
        JSONObject data = service.getJSONObject("data");
        System.out.println(data);
        //得到时间
        String eventTime = service.getString("eventTime");    
        		
        //得到length
        int length = data.getInt("length");
        String rawData = data.getString("rawdata");
        System.out.println(length);
        System.out.println(rawData);
        //base64解码成ascii码字符串
        Base64.Decoder decoder = Base64.getDecoder();

        String paramTele = new String(decoder.decode(rawData),"UTF-8");
        System.out.println(paramTele);
        /**
        int[] dataStr = Base64codeToInt.decode(rawData);
		int[] num = new int[dataStr.length/2];
		for(int i=0,j=0; i< dataStr.length;i+=2,j++) {
			num[j] = (dataStr[i] << 8) | dataStr[i+1];
			System.out.println(Integer.toHexString(dataStr[i])+Integer.toHexString(dataStr[i+1])+":"+num[j]);
		}
		**/
		 //Map<String,Integer> paramTele = new HashMap<>();
		 
       /**
        for (int i = 0; i < num.length; i++) {
            String key = (Integer.toHexString(2304+i*4)).toUpperCase();//从0900开始
	        if(key.length()<4) {
	        	  key = "0"+key;
	        }
	        String content = "{"+key+":"+num[i]+"}";
	        System.out.println(content);
	        paramTele.put(key, num[i]); 
        }
        **/
      /**发送到Thingsboard
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();
      **/
        Map<String, Object> paramPostTele = new HashMap<>();
        paramPostTele.put("ts", DateChange.Date2TimeStamp(eventTime));//转换成unix时间
        paramPostTele.put("values", paramTele);
        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostTele);
        System.out.println(jsonRequest);
        
        SendTelemToTB.sendTelemetry(jsonRequest, accessToken);
        //打印实时时间
        System.out.println(String.valueOf(System.currentTimeMillis()/1000));
        

        
      //包装成telemetry，还缺个公式
        /*
        int i;
        int dataLength = length;
        for(i=0;i<dataLength*2;i=i+2) {
        	     String hexNum = hexData.substring(i, i+2);
        	     System.out.println(hexNum);
        	     int x = Integer.parseInt(hexNum,16);
        	     System.out.println(x);
        	     String key = (Integer.toHexString(2304+i*2)).toUpperCase();
        	     System.out.println(key);
        	     String content = "{0"+key+":"+x+"}";
        	     System.out.println(content);
        	     SendToTB.sendTelemetry(content,accessToken);
        }
        **/
        System.out.println(DateChange.Date2TimeStamp(eventTime));
        /****出错的代码
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject pushData = new JSONObject(jsonTokener);  
        System.out.println(pushData);  
        
        //使用mqtt协议推送
        Server mqttPushData = new Server();
        
        //得到notifyType,deviceId,timestamp
        
        //deviceId
        String deviceId = pushData.getString("deviceId");
        String accessToken;
        if(deviceId.length() > 20) {
        	accessToken = deviceId.substring(0, 20);
        }else accessToken = deviceId;
        
        mqttPushData.setUserName(accessToken);
        mqttPushData.setClientId(accessToken);
              
        //得到service
        JSONObject service = pushData.getJSONObject("service");
        
        //得到eventTime
        String eventTime = service.getString("eventTime");
        
        //d得到data
        JSONObject data = service.getJSONObject("data");
        		
        //得到length
        int length = data.getInt("length");
        String rawData = data.getString("rawData");
        System.out.println("length:"+length);
        System.out.println("rawData:"+rawData);
        
        //Base64字符串转换成16进制数
        final Base64.Decoder decoder = Base64.getDecoder();
        String ascii = new String(decoder.decode(rawData),"UTF-8");
        System.out.println(ascii);
        
       /***
        * 发送到thingsboard
        * @param ascii,deocoder后的rawData
        * @param eventTime
        *
        //ascii码转换成10进制
        Map<String,Integer> paramTele = new HashMap<>();
        byte[] b = ascii.getBytes();
        int[] in = new int[b.length];
        System.out.println(in.length);
        for (int i = 0; i < in.length; i++) {
            in[i] = b[i]&0xff;
            System.out.println(b[i]+"  "+in[i]);
            String key = (Integer.toHexString(2304+i*4)).toUpperCase();
          //key前加个0
	        if(key.length()<4) {
	        	  key = "0"+key;
	        }
	        //String content = "{"+key+":"+in[i]+"}";
	        //System.out.println(content);
	        paramTele.put(key, in[i]);
	        //System.out.println(paramTele);
        }
        //System.out.println(paramTele);//这里也有问题了？？？？？
        
        Map<String, Object> paramPostTele = new HashMap<>();
        paramPostTele.put("ts", DateChange.Date2TimeStamp(eventTime));//转换成unix时间
        paramPostTele.put("values", paramTele);
        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostTele);
        System.out.println(jsonRequest);
        //mqttPushData.pushData(jsonRequest); 
        SendTelemToTB.sendTelemetry(jsonRequest, accessToken);
        ****/
    } 


}
