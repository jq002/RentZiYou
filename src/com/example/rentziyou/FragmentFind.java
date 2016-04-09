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
	 //��������
    private MapView mapView;
    private AMap aMap;
    
    //����AMapLocationClient�����
    //public AMapLocationClient mLocationClient = null;
    //������λ�ص�������
    //public AMapLocationListener mLocationListener = new AMapLocationListener();
    private LocationManagerProxy mAMapLocationManager;
    private OnLocationChangedListener mListener;
    
    public  double geoLat2;   //��λ���γ��
    public  double geoLng2;  //��λ��þ���
    
    private RadioGroup mGPSModeGroup1;//�ı���ӻ�����
	private RadioGroup mGPSModeGroup2;
	private LatLng center;
	private LatLng latlng=new LatLng(24.573877,118.091873);//Mark����õ������
	
	private ImageButton imagebutton;
	//���ƶ����
	private Marker marker;//���ƶ����
	private Boolean markremove=true;
	private LatLonPoint latLonPoint;
	private double geolatit;
	private double geolongtit;
	private GeocodeSearch geocoderSearch;//��������
	private String address;
	private Boolean flagmarker=false;//�Ƿ��ǿ��ƶ���ǵ�ģ���������
	//�����ͼ��
	private double geolatit1;
	private double geolongtit1;
	private LatLng latlng1;
	private LatLonPoint latLonPoint1;
	private Boolean flagmarkermap=false;//�Ƿ��ǵ����ͼ�ģ���������
	private Marker marker1;//�����ͼ�ı��
	private Boolean mapremove=true;//�����ɫ���
	private String chargeID;//���վID
	//����button
	private ImageButton searchButton;//������ť
    private AutoCompleteTextView searchText;// ���������ؼ���
    private String keyWord = "";// Ҫ�����poi�����ؼ���
    private PoiSearch.Query query;// Poi��ѯ������
    private PoiResult poiResult; // poi���صĽ��
	private int currentPage = 0;// ��ǰҳ�棬��0��ʼ����

	private PoiSearch poiSearch;// POI����
	private PoiItem poiItem;
	
	//����Ѿ����ڵĳ��վ
	private Boolean flagmarkmove=false;
    
	//��ͼ����
	private CloudSearch mCloudSearch;
	private LatLonPoint mCenterPoint = new LatLonPoint(24.578428,118.093482); // �ܱ��������ĵ�
	
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
	
	//����������������
	private String build;
	private String neighborhood;
	
	//��ӳ��վ
	private String parkaddress;
	
	//��ӳ��λ
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
		mapView.onCreate(savedInstanceState);// ����Ҫд
	    aMap = mapView.getMap();
	    Log.v("Rentactivity","erroe.....");
	    //initLocation();
	    setUpMap();
	    init();
	    addMarkers(latlng,"�������");
	    LatLng latlng3=new LatLng(24.579867, 118.095232);
	    addMarkers(latlng3,"�θ�ͼ���");
	    LatLng latlng4=new LatLng(24.577798, 118.099824);
	    addMarkers(latlng4,"���͹�԰");
//	    aMap.addMarker(new MarkerOptions()
//	     .position(latlng).title("�ƶ��˱�ǣ�ȷ��վ��λ��").draggable(true)
//	     .icon(BitmapDescriptorFactory
//					.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	    Log.v("Rentactivity","erroe11.....");
	    //addMarkersToMap();
	    
//	    initCloudKey();
//	    doSearchCloud();
		}
	
	 /**
	 * �ڵ�ͼ����ӿ��ƶ�marker
	 */
	
	private void addMarkersToMap() {
//		 Marker markerd;//���ƶ����
		center= aMap.getCameraPosition().target;
		if(!markremove){
			marker.remove();
		}
		
		 marker =aMap.addMarker(new MarkerOptions()
     .position(center).title("�ƶ��˱�ǣ�ȷ��վ��λ��").draggable(true).visible(true)
     .icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		 marker.showInfoWindow();

		Log.v("Rentactivity","erroe2222.....");
		if(markremove){
			markremove=false;
		}
		
	}
	 /**
	 * �ڵ�ͼ�����marker���Ѿ����ڵĳ���
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
				//aMap.clear();//����ƶ����
				
				addMarkersToMap();
		
			}
		});
		
		    searchText = (AutoCompleteTextView) getView().findViewById(R.id.keyWord);
			searchText.addTextChangedListener(this);// ����ı����������¼�
			searchButton=(ImageButton) getView().findViewById(R.id.search);
			/**
			 * ���������ť
			 */			
			searchButton.setOnClickListener(new Button.OnClickListener(){
				
				public void onClick(View arg0){
					
					keyWord = AMapUtil.checkEditText(searchText);
					if ("".equals(keyWord)) {
						ToastUtil.show(getActivity(), "�����������ؼ���");
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
		// �������ĵ㼰������Χ
				SearchBound bound = new SearchBound(new LatLonPoint(
				            mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 2000);
				//���ò�ѯ���� mTableID�ǽ����ݴ洢�����ݹ���̨���á�
				try {
					mQuery = new CloudSearch.Query(mTableID, "", bound);

		            mCloudSearch.searchCloudAsyn(mQuery);// �첽����
		            Log.v("FragmentFind","key2244555.....initCloud");
				} catch (AMapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public void initCloud(){
		Log.v("FragmentFind","erroe2222.....initCloud");
		mCloudSearch = new CloudSearch(getActivity());// ��ʼ����ѯ��
		mCloudSearch.setOnCloudSearchListener(this);// ���ûص�����
		// �������ĵ㼰������Χ
		SearchBound bound = new SearchBound(new LatLonPoint(
		            mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 4000);
		//���ò�ѯ���� mTableID�ǽ����ݴ洢�����ݹ���̨���á�
		try {
			mQuery = new CloudSearch.Query(mTableID, "��԰", bound);

            mCloudSearch.searchCloudAsyn(mQuery);// �첽����
            Log.v("FragmentFind","erroe33333.....initCloud");
		} catch (AMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 /**
     * ��ʼ����λ
     */
    private void setUpMap() {
   	 
     
        aMap.setLocationSource(this);// ���ö�λ����
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
        aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
        // ���ö�λ������Ϊ��λģʽ����λ��AMap.LOCATION_TYPE_LOCATE�������棨AMap.LOCATION_TYPE_MAP_FOLLOW��
        // ��ͼ������������ת��AMap.LOCATION_TYPE_MAP_ROTATE������ģʽ
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setOnMapClickListener(this); //���õ����ͼ�����¼�          
  	     aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
        aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
        aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
       
        //����Ĭ�϶�λ��ť�Ƿ���ʾ
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        //aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
        
        aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������
        
        geocoderSearch = new GeocodeSearch(getActivity());
		geocoderSearch.setOnGeocodeSearchListener(this);//������������
		//������
		mCloudSearch = new CloudSearch(getActivity());// ��ʼ����ѯ��
		mCloudSearch.setOnCloudSearchListener(this);// ���ûص�����
		progDialog = new ProgressDialog(getActivity());
    }
//aMap.setLocationSource(this);// ���ö�λ����------------------------------------

	/**
     * ���λ
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            //�˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
            //ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
            //�ڶ�λ�������ں��ʵ��������ڵ���destroy()����     
            //����������ʱ��Ϊ-1����λֻ��һ��
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork,-1, 10, this);
        }
    }
 
 
    /**
     * ֹͣ��λ
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
//aMap.setOnMapClickListener(this); //���õ����ͼ�����¼�    -------------------------------
    /**
     * �����ͼ�󣬻�ȡ��γ�ȣ����������
     */
	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		//tap_text.setText("��ѡ�����ͣ������λ���ǣ�"+point);
		if(!mapremove){//�������ĺ�ɫ���
			marker1.remove();
		}
		
		geolatit1=point.latitude;
		geolongtit1=point.longitude;
				
		//aMap.clear();//�����ͼ��
    	Log.v("map......",geolatit1+";"+geolongtit1);
		 latlng1= new LatLng(geolatit1, geolongtit1);//�������ʱ������
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
//aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������------------------------------------------------------------
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
//aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������--------------------------------------------------------
	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
//aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ-----------------------
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
        		showLatLng.setText("��ӳ��վ");
        		flagmarkmove=false;
        		
        	}
	 
		    showLatLng.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	String flagstation=(String) showLatLng.getText();
		        	Log.v("Oninfowindow","...."+flagstation);
		        	if(flagstation.equals("��ӳ��վ")){
		        		
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
		        	if(flagstation.equals("��ӳ��λ")){
		        		
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
	    * ��λ�ɹ���ص�����
	    */
		@Override
	   public void onLocationChanged(AMapLocation amapLocation) {
	       if (mListener != null && amapLocation != null) {
	           if (amapLocation.getAMapException().getErrorCode() == 0) {
	               //mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
	               
	               //��ȡλ����Ϣ
	              geoLat2 = amapLocation.getLatitude();
	              geoLng2 = amapLocation.getLongitude(); 
	              
	              Toast.makeText(getActivity(), geoLat2+"      "+geoLng2, Toast.LENGTH_SHORT).show();
					
//	              latit=geoLat2;
//	              longit=geoLng2;
//	              la2=Double.toString(geoLat2);
//	 		     lo2=Double.toString(geoLng2);
//	 		     
//	 		    //�ӷ��������json���޸ĺ�
//	 	        new Thread(){
//	 	        	public void run(){
//	 	        	String Web_results=" ";
////	 	        	if(flage==0){
////	 	        		//�����˺ţ����������ĳ���
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
				  mCenterPoint = new LatLonPoint(geoLat2,geoLng2); // �ܱ��������ĵ�
		          	
				  doSearchCloud();
				
		      

	           }
	       }
	   }
		/**
		 * ���ݶ�����ť״̬�����ú���animateCamera��moveCamera���ı��������
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
//aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������---------------------------------------------------------
		/**
		 * �����϶�markerʱ�¼��ص�
		 */
		@Override
		public void onMarkerDrag(Marker marker) {
			marker.hideInfoWindow();
		
//			latLonPoint = new LatLonPoint(geolatit,geolongtit);
//			
//			getAddress(latLonPoint);
			
			String curDes = "�϶�ʱ��ǰλ��:(Lan/Log)\n("
					+marker.getPosition().latitude+marker.getPosition().longitude+")";
			
			marker.setTitle(curDes);
			flagmarkmove=true;
			marker.showInfoWindow();
			//markerText.setText(curDes);
		}

		/**
		 * �����϶�marker�����¼��ص�
		 */
		@Override
		public void onMarkerDragEnd(Marker marker) {
			//markerText.setText(marker.getTitle() + "ֹͣ�϶�");
			marker.hideInfoWindow();
			geolatit=marker.getPosition().latitude;
			geolongtit=marker.getPosition().longitude;
            latLonPoint = new LatLonPoint(geolatit,geolongtit);
//            String curDes = "�����С���������������";
//			
//			marker.setTitle(curDes);
//			marker.showInfoWindow();
			flagmarker=true;
			
			getAddress(latLonPoint);
			
		}

		/**
		 * ������ʼ�϶�marker�¼��ص�
		 */
		@Override
		public void onMarkerDragStart(Marker marker) {
			//markerText.setText(marker.getTitle() + "��ʼ�϶�");
			marker.hideInfoWindow();
			String curDes = "���Կ�ʼ�϶�";
			
			marker.setTitle(curDes);
			flagmarkmove=true;
			marker.showInfoWindow();
			
            
		}
		//---------------------------------------------
		/**
		 * ��������
		 */
		public void getAddress(final LatLonPoint latLonPoint) {
			//showDialog();
			Log.v("Rentactivity","erroe2222.....getAddress22222222");
			RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
					GeocodeSearch.AMAP);// ��һ��������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�������������ʾ�ǻ�ϵ����ϵ����GPSԭ������ϵ
			geocoderSearch.getFromLocationAsyn(query);// ����ͬ��������������
			
		}
//--------------------------------------------------------------------------
		/**
		 * ��������ѯ�ص�
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
//					addressName = "��γ��ֵ:" + address.getLatLonPoint() + "\nλ������:"
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
		 * ��Ӧ��������
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
			        	//tap_text.setText("���ͣ����λ�ã�"+parkaddress);
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
//searchText.addTextChangedListener(this);// ����ı����������¼�------------------------------------------
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
		 * ������ʾ����
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
			String newText = s.toString().trim();
			Inputtips inputTips = new Inputtips(getActivity(),
					new InputtipsListener() {

						@Override
						public void onGetInputtips(List<Tip> tipList, int rCode) {
							if (rCode == 0) {// ��ȷ����
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
				inputTips.requestInputtips(newText,"");// ��һ��������ʾ��ʾ�ؼ��֣��ڶ�������Ĭ�ϴ���ȫ����Ҳ����Ϊ��������

			} catch (AMapException e) {
				e.printStackTrace();
			}
			
		}
	//------------------------------------------------------------
		/**
		 * ��ʼ����poi����
		 */
		protected void doSearchQuery() {
			//showProgressDialog();// ��ʾ���ȿ�
			currentPage = 0;
			query = new PoiSearch.Query(keyWord, "","");// ��һ��������ʾ�����ַ������ڶ���������ʾpoi�������ͣ�������������ʾpoi�������򣨿��ַ�������ȫ����
			query.setPageSize(15);// ����ÿҳ��෵�ض�����poiitem
			query.setPageNum(currentPage);// ���ò��һҳ

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
		 * POI��Ϣ��ѯ�ص�����,����intent��ת����
		 */
		@Override
		public void onPoiSearched(PoiResult result, int rCode) {
			//dissmissProgressDialog();// ���ضԻ���

			LatLonPoint latLonPoint2 = null;
			String title;
			if (rCode == 0) {
				if (result != null && result.getQuery() != null) {// ����poi�Ľ��
					if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��
						poiResult = result;
						// ȡ����������poiitems�ж���ҳ
						List<PoiItem> poiItems = poiResult.getPois();// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
						List<SuggestionCity> suggestionCities = poiResult
								.getSearchSuggestionCitys();// ����������poiitem����ʱ���᷵�غ��������ؼ��ֵĳ�����Ϣ

						if (poiItems != null && poiItems.size() > 0) {
							
							poiItem =poiItems.get(0);
							latLonPoint2=poiItem.getLatLonPoint();
							title=poiItem.getTitle();
							aMap.clear();// search����֮ǰ��ͼ��

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

		
	        LatLng latlng= new LatLng(latit, longit);//�������ʱ������
		     changeCamera(
				CameraUpdateFactory.newCameraPosition(new CameraPosition(
								latlng, 17, 0, 0)), null);
//	        //�ӷ��������json���޸ĺ�
//	        new Thread(){
//	        	public void run(){
//	        	String Web_results=" ";
////	        	if(flage==0){
////	        		//�����˺ţ����������ĳ���
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
							mPoiCloudOverlay.addToMap();//���marker���
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
								//hashmap����

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
									.equals(SearchBound.BOUND_SHAPE)) {// Բ��
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
							Toast.makeText(getActivity(), "����û�г��վ", Toast.LENGTH_SHORT).show();
							
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
		 * ��Ӧ�������
		 */
		public void getLatlon(final String name) {
			showDialog();
			Log.v("FragmentFind","getLatlon.........");

			GeocodeQuery query = new GeocodeQuery(name, "");// ��һ��������ʾ��ַ���ڶ���������ʾ��ѯ���У����Ļ�������ȫƴ��citycode��adcode��
			geocoderSearch.getFromLocationNameAsyn(query);// ����ͬ�������������
		}
		/**
		 * ��ʾ�������Ի���
		 */
		public void showDialog() {
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(true);
			progDialog.setMessage("���ڻ�ȡ��ַ");
			progDialog.show();
		}
//		public void showpdialog(){
//			m_pDialog=new ProgressDialog(LoginActivity.this);
//			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			m_pDialog.setMessage("��������Ӧ�С�����");
//		    m_pDialog.show();
//		}

		/**
		 * ���ؽ������Ի���
		 */
		public void dismissDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}

}
