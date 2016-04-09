package com.tools;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  SerializableMap implements  Serializable {
	List<Map<String, Object>> map = 
			new ArrayList<Map<String, Object>>(); 
	Map<String, Object> map2 = new HashMap<String, Object>();
	
	    public List<Map<String, Object>> getMap()  
	    {  
	        return map;  
	    }  
	    public void setMap(List<Map<String, Object>> map)  
	    {  
	        this.map=map;  
	    }  
	    
	    public Map<String, Object> getMap2()  
	    {  
	        return map2;  
	    }  
	    public void setMap2(Map<String, Object> map)  
	    {  
	        this.map2=map;  
	    }  
}
