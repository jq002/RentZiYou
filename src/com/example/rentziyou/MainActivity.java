package com.example.rentziyou;

import cn.jpush.android.api.JPushInterface;

import com.example.jpushdemo.ExampleUtil;
import com.tools.SysApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	    private SharedPreferences sharedPreferences;  
	    private Button main_login_btn;
	    private Button main_regist_btn;
      	private TextView tv;		
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //去除标题															
	      	this.requestWindowFeature(Window.FEATURE_NO_TITLE);				

	        //推送
	        init();
	     // used for receive msg
	        registerMessageReceiver();
	        SysApplication.getInstance().addActivity(this); //退出整个程序
	        sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
	        if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)) {  
	            
	            Intent intent = new Intent();  
	            intent.setClass(MainActivity.this, LookmainActivity.class);  
	            startActivity(intent);  
	            finish(); 
	        } else {  
	            setContentView(R.layout.activity_main);
	            // SysApplication.getInstance().addActivity(this); 
	             main_regist_btn=(Button) findViewById(R.id.main_regist_btn);
	             main_login_btn=(Button)findViewById(R.id.main_login_btn);
	             tv=(TextView)findViewById(R.id.main_tv);
//	             AssetManager mgr=getAssets();//得到AssetManager
//	             Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//根据路径得到Typeface
//	             tv.setTypeface(tf);//设置字体
//	             main_regist_btn.setTypeface(tf);//设置字体
//	             main_login_btn.setTypeface(tf);//设置字体
	             main_login_btn.setOnClickListener(new Button.OnClickListener() {
	         			@Override
	         			public void onClick(View v) {
	         		            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
	         				    startActivity(intent);
	         				    
	         			}
	         		});
	             
	             main_regist_btn.setOnClickListener(new Button.OnClickListener() {
	     			@Override
	     			public void onClick(View v) {
	     		            Intent intent=new Intent(MainActivity.this,ChooseRegisterActivity.class);
	     				    startActivity(intent);
	     			}
	     		});
	        } 
	  
	    }
	    private void init(){
			 JPushInterface.init(getApplicationContext());
		}
	    private MessageReceiver mMessageReceiver;
		public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
		public static final String KEY_TITLE = "title";
		public static final String KEY_MESSAGE = "message";
		public static final String KEY_EXTRAS = "extras";
		public static boolean isForeground = false;
		
		
		public void registerMessageReceiver() {
			mMessageReceiver = new MessageReceiver();
			IntentFilter filter = new IntentFilter();
			filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
			filter.addAction(MESSAGE_RECEIVED_ACTION);
			registerReceiver(mMessageReceiver, filter);
		}
		
		public class MessageReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
	            String messge = intent.getStringExtra(KEY_MESSAGE);
	            
	    		
	            String extras = intent.getStringExtra(KEY_EXTRAS);
	            StringBuilder showMsg = new StringBuilder();
	            showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
	            if (!ExampleUtil.isEmpty(extras)) {
	          	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
	            }
	            setCostomMsg(showMsg.toString());
				}
			}
		}
		private void setCostomMsg(String msg){
			//	 if (null != msgText) {
					// msgText.setText(msg);
					 //msgText.setVisibility(android.view.View.VISIBLE);
		    //  }
				
				//ToastUtil.show(MainActivity.this, msg);

			}
		@Override
		protected void onResume() {
			isForeground = true;
			//JPushInterface.onResume(getApplication());
			super.onResume();
			JPushInterface.onResume(getApplication());
		}


		@Override
		protected void onPause() {
			isForeground = false;
			//JPushInterface.onPause(getApplication());
			super.onPause();
			JPushInterface.onPause(getApplication());

		}

		@Override
		protected void onDestroy() {
			unregisterReceiver(mMessageReceiver);
			super.onDestroy();
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
				finish();
				//SysApplication.getInstance().exit();
				break;
			}
			return super.onOptionsItemSelected(item);
	    }
	    private long exitTime = 0;  
		   
	    @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        if (KeyEvent.KEYCODE_BACK == keyCode) {  
	            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出  
	            if (System.currentTimeMillis() - exitTime > 2000) {  
	                Toast.makeText(getApplicationContext(), "再按一次退出程序",  
	                        Toast.LENGTH_SHORT).show();  
	                // 将系统当前的时间赋值给exitTime  
	                exitTime = System.currentTimeMillis();  
	            } else {  
	            	SysApplication.getInstance().exit();
	            }  
	            return true;  
	        }  
	        return super.onKeyDown(keyCode, event);  
	    }
	}
