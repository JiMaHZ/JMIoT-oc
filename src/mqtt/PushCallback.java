package mqtt;

import java.util.Iterator;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;  
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.forward.PostCommand;
import com.huawei.utils.Constant;

import silentdoer.web.controller.DeviceModel;  
  
/**  
 * 发布消息的回调类  
 *   
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。  
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。  
 * 必须在回调类中实现三个方法：  
 *   
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。  
 *   
 *  public void connectionLost(Throwable cause)在断开连接时调用。  
 *   
 *  public void deliveryComplete(MqttDeliveryToken token))  
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。  
 *  由 MqttClient.connect 激活此回调。  
 *   
 */    
public class PushCallback implements MqttCallback {  
	//传入设备的accessToken
	private String access;
	private DeviceModel device = new DeviceModel();
	
	//传入DeviceModel的deviceId
	public void setAccessAndDevice(DeviceModel deviceModel) {
		this.access = deviceModel.getDeviceId();
		this.device = deviceModel;
	}
	
	public String getAccess() {
		return this.access;
	}
	
    public void connectionLost(Throwable cause) {  
        // 连接丢失后，一般在这里面进行重连  
        System.out.println("连接断开，可以做重连");  
    }  
    
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());  
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面 
      	System.out.println("设备ID："+device.deviceId);
        System.out.println("接收消息主题 : " + topic);
        
        //device.setRequest_id(topic);修改全局变量list<device>的某个元素的requestID值         
        Iterator<DeviceModel> iterator = Constant.DEVICE_REPO.iterator();
        while(iterator.hasNext()) {
        	    DeviceModel dev = iterator.next();
        	    if(dev.deviceId == device.deviceId) {
        	    	    dev.setRequest_id(topic);
        	    }
        }
      
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
        
        String command = new String(message.getPayload());
        PostCommand postCommand = new PostCommand();
        postCommand.sendCommandToOC(command, device);       
    }
}
