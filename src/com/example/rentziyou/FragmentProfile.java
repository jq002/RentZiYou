package com.example.rentziyou;

import com.amap.api.maps.MapView;
import com.example.rentziyou.MyUserInfoActivity.MyListener;
import com.jqjava.lesson5.DemoApplication;
import com.jqjava.lesson5.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentProfile extends Fragment {
	
	private String id;
	private String tel;
	
	    private RelativeLayout re_myinfo;
	    private RelativeLayout re_chongdian;
	    private RelativeLayout re_xinyong;
	    private RelativeLayout re_money_bag;
	    private RelativeLayout re_setting;
	    
	    private TextView tv_name;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		DemoApplication app = (DemoApplication)getActivity().getApplication();  		
		id=(String)app.get("id");
		tel=(String)app.get("tel");
		//ToastUtil.show(getActivity(), "DemoApplication"+tel);
			
		init();

		}
	private void init(){
		 re_myinfo = (RelativeLayout) getView().findViewById(R.id.re_myinfo);
		 re_chongdian=(RelativeLayout) getView().findViewById(R.id.re_chongdian);
		 re_xinyong=(RelativeLayout) getView().findViewById(R.id.re_xinyong);
		 re_money_bag=(RelativeLayout) getView().findViewById(R.id.re_money_bag);
		 re_setting=(RelativeLayout) getView().findViewById(R.id.re_setting);
		 
		 re_myinfo.setOnClickListener(new MyListener());
		 re_chongdian.setOnClickListener(new MyListener());
		 re_xinyong.setOnClickListener(new MyListener());
		 re_money_bag.setOnClickListener(new MyListener());
		 re_setting.setOnClickListener(new MyListener());
		 
		 tv_name=(TextView) getView().findViewById(R.id.tv_name);
		 tv_name.setText(tel);
	}


	 class MyListener implements OnClickListener {

	        @Override
	        public void onClick(View v) {
	            switch (v.getId()) {
	            case R.id.re_myinfo:
	            	startActivity(new Intent(getActivity(),
	                        MyUserInfoActivity.class));
	                break;
	            case R.id.re_chongdian:
	            	startActivity(new Intent(getActivity(),
	                        MyChargingPosActivity.class));			
	                break;
	            case R.id.re_xinyong:
	            	startActivity(new Intent(getActivity(),
	                        MyCreditScoreActivity.class));		
	                break;
	            case R.id.re_money_bag:
	            	startActivity(new Intent(getActivity(),
	                        MyMoneyBagActivity.class));		
	                break;
	            case R.id.re_setting:
	            	startActivity(new Intent(getActivity(),
	                        MySettingActivity.class));		
	                break;

	            }
	        }

	    }
}
