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
    			Toast.makeText(getApplicationContext(), "���������쳣",  
	                       Toast.LENGTH_SHORT).show(); 
    			return;
    		}
    		String ss=(String)msg.obj;

    		String flag=parser.getreturn(ss);
    		Toast.makeText(getApplicationContext(),"qqqqqq  "+ flag,  
                    Toast.LENGTH_SHORT).show(); 
    		if(flag.equals("")){
    			Toast.makeText(getApplicationContext(), "���������쳣",  
	                       Toast.LENGTH_SHORT).show(); 
    			return;
    		}
    		if(flag.equals("0")){
    			Toast.makeText(getApplicationContext(),"����  "+ flag,  
                        Toast.LENGTH_SHORT).show(); 
    		}else{
    			id=flag;
    			userlogin();    			
    		}

    	}
    };
    private String information; 
	Packager packager = new Packager(); // ��װ
	Parser parser=new Parser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ������															
      	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      	
		setContentView(R.layout.activity_login);

      	
		SysApplication.getInstance().addActivity(this); 
		
		//��Preferences������ΪuserInfo�������������������򴴽��µ�Preferences
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
		
		loginid=(EditText)findViewById(R.id.loginid);

		pw=(EditText)findViewById(R.id.pw);
		loginbt=(Button)findViewById(R.id.loginbt);
		regist_code=(EditText)findViewById(R.id.yanzhengma1);		
		v1=(ImageView) findViewById(R.id.imageview1);
		b2=(Button)findViewById(R.id.change1);		
		yanzhengma=(RelativeLayout) findViewById(R.id.view1);


		v1.setImageBitmap(Code.getInstance().createBitmap()); 
		code=Code.getCode();//��ȡ��ȷ��֤��
//        AssetManager mgr=getAssets();//�õ�AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//����·���õ�Typeface
//        pw.setTypeface(tf);//��������
//        loginbt.setTypeface(tf);//��������
//        regist_code.setTypeface(tf);//��������
//        b2.setTypeface(tf);//��������
//        loginid.setTypeface(tf);//��������
//		
		
		yanzhengma.setVisibility(View.VISIBLE);
   	    yzmFlage=1;
        b2.setOnClickListener(new View.OnClickListener() {  
		          
		         public void onClick(View v) {  
		          
		         v1.setImageBitmap(Code.getInstance().createBitmap());  
		 		 code=Code.getCode();//��ȡ��ȷ��֤��

		         }  
		        });  

    
		   
		loginbt.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 tel = loginid.getText().toString().trim();
				 pws= pw.getText().toString().trim();
				 final String rcode = regist_code.getText().toString().trim();//��ȡEditText����֤��
				if (tel.length()==0){
					loginid.setError("�������˺ţ�");
					loginid.requestFocus();
					return;
				}
				
				if ( tel.length() != 11) {
					// ����������ʾ
					loginid.setError("��������˺��д�");
					loginid.requestFocus();
					// ����ʾ��ѯ�����TextView���
					return;
				}
				
				if ( pws.length() == 0) {
					// ����������ʾ
					pw.setError("���������룡");
					pw.requestFocus();
					// ����ʾ��ѯ�����TextView���
					return;
				}
				
//			
				//login(tel,pws);
				if( rcode.equals("")&&yzmFlage==1){													  			
					Toast.makeText(LoginActivity.this, "��֤��δ��д", Toast.LENGTH_SHORT).show();
					return;
				}
				if((rcode.equals(code)&&yzmFlage==1)||(yzmFlage==0)){						
					//showpdialog();
		
//					m_pDialog=new ProgressDialog(LoginActivity.this);
//					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//					m_pDialog.setMessage("��¼���С�����");
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
			    		List<Para> list = new ArrayList<Para>();// ��Ų������б�
			    		list.add(new Para("information", information));// �������
			    		Web_result= HttpTools.postVisitWeb(url, list);
			       
		        	Message msg=new Message();
		        	msg.obj=Web_result;
		        	handler.sendMessage(msg);
		        	}
				
		        }.start();
					}
				
			    }

			if(rcode.equals(code)==false&&yzmFlage==1){
					Toast.makeText(LoginActivity.this, "��֤�����", Toast.LENGTH_SHORT).show();
			         v1.setImageBitmap(Code.getInstance().createBitmap());  					
				 		code=Code.getCode();//��ȡ��ȷ��֤��  											
					return;
			}
				
				

			}
			 
			
		});
		
	}
	
	public void showpdialog(){
		m_pDialog=new ProgressDialog(LoginActivity.this);
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		m_pDialog.setMessage("��������Ӧ�С�����");
	    m_pDialog.show();
	}
	private void userlogin(){
		 Toast.makeText(getApplicationContext(), "����ɹ�    "+id,  
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
