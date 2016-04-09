package com.example.rentziyou;

import java.io.File;
import java.util.ArrayList;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinark.apppickimagev3.adapter.PhotoWallAdapter;
import com.jqjava.lesson5.DemoApplication;

import static com.chinark.apppickimagev3.utils.Utility.isImage;

/**
 * 选择照片页面
 * Created by hanj on 14-10-15.
 */
public class UploatStation2Activity extends Activity {
    private TextView titleTV;

    private ArrayList<String> list;
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;

    /**
     * 当前文件夹路径
     */
    private String currentFolder = null;
    /**
     * 当前展示的是否为最近照片
     */
    private boolean isLatest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);

        titleTV = (TextView) findViewById(R.id.topbar_title_tv);
        titleTV.setText(R.string.latest_image);

        Button backBtn = (Button) findViewById(R.id.topbar_left_btn);
        Button confirmBtn = (Button) findViewById(R.id.topbar_right_btn);
        backBtn.setText(R.string.photo_album);
        backBtn.setVisibility(View.VISIBLE);
        confirmBtn.setText(R.string.main_confirm);
        confirmBtn.setVisibility(View.VISIBLE);

        mPhotoWall = (GridView) findViewById(R.id.photo_wall_grid);
        list = getLatestImagePaths(100);
        adapter = new PhotoWallAdapter(this, list);
        mPhotoWall.setAdapter(adapter);

        //选择照片完成
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片完成,回到起始页面
                ArrayList<String> paths = getSelectImagePaths();

//                Intent intent = new Intent(PhotoWallActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("code", paths != null ? 100 : 101);
//                intent.putStringArrayListExtra("paths", paths);
//                startActivity(intent);
                DemoApplication app1 = (DemoApplication)getApplication();  
            	String flageposition =(String)app1.get("flageposition");
            	Toast.makeText(getApplicationContext(),"qqqqqq  "+ paths,  
                        Toast.LENGTH_SHORT).show(); 
            	if(flageposition.equals("position")){
                Intent intent = new Intent(UploatStation2Activity.this, AddChargePositionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("code", paths != null ? 100 : 101);
                intent.putStringArrayListExtra("paths", paths);
                startActivity(intent);
            	}else{
            		Intent intent = new Intent(UploatStation2Activity.this, AddChargeStationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("code", paths != null ? 100 : 101);
                    intent.putStringArrayListExtra("paths", paths);
                    startActivity(intent);
            		
            	}
            }
        });

        //点击返回，回到选择相册页面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
            }
        });
    }

    /**
     * 第一次跳转至相册页面时，传递最新照片信息
     */
    private boolean firstIn = true;

    /**
     * 点击返回时，跳转至相册页面
     */
    private void backAction() {
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //传递“最近照片”分类信息
        if (firstIn) {
            if (list != null && list.size() > 0) {
                intent.putExtra("latest_count", list.size());
                intent.putExtra("latest_first_img", list.get(0));
            }
            firstIn = false;
        }

        startActivity(intent);
        //动画
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 根据图片所属文件夹路径，刷新页面
     */
    private void updateView(int code, String folderPath) {
        list.clear();
        adapter.clearSelectionMap();
        adapter.notifyDataSetChanged();

        if (code == 100) {   //某个相册
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            String folderName = folderPath.substring(lastSeparator + 1);
            titleTV.setText(folderName);
            list.addAll(getAllImagePathsByFolder(folderPath));
        } else if (code == 200) {  //最近照片
            titleTV.setText(R.string.latest_image);
            list.addAll(getLatestImagePaths(100));
        }

        adapter.notifyDataSetChanged();
        if (list.size() > 0) {
            //滚动至顶部
            mPhotoWall.smoothScrollToPosition(0);
        }
    }


    /**
     * 获取指定路径下的所有图片文件。
     */
    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }
    
    /**
     * 使用ContentProvider读取SD卡最近图片。
     */
    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);
                    Log.v("path",path);

                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    //获取已选择的图片路径
    private ArrayList<String> getSelectImagePaths() {
        SparseBooleanArray map = adapter.getSelectionMap();
        if (map.size() == 0) {
            return null;
        }

        ArrayList<String> selectedImageList = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            if (map.get(i)) {
                selectedImageList.add(list.get(i));
            }
        }

        return selectedImageList;
    }

    //从相册页面跳转至此页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //动画
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);

        int code = intent.getIntExtra("code", -1);
        if (code == 100) {
            //某个相册
            String folderPath = intent.getStringExtra("folderPath");
            if (isLatest || (folderPath != null && !folderPath.equals(currentFolder))) {
                currentFolder = folderPath;
                updateView(100, currentFolder);
                isLatest = false;
            }
        } else if (code == 200) {
            //“最近照片”
            if (!isLatest) {
                updateView(200, null);
                isLatest = true;
            }
        }
    }
}

//package com.example.rentziyou;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.kobjects.base64.Base64;
//import com.chinark.apppickimagev3.adapter.MainGVAdapter;
//import com.chinark.apppickimagev3.utils.ScreenUtils;
//import com.chinark.apppickimagev3.utils.Utility;
//import com.jqjava.lesson5.DemoApplication;
//import com.jqjava.lesson5.MyDialog;
//import com.jqjava.lesson5.ToastUtil;
//import com.tools.SysApplication;
//
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//public class UploatStation2Activity extends Activity {
//	
//	private Button button;
//	ProgressDialog m_pDialog;
//	private MyDialog myDialog;
//	private ProgressDialog progDialog = null;//进度框
//	private int longClickPosition;
//
//		
//	    private   String image1=" ";
//	 private MainGVAdapter adapter;
//	    private ArrayList<String> imagePathList;
//	   
////	    private String tel;
////		private String flage;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_uploat_station2);
//		//广播，退出所有
//				SysApplication.getInstance().addActivity(this); 
//		 Button selectImgBtn = (Button) findViewById(R.id.select_img);
//		 selectImgBtn.setOnClickListener(new View.OnClickListener() {
//	            @Override
//	            public void onClick(View v) {
//	                //跳转至最终的选择图片页面
//	                Intent intent = new Intent(UploatStation2Activity.this, PhotoWallActivity.class);
//	                
//	             
//	                startActivity(intent);
//	            }
//	        });
//
//		
//		
//	
//	}
//	/**
//	 * 显示进度条对话框
//	 */
//	public void showDialog() {
//		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progDialog.setIndeterminate(false);
//		progDialog.setCancelable(true);
//		progDialog.setMessage("正在获取地址");
//		progDialog.show();
//	}
//
//	/**
//	 * 隐藏进度条对话框
//	 */
//	public void dismissDialog() {
//		if (progDialog != null) {
//			progDialog.dismiss();
//		}
//	}
//
//	public  String createJsonString(String key, Object value) {
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put(key, value);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return jsonObject.toString();
//	}
//
//	
//	public String testPhoto(int i){
//		
//		String uploadBuffer="";
//		   try{  
//		 FileInputStream fis = new FileInputStream(imagePathList.get(i));  
//         ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//         byte[] buffer = new byte[1024];  
//         int count = 0;  
//         while((count = fis.read(buffer)) >= 0){  
//             baos.write(buffer, 0, count);  
//         }  
//          uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //进行Base64编码  
//        
//		   }catch(Exception e){  
//	            e.printStackTrace();  
//	        }  
//		   return uploadBuffer;
//	}
//	
//    @Override
//	    protected void onNewIntent(Intent intent) {
//	        super.onNewIntent(intent);
//
////        	Toast.makeText(getApplicationContext(), " a",  
////                       Toast.LENGTH_SHORT).show(); 
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
//	        Log.v("paths","paths"+paths.get(0));
//	        //添加，去重
////	        boolean hasUpdate = false;
////	        for (String path : paths) {
////	            if (!imagePathList.contains(path)) {
////	                //最多9张
////	                if (imagePathList.size() == 1) {
////	                    Utility.showToast(this, "最多可添加9张图片。");
////	                    break;
////	                }
////
////	                imagePathList.add(path);
////	                hasUpdate = true;
////	            }
////	        }
////
////	        if (hasUpdate) {
////	            adapter.notifyDataSetChanged();
////	        }
//	       ImageView img = (ImageView) findViewById(R.id.image);
//	        if(true){
//	   		 
//			       Bitmap bm = BitmapFactory.decodeFile(paths.get(0));
//			       img.setImageBitmap(bm);
//			}
//	        
//	       
//	        
//	    }
//	public void rightOnclick() {
//		// TODO Auto-generated method stub
//		myDialog.dismiss();
//	
//		imagePathList.remove(longClickPosition);
//		adapter.notifyDataSetChanged();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.uploat_station2, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}
