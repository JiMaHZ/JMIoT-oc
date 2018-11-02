package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DevServer {
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
		    
    public void setClientId(String accessToken) {
	    	   this.clientid = accessToken;    	   
    }   

    public void pushData(String accessToken,String telemetry,String topicR) throws MqttException {
	    	
  	    clientid = accessToken;
      	userName = accessToken;
	      	
    	    client = new MqttClient(HOST, clientid, new MemoryPersistence());
	    	MqttConnectOptions options = new MqttConnectOptions();
	    options.setCleanSession(false);
	    options.setUserName(userName);
	    // 设置超时时间
	    options.setConnectionTimeout(10);
	    // 设置会话心跳时间
	    options.setKeepAliveInterval(20);
	    try {
	          client.setCallback(new PushCallback());
	          client.connect(options);
	          topic = client.getTopic(topicR);
	    } catch (Exception e) {
	          e.printStackTrace();
	    }

        message = new MqttMessage();
        message.setQos(2);
        message.setRetained(true);
        message.setPayload(telemetry.getBytes());
        publish(topic , message);
	        
        System.out.println(message.isRetained() + "------ratained状态");   
		        
	}
		
	public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,MqttException {
          MqttDeliveryToken token = topic.publish(message);
          token.waitForCompletion();
          System.out.println("message is published completely! "
           + token.isComplete());
	}

    /*****
    public static final String HOST ="tcp://140.143.23.199:1883";
    //public static final String TOPIC = "v1/devices/me/telemetry";//toclient/124";//定义一个主题
	private String clientid;//定义mqtt的id，可以在mqtt服务配置中指定,和clientId无关

    private MqttClient client;
    private MqttTopic topic;
	private String userName;
	private MqttMessage message;
	    
    public void setUserName(String deviceId) {
	    	   this.userName = deviceId;
    }
	    
    public void setClientId(String clientId) {
	    	   this.clientid = clientId;    	   
    }   

	public void pushData(String topicR,String telemetry) throws MqttException {
	    	client = new MqttClient(HOST, clientid, new MemoryPersistence());
	    	MqttConnectOptions options = new MqttConnectOptions();
	    options.setCleanSession(false);
	    options.setUserName(userName);
	    // 设置超时时间
	    options.setConnectionTimeout(10);
	    // 设置会话心跳时间
	    options.setKeepAliveInterval(20);
	    try {
	          client.setCallback(new PushCallback());
	          client.connect(options);
	          topic = client.getTopic(topicR);
	    } catch (Exception e) {
	          e.printStackTrace();
	    }
	    
	    DevServer server = new DevServer();

        server.message = new MqttMessage();
        server.message.setQos(2);
        server.message.setRetained(true);
        server.message.setPayload(telemetry.getBytes());
        server.publish(server.topic , server.message);
        
        System.out.println(server.message.isRetained() + "------ratained状态");   
	        
	}
	
	public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,MqttException {
          MqttDeliveryToken token = topic.publish(message);
          token.waitForCompletion();
          System.out.println("message is published completely! "
           + token.isComplete());
	}
	****/
}

