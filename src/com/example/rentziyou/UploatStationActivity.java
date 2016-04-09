package com.example.rentziyou;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import com.chinark.apppickimagev3.adapter.MainGVAdapter;
import com.chinark.apppickimagev3.utils.ScreenUtils;
import com.chinark.apppickimagev3.utils.Utility;
import com.jqjava.lesson5.DemoApplication;
import com.jqjava.lesson5.MyDialog;
import com.jqjava.lesson5.ToastUtil;
import com.tools.SysApplication;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class UploatStationActivity extends Activity {
	
	private Button button;
	ProgressDialog m_pDialog;
	private MyDialog myDialog;
	private ProgressDialog progDialog = null;//进度框
	private int longClickPosition;

		
	    private   String image1=" ";
	 private MainGVAdapter adapter;
	    private ArrayList<String> imagePathList;
	   
//	    private String tel;
//		private String flage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploat_station);
		//广播，退出所有
				SysApplication.getInstance().addActivity(this); 
		//获取屏幕像素
        ScreenUtils.initScreen(this);
        myDialog = new MyDialog(this, R.style.MyDialogStyle);
        Button selectImgBtn = (Button) findViewById(R.id.main_select_image);
        final GridView mainGV = (GridView) findViewById(R.id.main_gridView);
//        AssetManager mgr=getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/words.TTF");//根据路径得到Typeface
//        selectImgBtn.setTypeface(tf);//设置字体
        imagePathList = new ArrayList<String>();
        adapter = new MainGVAdapter(this, imagePathList);
        mainGV.setAdapter(adapter);

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至最终的选择图片页面
                Intent intent = new Intent(UploatStationActivity.this, PhotoWallActivity.class);
                
             
                startActivity(intent);
            }
        });
    	mainGV.setOnItemLongClickListener(new GridView.OnItemLongClickListener()
    	{   
    		@Override
    		public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id)
    		{
    			int[] location = new int[2];
				
				view.getLocationOnScreen(location);
				longClickPosition = position;
				DisplayMetrics displayMetrics = new DisplayMetrics();
				Display display = UploatStationActivity.this.getWindowManager().getDefaultDisplay();
				display.getMetrics(displayMetrics);
				WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
				params.gravity = Gravity.BOTTOM;
				params.y =display.getHeight() -  location[1];
				myDialog.getWindow().setAttributes(params);
				myDialog.setCanceledOnTouchOutside(true);
				myDialog.show();
				return false;
//    			imagePathList.remove(position);
//    			adapter.notifyDataSetChanged();
    		}
    		
		});
		
		   button=(Button)findViewById(R.id.button);//此按钮，进入登记停车位页面
		   //button.setTypeface(tf);//设置字体

		    button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(imagePathList.isEmpty()){
					ToastUtil.show(UploatStationActivity.this, "尚未选择图片");
	    			return;
					
				}else{
	
					image1=testPhoto(0);
																		    	
			    	DemoApplication app2 = (DemoApplication)getApplication();  
					app2.put("image", image1);
					
			    	Intent intent=new Intent(UploatStationActivity.this ,AddChargeStationActivity.class);
				
			    	startActivity(intent);
				}
				
			

				
			}
			 
		 });
	}
	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
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

	public  String createJsonString(String key, Object value) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	
	public String testPhoto(int i){
		
		String uploadBuffer="";
		   try{  
		 FileInputStream fis = new FileInputStream(imagePathList.get(i));  
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
	
    @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);

//        	Toast.makeText(getApplicationContext(), " a",  
//                       Toast.LENGTH_SHORT).show(); 
	        int code = intent.getIntExtra("code", -1);
	        if (code != 100) {
	            return;
	        }


	        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
	        Toast.makeText(getApplicationContext(), " a"+paths,  
	                Toast.LENGTH_SHORT).show(); 
	       
	        
	        //添加，去重
	        boolean hasUpdate = false;
	        for (String path : paths) {
	            if (!imagePathList.contains(path)) {
	                //最多9张
	                if (imagePathList.size() == 1) {
	                    Utility.showToast(this, "最多可添加9张图片。");
	                    break;
	                }

	                imagePathList.add(path);
	                hasUpdate = true;
	            }
	        }

	        if (hasUpdate) {
	            adapter.notifyDataSetChanged();
	        }
	        
	       
	        
	    }
	public void rightOnclick() {
		// TODO Auto-generated method stub
		myDialog.dismiss();
	
		imagePathList.remove(longClickPosition);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uploat_station, menu);
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
