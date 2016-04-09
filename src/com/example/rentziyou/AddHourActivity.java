package com.example.rentziyou;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddHourActivity extends Activity implements OnClickListener {
	private Button begintimeBtn;//开始时间
	private Button endtimeBtn;//结束时间
	private Button add;//添加
	
	//设置时间
	private Calendar calendar ;
	private String beginTime;
	private String endTime;
	//上个页面
	private String chargeID;
	private String chargename;
	
	private String information; 
    Packager packager = new Packager(); // 封装
    Parser parser=new Parser();
    private  String  id;
	private  String  tel;
	private String positionid;
	private String flag;
	
	 Handler handler=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
//	    		m_pDialog.hide();
	    		if(msg.obj==null){
	    			Toast.makeText(AddHourActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		flag=parser.getreturn(ss);
	    		if(flag.equals("0")){
	    			Toast.makeText(getApplicationContext(),"添加失败  ",  
	                        Toast.LENGTH_SHORT).show(); 

	    		}else{
          //chargeID=flag;
          Toast.makeText(getApplicationContext(),"添加成功" ,  
                  Toast.LENGTH_SHORT).show(); 
          Intent intent=new Intent(AddHourActivity.this ,LookmainActivity.class);
			intent.putExtra("positionid",flag);
			startActivity(intent);
	    		}
	      	}
	    	
	    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_hour);
		//取得ActionBar对象
        ActionBar actionBar =getActionBar();
        //调用hide方法，隐藏actionbar
        actionBar.hide();
      //广播，退出所有
      		SysApplication.getInstance().addActivity(this); 
        DemoApplication app = (DemoApplication)getApplication();  
    	id=(String)app.get("id");
    	tel=(String)app.get("tel");
        init();
        positionid=getIntent().getStringExtra("positionid").toString();
      //获取日历实例
        calendar = Calendar.getInstance();
	}
	private void init(){
		begintimeBtn=(Button)findViewById(R.id.setTimeBtn);//开始时间
		endtimeBtn=(Button)findViewById(R.id.endTimeBtn);//结束时间
		add=(Button)findViewById(R.id.add);//添加
		
		begintimeBtn.setOnClickListener(this);
		endtimeBtn.setOnClickListener(this);
		add.setOnClickListener(this);
	}
	 public void back(View view) {
	        finish();
	    }
	 @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			  switch (v.getId()) {
	          case R.id.setTimeBtn:
	        	  Log.v("begintime", "click the time button to set begin time");
					calendar.setTimeInMillis(System.currentTimeMillis());
					new TimePickerDialog(AddHourActivity.this,new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker arg0, int h, int m) {
							//更新按钮上的时间
							//begintimeBtn.setText(formatTime(h,m));
							begintimeBtn.setText(formatTime(h,m));
							//设置日历的时间，主要是让日历的年月日和当前同步
							calendar.setTimeInMillis(System.currentTimeMillis());
							//设置日历的小时和分钟
							calendar.set(Calendar.HOUR_OF_DAY, h);
							calendar.set(Calendar.MINUTE, m);
							//将秒和毫秒设置为0
							calendar.set(Calendar.SECOND, 0);
							calendar.set(Calendar.MILLISECOND, 0);
							beginTime=formatTime(h,m).toString();
						}
					},calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();				
				

	              break;
	          case R.id.endTimeBtn:
	        	  Log.v("endtime", "click the time button to set end time");
					calendar.setTimeInMillis(System.currentTimeMillis());
					new TimePickerDialog(AddHourActivity.this,new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker arg0, int h, int m) {
							//更新按钮上的时间
							//endtimeBtn.setText(formatTime(h,m));
							endtimeBtn.setText(formatTime(h,m));
							//设置日历的时间，主要是让日历的年月日和当前同步
							calendar.setTimeInMillis(System.currentTimeMillis());
							//设置日历的小时和分钟
							calendar.set(Calendar.HOUR_OF_DAY, h);
							calendar.set(Calendar.MINUTE, m);
							//将秒和毫秒设置为0
							calendar.set(Calendar.SECOND, 0);
							calendar.set(Calendar.MILLISECOND, 0);
							endTime=formatTime(h,m).toString();
						
						}
					},calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();				
				
			
	              break;
	          case R.id.add:

	        
	    		    Log.v("add", "... id"+id+"begin"+beginTime+"end"+endTime);
	    		    if(LoginActivity.testflage){
	    		    	 Toast.makeText(AddHourActivity.this, "测试中", Toast.LENGTH_SHORT).show();

	    		    	
	    		    }else{
	    		    information = packager.addTimeSlicePackager(positionid, beginTime, endTime);    		      
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
	    		    

		
	              break;


	          }
		}

	 public String formatTime(int h , int m) {
	    	StringBuffer buf = new StringBuffer();
	    	if(h<10){
	    		buf.append("0"+h);
	    	}else {
				buf.append(h);
			}
	    	buf.append(" : ");
	    	if(m<10){
	    		buf.append("0"+m);
	    	}else {
	    		buf.append(m);
			}
			return buf.toString();
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_hour, menu);
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
