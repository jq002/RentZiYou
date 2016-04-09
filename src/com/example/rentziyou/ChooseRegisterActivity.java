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
	private TextView result;//��ʾ���ú�����ע��
	private Button judge_vfc;//�ύ��֤��
    private Button get_getverif;//�����֤��
//    private Button b1;  
    private EditText login_user_edit;//�绰
    private EditText login_vfc_edit;//��֤��
    
    Handler handler2=new Handler(){
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
     		   intent.setClass(ChooseRegisterActivity.this,UserInterFaceActivity.class);
     		intent.putExtra("tel", tel);
     		startActivity(intent);
    			//result.setText("�ú�����ע��");
    		}
    		if(flag.equals("0")){
    			//��֤�����
    			//result.setText("�ú���δע��");
    			Toast.makeText(getApplicationContext(), "��֤�����",  
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
//    				Toast.makeText(ChooseRegisterActivity.this, "��֤�����", Toast.LENGTH_SHORT).show();
//    				 new CountDownTimer(15000, 1000) {
//				            // ��һ���������ܵĵ���ʱʱ��
//				            // �ڶ���������ÿ������ʱ��(ms)����һ��onTick()����
//				            public void onTick(long millisUntilFinished) {
//				            	judge_vfc.setText(millisUntilFinished / 1000 + "s������֤");
//				            	judge_vfc.setEnabled(false);
//				            }
//				 
//				            public void onFinish() {
//				            	judge_vfc.setText("�ύ��֤��");
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
    			Toast.makeText(getApplicationContext(), "���������쳣",  
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
    			result.setText("�ú�����ע��");
    		}
    		if(flag.equals("0")){
    			result.setText("�ú���δע��");
    		}
//    		if(ss.equals("1")){
//    		result.setText("�ú�����ע��");
//    		
//    			}
    	
    	}
    };
//
    private String retString;
	Packager packager = new Packager(); // ��װ
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
		
//        AssetManager mgr=getAssets();//�õ�AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//����·���õ�Typeface
//        get_getverif.setTypeface(tf);//��������	
//        judge_vfc.setTypeface(tf);//��������
//        login_user_edit.setTypeface(tf);//��������
//        login_vfc_edit.setTypeface(tf);//��������
		
		get_getverif.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					result.setText("");
					final String phoneSec = login_user_edit.getText().toString().trim();
					tel=phoneSec;
					// ���ж��û�������ֻ����루�Σ��Ƿ�Ϸ�
					if ("".equals(phoneSec) || phoneSec.length() < 7) {
						// ����������ʾ
						login_user_edit.setError("��������ֻ����루�Σ�����");
						login_user_edit.requestFocus();
						// ����ʾ��ѯ�����TextView���
						return;
					}
					 new CountDownTimer(45000, 1000) {
				            // ��һ���������ܵĵ���ʱʱ��
				            // �ڶ���������ÿ������ʱ��(ms)����һ��onTick()����
				            public void onTick(long millisUntilFinished) {
				            	get_getverif.setText(millisUntilFinished / 1000 + "s�����·���");
				            	get_getverif.setEnabled(false);
				            }
				 
				            public void onFinish() {
				            	get_getverif.setText("���»�ȡ��֤��");
				            	get_getverif.setEnabled(true);
				            }
				        }.start();
				        information = packager.registerPackager(tel);
				        
				        //result.setText("�ú�����ע��");
				        new Thread(){
				        	public void run(){
				        		//sendHttp(information);
				        		
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
				// ���ж��û�������ֻ����루�Σ��Ƿ�Ϸ�
				if ( vfc.length() < 4) {
					// ����������ʾ
					login_vfc_edit.setError("���������֤������");
					login_vfc_edit.requestFocus();
					// ����ʾ��ѯ�����TextView���
					return;
				}
//				m_pDialog=new ProgressDialog(ChooseRegisterActivity.this);
//				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//				m_pDialog.setMessage("��������Ӧ�С�����");
//			
//			    m_pDialog.show();
			    
			    
			   information = packager.registerPackager1(tel,vfc);
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
			//Toast.makeText(MainActivity.this, "�����", 1).show();
			SysApplication.getInstance().exit();
			break;
		
		
		}
		return super.onOptionsItemSelected(item);
	}
	
//	private void sendHttp(String information) {
//		Log.v("Rentactivity","erroe111111111.....");
//		String url = IP.URL;
//		List<Para> list = new ArrayList<Para>();// ��Ų������б�
//		list.add(new Para("information", information));// �������
//		retString = HttpTools.postVisitWeb(url, list);
//		String flag=parser.getreturn(retString);
//		if(flag.equals("1")){
//			result.setText("�ú�����ע��");
//		}
//		if(flag.equals("0")){
//			result.setText("�ú���δע��");
//		}
//	}
//	private void sendHttp1(String information) {
//		Log.v("Rentactivity","erroe22222222222222222.....");
//		String url = IP.URL;
//		List<Para> list = new ArrayList<Para>();// ��Ų������б�
//		list.add(new Para("information", information));// �������
//		retString = HttpTools.postVisitWeb(url, list);
//		String flag=parser.getreturn(retString);
//		if(flag.equals("1")){
//			//��֤����ȷ
//			Intent intent=new Intent();
// 		   intent.setClass(ChooseRegisterActivity.this,UserInterFaceActivity.class);
// 		intent.putExtra("tel", tel);
// 		startActivity(intent);
//			//result.setText("�ú�����ע��");
//		}
//		if(flag.equals("0")){
//			//��֤�����
//			//result.setText("�ú���δע��");
//			Toast.makeText(getApplicationContext(), "��֤�����",  
//                    Toast.LENGTH_SHORT).show(); 
//		}
//	}
}
