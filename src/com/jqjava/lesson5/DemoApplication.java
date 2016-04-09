package com.jqjava.lesson5;

import java.util.HashMap;

import android.app.Application;

public class DemoApplication extends Application {
	
public HashMap<String, Object> map = new HashMap<String, Object>();  
    
    public void put(String key,Object object){  
        map.put(key, object);  
    }  
      
    public Object get(String key){  
        return map.get(key);  
    }  

}
