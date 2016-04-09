package com.example.rentziyou;

import com.jqjava.lesson5.DemoApplication;
import com.tools.SysApplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LookmainActivity extends FragmentActivity  {

	 private Fragment[] fragments;
	 private FragmentFind findfragment;
	 private FragmentProfile profilefragment;
	 private FragmentZiyou ziyoufragment;
	 //
	 private ImageView[] imagebuttons;
	    private TextView[] textviews;
	    private int index;
	    // 当前fragment的index
	    private int currentTabIndex=1;
	    private static  SharedPreferences sharedPreferences;
		private Context otherContext; 
		private String tel;
		private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lookmain);
		//取得ActionBar对象
        ActionBar actionBar =getActionBar();
        //调用hide方法，隐藏actionbar
        actionBar.hide();
        //调用show方法，展示actionbar
        //actionBar.show();
        
      //广播，退出所有
      		SysApplication.getInstance().addActivity(this); 
      		try {
    			otherContext = this.createPackageContext("com.example.rentziyou", Context.CONTEXT_IGNORE_SECURITY);
    			sharedPreferences = otherContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    			tel=sharedPreferences.getString("tel","");
    			id=sharedPreferences.getString("id","");
    			 DemoApplication app = (DemoApplication)getApplication();  
    				app.put("tel", tel);
    				app.put("id", id);
    		} catch (NameNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		findfragment = new FragmentFind();
        profilefragment = new FragmentProfile();
        ziyoufragment=new FragmentZiyou();
        fragments = new Fragment[] {
        		ziyoufragment,findfragment, profilefragment };
        
        imagebuttons = new ImageView[3];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_ziyou);

        imagebuttons[1] = (ImageView) findViewById(R.id.ib_find);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_profile);

        imagebuttons[1].setSelected(true);
        textviews = new TextView[3];
        textviews[0] = (TextView) findViewById(R.id.tv_ziyou);

        textviews[1] = (TextView) findViewById(R.id.tv_find);
        textviews[2] = (TextView) findViewById(R.id.tv_profile);
        textviews[1].setTextColor(0xFF2828FF);
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
         .add(R.id.fragment_container, ziyoufragment)
        .add(R.id.fragment_container, findfragment)
                .add(R.id.fragment_container, profilefragment)               
                .show(findfragment).hide(profilefragment).hide(ziyoufragment).commit();
	}

	 public void onTabClicked(View view) {
	        switch (view.getId()) {
	        case R.id.re_ziyou:
	            index = 0;
	            break;

	        case R.id.re_find:
	            index = 1;
	            break;
	        case R.id.re_profile:
	            index = 2;
	            break;

	        }

	        if (currentTabIndex != index) {
	            FragmentTransaction trx = getSupportFragmentManager()
	                    .beginTransaction();
	            trx.hide(fragments[currentTabIndex]);
	            if (!fragments[index].isAdded()) {
	                trx.add(R.id.fragment_container, fragments[index]);
	            }
	            trx.show(fragments[index]).commit();
	        }
	        imagebuttons[currentTabIndex].setSelected(false);
	        // 把当前tab设为选中状态
	        imagebuttons[index].setSelected(true);
	        textviews[currentTabIndex].setTextColor(0xFF999999);
	        textviews[index].setTextColor(0xFF2828FF);
	        currentTabIndex = index;
	    }


	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lookmain, menu);
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
