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
	private ProgressDialog progDialog = null;//进度框
	
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
	    String user_name="黄方意";
	    String user_sex="男";
	    String user_tel1;
	    //String user_credit;
	    
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
		    		List<Map<String, Object>> orderInfo= new ArrayList<Map<String, Object>>();
					String ss=msg.obj.toString();    			
					//Toast.makeText(SearchOrderActivity.this,ss, Toast.LENGTH_SHORT).show();
					orderInfo = JsonUtils.listKeyMaps("address",ss);
//					Toast.makeText(getApplicationContext(), "网络"+orderInfo,  
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
		    			Toast.makeText(getApplicationContext(), "网络连接异常",  
			                       Toast.LENGTH_SHORT).show(); 
		    			return;
		    		}
		    		String ss=(String)msg.obj;
		    		String flag=parser.getreturn(ss);
		    		if(flag.equals("0")){
		    			Toast.makeText(getApplicationContext(),"性别修改失败  ",  
		                        Toast.LENGTH_SHORT).show(); 
		    			tv_sex.setText(user_sex);
		    		}else{
		    			Toast.makeText(getApplicationContext(),"性别修改成功  ",  
		                        Toast.LENGTH_SHORT).show(); 
		    		}

		    	}
		 };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_user_info);
		//取得ActionBar对象
        ActionBar actionBar =getActionBar();
        //调用hide方法，隐藏actionbar
        actionBar.hide();
      //广播，退出所有
      		SysApplication.getInstance().addActivity(this); 
        DemoApplication app = (DemoApplication)getApplication();  		
		id=(String)app.get("id");
		tel=(String)app.get("tel");
		
        initview();
        //获取全部个人信息
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
	        
	        // 头像
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
	        // *** 主要就是在这里实现这种效果的.
	        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
	        window.setContentView(R.layout.alertdialog);
	        LinearLayout ll_title = (LinearLayout) window
	                .findViewById(R.id.ll_title);
	        ll_title.setVisibility(View.VISIBLE);
	        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
	        tv_title.setText("性别");
	        // 为确认按钮添加事件,执行退出应用操作
	        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	        tv_paizhao.setText("男");
	        tv_paizhao.setOnClickListener(new View.OnClickListener() {
	            @SuppressLint("SdCardPath")
	            public void onClick(View v) {
	                if (!user_sex.equals("男")) {
	                    tv_sex.setText("男");

	                    updateSex("0");
	                }

	                dlg.cancel();
	            }
	        });
	        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
	        tv_xiangce.setText("女");
	        tv_xiangce.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	                if (!user_sex.equals("女")) {

	                    tv_sex.setText("女");
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
	    		List<Para> list = new ArrayList<Para>();// 存放参数的列表
	    		list.add(new Para("information", information));// 参数打包
	    		Web_result= HttpTools.postVisitWeb(url, list);
	       
	    	Message msg=new Message();
	    	msg.obj=Web_result;
	    	handler2.sendMessage(msg);
	    	}
		
	    }.start();
			}
	 }
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		 switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
//		    case 1:
//		     Bundle b=data.getExtras(); //data为B中回传的Intent
//		     String str=b.getString("str1");//str即为回传的值
//		     break;
//		 default:
//		     break;
//		     }
		 }
	 /**
		 * 显示进度条对话框
		 */
		public void showDialog() {
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(true);
			progDialog.setMessage("正在加载");
			progDialog.show();
		}

		/**
		 * 隐藏进度条对话框
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
