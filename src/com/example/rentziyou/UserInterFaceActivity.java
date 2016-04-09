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
	Packager packager = new Packager(); // ��װ
	Parser parser=new Parser();
	private String information;
	
	   Handler handler=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
	    		if(msg.obj==null){
	    			m_pDialog.hide();
	    			Toast.makeText(getApplicationContext(), "���������쳣",  
		                       Toast.LENGTH_SHORT).show(); 
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		String flag=parser.getreturn(ss);
	    		if(flag.equals("1")){
	    			//��֤����ȷ
	    			Intent intent=new Intent();
	     		   intent.setClass(UserInterFaceActivity.this,MainActivity.class);

	     		startActivity(intent);
	    			//result.setText("�ú�����ע��");
	    		}
	    		if(flag.equals("0")){
	    			//��֤�����
	    			//result.setText("�ú���δע��");
	    			Toast.makeText(getApplicationContext(), "ʧ��",  
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
//	    			Toast.makeText(UserInterFaceActivity.this, "����������Ӧ", Toast.LENGTH_SHORT).show();
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
//        AssetManager mgr=getAssets();//�õ�AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//����·���õ�Typeface
//        register_bt.setTypeface(tf);//��������
//        pw.setTypeface(tf);//��������
//        againpw.setTypeface(tf);//��������
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
//					m_pDialog.setMessage("��������Ӧ�С�����");
//				
//				    m_pDialog.show();
					//m_pDialog.setCancelable(false);
					 information = packager.registerPackager2(tel,edit2);
						//textView1.setText(information);
						
					  new Thread(){
				        	public void run(){
				        		String Web_result="";
					        	String url = IP.URL;
					    		List<Para> list = new ArrayList<Para>();// ��Ų������б�
					    		list.add(new Para("information", information));// �������
					    		Web_result= HttpTools.postVisitWeb(url, list);
					        	//Web_result=RegisterWebgetRemote.getRemoteInfo(phoneSec);
					        	Message msg=new Message();
					        	msg.obj=Web_result;
					        	handler.sendMessage(msg);

				        	}
				        }.start();

				}
				else{
					 Toast.makeText(UserInterFaceActivity.this, "������������벻һ��", Toast.LENGTH_SHORT).show();
					 
				}
				
			}
		});
	
	}
//	private void sendHttp(String information) {
//		Log.v("Rentactivity","erroe33333333333.....");
//		String url = IP.URL;
//		List<Para> list = new ArrayList<Para>();// ��Ų������б�
//		list.add(new Para("information", information));// �������
//		retString = HttpTools.postVisitWeb(url, list);
//		String flag=parser.getreturn(retString);
//		if(flag.equals("1")){
//			//��֤����ȷ
//			Intent intent=new Intent();
// 		   intent.setClass(UserInterFaceActivity.this,MainActivity.class);
//
// 		startActivity(intent);
//			//result.setText("�ú�����ע��");
//		}
//		if(flag.equals("0")){
//			//��֤�����
//			//result.setText("�ú���δע��");
//			Toast.makeText(getApplicationContext(), "ʧ��",  
//                    Toast.LENGTH_SHORT).show(); 
//		}
//	}
	private void sendHttp(String information) {
		//String Web_result="";
    	String url = IP.URL;
		List<Para> list = new ArrayList<Para>();// ��Ų������б�
		list.add(new Para("information", information));// �������
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
			//Toast.makeText(MainActivity.this, "�����", 1).show();
			SysApplication.getInstance().exit();
			break;
		
		
		}
		return super.onOptionsItemSelected(item);
	}
}

	