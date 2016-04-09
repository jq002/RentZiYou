package com.example.rentziyou;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.chinark.apppickimagev3.utils.ScreenUtils;
import com.chinark.apppickimagev3.utils.Utility;
import com.entity.Para;
import com.httpconnet.HttpTools;
import com.httpconnet.Httptool;
import com.httpconnet.IP;
import com.httpconnet.Packager;
import com.httpconnet.Parser;
import com.jqjava.lesson5.DemoApplication;
import com.tools.SysApplication;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddChargeStationActivity extends Activity {
	
	 private EditText parklotName;
	    private EditText helpAddress;  
	    private Button judge_add;
	    private String parkAddress;
	    private String  longtitude;//����
	    private String  latitude;//γ��
	    private String  chargeID;
	    private String flag;
	    private String  text1;
	    private String  text2;
	    private String image="";

//	    ProgressDialog m_pDialog;

	    
	    Handler handler=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
//	    		m_pDialog.hide();
	    		dialog.dismiss();
	    		if(msg.obj==null){
	    			Toast.makeText(AddChargeStationActivity.this, "���������쳣", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		flag=parser.getreturn(ss);
	    		if(flag.equals("")){
	    			Toast.makeText(AddChargeStationActivity.this, "���������쳣", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		if(flag.equals("0")){
	    			Toast.makeText(getApplicationContext(),"���ʧ��  ",  
	                        Toast.LENGTH_SHORT).show(); 

	    		}else{
                chargeID=flag;
                new Thread(){
    		    	public void run(){
    		    		String Web_result="";

    		        	sendHttpDataJi();
//    		    		List<Para> list = new ArrayList<Para>();// ��Ų������б�
//    		    		list.add(new Para("information", information));// �������
    		    		Web_result= Httptool.postVisitWeb(url2, list);
    		       
    		    	Message msg=new Message();
    		    	msg.obj=Web_result;
    		    	handler2.sendMessage(msg);

    		    	}
    			
    		    }.start();

                image=null;//�������ͷ��ڴ�ռ�
                Toast.makeText(getApplicationContext(),"chargeID " +chargeID,  
                        Toast.LENGTH_SHORT).show(); 
                Intent intent=new Intent(AddChargeStationActivity.this ,AddChargePositionActivity.class);
                intent.putExtra("chargeID", flag);
				intent.putExtra("chargename", text1);
				startActivity(intent);
				finish();
	    		}

	    		
	    	}
	    	
	    };
	    Handler handler2=new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
//	    		m_pDialog.hide();
	    		if(msg.obj==null){
	    			Toast.makeText(AddChargeStationActivity.this, "���������쳣", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		Log.v("", ss);
	    	}
	    };

	  private TextView tap_text;//��ʾѡ��ĳ���λ��
	    
	  private Button select_img;
	  //private TextView text3;
	  private  String  id;
	  private  String  tel;
	  
	  private String information; 
      Packager packager = new Packager(); // ��װ
      Parser parser=new Parser();
      //ͼƬ
      private String imagePathList;
//      private ArrayList<String> imagePathList;
      ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_charge_station);
        //��ȡ��Ļ����
        ScreenUtils.initScreen(this);
		//ȡ��ActionBar����
        ActionBar actionBar =getActionBar();
        //����hide����������actionbar
        actionBar.hide();
        //�㲥���˳�����
      	SysApplication.getInstance().addActivity(this); 
      	
        DemoApplication app = (DemoApplication)getApplication();  
    	id=(String)app.get("id");
    	tel=(String)app.get("tel");
    	
    	parklotName=(EditText)findViewById(R.id.parklotName);
    	helpAddress=(EditText)findViewById(R.id.helpAddress);
    	
    	parkAddress=getIntent().getStringExtra("parkAddress").toString();
    	longtitude=getIntent().getStringExtra("longtitude").toString();
    	latitude=getIntent().getStringExtra("latitude").toString();
    	
    	tap_text=(TextView)findViewById(R.id.tap_text);
    	tap_text.setText("��񻯵�ַ��"+parkAddress);
    	judge_add=(Button)findViewById(R.id.judge_add);
    	
    	judge_add.setOnClickListener(new Button.OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    		    text1=parklotName.getText().toString().trim();
    		    text2=helpAddress.getText().toString().trim();
    		    
    		    if ("".equals(text1)) {
    				// ����������ʾ
    		    	parklotName.setError("���վ������Ϊ�գ����������롣");
    		    	parklotName.requestFocus();
    				// ����ʾ��ѯ�����TextView���
    				return;
    			}
    		    if ("".equals(text2)) {
    				// ����������ʾ
    		    	helpAddress.setError("������Ϣ����Ϊ�գ����������롣");
    		    	helpAddress.requestFocus();
    				// ����ʾ��ѯ�����TextView���
    				return;
    			}
    		    if(image.length()==0||image.equals("")){
    		    	Toast.makeText(AddChargeStationActivity.this, "��ѡ����վͼƬ", Toast.LENGTH_SHORT).show();	

    		    	return;
    		    	
    		    }

    			dialog=new ProgressDialog(AddChargeStationActivity.this);
    			dialog.setMessage("���ڼ���....");
    			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    			dialog.show();
    		    //Log.v(" image222","id "+parkAddress+" image "+ image);
    		    lola=longtitude+","+latitude;
    		    Log.v(" lola.....",longtitude+","+latitude);
    		    information = packager.addStationPackager(id,text1,text2,parkAddress,longtitude,latitude,image);    		      
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
    	});
    	
    	//text3=(TextView)findViewById(R.id.text2);
    	select_img=(Button)findViewById(R.id.select_img);
    	select_img.setOnClickListener(new Button.OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			DemoApplication app1 = (DemoApplication)getApplication();  
      			app1.put("flageposition", "station");
//    			Intent intent=new Intent(AddChargeStationActivity.this, PhotoWallActivity.class);
//    				startActivity(intent); 
      			Intent intent=new Intent(AddChargeStationActivity.this, UploatStation2Activity.class);
				startActivity(intent);
    			
    		}
    	});

	}
   	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//    	Toast.makeText(getApplicationContext(), " a",  
//                   Toast.LENGTH_SHORT).show(); 
        //���û��ȡ��ֵ���򷵻�-1��
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");

        Log.v("paths","paths "+paths.get(0));
        ImageView img = (ImageView) findViewById(R.id.image);
//        if(true){  		 
//		       Bitmap bm = BitmapFactory.decodeFile(paths.get(0));
//		       img.setImageBitmap(bm);
//		}
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //inJustDecodeBounds��Ϊtrue��BitmapFactory.decodeFile
        //��������ķ���һ��Bitmap���㣬������������Ŀ���ȡ�������㣬�����Ͳ���ռ��̫����ڴ�
        Bitmap bmp = BitmapFactory.decodeFile(paths.get(0), options);
        
        int height = options.outHeight * 200 / options.outWidth;
        options.inSampleSize = options.outWidth / 200;
        //Ӱ��һ��ͼƬռ�ÿռ�Ļ�����ɫ��ϸ��
        //λͼλ��Խ�ߴ�������Դ洢����ɫ��ϢԽ�࣬��Ȼͼ��Ҳ��Խ���档
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // Ĭ����Bitmap.Config.ARGB_8888
        //  ��� inPurgeable ��ΪTrue�Ļ���ʾʹ��BitmapFactory������Bitmap 
         //���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա����գ� 
        options.inPurgeable = true;        
        options.inInputShareable = true;
        
        options.outWidth = 200;
        options.outHeight = height; 
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(paths.get(0), options);
        img.setImageBitmap(bm);

        imagePathList=paths.get(0);
        Log.v("imagePathList"," image "+ imagePathList);
       
        image=testPhoto(0);
        //Log.v("imagePathList"," image "+ imagePathList);
        
    }
public String testPhoto(int i){
		
		String uploadBuffer="";
		   try{  
		 FileInputStream fis = new FileInputStream(imagePathList);  
         ByteArrayOutputStream baos = new ByteArrayOutputStream();  
         byte[] buffer = new byte[1024];  
         int count = 0;  
         while((count = fis.read(buffer)) >= 0){  
             baos.write(buffer, 0, count);  
         }  
          uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //����Base64����  
        fis.close();
		   }catch(Exception e){  
	            e.printStackTrace();  
	        }  
		   return uploadBuffer;
	}

	 public void back(View view) {
	        finish();
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_charge_station, menu);
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
	//------------------------------------------------------------
//	 private ArrayList<String> imagePathList;
//	  @Override
//	    protected void onNewIntent(Intent intent) {
//	        super.onNewIntent(intent);
//
////      	Toast.makeText(getApplicationContext(), " a",  
////                     Toast.LENGTH_SHORT).show(); 
//	        int code = intent.getIntExtra("code", -1);
//	        if (code != 100) {
//	            return;
//	        }
//
//
//	        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
//	        Toast.makeText(getApplicationContext(), " a"+paths,  
//	                Toast.LENGTH_SHORT).show(); 
//	       
//	        
//	        //��ӣ�ȥ��
//	        boolean hasUpdate = false;
//	        for (String path : paths) {
//	            if (!imagePathList.contains(path)) {
//	                //���9��
//	                if (imagePathList.size() == 1) {
//	                    Utility.showToast(this, "�������9��ͼƬ��");
//	                    break;
//	                }
//
//	                imagePathList.add(path);
//	                hasUpdate = true;
//	            }
//	        }
//
//	        if (hasUpdate) {
//	            adapter.notifyDataSetChanged();
//	        }
//	        
//	       
//	        
//	    }
	
	//--------------------------------------------------������ͼ��json------------		
			JSONArray array = new JSONArray();  
			JSONObject object = new JSONObject();  
			JSONObject stoneObject = new JSONObject(); 
			String lola;
			private String changeDateToJson2(List<Para> stoneList){  
			    try {  
//			        JSONArray array = new JSONArray();  
//			        JSONObject object = new JSONObject();  
			        int length = stoneList.size();  
			        for (int i = 0; i < length; i++) {  
			            Para para = stoneList.get(i);  
			            String name = para.getName();  
			            String size = para.getValue();  
			            
			            stoneObject.put(name , size);  
			             
			            
			        } 
			        array.put(stoneObject); 
			        object.put("array", array);  
			        return object.toString();  
			    } catch (JSONException e) {  
			        e.printStackTrace();  
			    }  
			    return null;  
			} 
			List<Para> list = new ArrayList<Para>();// ��Ų������б�	
			final String url2 = "http://yuntuapi.amap.com/datamanage/data/create";

			private void sendHttpDataJi() {
				
				Log.v("create DATA", "======");
//				final String url2 = "http://yuntuapi.amap.com/datamanage/data/create";
//				List<Para> list = new ArrayList<Para>();// ��Ų������б�
				list.add(new Para("key", "6dd46e7359b087a31c6a3e930b386a95"));// �������
				list.add(new Para("tableid","56d6a0e5305a2a3288a876a8"));
				list.add(new Para("loctype","1"));
				
				List<Para> lista = new ArrayList<Para>();// ��Ų������б�
				lista.add(new Para("_name",text1));
				lista.add(new Para("_location",lola));
				lista.add(new Para("coordtype","2"));
				lista.add(new Para("_address",parkAddress));
				lista.add(new Para("station_address",text2));
				lista.add(new Para("chargeID",chargeID));
				
				String json=changeDateToJson2(lista);
				Log.v("",stoneObject.toString());
				
				list.add(new Para("data",stoneObject.toString()));
				list.add(new Para("sig",""));
				


			}
			
}
