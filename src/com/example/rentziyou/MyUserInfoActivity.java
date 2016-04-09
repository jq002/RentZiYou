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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyUserInfoActivity extends Activity {
	private ProgressDialog progDialog = null;//���ȿ�
	
	    private RelativeLayout re_avatar;
	    private RelativeLayout re_name;
	    private RelativeLayout re_phone;
	    private RelativeLayout re_sex;
	    private RelativeLayout re_pw;
	    
	    private ImageView iv_avatar;
	    private TextView tv_name;
	    private TextView tv_phone;
	    private TextView tv_sex;

	    String sex="1";
	    String id;
	    String tel;
	    String user_name="�Ʒ���";
	    String user_sex="��";
	    String user_tel1;
	    //String user_credit;
	    
	    private String information; 
		Packager packager = new Packager(); // ��װ
		Parser parser=new Parser();
		
		 Handler handler=new Handler(){
		    	@Override
		    	public void handleMessage(Message msg){
		    		//m_pDialog.hide();
		    		
		    		if(msg.obj==null){
		    			Toast.makeText(getApplicationContext(), "���������쳣",  
			                       Toast.LENGTH_SHORT).show(); 
		    			return;
		    		}
		    		List<Map<String, Object>> orderInfo= new ArrayList<Map<String, Object>>();
					String ss=msg.obj.toString();    			
					//Toast.makeText(SearchOrderActivity.this,ss, Toast.LENGTH_SHORT).show();
					orderInfo = JsonUtils.listKeyMaps("address",ss);
//					Toast.makeText(getApplicationContext(), "����"+orderInfo,  
//		                       Toast.LENGTH_SHORT).show(); 
					for(int i=0;i<orderInfo.size();i++){
						
						Map<String, Object> or=new HashMap<String, Object>();
						or=orderInfo.get(i);				
						user_name=or.get("user_name").toString();			
						user_tel1=or.get("user_tel1").toString();
					
						user_sex=or.get("user_sex").toString();
						//user_credit=or.get("user_credit").toString();							
					}
					initUserInfo();
		    	}
		 };
		 
		 Handler handler2=new Handler(){
		    	@Override
		    	public void handleMessage(Message msg){
		    		dismissDialog();

		    		if(msg.obj==null){
		    			Toast.makeText(getApplicationContext(), "���������쳣",  
			                       Toast.LENGTH_SHORT).show(); 
		    			return;
		    		}
		    		String ss=(String)msg.obj;
		    		String flag=parser.getreturn(ss);
		    		if(flag.equals("0")){
		    			Toast.makeText(getApplicationContext(),"�Ա��޸�ʧ��  ",  
		                        Toast.LENGTH_SHORT).show(); 
		    			tv_sex.setText(user_sex);
		    		}else{
		    			Toast.makeText(getApplicationContext(),"�Ա��޸ĳɹ�  ",  
		                        Toast.LENGTH_SHORT).show(); 
		    		}

		    	}
		 };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_user_info);
		//ȡ��ActionBar����
        ActionBar actionBar =getActionBar();
        //����hide����������actionbar
        actionBar.hide();
      //�㲥���˳�����
      		SysApplication.getInstance().addActivity(this); 
        DemoApplication app = (DemoApplication)getApplication();  		
		id=(String)app.get("id");
		tel=(String)app.get("tel");
		
        initview();
        //��ȡȫ��������Ϣ
        if(LoginActivity.testflage){
        	Toast.makeText(getApplicationContext(), "id "+id+"tel "+tel,  
                    Toast.LENGTH_SHORT).show();
		}else{
//			Toast.makeText(getApplicationContext(), "id "+id+"tela "+tel,  
//                    Toast.LENGTH_SHORT).show();
		 information = packager.getUserInfoPackager(id);
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

	private void initview(){
		 re_avatar = (RelativeLayout) this.findViewById(R.id.re_avatar);
	        re_name = (RelativeLayout) this.findViewById(R.id.re_name);
	        re_phone = (RelativeLayout) this.findViewById(R.id.re_phone);
	        re_sex = (RelativeLayout) this.findViewById(R.id.re_sex);
	        re_pw = (RelativeLayout) this.findViewById(R.id.re_pw);
	        re_avatar.setOnClickListener(new MyListener());
	        re_name.setOnClickListener(new MyListener());
	        re_phone.setOnClickListener(new MyListener());
	        re_sex.setOnClickListener(new MyListener());
	        re_pw.setOnClickListener(new MyListener());
	        
	        // ͷ��
	        iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);
	        tv_name = (TextView) this.findViewById(R.id.tv_name);
	        tv_phone = (TextView) this.findViewById(R.id.tv_phone);
	        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
	       
		
	}
	private void initUserInfo(){
		tv_name.setText(user_name);
		tv_phone.setText(user_tel1);
		tv_sex.setText(user_sex);
	}
	 class MyListener implements OnClickListener {

	        @Override
	        public void onClick(View v) {
	            switch (v.getId()) {
	            case R.id.re_avatar:

	                //showPhotoDialog();

	                break;
	            case R.id.re_name:
//	                startActivityForResult(new Intent(MyUserInfoActivity.this,
//	                        UpdateNickActivity.class),1);
	            	Intent intent=new Intent(MyUserInfoActivity.this,
	                        UpdateNickActivity.class);
					intent.putExtra("user_name",user_name);	
					startActivity(intent);
					
	                break;
	            case R.id.re_phone:
//	                if (LocalUserInfo.getInstance(MyUserInfoActivity.this)
//	                        .getUserInfo("fxid").equals("0")) {
//	                    startActivityForResult(new Intent(MyUserInfoActivity.this,
//	                            UpdateFxidActivity.class),UPDATE_FXID);
//
//	                }
	                break;
	            case R.id.re_sex:
	                showSexDialog();
	                break;
	            case R.id.re_pw:
	            	Intent intent1=new Intent(MyUserInfoActivity.this,
	                        ChangepwdActivity.class);
					
					startActivity(intent1);

	                break;

	            }
	        }

	    }
	 private void showSexDialog() {
	        final AlertDialog dlg = new AlertDialog.Builder(this).create();
	        dlg.show();
	        Window window = dlg.getWindow();
	        // *** ��Ҫ����������ʵ������Ч����.
	        // ���ô��ڵ�����ҳ��,shrew_exit_dialog.xml�ļ��ж���view����
	        window.setContentView(R.layout.alertdialog);
	        LinearLayout ll_title = (LinearLayout) window
	                .findViewById(R.id.ll_title);
	        ll_title.setVisibility(View.VISIBLE);
	        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
	        tv_title.setText("�Ա�");
	        // Ϊȷ�ϰ�ť����¼�,ִ���˳�Ӧ�ò���
	        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	        tv_paizhao.setText("��");
	        tv_paizhao.setOnClickListener(new View.OnClickListener() {
	            @SuppressLint("SdCardPath")
	            public void onClick(View v) {
	                if (!user_sex.equals("��")) {
	                    tv_sex.setText("��");

	                    updateSex("0");
	                }

	                dlg.cancel();
	            }
	        });
	        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
	        tv_xiangce.setText("Ů");
	        tv_xiangce.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	                if (!user_sex.equals("Ů")) {

	                    tv_sex.setText("Ů");
	                    updateSex("1");
	                }

	                dlg.cancel();
	            }
	        });

	    }
	 public void updateSex(final String sexnum) {
		 if(LoginActivity.testflage){
	        	Toast.makeText(getApplicationContext(), "id "+id+"sexnum "+sexnum,  
	                    Toast.LENGTH_SHORT).show();
			}else{
//				Toast.makeText(getApplicationContext(), "id "+id+"sexnum "+sexnum,  
//	                    Toast.LENGTH_SHORT).show();
				 Log.v("Rentactivity","erroe....."+id+"sex"+sexnum);
			 information = packager.updateUserSexPackager(id, sexnum);
			 //showDialog();
	       new Thread(){
	    	public void run(){
	    		String Web_result="";
	        	String url = IP.URL;
	    		List<Para> list = new ArrayList<Para>();// ��Ų������б�
	    		list.add(new Para("information", information));// �������
	    		Web_result= HttpTools.postVisitWeb(url, list);
	       
	    	Message msg=new Message();
	    	msg.obj=Web_result;
	    	handler2.sendMessage(msg);
	    	}
		
	    }.start();
			}
	 }
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		 switch (resultCode) { //resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
//		    case 1:
//		     Bundle b=data.getExtras(); //dataΪB�лش���Intent
//		     String str=b.getString("str1");//str��Ϊ�ش���ֵ
//		     break;
//		 default:
//		     break;
//		     }
		 }
	 /**
		 * ��ʾ�������Ի���
		 */
		public void showDialog() {
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(true);
			progDialog.setMessage("���ڼ���");
			progDialog.show();
		}

		/**
		 * ���ؽ������Ի���
		 */
		public void dismissDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}
	 public void back(View view) {
	        finish();
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_user_info, menu);
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
