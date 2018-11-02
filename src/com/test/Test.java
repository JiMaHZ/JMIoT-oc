package com.test;

import java.util.Iterator;
import java.util.List;

import com.huawei.utils.Constant;

import silentdoer.web.controller.DeviceModel;

public class Test {
	
	 public static void main(String args[]) throws Exception {
		 
		 String deviceId = "d945d3d5-2ab8-4a85-a28f-316ac730d752";
		 String topic = "4567";
		 DeviceModel device = new DeviceModel();
		 device.setDeviceId(deviceId);
		 device.setRequest_id("abc");
		 
		 Constant.DEVICE_REPO.add(device);
		 
		 List<DeviceModel> dev0 = Constant.DEVICE_REPO;
         for(DeviceModel device1:dev0) {
        	    System.out.println(device1.request_id);
         }
		 		 
	     Iterator<DeviceModel> iterator = Constant.DEVICE_REPO.iterator();
         while(iterator.hasNext()) {
    	        DeviceModel dev = iterator.next();
    	        if(dev.deviceId == deviceId) {
    	    	      dev.setRequest_id(topic);
    	        }
         }
         
         List<DeviceModel> dev = Constant.DEVICE_REPO;
         for(DeviceModel device1:dev) {
        	    System.out.println(device1.request_id);
        	    System.out.println(device1.deviceId);
         }
         
	 }
}
    
