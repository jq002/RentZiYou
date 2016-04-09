package com.jqjava.lesson5;


import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import android.widget.EditText;



public class AMapUtil {
	/**
	 * �ж�edittext�Ƿ�null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	
	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";
	
	/**
	 * ��LatLonPoint����ת��ΪLatLon����
	 */
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}
}



