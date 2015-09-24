package com.tcnr14.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Fragment_Map3 extends Fragment implements LocationListener,
		OnCameraChangeListener {
	private final String TAG = "==MapTrace==>";
	static LatLng VGPS = new LatLng(24.253857, 120.705978);
	private static String[][] locations = { { "目前位置", "0, 0" },
			{ "墾丁國家公園", "21.949812,120.779816" },
			{ "玉山國家公園", "23.406609, 121.065433" },
			{ "陽明山國家公園", "25.193935, 121.587715" },
			{ "太魯閣國家公園", "24.203042, 121.515856" },
			{ "雪霸國家公園", "24.402365, 121.162023" },
			{ "金門國家公園", "24.432856, 118.358299" },
			{ "東沙環礁國家公園", "20.702403, 116.932505" },
			{ "台江國家公園", "23.019234, 120.140455" },
			{ "澎湖南方四島國家公園", "23.249330, 119.613579" },

			{ "武陵農場", "24.374415, 121.310557" },
			{ "清境農場", "24.058596, 121.163993" },
			{ "福壽山農場", "24.245984, 121.245830" },
			{ "日月潭風景區", "23.84932,120.929622" },
			{ "秋紅谷生態公園", "24.1477360,120.6736480" },
			{ "奧萬大", "25.1424660,121.5706840" },
			{ "大屯山花卉農場", "25.1755790,121.4384700" },
			{ "心鮮森林", "24.7687280,121.0934880" },
			{ "知卡宣森林公園", "23.9566370,121.5846240" },
			{ "老梅綠石槽", "25.2908280,121.5671380" },

			{ "台北世貿101", "25.033773,121.564787" },
			{ "國立故宮博物院", "25.102398,121.548613" },
			{ "東海大學", "24.179051,120.600610" },
			{ "台中公園", "24.144671,120.683981" },
			{ "台中科博館", "24.1579361,120.6659828" } };
	private static String[] mapType = { "街道圖", "衛星圖", "地形圖", "混合圖" };
	private float zoomsize = 16; // // 設定放大倍率1(地球)-21(街景)
	private float currentZoom = 16;
	private Spinner mSpnLocation, mSpnMapType;
	private BitmapDescriptor image_des;
	private int icosel = 0;
	private double dLat, dLon;
	private boolean change = true;
	// private MapView mMapView;
	// private MapController mMapCtrl;

	/** Map */
	private GoogleMap map;
	private TextView txtOutput;
	private TextView tmsg;
	private Marker markerMe;
	/** 記錄軌跡 */
	private ArrayList<LatLng> mytrace; // 追蹤我的位置

	/** GPS */
	private LocationManager locationMgr;
	private String provider; // 提供資料

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_map2, container, false);

		setupWebView(view);
		return view;
	}

	private void setupWebView(View view) {
		mSpnLocation = (Spinner) view.findViewById(R.id.spnLocation);
		mSpnMapType = (Spinner) view.findViewById(R.id.spnMapType);
		// mMapView = (MapView) findViewById(R.id.map);

		// mMapCtrl = mMapView.getController();
		// mMapCtrl.setZoom(16);
		// icosel = 1;
		/* 設定景點下拉資料,由陣列傳入spinner下拉 */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item);

		for (int i = 0; i < locations.length; i++)
			adapter.add(locations[i][0]);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnLocation.setAdapter(adapter);
		mSpnLocation.setOnItemSelectedListener(mSpnLocationOnItemSelLis);

		/* 設定景點下拉資料,由陣列傳入spinner下拉 */
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item);

		for (int i = 0; i < mapType.length; i++)
			adapter.add(mapType[i]);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnMapType.setAdapter(adapter);
		mSpnMapType.setOnItemSelectedListener(mSpnMapTypeOnItemSelLis);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initView(); // 連結layout
		initMap(); // 安裝googlemap地圖
		if (initLocationProvider()) {
			nowaddress();
		} else {
			txtOutput.setText("GPS未開啟,請先開啟定位！");
		}
	}

	@Override
	public void onStop() {
		locationMgr.removeUpdates(locationListener);
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		map.clear();
		initMap();
	}

	private void initMap() {
		// TODO Auto-generated method stub
		txtOutput = (TextView) getActivity().findViewById(R.id.txtOutput);
		tmsg = (TextView) getActivity().findViewById(R.id.msg);
	}

	/************************************************
	 * Map部份
	 ***********************************************/

	private void initView() {
		// TODO Auto-generated method stub
		if (map == null) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (map != null) {
				// 設定地圖類型--------------------
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

				map = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();
				// --------------------------
				// map.setMyLocationEnabled(true); // 顯示自己位置
				// map.setTrafficEnabled(true); // 顯示交通資訊
				map.getUiSettings().setZoomControlsEnabled(true); // 顯示縮放按鈕
				map.getUiSettings().setCompassEnabled(true); // 顯示指北針
				map.getUiSettings().setIndoorLevelPickerEnabled(true);
				map.getUiSettings().setMapToolbarEnabled(false); // 工具開關
				map.setOnCameraChangeListener(new OnCameraChangeListener() {

					@Override
					public void onCameraChange(CameraPosition arg0) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onCameraChange");
						if (map.getCameraPosition().zoom != currentZoom) {
							// currentZoom = map.getCameraPosition().zoom; //
							// here you get zoom level
							currentZoom = arg0.zoom;
							zoomsize = currentZoom;
						}
						tmsg.setText("目前Zoom:" + currentZoom);
					}
				});
				// --------------------------------
			}
		}
	}

	private boolean initLocationProvider() {
		locationMgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			return true;
		}
		return false;
	}

	// ------------------------------------------
	/* 建立位置改變偵聽器 預先顯示上次的已知位置 */
	private void nowaddress() {
		// 取得上次已知的位置
		Location location = locationMgr.getLastKnownLocation(provider);
		updateWithNewLocation(location);

		// 監聽 GPS Listener
		locationMgr.addGpsStatusListener(gpsListener);
		// Location Listener
		long minTime = 2000;// ms
		float minDist = 2.0f;// meter
		locationMgr.requestLocationUpdates(provider, minTime, minDist,
				locationListener);
	}

	// ------------------------------------------
	/** 顯示目前位置 */
	private void showMarkerMe(double lat, double lng) {
		if (markerMe != null) {
			markerMe.remove();
		}

		MarkerOptions markerOpt = new MarkerOptions();
		markerOpt.position(new LatLng(lat, lng));
		markerOpt.title("目前所在位置:" + lat + "," + lng);
		if (icosel == 1) {
			markerOpt.icon(BitmapDescriptorFactory
					.fromResource(getResources().getIdentifier("t00", "raw",
							getActivity().getPackageName())));
		}

		markerMe = map.addMarker(markerOpt);
		locations[0][1] = lat + "," + lng; // 用GPS找到的位置更換 陣列的目前位置
	}

	private void cameraFocusOnMe(double lat, double lng) {
		CameraPosition camPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, lng)).zoom(currentZoom).build();
		/* 移動地圖鏡頭 */
		map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
	}

	// ------------------------------------------
	/** 更新並顯示新位置 @param location */
	private void updateWithNewLocation(Location location) {
		String where = "";

		if (location != null) {
			double lng = location.getLongitude();// 經度
			double lat = location.getLatitude();// 緯度
			float speed = location.getSpeed();// 速度
			long time = location.getTime();// 時間
			String timeString = getTimeString(time);

			where = "經度: " + lng + "\n緯度: " + lat + "\n速度: " + speed + "\n時間: "
					+ timeString + "\nProvider: " + provider;

			// 標記"我的位置"
			showMarkerMe(lat, lng);
			if (change) {
				cameraFocusOnMe(lat, lng);
			}
			trackMe(lat, lng);
		} else {
			where = "*位置訊號消失*";
		}

		// 位置改變顯示
		// float zoomlevel = map.getCameraPosition().zoom;
		txtOutput.setText(where);
		// onCameraChange(map.getCameraPosition());
	}

	// ---------追蹤目前我的位置畫軌跡圖----------------
	private void trackMe(double lat, double lng) {
		if (mytrace == null) {
			mytrace = new ArrayList<LatLng>();
		}
		mytrace.add(new LatLng(lat, lng));

		PolylineOptions polylineOpt = new PolylineOptions();
		for (LatLng latlng : mytrace) {
			polylineOpt.add(latlng);

			/* 畫圓圈圈 anchor設在中心點, default 是擺在圖片下緣中心 */
			VGPS = new LatLng(lat, lng);
			map.addMarker(new MarkerOptions().position(VGPS).title("+")
					.snippet("座標:" + lat + "," + lng).anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory.fromResource(R.raw.c00)));
		}

		polylineOpt.color(Color.RED); // 軌跡顏色

		Polyline line = map.addPolyline(polylineOpt);
		line.setWidth(10); // 軌跡寬度
	}

	// ------------------------------------------
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		/* 監聽GPS 狀態 */
		@Override
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Log.d(TAG, "GPS_EVENT_STARTED");
				tmsg.setText("GPS_EVENT_STARTED");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.d(TAG, "GPS_EVENT_STOPPED");
				tmsg.setText("GPS_EVENT_STOPPED");
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.d(TAG, "GPS_EVENT_FIRST_FIX");
				tmsg.setText("GPS_EVENT_FIRST_FIX");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
				tmsg.setText("GPS_EVENT_SATELLITE_STATUS");
				break;
			}
		}
	};

	LocationListener locationListener = new LocationListener() {
		/* 位置變更狀態監視 */
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			Log.d(TAG, "onLocationChanged");
		}

		@Override
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
			Log.d(TAG, "onProviderDisabled");
		}

		@Override
		public void onProviderEnabled(String provider) {
			tmsg.setText("onProviderEnabled");
			Log.d(TAG, "onProviderEnabled");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Log.v(TAG, "Status Changed: Out of Service");
				tmsg.setText("Out of Service");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.v(TAG, "Status Changed: Temporarily Unavailable");
				tmsg.setText("Temporarily Unavailable");
				break;
			case LocationProvider.AVAILABLE:
				Log.v(TAG, "Status Changed: Available");
				tmsg.setText("Available");
				break;
			}
		}
	};

	// =====監聽景點選取==========================================================
	private OnItemSelectedListener mSpnLocationOnItemSelLis = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			if (position == 0) {
				change = true;
			} else {
				change = false;
			}
			setMapLocation();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	// =====監聽地圖樣式選取==========================================================
	private OnItemSelectedListener mSpnMapTypeOnItemSelLis = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub

			switch (position) {
			case 0:
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 2:
				map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
			case 3:
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	private String getTimeString(long timeInMilliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(timeInMilliseconds);
	}

	public void setMapLocation() {
		showloc();
		int iSelect = mSpnLocation.getSelectedItemPosition();

		String[] sLocation = locations[iSelect][1].split(",");

		dLat = Double.parseDouble(sLocation[0]); // 南北緯
		dLon = Double.parseDouble(sLocation[1]); // 東西經
		String vtitle = locations[iSelect][0];
		// GeoPoint gp = new GeoPoint((int)(dLat * 1e6), (int)(dLon * 1e6));
		// mMapCtrl.animateTo(gp);
		image_des = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);

		VGPS = new LatLng(dLat, dLon);
		// --------------------------------
		// ---根據所選位置項目顯示地圖/標示文字與圖片 ---//
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		Marker vgps = map.addMarker(new MarkerOptions().position(VGPS)
				.title(vtitle).snippet("座標:" + dLat + "," + dLon)
				.icon(image_des));

		// map.setMyLocationEnabled(true);
		// map.getUiSettings().setZoomControlsEnabled(true);
		// map.getUiSettings().setCompassEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS, zoomsize));
		// 移動地圖鏡頭到指定座標點,並設定地圖縮放等級
		// map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS, 14));
		onCameraChange(map.getCameraPosition());

	}

	private void showloc() {
		// TODO Auto-generated method stub
		for (int i = 0; i < locations.length; i++) {
			String[] sLocation = locations[i][1].split(",");
			dLat = Double.parseDouble(sLocation[0]);
			dLon = Double.parseDouble(sLocation[1]);
			String vtitle = locations[i][0];
			// ---設定所選位置之當地圖片 ---//
			switch (icosel) {
			case 0:
				image_des = BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
				break;
			case 1:
				String idName = "t" + String.format("%02d", i);
				int resID = getResources().getIdentifier(idName, "raw",
						getActivity().getPackageName());
				image_des = BitmapDescriptorFactory.fromResource(resID);
				break;
			}

			VGPS = new LatLng(dLat, dLon);
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			Marker vgps = map.addMarker(new MarkerOptions().position(VGPS)
					.title(vtitle).snippet("座標:" + dLat + "," + dLon)
					.icon(image_des));
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent it = new Intent();

		int id = item.getItemId();
		switch (id) {
		// case R.id.action_settings01:
		// map.clear();
		// if (icosel < 1) {
		// icosel = 1;
		// showloc();
		// } else {
		// icosel = 0;
		// }
		// showloc();
		// break;
		case R.id.action_settings:
			// return true;
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		// Log.d(TAG, "onCameraChange");
		// if (map.getCameraPosition().zoom != currentZoom){
		// // currentZoom = map.getCameraPosition().zoom; // here you get zoom
		// level
		// currentZoom = arg0.zoom;
		// zoomsize = currentZoom;
		// }
		// tmsg.setText("目前Zoom:"+currentZoom);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}