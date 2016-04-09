package com.example.rentziyou;

import java.util.ArrayList;
import java.util.List;

import com.entity.Para;
import com.example.rentziyou.FragmentProfile.MyListener;
import com.httpconnet.HttpTools;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;
import com.jqjava.lesson5.DemoApplication;
import com.tools.Code;
import com.tools.SysApplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MySettingActivity extends Activity {
	private RelativeLayout re_tuichu;
	private static  SharedPreferences sharedPreferences;
	private String information; 
    Packager packager = new Packager(); // 封装
    Parser parser=new Parser();
    private  String  id;
	private  String  tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_setting);
		//取得ActionBar对象
        ActionBar actionBar =getActionBar();
        //调用hide方法，隐藏actionbar
        actionBar.hide();
      //广播，退出所有
      		SysApplication.getInstance().addActivity(this); 
      		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    	    
        DemoApplication app = (DemoApplication)getApplication();  
    	id=(String)app.get("id");
    	tel=(String)app.get("tel");
        re_tuichu=(RelativeLayout)findViewById(R.id.re_tuichu);
		 
        re_tuichu.setOnClickListener(new View.OnClickListener() {  
	          
	         public void onClick(View v) {  
	          
	        	 sharedPreferences.edit().putBoolean("AUTO_ISCHECK", false).commit(); 
	 			
	        	 Log.v("quitLogin","quitLogin....");	 
	 	           	 information = packager.quitLoginPackager(tel);    		      
	 	    		    new Thread(){
	 	    		    	public void run(){
	 	    		    		String Web_result="";
	 	    		        	String url = IP.URL;
	 	    		    		List<Para> list = new ArrayList<Para>();// 存放参数的列表
	 	    		    		list.add(new Para("information", information));// 参数打包
	 	    		    		Web_result= HttpTools.postVisitWeb(url, list);
	 	    		    	}
	 	    			
	 	    		    }.start();
	 			        SysApplication.getInstance().exit();

	         }  
	        });  
	}
	
	public void back(View view) {
        finish();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
