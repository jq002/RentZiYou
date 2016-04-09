package com.example.rentziyou;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.help.Inputtips.InputtipsListener;
//import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.cloud.CloudSearch.OnCloudSearchListener;
import com.amap.api.services.cloud.CloudSearch.SearchBound;
import com.entity.Para;


import com.jqjava.lesson5.AMapUtil;
import com.jqjava.lesson5.CloudOverlay;
import com.jqjava.lesson5.ToastUtil;
import com.tools.SysApplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FragmentFind extends Fragment implements LocationSource, OnMapClickListener, OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, AMapLocationListener, OnCheckedChangeListener, OnMarkerDragListener, OnGeocodeSearchListener, TextWatcher, OnPoiSearchListener, OnCloudSearchListener 
{
	private ProgressDialog progDialog = null;
	 //声明变量
    private MapView mapView;
    private AMap aMap;
    
    //声明AMapLocationClient类对象
    //public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    //public AMapLocationListener mLocationListener = new AMapLocationListener();
    private LocationManagerProxy mAMapLocationManager;
    private OnLocationChangedListener mListener;
    
    public  double geoLat2;   //定位获得纬度
    public  double geoLng2;  //定位获得经度
    
    private RadioGroup mGPSModeGroup1;//改变可视化区域
	private RadioGroup mGPSModeGroup2;
	private LatLng center;
	private LatLng latlng=new LatLng(24.573877,118.091873);//Mark标记用到，万达
	
	private ImageButton imagebutton;
	//可移动标记
	private Marker marker;//可移动标记
	private Boolean markremove=true;
	private LatLonPoint latLonPoint;
	private double geolatit;
	private double geolongtit;
	private GeocodeSearch geocoderSearch;//逆地理编码
	private String address;
	private Boolean flagmarker=false;//是否是可移动标记点的，逆地理编码
	//点击地图后
	private double geolatit1;
	private double geolongtit1;
	private LatLng latlng1;
	private LatLonPoint latLonPoint1;
	private Boolean flagmarkermap=false;//是否是点击地图的，逆地理编码
	private Marker marker1;//点击地图的标记
	private Boolean mapremove=true;//清除红色标记
	private String chargeID;//充电站ID
	//搜索button
	private ImageButton searchButton;//搜索按钮
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiSearch.Query query;// Poi查询条件类
    private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数

	private PoiSearch poiSearch;// POI搜索
	private PoiItem poiItem;
	
	//添加已经存在的充电站
	private Boolean flagmarkmove=false;
    
	//云图搜索
	private CloudSearch mCloudSearch;
	private LatLonPoint mCenterPoint = new LatLonPoint(24.578428,118.093482); // 周边搜索中心点
	
	//private String mTableID="56ac95bf305a2a328875aacb";
	private String mTableID="56d6a0e5305a2a3288a876a8";
	private CloudSearch.Query mQuery;
	private List<CloudItem> mCloudItems;
	private CloudOverlay mPoiCloudOverlay;
	private ArrayList<CloudItem> items = new ArrayList<CloudItem>();
	
	private LatLonPoint mPoint1 = new LatLonPoint(39.941711, 116.382248);
	private LatLonPoint mPoint2 = new LatLonPoint(39.884882, 116.359566);
	private LatLonPoint mPoint3 = new LatLonPoint(39.878120, 116.437630);
	private LatLonPoint mPoint4 = new LatLonPoint(39.941711, 116.382248);
	
	//测试逆地理编码数据
	private String build;
	private String neighborhood;
	
	//添加充电站
	private String parkaddress;
	
	//添加充电位
	private String chargename;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.activity_order_main, container, false);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("Rentactivity","erroe111111111.....");

		 mapView = (MapView)getView().findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
	    aMap = mapView.getMap();
	    Log.v("Rentactivity","erroe.....");
	    //initLocation();
	    setUpMap();
	    init();
	    addMarkers(latlng,"集美万达");
	    LatLng latlng3=new LatLng(24.579867, 118.095232);
	    addMarkers(latlng3,"嘉庚图书馆");
	    LatLng latlng4=new LatLng(24.577798, 118.099824);
	    addMarkers(latlng4,"敬贤公园");
//	    aMap.addMarker(new MarkerOptions()
//	     .position(latlng).title("移动此标记，确定站点位置").draggable(true)
//	     .icon(BitmapDescriptorFactory
//					.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	    Log.v("Rentactivity","erroe11.....");
	    //addMarkersToMap();
	    
//	    initCloudKey();
//	    doSearchCloud();
		}
	
	 /**
	 * 在地图上添加可移动marker
	 */
	
	private void addMarkersToMap() {
//		 Marker markerd;//可移动标记
		center= aMap.getCameraPosition().target;
		if(!markremove){
			marker.remove();
		}
		
		 marker =aMap.addMarker(new MarkerOptions()
     .position(center).title("移动此标记，确定站点位置").draggable(true).visible(true)
     .icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		 marker.showInfoWindow();

		Log.v("Rentactivity","erroe2222.....");
		if(markremove){
			markremove=false;
		}
		
	}
	 /**
	 * 在地图上添加marker，已经存在的车场
	 */
	private void addMarkers(LatLng latlng,String title){
		//Object  obj2=parklotid;
		
		aMap.addMarker(new MarkerOptions()
     .position(latlng).title(title)
     .icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		
	}
	public  void init(){
		
		mGPSModeGroup1 = (RadioGroup) getView().findViewById(R.id.gps_radio_group);
		mGPSModeGroup1.setOnCheckedChangeListener(this);
		mGPSModeGroup2 = (RadioGroup) getView().findViewById(R.id.gps_angle_group);
		mGPSModeGroup2.setOnCheckedChangeListener(this);
		
		imagebutton=(ImageButton) getView().findViewById(R.id.add);
		imagebutton.setOnClickListener(new Button.OnClickListener(){
			
			public void onClick(View arg0){
				flagmarkmove=true;
				//aMap.clear();//添加移动标记
				
				addMarkersToMap();
		
			}
		});
		
		    searchText = (AutoCompleteTextView) getView().findViewById(R.id.keyWord);
			searchText.addTextChangedListener(this);// 添加文本输入框监听事件
			searchButton=(ImageButton) getView().findViewById(R.id.search);
			/**
			 * 点击搜索按钮
			 */			
			searchButton.setOnClickListener(new Button.OnClickListener(){
				
				public void onClick(View arg0){
					
					keyWord = AMapUtil.checkEditText(searchText);
					if ("".equals(keyWord)) {
						ToastUtil.show(getActivity(), "请输入搜索关键字");
						return;
					} else {
						//showDialog() ;
						//doSearchQuery();
						//ToastUtil.show(getActivity(), "keyWord"+keyWord);
						getLatlon(keyWord);
	                }
				}
			});

	}

	public void doSearchCloud(){
		// 设置中心点及检索范围
				SearchBound bound = new SearchBound(new LatLonPoint(
				            mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 2000);
				//设置查询条件 mTableID是将数据存储到数据管理台后获得。
				try {
					mQuery = new CloudSearch.Query(mTableID, "", bound);

		            mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
		            Log.v("FragmentFind","key2244555.....initCloud");
				} catch (AMapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public void initCloud(){
		Log.v("FragmentFind","erroe2222.....initCloud");
		mCloudSearch = new CloudSearch(getActivity());// 初始化查询类
		mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
		// 设置中心点及检索范围
		SearchBound bound = new SearchBound(new LatLonPoint(
		            mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 4000);
		//设置查询条件 mTableID是将数据存储到数据管理台后获得。
		try {
			mQuery = new CloudSearch.Query(mTableID, "公园", bound);

            mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
            Log.v("FragmentFind","erroe33333.....initCloud");
		} catch (AMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 /**
     * 初始化定位
     */
    private void setUpMap() {
   	 
     
        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setOnMapClickListener(this); //设置点击地图监听事件          
  	     aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
       
        //设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        //aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        
        geocoderSearch = new GeocodeSearch(getActivity());
		geocoderSearch.setOnGeocodeSearchListener(this);//地理，逆地理编码
		//云搜索
		mCloudSearch = new CloudSearch(getActivity());// 初始化查询类
		mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
		progDialog = new ProgressDialog(getActivity());
    }
//aMap.setLocationSource(this);// 设置定位监听------------------------------------

	/**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法     
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork,-1, 10, this);
        }
    }
 
 
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }
//aMap.setOnMapClickListener(this); //设置点击地图监听事件    -------------------------------
    /**
     * 点击地图后，获取经纬度，进行逆编码
     */
	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		//tap_text.setText("您选择添加停车场的位置是："+point);
		if(!mapremove){//清除多余的红色标记
			marker1.remove();
		}
		
		geolatit1=point.latitude;
		geolongtit1=point.longitude;
				
		//aMap.clear();//点击地图后
    	Log.v("map......",geolatit1+";"+geolongtit1);
		 latlng1= new LatLng(geolatit1, geolongtit1);//点击搜索时的内容
		marker1=aMap.addMarker(new MarkerOptions()
        .position(latlng1)
        .icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .perspective(true));
		marker1.hideInfoWindow();
		if(mapremove){
			mapremove=false;
		}
		
		flagmarkermap=true;
		//Toast.makeText(Look2Activity.this, geoLat+" "+geoLng, Toast.LENGTH_SHORT).show();

		//getAddress(Point);
		latLonPoint1 = new LatLonPoint(geolatit1,geolongtit1);
		Log.v("Rentactivity","erroe2222.....getAddress");
		getAddress(latLonPoint1);
		
	
		
	}
//aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器------------------------------------------------------------
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if(marker.isDraggable()){
			flagmarkmove=true;
			
		}
		chargeID=(String) marker.getObject();
		Log.v("onmarker","....chargeID "+chargeID);
		return false;
	}
//aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器--------------------------------------------------------
	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
//aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式-----------------------
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		  View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
			 
		    TextView title = (TextView) infoWindow.findViewById(R.id.marker_title);
//		    TextView snippet = (TextView) infoWindow.findViewById(R.id.marker_snippet);
		    final Button showLatLng = (Button) infoWindow.findViewById(R.id.marker_show_latlng);
		 
		    String titleString = marker.getTitle();
		    chargename=titleString;
		    if (TextUtils.isEmpty(titleString)) {
		        titleString = "no title";
		    }
		 
//		    String snippetString = marker.getSnippet();
//		    if (TextUtils.isEmpty(snippetString)) {
//		        snippetString = "no snippet";
//		    }
		 
		    title.setText(titleString);
//		    snippet.setText(snippetString);
		    if(flagmarkmove){
        		showLatLng.setText("添加充电站");
        		flagmarkmove=false;
        		
        	}
	 
		    showLatLng.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	String flagstation=(String) showLatLng.getText();
		        	Log.v("Oninfowindow","...."+flagstation);
		        	if(flagstation.equals("添加充电站")){
		        		
		        		String sgeolatit=String.valueOf(geolatit1);
						String sgeolongit=String.valueOf(geolongtit1);
						
						Log.v("sgeolatit..",sgeolatit+";"+sgeolongit);
						//Log.v("Oninfowindow","...."+geolongtit1+parkaddress);
						
						Intent intent=new Intent(getActivity() ,AddChargeStationActivity.class);
						intent.putExtra("latitude", sgeolatit);
						intent.putExtra("longtitude",sgeolongit);
						intent.putExtra("parkAddress", parkaddress);
						startActivity(intent);
		        		
		        	}
		        	if(flagstation.equals("添加充电位")){
		        		
		        		Intent intent=new Intent(getActivity() ,AddChargePositionActivity.class);
						intent.putExtra("chargeID", chargeID);
						intent.putExtra("chargename", chargename);

						startActivity(intent);
		        		
		        	}
		        	
		        }
		    });
		 
		    return infoWindow;
	}
//mAMapLocationManager-------------------------------------------------------------
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	 /**
	    * 定位成功后回调函数
	    */
		@Override
	   public void onLocationChanged(AMapLocation amapLocation) {
	       if (mListener != null && amapLocation != null) {
	           if (amapLocation.getAMapException().getErrorCode() == 0) {
	               //mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
	               
	               //获取位置信息
	              geoLat2 = amapLocation.getLatitude();
	              geoLng2 = amapLocation.getLongitude(); 
	              
	              Toast.makeText(getActivity(), geoLat2+"      "+geoLng2, Toast.LENGTH_SHORT).show();
					
//	              latit=geoLat2;
//	              longit=geoLng2;
//	              la2=Double.toString(geoLat2);
//	 		     lo2=Double.toString(geoLng2);
//	 		     
//	 		    //从服务器获得json，修改后
//	 	        new Thread(){
//	 	        	public void run(){
//	 	        	String Web_results=" ";
////	 	        	if(flage==0){
////	 	        		//公共账号，搜索无主的车场
////	 	        		Web_results=SelectLocateWeb.SelectPerParking(la2, lo2);
////	 	        	}else{
//	 	        	Web_results=SelectLocateWeb.SelectParking(la2, lo2);
////		        	}
//	 	        	Message msg=new Message();
//	 	        	msg.obj=Web_results;
//	 	        	handler.sendMessage(msg);
//	 	        	}
//	 			
//	 	        }.start();
//	 	       dismissDialog();
	             
	 		   LatLng latlng4=new LatLng(geoLat2,geoLng2);
	 		   
				  changeCamera(
							CameraUpdateFactory.newCameraPosition(new CameraPosition(
									latlng4, 17, 0, 0)), null);
				  mCenterPoint = new LatLonPoint(geoLat2,geoLng2); // 周边搜索中心点
		          	
				  doSearchCloud();
				
		      

	           }
	       }
	   }
		/**
		 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
		 */
		private void changeCamera(CameraUpdate update, CancelableCallback callback) {
			//boolean animated = ((CompoundButton) findViewById(R.id.animate))
			//		.isChecked();
			//if (animated) {
			//	aMap.animateCamera(update, 1000, callback);
			//} else {
				aMap.moveCamera(update);
			//}
		}
//OnCheckedChangeListener----------------------------------------------------
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			
			center= aMap.getCameraPosition().target;
			switch (checkedId) {
			case R.id.small:
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								center, 17, 0, 0)), null);
				break;
			case R.id.mid:
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								center, 14, 0, 0)), null);
				break;
			case R.id.big:
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								center, 9, 0, 0)), null);
				break;
			case R.id.low:
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								center, 18, 30, 0)), null);
				break;
			case R.id.middle:
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								center, 18, 60, 0)), null);
				break;
			case R.id.high:
				changeCamera(
						CameraUpdateFactory.newCameraPosition(new CameraPosition(
								center, 18, 0, 0)), null);
				break;
			}
			
		}
//aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器---------------------------------------------------------
		/**
		 * 监听拖动marker时事件回调
		 */
		@Override
		public void onMarkerDrag(Marker marker) {
			marker.hideInfoWindow();
		
//			latLonPoint = new LatLonPoint(geolatit,geolongtit);
//			
//			getAddress(latLonPoint);
			
			String curDes = "拖动时当前位置:(Lan/Log)\n("
					+marker.getPosition().latitude+marker.getPosition().longitude+")";
			
			marker.setTitle(curDes);
			flagmarkmove=true;
			marker.showInfoWindow();
			//markerText.setText(curDes);
		}

		/**
		 * 监听拖动marker结束事件回调
		 */
		@Override
		public void onMarkerDragEnd(Marker marker) {
			//markerText.setText(marker.getTitle() + "停止拖动");
			marker.hideInfoWindow();
			geolatit=marker.getPosition().latitude;
			geolongtit=marker.getPosition().longitude;
            latLonPoint = new LatLonPoint(geolatit,geolongtit);
//            String curDes = "加载中。。。。。。。。";
//			
//			marker.setTitle(curDes);
//			marker.showInfoWindow();
			flagmarker=true;
			
			getAddress(latLonPoint);
			
		}

		/**
		 * 监听开始拖动marker事件回调
		 */
		@Override
		public void onMarkerDragStart(Marker marker) {
			//markerText.setText(marker.getTitle() + "开始拖动");
			marker.hideInfoWindow();
			String curDes = "可以开始拖动";
			
			marker.setTitle(curDes);
			flagmarkmove=true;
			marker.showInfoWindow();
			
            
		}
		//---------------------------------------------
		/**
		 * 逆地理编码
		 */
		public void getAddress(final LatLonPoint latLonPoint) {
			//showDialog();
			Log.v("Rentactivity","erroe2222.....getAddress22222222");
			RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
					GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
			geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
			
		}
//--------------------------------------------------------------------------
		/**
		 * 地理编码查询回调
		 */
		@Override
		public void onGeocodeSearched(GeocodeResult result, int rCode) {
			dismissDialog();
			if (rCode == 0) {
				if (result != null && result.getGeocodeAddressList() != null
						&& result.getGeocodeAddressList().size() > 0) {
					GeocodeAddress address = result.getGeocodeAddressList().get(0);
//					aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//							AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
//					geoMarker.setPosition(AMapUtil.convertToLatLng(address
//							.getLatLonPoint()));
//					addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//							+ address.getFormatAddress();
					mCenterPoint=address.getLatLonPoint();
					doSearchCloud();
					//ToastUtil.show(getActivity(), "addressName"+mCenterPoint);
				} else {
					ToastUtil.show(getActivity()," R.string.no_result");
				}
			} else if (rCode == 27) {
				ToastUtil.show(getActivity(), "R.string.error_network");
			} else if (rCode == 32) {
				ToastUtil.show(getActivity(), "R.string.error_key");
			} else {
				ToastUtil.show(getActivity(),
						"getString(R.string.error_other) + rCode");
			}
		}
		/**
		 * 响应逆地理编码
		 */
		public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
			// TODO Auto-generated method stub
			Log.v("Rentactivity","erroe2222.....onRegeocodeSearched");
			
			 if(rCode == 0){ 
			        //progDialog.dismiss(); 
			        if(result != null&&result.getRegeocodeAddress() != null 
			                 &&result.getRegeocodeAddress().getFormatAddress()!=null){ 
			        	address= result.getRegeocodeAddress().getFormatAddress(); 
			        	parkaddress=address;
			        	 build=result.getRegeocodeAddress().getBuilding();
			        	 neighborhood=result.getRegeocodeAddress().getNeighborhood();
			        
			        	Log.v("Rentactivity",address);
			        	Log.v("Rentactivity",build);
			        	Log.v("Rentactivity",neighborhood);
			        	//tap_text.setText("添加停车场位置："+parkaddress);
			        	if(flagmarker){
			        		Log.v("Rentactivity","erroe2222.....flagmarker");
			    			
			        		//marker.hideInfoWindow();
			    			
			    			
			    			marker.setTitle(address);
			    			flagmarkmove=true;
			    			marker.showInfoWindow();
			    			flagmarker=false;
			        	}
			        	if(flagmarkermap){
			        		
			        		Log.v("Rentactivity","erroe2222.....flagmarkermap");
			    			
			    			marker1.setTitle(address);
			    			flagmarkmove=true;
			    			marker1.showInfoWindow();
			    			flagmarkermap=false;
			        	}
			       
			        }else{ 
			            //ToastUtil.show(LookActivity.this, R.string.no_result); 
			        } 
			    }else{ 
			        //ToastUtil.show(LookActivity.this, R.string.error_network); 
			    } 
			  
		}
//searchText.addTextChangedListener(this);// 添加文本输入框监听事件------------------------------------------
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * 输入提示搜素
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
			String newText = s.toString().trim();
			Inputtips inputTips = new Inputtips(getActivity(),
					new InputtipsListener() {

						@Override
						public void onGetInputtips(List<Tip> tipList, int rCode) {
							if (rCode == 0) {// 正确返回
								List<String> listString = new ArrayList<String>();
								for (int i = 0; i < tipList.size(); i++) {
									listString.add(tipList.get(i).getName());
								}
								ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
										getActivity().getApplicationContext(),
										R.layout.route_inputs, listString);
								searchText.setAdapter(aAdapter);
								aAdapter.notifyDataSetChanged();
							}
						}
					});
			try {
				inputTips.requestInputtips(newText,"");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

			} catch (AMapException e) {
				e.printStackTrace();
			}
			
		}
	//------------------------------------------------------------
		/**
		 * 开始进行poi搜索
		 */
		protected void doSearchQuery() {
			//showProgressDialog();// 显示进度框
			currentPage = 0;
			query = new PoiSearch.Query(keyWord, "","");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
			query.setPageSize(15);// 设置每页最多返回多少条poiitem
			query.setPageNum(currentPage);// 设置查第一页

			poiSearch = new PoiSearch(getActivity(), query);
			poiSearch.setOnPoiSearchListener( this);
			poiSearch.searchPOIAsyn();
		}
//poiSearch.setOnPoiSearchListener( this);-----------------------------------------------------
		
//		public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}

		/**
		 * POI信息查询回调方法,包含intent跳转代码
		 */
		@Override
		public void onPoiSearched(PoiResult result, int rCode) {
			//dissmissProgressDialog();// 隐藏对话框

			LatLonPoint latLonPoint2 = null;
			String title;
			if (rCode == 0) {
				if (result != null && result.getQuery() != null) {// 搜索poi的结果
					if (result.getQuery().equals(query)) {// 是否是同一条
						poiResult = result;
						// 取得搜索到的poiitems有多少页
						List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
						List<SuggestionCity> suggestionCities = poiResult
								.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

						if (poiItems != null && poiItems.size() > 0) {
							
							poiItem =poiItems.get(0);
							latLonPoint2=poiItem.getLatLonPoint();
							title=poiItem.getTitle();
							aMap.clear();// search清理之前的图标

						} else if (suggestionCities != null
								&& suggestionCities.size() > 0) {
							//showSuggestCity(suggestionCities);
						} else {
							ToastUtil.show(getActivity(),
									"R.string.no_result");
						}
					}
				} else {
					ToastUtil.show(getActivity(),
							"R.string.no_result");
				}
			} else if (rCode == 27) {
				ToastUtil.show(getActivity(),
						"R.string.error_network");
			} else if (rCode == 32) {
				ToastUtil.show(getActivity(), "R.string.error_key");
			} else {
				ToastUtil.show(getActivity(), "getString(R.string.error_other) + rCode");
			}
			double latit=latLonPoint2.getLatitude();
			double longit=latLonPoint2.getLongitude();
//			la=Double.toString(latit);
//			lo=Double.toString(longit);

		
	        LatLng latlng= new LatLng(latit, longit);//点击搜索时的内容
		     changeCamera(
				CameraUpdateFactory.newCameraPosition(new CameraPosition(
								latlng, 17, 0, 0)), null);
//	        //从服务器获得json，修改后
//	        new Thread(){
//	        	public void run(){
//	        	String Web_results=" ";
////	        	if(flage==0){
////	        		//公共账号，搜索无主的车场
////	        		Web_results=SelectLocateWeb.SelectPerParking(la, lo);
////	        	}else{
//	        	Web_results=SelectLocateWeb.SelectParking(la, lo);
////	        	}
//	        	Message msg=new Message();
//	        	msg.obj=Web_results;
//	        	handler.sendMessage(msg);
//	        	}
//			
//	        }.start();
//	    	dismissDialog();


		}

		@Override
		public void onPoiItemSearched(PoiItem arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		//OnCloudSearchListener---------------------------------------------
		@Override
		public void onCloudItemDetailSearched(CloudItemDetail arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCloudSearched(CloudResult result, int rCode) {
			//dissmissProgressDialog();
			Log.v("FragmentFind","erroe444.....initCloud");
			
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(mCenterPoint.getLatitude(),
							mCenterPoint.getLongitude()), 17));

			if (rCode == 0) {
				if (result != null && result.getQuery() != null) {
					if (result.getQuery().equals(mQuery)) {
						mCloudItems = result.getClouds();
						Log.v("FragmentFind","erroe555.....initCloud");

						if (mCloudItems != null && mCloudItems.size() > 0) {
							aMap.clear();
							Log.v("FragmentFind","erroe666.....initCloud");
							mPoiCloudOverlay = new CloudOverlay(aMap, mCloudItems);
							mPoiCloudOverlay.removeFromMap();
							mPoiCloudOverlay.addToMap();//添加marker标记
							// mPoiCloudOverlay.zoomToSpan();
							for (CloudItem item : mCloudItems) {
								items.add(item);
								Log.d("Lookmainactivity", "_id " + item.getID());
								Log.d("Lookmainactivity", "_location "
										+ item.getLatLonPoint().toString());
								Log.d("Lookmainactivity", "_name " + item.getTitle());
								Log.d("Lookmainactivity", "_address " + item.getSnippet());
								Log.d("Lookmainactivity", "chargeID "+item.getCustomfield().get("chargeID"));
								Log.d("Lookmainactivity", "station_address "+item.getCustomfield().get("station_address"));
								//hashmap遍历

//								Iterator iter = item.getCustomfield().entrySet()
//										.iterator();
//								while (iter.hasNext()) {
//									Map.Entry entry = (Map.Entry) iter.next();
//									Object key = entry.getKey();
//									Object val = entry.getValue();
//									Log.d("Lookmainactivity", key+" "+val);
//								}
//								Log.d(TAG, "_caretetime " + item.getCreatetime());
//								Log.d(TAG, "_updatetime " + item.getUpdatetime());
//								Log.d(TAG, "_distance " + item.getDistance());

							}
							if (mQuery.getBound().getShape()
									.equals(SearchBound.BOUND_SHAPE)) {// 圆形
//								aMap.addCircle(new CircleOptions()
//										.center(new LatLng(mCenterPoint
//												.getLatitude(), mCenterPoint
//												.getLongitude())).radius(5000)
//										.strokeColor(
//										// Color.argb(50, 1, 1, 1)
//												Color.RED)
//										.fillColor(Color.argb(50, 1, 1, 1))
//										.strokeWidth(25));

//								aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//										new LatLng(mCenterPoint.getLatitude(),
//												mCenterPoint.getLongitude()), 17));

							} else if (mQuery.getBound().getShape()
									.equals(SearchBound.POLYGON_SHAPE)) {
								aMap.addPolygon(new PolygonOptions()
										.add(AMapUtil.convertToLatLng(mPoint1))
										.add(AMapUtil.convertToLatLng(mPoint2))
										.add(AMapUtil.convertToLatLng(mPoint3))
										.add(AMapUtil.convertToLatLng(mPoint4))
										.fillColor(Color.LTGRAY)
										.strokeColor(Color.RED).strokeWidth(1));
								LatLngBounds bounds = new LatLngBounds.Builder()
										.include(AMapUtil.convertToLatLng(mPoint1))
										.include(AMapUtil.convertToLatLng(mPoint2))
										.include(AMapUtil.convertToLatLng(mPoint3))
										.build();
								aMap.moveCamera(CameraUpdateFactory
										.newLatLngBounds(bounds, 50));
							} else if ((mQuery.getBound().getShape()
									.equals(SearchBound.LOCAL_SHAPE))) {
								mPoiCloudOverlay.zoomToSpan();
							}

						} else {
							//ToastUtil.show(this, R.string.no_result);
							Toast.makeText(getActivity(), "附近没有充电站", Toast.LENGTH_SHORT).show();
							
						}
					}
				} else {
					//ToastUtil.show(this, R.string.no_result);
					Toast.makeText(getActivity(), "    123456  ", Toast.LENGTH_SHORT).show();
					
				}
			} else {
				//ToastUtil.show(this, R.string.error_network);
				Toast.makeText(getActivity(), "    123789  ", Toast.LENGTH_SHORT).show();
				
			}

		}
		
		/**
		 * 响应地理编码
		 */
		public void getLatlon(final String name) {
			showDialog();
			Log.v("FragmentFind","getLatlon.........");

			GeocodeQuery query = new GeocodeQuery(name, "");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
			geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
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
//		public void showpdialog(){
//			m_pDialog=new ProgressDialog(LoginActivity.this);
//			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			m_pDialog.setMessage("服务器响应中。。。");
//		    m_pDialog.show();
//		}

		/**
		 * 隐藏进度条对话框
		 */
		public void dismissDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}

}
