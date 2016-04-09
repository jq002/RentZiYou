package com.example.rentziyou;

import static com.chinark.apppickimagev3.utils.Utility.isImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import com.chinark.apppickimagev3.adapter.PhotoAlbumLVAdapter;
import com.chinark.apppickimagev3.model.PhotoAlbumLVItem;
import com.chinark.apppickimagev3.utils.Utility;
import com.tools.SysApplication;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PhotoAlbumActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_album);
		//�㲥���˳�����
				SysApplication.getInstance().addActivity(this); 
		  if (!Utility.isSDcardOK()) {
	            Utility.showToast(this, "SD�������á�");
	            return;
	        }

	        Intent t = getIntent();
	        if (!t.hasExtra("latest_count")) {
	            return;
	        }

	        TextView titleTV = (TextView) findViewById(R.id.topbar_title_tv);
	        titleTV.setText(R.string.select_album);

	        Button cancelBtn = (Button) findViewById(R.id.topbar_right_btn);
	        cancelBtn.setText(R.string.main_cancel);
	        cancelBtn.setVisibility(View.VISIBLE);

	        ListView listView = (ListView) findViewById(R.id.select_img_listView);

//	        //��һ�ַ�ʽ��ʹ��file
//	        File rootFile = new File(Utility.getSDcardRoot());
//	        //����/mnt/sdcard/DCIM/.thumbnailsĿ¼
//	        String ignorePath = rootFile + File.separator + "DCIM" + File.separator + ".thumbnails";
//	        getImagePathsByFile(rootFile, ignorePath);

	        //�ڶ��ַ�ʽ��ʹ��ContentProvider����Ч�ʸ��ߣ�
	        final ArrayList<PhotoAlbumLVItem> list = new ArrayList<PhotoAlbumLVItem>();
	        //�������Ƭ��
	        list.add(new PhotoAlbumLVItem(getResources().getString(R.string.latest_image),
	                t.getIntExtra("latest_count", -1), t.getStringExtra("latest_first_img")));
	        //���
	        list.addAll(getImagePathsByContentProvider());

	        PhotoAlbumLVAdapter adapter = new PhotoAlbumLVAdapter(this, list);
	        listView.setAdapter(adapter);

	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                Intent intent = new Intent(PhotoAlbumActivity.this, PhotoWallActivity.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

	                //��һ��Ϊ�������Ƭ��
	                if (position == 0) {
	                    intent.putExtra("code", 200);
	                } else {
	                    intent.putExtra("code", 100);
	                    intent.putExtra("folderPath", list.get(position).getPathName());
	                }
	                startActivity(intent);
	            }
	        });

	        cancelBtn.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                //ȡ�����ص���ҳ��
	                backAction();
	            }
	        });
	    }

	    /**
	     * �������ʱ���ص����ҳ��
	     */
	    private void backAction() {
	        Intent intent = new Intent(this, MainActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
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
	     * ��ȡĿ¼��ͼƬ�ĸ�����
	     */
	    private int getImageCount(File folder) {
	        int count = 0;
	        File[] files = folder.listFiles();
	        for (File file : files) {
	            if (isImage(file.getName())) {
	                count++;
	            }
	        }

	        return count;
	    }

	    /**
	     * ��ȡĿ¼�����µ�һ��ͼƬ�ľ���·����
	     */
	    private String getFirstImagePath(File folder) {
	        File[] files = folder.listFiles();
	        for (int i = files.length - 1; i >= 0; i--) {
	            File file = files[i];
	            if (isImage(file.getName())) {
	                return file.getAbsolutePath();
	            }
	        }

	        return null;
	    }

	    /**
	     * ʹ��ContentProvider��ȡSD������ͼƬ��
	     */
	    private ArrayList<PhotoAlbumLVItem> getImagePathsByContentProvider() {
	        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
	        String key_DATA = MediaStore.Images.Media.DATA;

	        ContentResolver mContentResolver = getContentResolver();

	        // ֻ��ѯjpg��png��ͼƬ
	        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
	                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
	                new String[]{"image/jpg", "image/jpeg", "image/png"},
	                MediaStore.Images.Media.DATE_MODIFIED);

	        ArrayList<PhotoAlbumLVItem> list = null;
	        if (cursor != null) {
	            if (cursor.moveToLast()) {
	                //·�����棬��ֹ���ɨ��ͬһĿ¼
	                HashSet<String> cachePath = new HashSet<String>();
	                list = new ArrayList<PhotoAlbumLVItem>();

	                while (true) {
	                    // ��ȡͼƬ��·��
	                    String imagePath = cursor.getString(0);

	                    File parentFile = new File(imagePath).getParentFile();
	                    String parentPath = parentFile.getAbsolutePath();

	                    //��ɨ���ظ�·��
	                    if (!cachePath.contains(parentPath)) {
	                        list.add(new PhotoAlbumLVItem(parentPath, getImageCount(parentFile),
	                                getFirstImagePath(parentFile)));
	                        cachePath.add(parentPath);
	                    }

	                    if (!cursor.moveToPrevious()) {
	                        break;
	                    }
	                }
	            }

	            cursor.close();
	        }

	        return list;
	    }

	    @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);

	        //����
	        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_album, menu);
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
