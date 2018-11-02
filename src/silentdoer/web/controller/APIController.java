package silentdoer.web.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
  
import org.json.JSONObject;  
import org.json.JSONTokener;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.forward.QueryDevice;
import com.forward.SendTelemToTB;
import com.huawei.utils.Constant;
import com.huawei.utils.JsonUtil;
import com.test.DateChange;

import mqtt.Client;
import mqtt.DevServer;
import mqtt.Server;

@Controller
@RequestMapping("/api")
public class APIController {
	/**
	 * 测试用
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<HttpStatus> getUser() throws IOException{
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("name", "zhangsan");
		
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	/**
	 * 接受设备数据变化的推送，转发设备数据到thingsboard
	 * @param json
	 * @throws Exception
	 */
	@RequestMapping(value="/updataDeviceData",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpStatus> dataPush(@RequestBody String json) throws Exception{
		long tim0 = System.currentTimeMillis();
		 //得到总的数据，转换成JSONObject类型
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject pushData = new JSONObject(jsonTokener);
        System.out.println(pushData);  
        //Client mqttSubscribe = new Client();
        
        //得到notifyType,deviceId
        //String notifyType = pushData.getString("notifyType");
        String deviceId = pushData.getString("deviceId");
        String accessToken = QueryDevice.QueryNodeId(deviceId);
        System.out.println(accessToken);
        /**
        if(deviceId.length() > 20) {
        	accessToken = deviceId.substring(0, 20);
        }else accessToken = deviceId;       
         ***/      
        //得到service
        JSONObject service = pushData.getJSONObject("service");
        System.out.println(service);
        
        //得到data
        JSONObject data = service.getJSONObject("data");
        System.out.println(data);
        //得到时间
        String eventTime = service.getString("eventTime");    
        		
        //得到length
        int length = data.getInt("length");
        String rawData = data.getString("rawData");
        System.out.println(length);
        System.out.println(rawData);
        
        //base64解码       
        int[] dataStr = Base64codeToInt.decode(rawData);
		int[] num = new int[dataStr.length/2];
		for(int i=0,j=0; i< dataStr.length;i+=2,j++) {
			num[j] = (dataStr[i] << 8) | dataStr[i+1];
			System.out.println(Integer.toHexString(dataStr[i])+Integer.toHexString(dataStr[i+1])+":"+num[j]);
		}
		
		 Map<String,Integer> paramTele = new HashMap<>();
		 
        //final Base64.Decoder decoder = Base64.getDecoder();
        //System.out.println(decoder.decode(rawData));
        //String ascii = new String(decoder.decode(rawData),"UTF-8");        
        //System.out.println("ascii:"+ascii);
       
        for (int i = 0; i < num.length; i++) {
            String key = (Integer.toHexString(2304+i*4)).toUpperCase();//从0900开始
            //key前加个0
	        if(key.length()<4) {
	        	  key = "0"+key;
	        }
	        String content = "{"+key+":"+num[i]+"}";
	        System.out.println(content);
	        paramTele.put(key, num[i]); 
        }
      /**发送到Thingsboard
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();
      **/
        Map<String, Object> paramPostTele = new HashMap<>();
        paramPostTele.put("ts", DateChange.Date2TimeStamp(eventTime));//转换成unix时间
        paramPostTele.put("values", paramTele);
        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostTele);
        System.out.println(jsonRequest);
        //用mqtt发送
        /**
         * Server mqttPushData = new Server();
         * mqttPushData.setUserName(accessToken);
         * mqttPushData.setClientId(accessToken);
         * mqttPushData.pushData(jsonRequest);
         */
        //用http发送
        SendTelemToTB.sendTelemetry(jsonRequest, accessToken);
        
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);		
	}
	/**
	 * 接受设备绑定激活的推送，订阅thingsboard的rpc命令，转发命令
	 * 设备响应命令后会返回结果，推送到同一个URI，用notifyType来辨别进行不同的处理？？？
	 * @URI api/rpcSubscribe
	 * @param json
	 * @throws Exception
	 */
	@RequestMapping(value="/bindDeviceAndSubRPC",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpStatus> rpcSubscribe(@RequestBody String json) throws Exception{
		Client mqttClient = new Client();
		DeviceModel device = new DeviceModel();
				
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject jsonData = new JSONObject(jsonTokener);           
        String deviceId = jsonData.getString("deviceId");
        System.out.println(jsonData);
        
        device.setDeviceId(deviceId);
        device.setAccessToken(QueryDevice.QueryNodeId(deviceId));
        //在设备列表中加入一个设备实例       
		Iterator<DeviceModel> iterator = Constant.DEVICE_REPO.iterator();
		while(iterator.hasNext()){
			DeviceModel dev = iterator.next();
			if(device.deviceId.equals(dev.deviceId)) {
				//如果本来就有，删掉原来的，用新的覆盖
				iterator.remove();			
			}
		}
		Constant.DEVICE_REPO.add(device);
       
        mqttClient.setUsernameAndCallback(device);//直接传入一个devicemodel，将来还会存储service等，可以传到callback函数里
        mqttClient.start();    //具体的转发操作在pushcallback中进行
        
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	/**
	 * 设备响应命令的结果推送到此接口，用mqtt发布到v1/devices/me/rpc/request/requestID主题
	 * requestID怎么来？？？用deviceId从map里找device？？devicemodel里有requestID
	 * 如果下一个命令在上一个执行结果返回前传入，会乱掉（咋办？应该不会吧，还有批量开关的问题）
	 * 其实如果没有返回值...设备响应成功设备状态改变数据上报改变，用户平台会自动变成开启成功或者关闭成功，
	 * 但是如果没有返回结果，设备响应失败时是不会显示开启或关闭失败的
	 * mqtt返回给TB怎么还是指令超时
	 * @param json
	 * @throws Exception
	 */
	@RequestMapping(value="/reportCmdExecResult",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public static ResponseEntity<HttpStatus> reportCmdExecResult(@RequestBody String json)throws Exception{
		
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject jsonData = new JSONObject(jsonTokener);
		String deviceId = jsonData.getString("deviceId");
		JSONObject result = jsonData.getJSONObject("result");
		String status = result.getString("status");
		
		if(status.equals("SUCCESS")) {
			//System.out.println("进入if");
			String str = "{\"result\":\"ok\"}";
			Iterator<DeviceModel> iterator = Constant.DEVICE_REPO.iterator();
			System.out.println(iterator.hasNext());
			while(iterator.hasNext()){
				DeviceModel dev = iterator.next();
				System.out.println("设备ID："+dev.deviceId);
				System.out.println("订阅主题："+dev.request_id);
				System.out.println("***************************************");
				if(dev.deviceId.equals(deviceId)) {
					String topic = dev.request_id.replaceAll("request", "response");
					DevServer server = new DevServer();
					server.pushData(dev.accessToken,str,topic);//用mqtt发出去了，但是LWIot还是显示响应超时
				}
			}
		}
		 return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	/**
	 * (待开发)
	 * 当删除设备后，在Device_repo里删除对应设备，取消设备的订阅
	 * @param json
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteDevice",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public static void deleteDevice(@RequestBody String json)throws Exception{
		
	}
	
}
