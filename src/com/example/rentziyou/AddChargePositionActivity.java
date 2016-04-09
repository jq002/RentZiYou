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
	private Button select_img;//ѡ��ͼƬ
//	private TextView selecttext;//��δѡ����ѡ��
	private TextView tap_text;//ĳĳ���վ
	private EditText chargeaddress;//���׮��ַ
	private EditText bianhao;//���׮���
	private EditText method_one;//������۸�
	private EditText method_two;//���ٳ��۸�
	//��ȡ��ֵ
	private  String schargeaddress;//���׮��ַ
	private  String sbianhao;//���׮���
	private  String smethod_one;//������۸�
	private  String smethod_two;//���ٳ��۸�
	
//	private EditText changgui;//������ ����
//	private EditText kuaisu;//���ٳ�� ����
//	private String schanggui;//������ ����
//	private String skuaisu;//���ٳ�� ����	
//	private Button begintimeBtn;//��ʼʱ��
//	private Button endtimeBtn;//����ʱ��
	private Button add;//���
	
	//����ʱ��
//	private Calendar calendar ;
//	private String beginTime;
//	private String endTime;
	//�ϸ�ҳ��
	private String chargeID;
	private String chargename;
	
	private String information; 
    Packager packager = new Packager(); // ��װ
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
	    			Toast.makeText(AddChargePositionActivity.this, "���������쳣", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		String ss=(String)msg.obj;
	    		flag=parser.getreturn(ss);
	    		if(flag.equals("")){
	    			Toast.makeText(AddChargePositionActivity.this, "���������쳣", Toast.LENGTH_SHORT).show();	
	    			return;
	    		}
	    		if(flag.equals("0")){
	    			Toast.makeText(getApplicationContext(),"���ʧ��  ",  
	                        Toast.LENGTH_SHORT).show(); 

	    		}else if (flag.equals("-1")){
             //chargeID=flag;
                   Toast.makeText(getApplicationContext(),"���λ�ѱ�ע��" ,  
                     Toast.LENGTH_SHORT).show(); 
	    		}else{
	    			   image=null;//�������ͷ��ڴ�ռ�
	    			Toast.makeText(getApplicationContext(),"��ӳɹ�  ",  
	                        Toast.LENGTH_SHORT).show(); 
	    			Log.v("��ӳɹ� ",flag);
	            	Intent intent=new Intent(AddChargePositionActivity.this ,AddHourActivity.class);
					intent.putExtra("positionid",flag);
					startActivity(intent);
					finish();
	    			
	    		}
	    		
	    	}
	    	
	    };
	    //ͼƬ
	    private String imagePathList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_charge_position);
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
        init();
        
      //��ȡ����ʵ��
        //calendar = Calendar.getInstance();
        //��Ӧ���վID��name
        chargeID=getIntent().getStringExtra("chargeID").toString();
        chargename=getIntent().getStringExtra("chargename").toString();
        tap_text.setText(chargename);//ĳĳ���վ
        
	}
	private void init(){
		select_img=(Button)findViewById(R.id.select_img);//ѡ��ͼƬ
//		selecttext=(TextView)findViewById(R.id.selecttext);//��δѡ����ѡ��
		tap_text=(TextView)findViewById(R.id.tap_text);//ĳĳ���վ
		chargeaddress=(EditText)findViewById(R.id.chargeaddress);//���׮��ַ
		bianhao=(EditText)findViewById(R.id.bianhao);//���׮���
		method_one=(EditText)findViewById(R.id.method_one);//������۸�
		method_two=(EditText)findViewById(R.id.method_two);//���ٳ��۸�
//		changgui=(EditText)findViewById(R.id.changgui);//������ ����
//		kuaisu=(EditText)findViewById(R.id.kuaisu);//���ٳ�� ����
//		begintimeBtn=(Button)findViewById(R.id.setTimeBtn);//��ʼʱ��
//		endtimeBtn=(Button)findViewById(R.id.endTimeBtn);//����ʱ��
		add=(Button)findViewById(R.id.add);//���
		
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
     				// ����������ʾ
        			 chargeaddress.setError("���λ��ַ����Ϊ�գ����������롣");
        			 chargeaddress.requestFocus();
     				// ����ʾ��ѯ�����TextView���
     				return;
     			}
     		    if ("".equals(sbianhao)) {
     				// ����������ʾ
     		    	bianhao.setError("���׮��Ų���Ϊ�գ����������롣");
     		    	bianhao.requestFocus();
     				// ����ʾ��ѯ�����TextView���
     				return;
     			}
     		   if ("".equals(smethod_one)) {
    				// ����������ʾ
     			  method_one.setError("ÿСʱ�շѲ���Ϊ�գ����������롣");
     			 method_one.requestFocus();
    				// ����ʾ��ѯ�����TextView���
    				return;
    			}

    		    if ("".equals(smethod_two)) {
    				// ����������ʾ
    		    	method_two.setError("ÿСʱ�շѲ���Ϊ�գ����������롣");
    		    	method_two.requestFocus();
    				// ����ʾ��ѯ�����TextView���
    				return;
    			}
    		    if(image.length()==0||image.equals("")){
    		    	Toast.makeText(AddChargePositionActivity.this, "��ѡ����λͼƬ", Toast.LENGTH_SHORT).show();	

    		    	return;
    		    	
    		    }
//    		    if ("".equals(schanggui)) {
//     				// ����������ʾ
//        			 changgui.setError("���վ������Ϊ�գ����������롣");
//        			 changgui.requestFocus();
//     				// ����ʾ��ѯ�����TextView���
//     				return;
//     			}
//     		    if ("".equals(skuaisu)) {
//     				// ����������ʾ
//     		    	kuaisu.setError("������Ϣ����Ϊ�գ����������롣");
//     		    	kuaisu.requestFocus();
//     				// ����ʾ��ѯ�����TextView���
//     				return;
//     			}
    		    
    		    showDialog();
    		    Log.v("add", "... id"+id+"sme "+smethod_one+"beg ");
    		    if(LoginActivity.testflage){
    		    	 Toast.makeText(AddChargePositionActivity.this, "������", Toast.LENGTH_SHORT).show();

    		    	
    		    }else{
    		    information = packager.addSpace(sbianhao, chargeID, id, schargeaddress, smethod_one, smethod_two, image);    		      
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
       options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // Ĭ����Bitmap.Config.ARGB_8888
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
          uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //����Base64����  
        
		   }catch(Exception e){  
	            e.printStackTrace();  
	        }  
		   return uploadBuffer;
	}
	/**
	 * ��ʾ�������Ի���
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("���ڼ�����");
		progDialog.show();
	}
//	public void showpdialog(){
//		m_pDialog=new ProgressDialog(LoginActivity.this);
//		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		m_pDialog.setMessage("��������Ӧ�С�����");
//	    m_pDialog.show();
//	}

	/**
	 * ���ؽ������Ի���
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
