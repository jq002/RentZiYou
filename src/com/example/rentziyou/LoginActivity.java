package com.example.rentziyou;

import java.util.ArrayList;
import java.util.List;

import com.entity.Para;
import com.httpconnet.HttpTools;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;
import com.jqjava.lesson5.DemoApplication;
import com.tools.Code;
import com.tools.SysApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public static Boolean testflage=false;
	RelativeLayout yanzhengma;
	private int yzmFlage=0;
    private EditText loginid;
    private EditText pw;
    private Button loginbt;
    private SharedPreferences sharedPreferences;
	ProgressDialog m_pDialog;
    
    public String tel="13074886694";
    private static String tel_exist="";
    private String pws="111";
    private String code;

    private String result="1";
    public String id="1";   
	private EditText regist_code;
	private Button b2;
	private ImageView v1;

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

    		String flag=parser.getreturn(ss);
    		Toast.makeText(getApplicationContext(),"qqqqqq  "+ flag,  
                    Toast.LENGTH_SHORT).show(); 
    		if(flag.equals("")){
    			Toast.makeText(getApplicationContext(), "网络连接异常",  
	                       Toast.LENGTH_SHORT).show(); 
    			return;
    		}
    		if(flag.equals("0")){
    			Toast.makeText(getApplicationContext(),"出错  "+ flag,  
                        Toast.LENGTH_SHORT).show(); 
    		}else{
    			id=flag;
    			userlogin();    			
    		}

    	}
    };
    private String information; 
	Packager packager = new Packager(); // 封装
	Parser parser=new Parser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除标题															
      	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      	
		setContentView(R.layout.activity_login);

      	
		SysApplication.getInstance().addActivity(this); 
		
		//打开Preferences，名称为userInfo，如果存在则打开它，否则创建新的Preferences
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
		
		loginid=(EditText)findViewById(R.id.loginid);

		pw=(EditText)findViewById(R.id.pw);
		loginbt=(Button)findViewById(R.id.loginbt);
		regist_code=(EditText)findViewById(R.id.yanzhengma1);		
		v1=(ImageView) findViewById(R.id.imageview1);
		b2=(Button)findViewById(R.id.change1);		
		yanzhengma=(RelativeLayout) findViewById(R.id.view1);


		v1.setImageBitmap(Code.getInstance().createBitmap()); 
		code=Code.getCode();//获取正确验证码
//        AssetManager mgr=getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//根据路径得到Typeface
//        pw.setTypeface(tf);//设置字体
//        loginbt.setTypeface(tf);//设置字体
//        regist_code.setTypeface(tf);//设置字体
//        b2.setTypeface(tf);//设置字体
//        loginid.setTypeface(tf);//设置字体
//		
		
		yanzhengma.setVisibility(View.VISIBLE);
   	    yzmFlage=1;
        b2.setOnClickListener(new View.OnClickListener() {  
		          
		         public void onClick(View v) {  
		          
		         v1.setImageBitmap(Code.getInstance().createBitmap());  
		 		 code=Code.getCode();//获取正确验证码

		         }  
		        });  

    
		   
		loginbt.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 tel = loginid.getText().toString().trim();
				 pws= pw.getText().toString().trim();
				 final String rcode = regist_code.getText().toString().trim();//获取EditText的验证码
				if (tel.length()==0){
					loginid.setError("请输入账号！");
					loginid.requestFocus();
					return;
				}
				
				if ( tel.length() != 11) {
					// 给出错误提示
					loginid.setError("您输入的账号有错！");
					loginid.requestFocus();
					// 将显示查询结果的TextView清空
					return;
				}
				
				if ( pws.length() == 0) {
					// 给出错误提示
					pw.setError("请输入密码！");
					pw.requestFocus();
					// 将显示查询结果的TextView清空
					return;
				}
				
//			
				//login(tel,pws);
				if( rcode.equals("")&&yzmFlage==1){													  			
					Toast.makeText(LoginActivity.this, "验证码未填写", Toast.LENGTH_SHORT).show();
					return;
				}
				if((rcode.equals(code)&&yzmFlage==1)||(yzmFlage==0)){						
					//showpdialog();
		
//					m_pDialog=new ProgressDialog(LoginActivity.this);
//					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//					m_pDialog.setMessage("登录中中。。。");
//				
//				    m_pDialog.show();
					if(testflage){
						id="1";
						tel="13074886694";
						userlogin();
					}else{
					 information = packager.loginPackager(tel, pws);
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

			if(rcode.equals(code)==false&&yzmFlage==1){
					Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
			         v1.setImageBitmap(Code.getInstance().createBitmap());  					
				 		code=Code.getCode();//获取正确验证码  											
					return;
			}
				
				

			}
			 
			
		});
		
	}
	
	public void showpdialog(){
		m_pDialog=new ProgressDialog(LoginActivity.this);
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		m_pDialog.setMessage("服务器响应中。。。");
	    m_pDialog.show();
	}
	private void userlogin(){
		 Toast.makeText(getApplicationContext(), "登入成功    "+id,  
                 Toast.LENGTH_SHORT).show();
//		 DemoApplication app = (DemoApplication)getApplication();  
//			app.put("tel", tel);
//			app.put("id", id);
			
 	 Editor editor = sharedPreferences.edit();  
     editor.putString("id", id);
     editor.putString("tel", tel);
     //tel_exist=sharedPreferences.getString("tel","");
     editor.putBoolean("AUTO_ISCHECK", true);  
     editor.commit();
     
     SharedPreferences sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
     if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)) {  
 	 
	        Intent intent=new Intent(LoginActivity.this,LookmainActivity.class);
		    startActivity(intent);
		  
     }
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		    switch (item.getItemId()) {
			case R.id.action_settings:
				SysApplication.getInstance().exit();
				break;

			}
			
		
		return super.onOptionsItemSelected(item);
	}
	
}
