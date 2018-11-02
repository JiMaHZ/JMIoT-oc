package mqtt;

import java.util.concurrent.ScheduledExecutorService;  


import org.eclipse.paho.client.mqttv3.MqttClient;  
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;  
import org.eclipse.paho.client.mqttv3.MqttException;  
import org.eclipse.paho.client.mqttv3.MqttTopic;  
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import silentdoer.web.controller.DeviceModel;

  
public class Client {  
  
    public static final String HOST = "tcp://140.143.23.199:1883";  
    public static final String TOPIC = "v1/devices/me/rpc/request";  //toclient/124原订阅主题
    private String clientid = "jiaosi502";  
    private MqttClient client;  
    private MqttConnectOptions options;  
    private String userName = "6502"; //非必须//原：admin,要改的，用一个setUserName方法
    //private String passWord = "password";//非必须
    private PushCallback callback = new PushCallback();
    
    @SuppressWarnings("unused")
	private ScheduledExecutorService scheduler;  
    /**
     * 设置clientId
     * @param clientId
     * @return
     */
    public String setClientId(String clientId) {
    	   return this.clientid = clientId;    	   
    }
    //传入设备的accessToken
    public void setUsernameAndCallback(DeviceModel device) {
    	    this.userName = device.getAccessToken();
    	    System.out.println(this.userName);
    	    this.callback.setAccessAndDevice(device);
    	    System.out.println(callback.getAccess());
    }
    
    
    
    public void start() {  
        try {  
            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存  
            client = new MqttClient(HOST, clientid, new MemoryPersistence());  
            // MQTT的连接设置  
            options = new MqttConnectOptions();  
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接  
            options.setCleanSession(true);  
            // 设置连接的用户名  
            options.setUserName(userName);  
            // 设置连接的密码  
            //options.setPassword(passWord.toCharArray());  
            // 设置超时时间 单位为秒  
            options.setConnectionTimeout(10);  
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制  
            options.setKeepAliveInterval(20);  
            // 设置回调  
            client.setCallback(callback);  //原来是new PushCallback()
            MqttTopic topic = client.getTopic(TOPIC);  
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息    
            options.setWill(topic, "close".getBytes(), 2, true);  
              
            client.connect(options); 
            System.out.println("连接成功");
            
            /***
             * 订阅消息
             * TOPIC主题里不允许使用通配符，但是subscribe(String[] topicFilters,int[] qos)的主题过滤器参数允许使用通配符
             * 只有订阅v1/devices/me/rpc/request/+才能订阅成功
             * 要在topicFilter后加上/+
             */
            int[] Qos  = {1};
            String topicFilter = TOPIC+"/+";
            String[] topic1 = {topicFilter}; 
            client.subscribe(topic1,Qos);  
            System.out.println("订阅成功");
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
   
    public static void main(String[] args) throws MqttException {     
        Client client = new Client();  
        client.start();  
    }  
}
