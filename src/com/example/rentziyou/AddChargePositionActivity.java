package com.example.rentziyou;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.kobjects.base64.Base64;

import com.chinark.apppickimagev3.utils.ScreenUtils;
import com.entity.Para;
import com.httpconnet.HttpTools;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;
import com.jqjava.lesson5.DemoApplication;
import com.tools.SysApplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddChargePositionActivity extends Activity implements OnClickListener {
	private ProgressDialog progDialog = null;
	private Button select_img;//选择图片
//	private TextView selecttext;//尚未选择、已选择
	private TextView tap_text;//某某充电站
	private EditText chargeaddress;//充电桩地址
	private EditText bianhao;//充电桩编号
	private EditText method_one;//常规充电价格
	private EditText method_two;//快速充电价格
	//获取的值
	private  String schargeaddress;//充电桩地址
	private  String sbianhao;//充电桩编号
	private  String smethod_one;//常规充电价格
	private  String smethod_two;//快速充电价格
	
//	private EditText changgui;//常规充电 功率
//	private EditText kuaisu;//快速充电 功率
//	private String schanggui;//常规充电 功率
//	private String skuaisu;//快速充电 功率	
//	private Button begintimeBtn;//开始时间
//	private Button endtimeBtn;//结束时间
	private Button add;//添加
	
	//设置时间
//	private Calendar calendar ;
//	private String beginTime;
//	private String endTime;
	//上个页面
	private String chargeID;
	private String chargename;
	
	private String information; 
    Packager packager = new Packager(); // 封装
    Parser parser=new Parser();
    private  String  id;
	private  String  tel;
	private String flag;
	private String image="";
	 Handler handler=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){

	    		dismissDialog();
	    		if(msg.obj==null){
	    			Toast.makeText(AddChargePositionActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		flag=parser.getreturn(ss);
	    		if(flag.equals("")){
	    			Toast.makeText(AddChargePositionActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		if(flag.equals("0")){
	    			Toast.makeText(getApplicationContext(),"添加失败  ",  
	                        Toast.LENGTH_SHORT).show(); 

	    		}else if (flag.equals("-1")){
             //chargeID=flag;
                   Toast.makeText(getApplicationContext(),"充电位已被注册" ,  
                     Toast.LENGTH_SHORT).show(); 
	    		}else{
	    			   image=null;//有助于释放内存空间
	    			Toast.makeText(getApplicationContext(),"添加成功  ",  
	                        Toast.LENGTH_SHORT).show(); 
	    			Log.v("添加成功 ",flag);
	            	Intent intent=new Intent(AddChargePositionActivity.this ,AddHourActivity.class);
					intent.putExtra("positionid",flag);
					startActivity(intent);
					finish();
	    			
	    		}
	    		
	    	}
	    	
	    };
	    //图片
	    private String imagePathList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_charge_position);
		 //获取屏幕像素
        ScreenUtils.initScreen(this);
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
        
      //获取日历实例
        //calendar = Calendar.getInstance();
        //对应充电站ID、name
        chargeID=getIntent().getStringExtra("chargeID").toString();
        chargename=getIntent().getStringExtra("chargename").toString();
        tap_text.setText(chargename);//某某充电站
        
	}
	private void init(){
		select_img=(Button)findViewById(R.id.select_img);//选择图片
//		selecttext=(TextView)findViewById(R.id.selecttext);//尚未选择、已选择
		tap_text=(TextView)findViewById(R.id.tap_text);//某某充电站
		chargeaddress=(EditText)findViewById(R.id.chargeaddress);//充电桩地址
		bianhao=(EditText)findViewById(R.id.bianhao);//充电桩编号
		method_one=(EditText)findViewById(R.id.method_one);//常规充电价格
		method_two=(EditText)findViewById(R.id.method_two);//快速充电价格
//		changgui=(EditText)findViewById(R.id.changgui);//常规充电 功率
//		kuaisu=(EditText)findViewById(R.id.kuaisu);//快速充电 功率
//		begintimeBtn=(Button)findViewById(R.id.setTimeBtn);//开始时间
//		endtimeBtn=(Button)findViewById(R.id.endTimeBtn);//结束时间
		add=(Button)findViewById(R.id.add);//添加
		
//		begintimeBtn.setOnClickListener(this);
//		endtimeBtn.setOnClickListener(this);
		add.setOnClickListener(this);
		select_img.setOnClickListener(this);
		progDialog = new ProgressDialog(this);
		  DemoApplication app1 = (DemoApplication)getApplication();  
		app1.put("flageposition", "position");
		
	}
	 public void back(View view) {
	        finish();
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_charge_position, menu);
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		  switch (v.getId()) {
          case R.id.select_img:
        	
        	  Intent intent=new Intent(AddChargePositionActivity.this, PhotoWallActivity.class);
				startActivity(intent); 			
              break;

          case R.id.add:
        	  

        		schargeaddress=chargeaddress.getText().toString().trim();
        		sbianhao=bianhao.getText().toString().trim();
        		smethod_one=method_one.getText().toString().trim();
        		smethod_two=method_two.getText().toString().trim();
//        		schanggui=changgui.getText().toString().trim();
//        		skuaisu=kuaisu.getText().toString().trim();
        		 if ("".equals(schargeaddress)) {
     				// 给出错误提示
        			 chargeaddress.setError("充电位地址不能为空，请重新输入。");
        			 chargeaddress.requestFocus();
     				// 将显示查询结果的TextView清空
     				return;
     			}
     		    if ("".equals(sbianhao)) {
     				// 给出错误提示
     		    	bianhao.setError("充电桩编号不能为空，请重新输入。");
     		    	bianhao.requestFocus();
     				// 将显示查询结果的TextView清空
     				return;
     			}
     		   if ("".equals(smethod_one)) {
    				// 给出错误提示
     			  method_one.setError("每小时收费不能为空，请重新输入。");
     			 method_one.requestFocus();
    				// 将显示查询结果的TextView清空
    				return;
    			}

    		    if ("".equals(smethod_two)) {
    				// 给出错误提示
    		    	method_two.setError("每小时收费不能为空，请重新输入。");
    		    	method_two.requestFocus();
    				// 将显示查询结果的TextView清空
    				return;
    			}
    		    if(image.length()==0||image.equals("")){
    		    	Toast.makeText(AddChargePositionActivity.this, "请选择充电位图片", Toast.LENGTH_SHORT).show();	

    		    	return;
    		    	
    		    }
//    		    if ("".equals(schanggui)) {
//     				// 给出错误提示
//        			 changgui.setError("充电站名不能为空，请重新输入。");
//        			 changgui.requestFocus();
//     				// 将显示查询结果的TextView清空
//     				return;
//     			}
//     		    if ("".equals(skuaisu)) {
//     				// 给出错误提示
//     		    	kuaisu.setError("帮助信息不能为空，请重新输入。");
//     		    	kuaisu.requestFocus();
//     				// 将显示查询结果的TextView清空
//     				return;
//     			}
    		    
    		    showDialog();
    		    Log.v("add", "... id"+id+"sme "+smethod_one+"beg ");
    		    if(LoginActivity.testflage){
    		    	 Toast.makeText(AddChargePositionActivity.this, "测试中", Toast.LENGTH_SHORT).show();

    		    	
    		    }else{
    		    information = packager.addSpace(sbianhao, chargeID, id, schargeaddress, smethod_one, smethod_two, image);    		      
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
//	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//    	Toast.makeText(getApplicationContext(), " a",  
//                   Toast.LENGTH_SHORT).show(); 
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
//        Toast.makeText(getApplicationContext(), " a"+paths,  
//                Toast.LENGTH_SHORT).show();        
        Log.v("paths","paths"+paths.get(0));
       ImageView img = (ImageView) findViewById(R.id.image);
       BitmapFactory.Options options = new BitmapFactory.Options();
       options.inJustDecodeBounds = true;
       Bitmap bmp = BitmapFactory.decodeFile(paths.get(0), options);
       
       int height = options.outHeight * 200 / options.outWidth;
       options.inSampleSize = options.outWidth / 200;
       options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
       options.inPurgeable = true;
       options.inInputShareable = true;
       options.outWidth = 200;
       options.outHeight = height; 
       options.inJustDecodeBounds = false;
       Bitmap bm = BitmapFactory.decodeFile(paths.get(0), options);
       img.setImageBitmap(bm);
       
        imagePathList=paths.get(0);
        image=testPhoto();
        Log.v(" image"," image "+ image);
        
    }
public String testPhoto(){
		
		String uploadBuffer="";
		   try{  
		 FileInputStream fis = new FileInputStream(imagePathList);  
         ByteArrayOutputStream baos = new ByteArrayOutputStream();  
         byte[] buffer = new byte[1024];  
         int count = 0;  
         while((count = fis.read(buffer)) >= 0){  
             baos.write(buffer, 0, count);  
         }  
          uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //进行Base64编码  
        
		   }catch(Exception e){  
	            e.printStackTrace();  
	        }  
		   return uploadBuffer;
	}
	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在加载中");
		progDialog.show();
	}
//	public void showpdialog(){
//		m_pDialog=new ProgressDialog(LoginActivity.this);
//		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		m_pDialog.setMessage("服务器响应中。。。");
//	    m_pDialog.show();
//	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
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
}
