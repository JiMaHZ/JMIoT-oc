package com.forward;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

import silentdoer.web.controller.DeviceModel;
/**
 * 
 * @author mac
 * sendCommandToOC(String cmd,DeviceModel device)
 * login
 */
public class PostCommand {
	/**
	 * 传入控制命令和设备信息发送到电信
	 * @param cmd
	 * @param device
	 * @throws Exception
	 */
	public void sendCommandToOC(String cmd,DeviceModel device) throws Exception{
		 // Two-Way Authentication双向认证
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication，获取token
        String accessToken = login(httpsUtil);
        System.out.println("Token的值是："+accessToken);
        
        String urlPostAsynCmd = Constant.POST_ASYN_CMD;
        String appId = Constant.APPID;
        String callbackUrl = Constant.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;

        //deviceId要从外部传进来
        String deviceId = device.deviceId;             
        String serviceId = device.serviceId;
        String method = device.method;
        
        ObjectNode paras = JsonUtil.convertObject2ObjectNode(cmd);
        //System.out.println(x);
        Map<String, Object> paramCommand = new HashMap<>();
        paramCommand.put("serviceId", serviceId);
        paramCommand.put("method", method);
        paramCommand.put("paras", paras);      
        
        Map<String, Object> paramPostAsynCmd = new HashMap<>();
        paramPostAsynCmd.put("deviceId", deviceId);
        paramPostAsynCmd.put("command", paramCommand);
        paramPostAsynCmd.put("callbackUrl", callbackUrl);
        //ת����Json��ʽ
        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostAsynCmd);
                
        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        
        HttpResponse responsePostAsynCmd = httpsUtil.doPostJson(urlPostAsynCmd, header, jsonRequest);

        String responseBody = httpsUtil.getHttpResponseBody(responsePostAsynCmd);

        System.out.println("PostAsynCommand, response content:");
        System.out.print(responsePostAsynCmd.getStatusLine());
        System.out.println(responseBody);
        System.out.println();		
	}    
    /**
     * Authentication，get token
     * */
    @SuppressWarnings("unchecked")
    public static String login(HttpsUtil httpsUtil) throws Exception {

        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        Map<String, String> paramLogin = new HashMap<>();
        paramLogin.put("appId", appId);
        paramLogin.put("secret", secret);

        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

        System.out.println("app auth success,return accessToken:");
        System.out.println(responseLogin.getStatusLine());
        System.out.println(responseLogin.getContent());
        System.out.println();

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());//此行login改成private就会有警告
        System.out.println(data.get("accessToken"));
        return data.get("accessToken");
    }

}

