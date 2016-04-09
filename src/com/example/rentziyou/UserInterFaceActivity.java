package com.example.rentziyou;


import java.util.ArrayList;
import java.util.List;

import com.entity.Para;
import com.httpconnet.HttpTools;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;

import com.tools.SysApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

public class UserInterFaceActivity extends Activity {
	
	private Button register_bt;
    ProgressDialog m_pDialog;
    private EditText pw;
    private EditText againpw;
    private String tel;

    private String retString;
	Packager packager = new Packager(); // 封装
	Parser parser=new Parser();
	private String information;
	
	   Handler handler=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
	    		if(msg.obj==null){
	    			m_pDialog.hide();
	    			Toast.makeText(getApplicationContext(), "网络连接异常",  
		                       Toast.LENGTH_SHORT).show(); 
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		String flag=parser.getreturn(ss);
	    		if(flag.equals("1")){
	    			//验证码正确
	    			Intent intent=new Intent();
	     		   intent.setClass(UserInterFaceActivity.this,MainActivity.class);

	     		startActivity(intent);
	    			//result.setText("该号码已注册");
	    		}
	    		if(flag.equals("0")){
	    			//验证码错误
	    			//result.setText("该号码未注册");
	    			Toast.makeText(getApplicationContext(), "失败",  
	                        Toast.LENGTH_SHORT).show(); 
	    		}
//	    		if(msg.obj.equals("1")){
//	    			m_pDialog.hide();
//	    			  Intent intent=new Intent(); 
//	    	    	    intent.setClass(UserInterFaceActivity.this, SucRegisterActivity.class);
//	    	    		//intent.setComponent(comp);
//	    	    		//intent.putExtra("tel", tel);
//	    	    		startActivity(intent);
//	    			}
//	    		else{
//	    			m_pDialog.hide();
//	    			Toast.makeText(UserInterFaceActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
//	    			
//	    		}
	    	
	    	}
	    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_inter_face);
		SysApplication.getInstance().addActivity(this); 
		register_bt=(Button)findViewById(R.id.register_bt);
		pw=(EditText)findViewById(R.id.pw);
		againpw=(EditText)findViewById(R.id.againpw);
//        AssetManager mgr=getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//根据路径得到Typeface
//        register_bt.setTypeface(tf);//设置字体
//        pw.setTypeface(tf);//设置字体
//        againpw.setTypeface(tf);//设置字体
		Intent intent=getIntent();
	     Bundle bundle=intent.getExtras();
	    tel=bundle.getString("tel");
	    // m_pDialog.setCancelable(false);
		register_bt.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String edit1 = pw.getText().toString().trim();
				String edit2= againpw.getText().toString().trim();
				if(edit1.equals(edit2)){
//					m_pDialog=new ProgressDialog(UserInterFaceActivity.this);
//					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//					m_pDialog.setMessage("服务器响应中。。。");
//				
//				    m_pDialog.show();
					//m_pDialog.setCancelable(false);
					 information = packager.registerPackager2(tel,edit2);
						//textView1.setText(information);
						
					  new Thread(){
				        	public void run(){
				        		String Web_result="";
					        	String url = IP.URL;
					    		List<Para> list = new ArrayList<Para>();// 存放参数的列表
					    		list.add(new Para("information", information));// 参数打包
					    		Web_result= HttpTools.postVisitWeb(url, list);
					        	//Web_result=RegisterWebgetRemote.getRemoteInfo(phoneSec);
					        	Message msg=new Message();
					        	msg.obj=Web_result;
					        	handler.sendMessage(msg);

				        	}
				        }.start();

				}
				else{
					 Toast.makeText(UserInterFaceActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
					 
				}
				
			}
		});
	
	}
//	private void sendHttp(String information) {
//		Log.v("Rentactivity","erroe33333333333.....");
//		String url = IP.URL;
//		List<Para> list = new ArrayList<Para>();// 存放参数的列表
//		list.add(new Para("information", information));// 参数打包
//		retString = HttpTools.postVisitWeb(url, list);
//		String flag=parser.getreturn(retString);
//		if(flag.equals("1")){
//			//验证码正确
//			Intent intent=new Intent();
// 		   intent.setClass(UserInterFaceActivity.this,MainActivity.class);
//
// 		startActivity(intent);
//			//result.setText("该号码已注册");
//		}
//		if(flag.equals("0")){
//			//验证码错误
//			//result.setText("该号码未注册");
//			Toast.makeText(getApplicationContext(), "失败",  
//                    Toast.LENGTH_SHORT).show(); 
//		}
//	}
	private void sendHttp(String information) {
		//String Web_result="";
    	String url = IP.URL;
		List<Para> list = new ArrayList<Para>();// 存放参数的列表
		list.add(new Para("information", information));// 参数打包
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			//Toast.makeText(MainActivity.this, "被点击", 1).show();
			SysApplication.getInstance().exit();
			break;
		
		
		}
		return super.onOptionsItemSelected(item);
	}
}

	