package silentdoer.web.controller;

public class DeviceModel {
	/****deviceInfo*****
	{
		"deviceId":"f9d8698a-c78e-4588-9139-9f8e0e83b62d",
		"gatewayId":"f9d8698a-c78e-4588-9139-9f8e0e83b62d",
		"nodeType":"GATEWAY",
		"createTime":"20180621T054142Z",
		"lastModifiedTime":"20180628T082645Z",
		"deviceInfo":{
			"nodeId":"9876543210",
			"name":"TouChuanDev",
			"description":null,
			"manufacturerId":"JiMa",
			"manufacturerName":"JiMa",
			"mac":null,
			"location":"Shenzhen",
			"deviceType":"NBDev",
			"model":"NBIoTDevice",
			"swVersion":null,
			"fwVersion":null,
			"hwVersion":null,
			"protocolType":"CoAP",
			"bridgeId":null,
			"status":"ONLINE",
			"statusDetail":"NONE",
			"mute":"FALSE",
			"supportedSecurity":null,
			"isSecurity":null,
			"signalStrength":null,
			"sigVersion":null,
			"serialNumber":null,
			"batteryLevel":null
			},
		"services":[
		              {
		              	"serviceId":"Transmission",
		              	"serviceType":"Transmission",
		              	"data":{
		              		"length":8,
		              		"rawData":"ERITFBUWFxg="
		              		},
		              	"eventTime":"20180628T082645Z",
		              	"serviceInfo":null
		              }
		           ],
	    "connectionInfo":{},
	    "devGroupIds":[]
	}
	***********************/
	public String deviceId;//deviceId
	public String accessToken;
	private String gatewayId;//网关Id
	private String nodeType;//类型
	private String createTime;//创建时间
    
	public String serviceId = "service";
	public String serviceType = "service";
	public String method = "downData";
	
	public String request_id;
	
	//setDeviceIdAndAccessToken可以合并
	public void setDeviceId(String accessToken) {
		this.deviceId = accessToken;
	}
	
	public String getDeviceId() {
		return this.deviceId;
	}
	
	public void setAccessToken(String deviceId) {
		if(deviceId.length() > 20) {
			this.accessToken = deviceId.substring(0, 20);
		}else  this.accessToken = deviceId;	
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	//设置request_id
	public void setRequest_id(String topic) {
		this.request_id = topic;
	}
}

