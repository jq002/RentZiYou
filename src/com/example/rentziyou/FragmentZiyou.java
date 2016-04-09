package com.example.rentziyou;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kobjects.base64.Base64;

import com.httpconnet.Packager;
import com.httpconnet.Parser;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FragmentZiyou extends Fragment{
	 ProgressDialog dialog;
	    private String information; 
		Packager packager = new Packager(); // ·â×°
		Parser parser=new Parser();
		MyAdapter adapter;
		List<Map<String, Object>> orderInfo= new ArrayList<Map<String, Object>>();
		 ListView listview;
		 private Button kaiguan;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tab01, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		    listview=(ListView) getActivity().findViewById(R.id.listview);
			adapter=new MyAdapter(getActivity());
			listview.setAdapter(adapter);
		
	}
	
	class MyAdapter extends BaseAdapter{
		LayoutInflater inflater;
		public MyAdapter(Context context) {
			// TODO Auto-generated constructor stub
			inflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=inflater.inflate(R.layout.order_item_rent, null);
//			Button kaiguan=(Button) convertView.findViewById(R.id.kaiguan);
			//kaiguan.setOnClickListener(new MyListener(position));
			TextView carAddress=(TextView) convertView.findViewById(R.id.carAddress);
			TextView car=(TextView) convertView.findViewById(R.id.car);
		
			TextView money=(TextView) convertView.findViewById(R.id.money);
			TextView time=(TextView) convertView.findViewById(R.id.time);
			
//			kaiguan.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//				}
//			 });
//			RelativeLayout re_position=(RelativeLayout) convertView.findViewById(R.id.re_xiang);
//			re_position.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					
//					
//				}
//			});

			
			return convertView;
		}
		
	}

}
