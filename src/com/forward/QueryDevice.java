package com.forward;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

/**
 * Query All Devices:
 * This interface is used to query information about devices in batches.
 */
public class QueryDevice {

    public static String QueryNodeId(String devId) throws Exception {

        // Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication，get token
        String accessToken = login(httpsUtil);

        //Please make sure that the following parameter values have been modified in the Constant file.
        String appId = Constant.APPID;
        String deviceId = devId;
        String urlQueryDevices = Constant.QUERY_DEVICES+"/"+deviceId;

        Map<String, String> paramQueryDevices = new HashMap<>();
    
        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        
        StreamClosedHttpResponse bodyQueryDevices = httpsUtil.doGetWithParasGetStatusLine(urlQueryDevices,
                paramQueryDevices, header);

        System.out.println("QueryDevices, response content:");
        //System.out.print(bodyQueryDevices.getStatusLine());
        System.out.println(bodyQueryDevices.getContent());
        JSONTokener jsonTokener = new JSONTokener(bodyQueryDevices.getContent());
		JSONObject pushData = new JSONObject(jsonTokener);
		JSONObject deviceInfo = pushData.getJSONObject("deviceInfo");
		String nodeId = deviceInfo.getString("nodeId");
        System.out.println(nodeId);
        return nodeId;
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

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        return data.get("accessToken");
    }

}
