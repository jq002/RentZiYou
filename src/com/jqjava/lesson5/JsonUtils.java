package com.jqjava.lesson5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {

	public JsonUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getJsonContent(String url_path) {
		try {
			URL url = new URL(url_path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			int code = connection.getResponseCode();
			if (code == 200) {
				return changeInputStream(connection.getInputStream());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	private static String changeInputStream(InputStream inputStream) {
		// TODO Auto-generated method stub
		String jsonString = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[1024];
		try {
			while ((len = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, len);
			}
			jsonString = new String(outputStream.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	public static List<Map<String, Object>> listKeyMaps(String key,
			String jsonString) {
		String  json_value="";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray(key);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				 Iterator<String> iterator = jsonObject2.keys();
				 while (iterator.hasNext()) {
					 String json_key = iterator.next();
					 json_value = jsonObject2.get(json_key).toString();
 					if (json_value == null) {
 						json_value = "";
 					}
 					map.put(json_key, json_value);
				}
	 			list.add(map);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	 /** 
     * 将数组转换为JSON格式的数据。 
     * @param stoneList 数据源 
     * @return JSON格式的数据 
     */  
//    public static String changeArrayDateToJson(ArrayList<Stone> stoneList){  
//        try {  
//            JSONArray array = new JSONArray();  
//            JSONObject object = new JSONObject();  
//            int length = stoneList.size();  
//            for (int i = 0; i < length; i++) {  
//                Stone stone = stoneList.get(i);  
//                String name = stone.getName();  
//                String size = stone.getSize();  
//                JSONObject stoneObject = new JSONObject();  
//                stoneObject.put("name", name);  
//                stoneObject.put("size", size);  
//                array.put(stoneObject);  
//            }  
//            object.put("stones", array);  
//            return object.toString();  
//        } catch (JSONException e) {  
//            e.printStackTrace();  
//        }  
//        return null;  
//    }  
}
