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
import android.os.CountDownTimer;
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

public class ChooseRegisterActivity extends Activity {
	private String tel="";
    ProgressDialog m_pDialog;
	private TextView result;//提示，该号码已注册
	private Button judge_vfc;//提交验证码
    private Button get_getverif;//获得验证码
//    private Button b1;  
    private EditText login_user_edit;//电话
    private EditText login_vfc_edit;//验证码
    
    Handler handler2=new Handler(){
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
     		   intent.setClass(ChooseRegisterActivity.this,UserInterFaceActivity.class);
     		intent.putExtra("tel", tel);
     		startActivity(intent);
    			//result.setText("该号码已注册");
    		}
    		if(flag.equals("0")){
    			//验证码错误
    			//result.setText("该号码未注册");
    			Toast.makeText(getApplicationContext(), "验证码错误",  
                        Toast.LENGTH_SHORT).show(); 
    		}
//    		if(msg.obj.equals("1")){
//    			m_pDialog.hide();
//    		//ComponentName comp =new ComponentName(RegisterActivity.this,UserInterFaceActivity.class);
////    		Intent intent=new Intent();
////    		   intent.setClass(RegisterActivity.this,UserInterFaceActivity.class);
////    		intent.putExtra("tel", tel);
////    		startActivity(intent);
//    			}else{
//    				m_pDialog.hide();
//    				Toast.makeText(ChooseRegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//    				 new CountDownTimer(15000, 1000) {
//				            // 第一个参数是总的倒计时时间
//				            // 第二个参数是每隔多少时间(ms)调用一次onTick()方法
//				            public void onTick(long millisUntilFinished) {
//				            	judge_vfc.setText(millisUntilFinished / 1000 + "s重新验证");
//				            	judge_vfc.setEnabled(false);
//				            }
//				 
//				            public void onFinish() {
//				            	judge_vfc.setText("提交验证码");
//				            	judge_vfc.setEnabled(true);
//				            }
//				        }.start();
//    			}
    	
    	}
    };
    Handler handler=new Handler(){
    	@Override
    	public void handleMessage(Message msg){
    		if(msg.obj==null){
    			Toast.makeText(getApplicationContext(), "网络连接异常",  
	                       Toast.LENGTH_SHORT).show(); 
    			return;
    		}
    		String ss=(String)msg.obj;
    		Toast.makeText(getApplicationContext(), ss,  
                    Toast.LENGTH_SHORT).show(); 
    		String flag=parser.getreturn(ss);
    		Toast.makeText(getApplicationContext(),"qqqqqq  "+ flag,  
                    Toast.LENGTH_SHORT).show(); 
    		if(flag.equals("1")){
    			result.setText("该号码已注册");
    		}
    		if(flag.equals("0")){
    			result.setText("该号码未注册");
    		}
//    		if(ss.equals("1")){
//    		result.setText("该号码已注册");
//    		
//    			}
    	
    	}
    };
//
    private String retString;
	Packager packager = new Packager(); // 封装
	Parser parser=new Parser();
	private String information;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_register);
		
		SysApplication.getInstance().addActivity(this); 
		
		get_getverif=(Button) findViewById(R.id.get_getverif);
		judge_vfc=(Button)findViewById(R.id.judge_vfc);
		
		login_user_edit=(EditText)findViewById(R.id.login_user_edit);
		login_vfc_edit=(EditText)findViewById(R.id.login_vfc_edit);
		
		result=(TextView)findViewById(R.id.trytxt);
		
//        AssetManager mgr=getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//根据路径得到Typeface
//        get_getverif.setTypeface(tf);//设置字体	
//        judge_vfc.setTypeface(tf);//设置字体
//        login_user_edit.setTypeface(tf);//设置字体
//        login_vfc_edit.setTypeface(tf);//设置字体
		
		get_getverif.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					result.setText("");
					final String phoneSec = login_user_edit.getText().toString().trim();
					tel=phoneSec;
					// 简单判断用户输入的手机号码（段）是否合法
					if ("".equals(phoneSec) || phoneSec.length() < 7) {
						// 给出错误提示
						login_user_edit.setError("您输入的手机号码（段）有误！");
						login_user_edit.requestFocus();
						// 将显示查询结果的TextView清空
						return;
					}
					 new CountDownTimer(45000, 1000) {
				            // 第一个参数是总的倒计时时间
				            // 第二个参数是每隔多少时间(ms)调用一次onTick()方法
				            public void onTick(long millisUntilFinished) {
				            	get_getverif.setText(millisUntilFinished / 1000 + "s后重新发送");
				            	get_getverif.setEnabled(false);
				            }
				 
				            public void onFinish() {
				            	get_getverif.setText("重新获取验证码");
				            	get_getverif.setEnabled(true);
				            }
				        }.start();
				        information = packager.registerPackager(tel);
				        
				        //result.setText("该号码已注册");
				        new Thread(){
				        	public void run(){
				        		//sendHttp(information);
				        		
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
			});
		judge_vfc.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				result.setText("");
				String phoneSec = login_user_edit.getText().toString().trim();
				tel=phoneSec;
				final String tels=phoneSec;
				final String vfc = login_vfc_edit.getText().toString().trim();
				//tel=phoneSec;
				// 简单判断用户输入的手机号码（段）是否合法
				if ( vfc.length() < 4) {
					// 给出错误提示
					login_vfc_edit.setError("您输入的验证码有误！");
					login_vfc_edit.requestFocus();
					// 将显示查询结果的TextView清空
					return;
				}
//				m_pDialog=new ProgressDialog(ChooseRegisterActivity.this);
//				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//				m_pDialog.setMessage("服务器响应中。。。");
//			
//			    m_pDialog.show();
			    
			    
			   information = packager.registerPackager1(tel,vfc);
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
				        	handler2.sendMessage(msg);

			        	}
			        }.start();

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_register, menu);
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
	
//	private void sendHttp(String information) {
//		Log.v("Rentactivity","erroe111111111.....");
//		String url = IP.URL;
//		List<Para> list = new ArrayList<Para>();// 存放参数的列表
//		list.add(new Para("information", information));// 参数打包
//		retString = HttpTools.postVisitWeb(url, list);
//		String flag=parser.getreturn(retString);
//		if(flag.equals("1")){
//			result.setText("该号码已注册");
//		}
//		if(flag.equals("0")){
//			result.setText("该号码未注册");
//		}
//	}
//	private void sendHttp1(String information) {
//		Log.v("Rentactivity","erroe22222222222222222.....");
//		String url = IP.URL;
//		List<Para> list = new ArrayList<Para>();// 存放参数的列表
//		list.add(new Para("information", information));// 参数打包
//		retString = HttpTools.postVisitWeb(url, list);
//		String flag=parser.getreturn(retString);
//		if(flag.equals("1")){
//			//验证码正确
//			Intent intent=new Intent();
// 		   intent.setClass(ChooseRegisterActivity.this,UserInterFaceActivity.class);
// 		intent.putExtra("tel", tel);
// 		startActivity(intent);
//			//result.setText("该号码已注册");
//		}
//		if(flag.equals("0")){
//			//验证码错误
//			//result.setText("该号码未注册");
//			Toast.makeText(getApplicationContext(), "验证码错误",  
//                    Toast.LENGTH_SHORT).show(); 
//		}
//	}
}
