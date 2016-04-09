package com.example.rentziyou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entity.Para;
import com.httpconnet.HttpTools;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;
import com.jqjava.lesson5.DemoApplication;
import com.jqjava.lesson5.JsonUtils;
import com.tools.SysApplication;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyMoneyBagActivity extends Activity {
	
	private TextView tv_ba;
	private String money;
	private String id;
	private String tel;
	
	private String information; 
	Packager packager = new Packager(); // 封装
	Parser parser=new Parser();
	
	 Handler handler=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
	    		//m_pDialog.hide();
	    		
	    		if(msg.obj==null){
	    			Toast.makeText(getApplicationContext(), "网络连接异常",  
		                       Toast.LENGTH_SHORT).show(); 
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		money=parser.getreturn(ss);
	    		   tv_ba.setText(money);
	    	}
	 };
			

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_money_bag);
		//取得ActionBar对象
        ActionBar actionBar =getActionBar();
        //调用hide方法，隐藏actionbar
        actionBar.hide();
      //广播，退出所有
      		SysApplication.getInstance().addActivity(this); 
        DemoApplication app = (DemoApplication)getApplication();  		
		id=(String)app.get("id");
		tel=(String)app.get("tel");
        
        tv_ba=(TextView) findViewById(R.id.tv_ba);
        
        if(LoginActivity.testflage){
        	Toast.makeText(getApplicationContext(), "tel测试"+tel,  
                    Toast.LENGTH_SHORT).show();
		}else{
//			Toast.makeText(getApplicationContext(),"tel "+tel,  
//                    Toast.LENGTH_SHORT).show();
		 information = packager.selectUserCashPackager(tel);
       new Thread(){
    	public void run(){
    		String Web_result="";
        	String url = IP.URL;
    		List<Para> list = new ArrayList<Para>();// 存放参数的列表
    		list.add(new Para("information", information));// 参数打包
    		Web_result= HttpTools.postVisitWeb(url, list);
       
    	Message msg=new Message();
    	msg.obj=Web_result;
    	handler.sendMessage(msg);
    	}
	
    }.start();
		}
     
	}
	public void back(View view) {
        finish();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_money_bag, menu);
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
