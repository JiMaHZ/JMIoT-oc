package com.test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import mqtt.PushCallback;

public class TestMqttServer {
    public static final String HOST ="tcp://140.143.23.199:1883";
    //public static final String TOPIC = "v1/devices/me/telemetry";//toclient/124";//定义一个主题
	private String clientid;//定义mqtt的id，可以在mqtt服务配置中指定,和clientId无关

    private MqttClient client;
    private MqttTopic topic;
	private String userName;
	private MqttMessage message;
	    
    public void setUserName(String accessToken) {
	    	   this.userName = accessToken;
    }
	    
    public void setClientId() {
	    	   this.clientid = String.valueOf(System.currentTimeMillis());    	   
    }   

    public static void main(String args[]) throws MqttException {
    	
    	    String topicR = "v1/devices/me/rpc/response/13";//"v1/devices/me/telemetry";
    	    String str = "{\"0100\":\"20\"}";
    	    String accessToken = "2c6bd53b-d7c1-4b56-a2fa-d4454dcf9ed2";
    	
      	TestMqttServer server = new TestMqttServer();
      	server.setClientId();
      	server.setUserName(accessToken.substring(0, 20));
      	
    	    server.client = new MqttClient(HOST, server.clientid, new MemoryPersistence());
	    	MqttConnectOptions options = new MqttConnectOptions();
	    options.setCleanSession(false);
	    options.setUserName(server.userName);
	    // 设置超时时间
	    options.setConnectionTimeout(10);
	    // 设置会话心跳时间
	    options.setKeepAliveInterval(20);
	    try {
	          server.client.setCallback(new PushCallback());
	          server.client.connect(options);
	          server.topic = server.client.getTopic(topicR);
	    } catch (Exception e) {
	          e.printStackTrace();
	    }

        server.message = new MqttMessage();
        server.message.setQos(2);
        server.message.setRetained(true);
        server.message.setPayload(str.getBytes());
        server.publish(server.topic , server.message);
        
        System.out.println(server.message.isRetained() + "------ratained状态");   
	        
	}
	
	public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,MqttException {
          MqttDeliveryToken token = topic.publish(message);
          token.waitForCompletion();
          System.out.println("message is published completely! "
           + token.isComplete());
	}

}
