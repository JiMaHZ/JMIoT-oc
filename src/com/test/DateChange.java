package com.test;

import java.text.SimpleDateFormat;

public class DateChange {
	/***
	 * 将电线平台的时间转为Unix时间戳（非北京时间）
	 * @param dateStr
	 * @return Unix Timestamp 13
	 */
	public static String Date2TimeStamp(String dateStr) {
        try {
        		String format = "yyyyMMdd'T'HHmmss'Z'";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime()+28800000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
