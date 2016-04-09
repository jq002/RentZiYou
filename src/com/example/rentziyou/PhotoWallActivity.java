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
 * ѡ����Ƭҳ��
 * Created by hanj on 14-10-15.
 */
public class PhotoWallActivity extends Activity {
    private TextView titleTV;

    private ArrayList<String> list;
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;

    /**
     * ��ǰ�ļ���·��
     */
    private String currentFolder = null;
    /**
     * ��ǰչʾ���Ƿ�Ϊ�����Ƭ
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

        //ѡ����Ƭ���
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ѡ��ͼƬ���,�ص���ʼҳ��
                ArrayList<String> paths = getSelectImagePaths();

//                Intent intent = new Intent(PhotoWallActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("code", paths != null ? 100 : 101);
//                intent.putStringArrayListExtra("paths", paths);
//                startActivity(intent);
                DemoApplication app1 = (DemoApplication)getApplication();  
            	String flageposition =(String)app1.get("flageposition");
//            	Toast.makeText(getApplicationContext(),"qqqqqq  "+ paths,  
//                        Toast.LENGTH_SHORT).show(); 
            	if(flageposition.equals("position")){
                Intent intent = new Intent(PhotoWallActivity.this, AddChargePositionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("code", paths != null ? 100 : 101);
                intent.putStringArrayListExtra("paths", paths);
                startActivity(intent);
            	}else{
            		Intent intent = new Intent(PhotoWallActivity.this, AddChargeStationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("code", paths != null ? 100 : 101);
                    intent.putStringArrayListExtra("paths", paths);
                    startActivity(intent);
            		
            	}
            }
        });

        //������أ��ص�ѡ�����ҳ��
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
            }
        });
    }

    /**
     * ��һ����ת�����ҳ��ʱ������������Ƭ��Ϣ
     */
    private boolean firstIn = true;

    /**
     * �������ʱ����ת�����ҳ��
     */
    private void backAction() {
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //���ݡ������Ƭ��������Ϣ
        if (firstIn) {
            if (list != null && list.size() > 0) {
                intent.putExtra("latest_count", list.size());
                intent.putExtra("latest_first_img", list.get(0));
            }
            firstIn = false;
        }

        startActivity(intent);
        //����
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    //��д���ؼ�
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
     * ����ͼƬ�����ļ���·����ˢ��ҳ��
     */
    private void updateView(int code, String folderPath) {
        list.clear();
        adapter.clearSelectionMap();
        adapter.notifyDataSetChanged();

        if (code == 100) {   //ĳ�����
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            String folderName = folderPath.substring(lastSeparator + 1);
            titleTV.setText(folderName);
            list.addAll(getAllImagePathsByFolder(folderPath));
        } else if (code == 200) {  //�����Ƭ
            titleTV.setText(R.string.latest_image);
            list.addAll(getLatestImagePaths(100));
        }

        adapter.notifyDataSetChanged();
        if (list.size() > 0) {
            //����������
            mPhotoWall.smoothScrollToPosition(0);
        }
    }


    /**
     * ��ȡָ��·���µ�����ͼƬ�ļ���
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
     * ʹ��ContentProvider��ȡSD�����ͼƬ��
     */
    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // ֻ��ѯjpg��png��ͼƬ,�������޸�����
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = null;
        if (cursor != null) {
            //�����µ�ͼƬ��ʼ��ȡ.
            //��cursor��û������ʱ��cursor.moveToLast()������false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // ��ȡͼƬ��·��
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);
//                    Log.v("path",path);

                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    //��ȡ��ѡ���ͼƬ·��
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

    //�����ҳ����ת����ҳ
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //����
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);

        int code = intent.getIntExtra("code", -1);
        if (code == 100) {
            //ĳ�����
            String folderPath = intent.getStringExtra("folderPath");
            if (isLatest || (folderPath != null && !folderPath.equals(currentFolder))) {
                currentFolder = folderPath;
                updateView(100, currentFolder);
                isLatest = false;
            }
        } else if (code == 200) {
            //�������Ƭ��
            if (!isLatest) {
                updateView(200, null);
                isLatest = true;
            }
        }
    }
}
