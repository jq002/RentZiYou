package com.example.rentziyou;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.tools.SysApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OrderMainActivity extends Activity {
	  //声明变量
    private MapView mapView;
    private AMap aMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_main);
		//广播，退出所有
				SysApplication.getInstance().addActivity(this); 
		mapView = (MapView) findViewById(R.id.map);
	    mapView.onCreate(savedInstanceState);// 必须要写
	    aMap = mapView.getMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_main, menu);
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
