package com.example.rentziyou;

import java.util.ArrayList;
import java.util.List;

import com.entity.Para;
import com.httpconnet.HttpTools;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;
import com.jqjava.lesson5.DemoApplication;
import com.tools.SysApplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangepwdActivity extends Activity {

	  private SharedPreferences sharedPreferences;  
		private EditText old_pwd;
		private EditText new_pwd_again;
		private EditText new_pwd;
		private Button b1;
		private String id;
		private TextView v1,v2,v3;
		Handler handler=new Handler(){
			public void handleMessage(Message msg){
				if(msg.obj==null){
	    			Toast.makeText(getApplicationContext(), "网络连接异常",  
		                       Toast.LENGTH_SHORT).show(); 
	    			return;
	    		}
				String ss=(String)msg.obj;

	    		String flag=parser.getreturn(ss);
	    		
	    		if(flag.equals("0")){
	    			Toast.makeText(getApplicationContext(),"原密码错误  ",  
	                        Toast.LENGTH_SHORT).show(); 
	    		}else{
	    			Toast.makeText(getApplicationContext(),"修改成功  ",  
	                        Toast.LENGTH_SHORT).show(); 
	    			 Intent intent=new Intent(ChangepwdActivity.this,MyUserInfoActivity.class);
						
	                    startActivity(intent);
	                    finish();
	    			
	    		}
			}
		};
//		private String tel;
		  private String information; 
			Packager packager = new Packager(); // 封装
			Parser parser=new Parser();
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_changepwd);
			//取得ActionBar对象
	        ActionBar actionBar =getActionBar();
	        //调用hide方法，隐藏actionbar
	        actionBar.hide();
	        
			SysApplication.getInstance().addActivity(this); 			

			old_pwd=(EditText)findViewById(R.id.get_old_pwd);
			new_pwd_again=(EditText)findViewById(R.id.get_new_pwd_again);
			new_pwd=(EditText)findViewById(R.id.get_new_pwd);
			b1=(Button)findViewById(R.id.confirm);
			v1=(TextView)findViewById(R.id.change_old_pwd);
			v2=(TextView)findViewById(R.id.change_new_pwd);
			//v3=(TextView)findViewById(R.id.change_new_pwd_agagin);
//	        AssetManager mgr=getAssets();//得到AssetManager
//	        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//根据路径得到Typeface
//	        old_pwd.setTypeface(tf);//设置字体
//	        new_pwd_again.setTypeface(tf);//设置字体
//	        new_pwd.setTypeface(tf);//设置字体
//	        b1.setTypeface(tf);//设置字体
//	        v1.setTypeface(tf);//设置字体
//	        v2.setTypeface(tf);//设置字体
	       // v3.setTypeface(tf);//设置字体
			try{
			Context otherContext = this.createPackageContext("com.example.rentziyou", Context.CONTEXT_IGNORE_SECURITY);
			sharedPreferences = otherContext.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
			id=sharedPreferences.getString("id","");
			Log.v("changepwdsharararara", id);
//			DemoApplication app = (DemoApplication)getApplication();  			
//			tel=(String)app.get("tel");
//			id=(String)app.get("id");
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			b1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 if(LoginActivity.testflage){
				        	Toast.makeText(getApplicationContext(), "测试中",  
				                    Toast.LENGTH_SHORT).show();
				        	finish();
						}
					 final String old = old_pwd.getText().toString().trim();
					 final String new_again= new_pwd_again.getText().toString().trim();
					 final String newpwd= new_pwd.getText().toString().trim();
					 if(!newpwd.equals(new_again)){
							Toast.makeText(ChangepwdActivity.this, "两次输入的新密码不正确", Toast.LENGTH_SHORT).show();

					 }else if(newpwd.equals(null)||old.equals(null)||new_again.equals(null)){
							Toast.makeText(ChangepwdActivity.this, "新旧密码都不能为空", Toast.LENGTH_SHORT).show();
					 }
					 else{
						 Log.v("changepwd", id);
						 //Toast.makeText(ChangepwdActivity.this,"id"+id+"mima "+old+newpwd, Toast.LENGTH_SHORT).show();

						 information = packager.updateUserPwPackager(id, old, newpwd);
                         
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
				
			});
		}

		 public void back(View view) {
		        finish();
		    }
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.changepwd, menu);
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
