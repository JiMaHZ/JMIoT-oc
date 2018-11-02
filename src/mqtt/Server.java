package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
/**
 * 
 * Title:Server
 * Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * @author cyt
 * 2016年1月6日下午3:29:28
 */
public class Server {

    public static final String HOST ="tcp://140.143.23.199:1883";//"tcp://192.168.1.3:61613";//mqtt安装的服务器broker地址
    public static final String TOPIC = "v1/devices/me/telemetry";//toclient/124";//定义一个主题
    private String clientid = "2c6bd53b-d7c1-4b56-a";//定义mqtt的id，可以在mqtt服务配置中指定,和clientId无关

    private MqttClient client;
    private MqttTopic topic;
    //private MqttTopic topic125;
    private String userName = "2c6bd53b-d7c1-4b56-a";//要变的
    //private String passWord = "password";

    private MqttMessage message;
    /**
     * 设置登录用户名，默认6502
     * @param deviceId
     * @return
     */
    public void setUserName(String deviceId) {
    	   this.userName = deviceId;
    }
    /**
     * 设置clientId
     * @param clientId
     * @return
     */
    public void setClientId(String clientId) {
    	   this.clientid = clientId;    	   
    } 
    /**
     * 构造函数
     * @throws MqttException
     */
    public Server() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }
    /**
     * 连接服务器broker
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        //options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
            topic = client.getTopic(TOPIC);
            //topic125 = client.getTopic(TOPIC125);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***
     * 
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }
/***
 * 启动入口
 * @param args
 * @throws MqttException
 */
    public static void main(String[] args) throws MqttException {
        Server server = new Server();
        
        server.message = new MqttMessage();
        server.message.setQos(2);
        server.message.setRetained(true);
        String telemetry = "{\"0200\":\"90\"}";
        //server.message.setPayload("给客户端124推送的信息".getBytes());
        server.message.setPayload(telemetry.getBytes());
        server.publish(server.topic , server.message);
        
        System.out.println(server.message.isRetained() + "------ratained状态");        
    }
    /**
     * 转发设备数据变化
     * @param telemetry
     * @throws MqttException
     */
    public void pushData(String telemetry) throws MqttException {

        message = new MqttMessage();
        message.setQos(2);
        message.setRetained(true);
        message.setPayload(telemetry.getBytes());
        publish(topic , message);
        
        System.out.println(message.isRetained() + "------ratained状态");
    }
    
}
