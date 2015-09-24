package com.tcnr14.example;

import java.text.SimpleDateFormat;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Fragment_Map extends Fragment implements LocationListener {
	private static String[][] locations = { { "目前位置", "0,0", "m00.gif" },
			{ "臺灣大學醫學院附設醫院", "25.040663, 121.518990", "m01.jpg" },
			{ "臺北榮民總醫院", "25.120748, 121.519312", "m02.jpg" },
			{ "財團法人馬偕紀念醫院", "25.058458, 121.522429", "m03.jpg" },
			{ "國防醫學院三軍總醫院", "25.071678, 121.594039", "m04.jpg" },
			{ "臺北市立聯合醫院", "25.037512, 121.545213", "m05.jpg" },

			{ "財團法人桃園長庚紀念醫院", "25.029643, 121.366609", "m06.jpg" },
			{ "財團法人羅東博愛醫院", "24.671862, 121.772038", "m07.jpg" },
			{ "財團法人台北慈濟醫院", "24.985897, 121.535600", "m08.jpg" },
			{ "臺中榮民總醫院", "24.184630, 120.604673", "m09.jpg" },
			{ "中國醫藥大學附設醫院", "24.156717, 120.681207", "m10.jpg" },

			{ "中山醫學大學附設醫院中興分院", "24.118124, 120.659108", "m11.jpg" },
			{ "財團法人光田綜合醫院", "24.235448, 120.558864", "m12.jpg" },
			{ "財團法人彰化基督教醫院", "24.071496, 120.544347", "m13.jpg" },
			{ "成功大學醫學院附設醫院", "23.001975, 120.219643", "m14.jpg" },
			{ "財團法人奇美醫院", "23.020974, 120.222044", "m15.jpg" },

			{ "財團法人高雄長庚紀念醫院", "22.649022, 120.354982", "m16.jpg" },
			{ "高雄榮民總醫院", "22.677556, 120.322622", "m17.jpg" },
			{ "財團法人屏東基督教醫院", "22.682134, 120.502700", "m18.jpg" },
			{ "財團法人花蓮慈濟醫院", "23.995781, 121.591638", "m19.jpg" },
			{ "財團法人台東聖母醫院", "22.757737, 121.146807", "m20.jpg" }, };
	private Spinner mSpnLocation;

	private static final String MAP_URL = "file:///android_asset/GoogleMAP.html";
	// 自建的html檔名
	private WebView webView;
	private String Lat;
	private String Lon;
	private String jcontent;// 地名變數
	private String jimg;// 景觀變數
	// ----------------------

	/** GPS */
	private LocationManager locationMgr;
	private String provider; // 提供資料
	private TextView txtOutput;
	int iSelect;
	private final String TAG = "<-V3";

	/** Navigation **/
	private Button bNav;
	String[] sLocation;
	String Navon = "off";
	String Navstart = "24.172127,120.610313"; // 起始點
	String Navend = "24.144671,120.683981"; // 結束點

	// --------------------------------------------------------
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_map, container, false);

		setupWebView(view);
		return view;
	}

	private void setupWebView(View view) {
		mSpnLocation = (Spinner) view.findViewById(R.id.spnLocation);
		// ----Location-----------
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item);

		for (int i = 0; i < locations.length; i++)
			adapter.add(locations[i][0]);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnLocation.setAdapter(adapter);
		mSpnLocation.setOnItemSelectedListener(mSpnLocationOnItemSelLis);

		webView = (WebView) view.findViewById(R.id.webview);
		txtOutput = (TextView) view.findViewById(R.id.txtOutput);
		// --導航監聽--
		bNav = (Button) view.findViewById(R.id.Navigation);
		bNav.setOnClickListener(bNavselectOn);
	}

	// --導航監聽--
	private Button.OnClickListener bNavselectOn = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (Navon == "off") {

				bNav.setTextColor(getResources().getColor(R.drawable.blue));
				Navon = "on";
				bNav.setText("關閉路徑規劃");
				setMapLocation();

			} else {
				Navon = "off";
				bNav.setTextColor(getResources().getColor(R.drawable.red));
				bNav.setText("開啟路徑規劃");
				setMapLocation();
			}
		}
	};

	// ----------------------------------------------
	@JavascriptInterface
	public String Navon() {
		return Navon;
	}

	@JavascriptInterface
	public String Getstart() {
		return Navstart;
	}

	@JavascriptInterface
	public String Getend() {
		return Navend;
	}

	// ----------------------------------------------------
	private OnItemSelectedListener mSpnLocationOnItemSelLis = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView parent, View v, int position,
				long id) {
			setMapLocation();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	};

	// ===========================================================
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void setMapLocation() {
		// TODO Auto-generated method stub
		iSelect = mSpnLocation.getSelectedItemPosition();

		sLocation = locations[iSelect][1].split(",");

		Lat = sLocation[0]; // 南北緯
		Lon = sLocation[1]; // 東西經

		jcontent = locations[iSelect][0]; // 地名
		jimg = locations[iSelect][2]; // 景觀
		// ---------增加判斷是否規畫路徑------------------
		if (Navon == "on" && iSelect != 0) {
			Navstart = locations[0][1];
			Navend = locations[iSelect][1];
		}
		// -------------------------------------------------------------
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(Fragment_Map.this, "AndroidFunction");

		webView.loadUrl(MAP_URL);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent it = new Intent();
		switch (item.getItemId()) {
		case R.id.action_settings:
			// return true;
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// -----------------------------
	@JavascriptInterface
	public String GetLat() {
		return Lat;
	}

	@JavascriptInterface
	public String GetLon() {
		return Lon;
	}

	@JavascriptInterface
	public String Getjcontent() {
		return jcontent;
	}

	@JavascriptInterface
	public String Getjimg() {
		return jimg;
	}

	// -------------------------------
	/* 開啟時先檢查是否有啟動GPS精緻定位 */
	@Override
	public void onStart() {
		super.onStart();

		if (initLocationProvider()) {
			nowaddress();
		} else {
			txtOutput.setText("GPS未開啟,請先開啟定位！");
		}
	}

	@Override
	public void onStop() {
		locationMgr.removeUpdates(this);
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/************************************************
	 * GPS部份
	 ***********************************************/
	/* 檢查GPS 是否開啟 */
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
		long minTime = 5000;// ms
		float minDist = 5.0f;// meter
		locationMgr.requestLocationUpdates(provider, minTime, minDist, this);
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
			locations[0][1] = lat + "," + lng; // 用GPS找到的位置更換 陣列的目前位置

		} else {
			where = "*位置訊號消失*";
		}

		// 位置改變顯示
		txtOutput.setText(where);
	}

	// ------------------------------------------
	private String getTimeString(long timeInMilliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(timeInMilliseconds);
	}

	// -------------------------------
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		/* 監聽GPS 狀態 */
		@Override
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Log.d(TAG, "GPS_EVENT_STARTED");
				break;

			case GpsStatus.GPS_EVENT_STOPPED:
				Log.d(TAG, "GPS_EVENT_STOPPED");
				break;

			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.d(TAG, "GPS_EVENT_FIRST_FIX");
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
				break;
			}
		}
	};

	// ------------------
	/* 位置變更狀態監視 */
	@Override
	public void onLocationChanged(Location location) {
		// 定位改變時
		updateWithNewLocation(location);

		// 將畫面移至定位點的位置
		final String centerURL = "javascript:centerAt("
				+ location.getLatitude() + "," + location.getLongitude() + ")";
		webView.loadUrl(centerURL);

		Log.d(TAG, "onLocationChanged");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(TAG, "Status Changed: Out of Service");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(TAG, "Status Changed: Temporarily Unavailable");
			break;
		case LocationProvider.AVAILABLE:
			Log.v(TAG, "Status Changed: Available");
			break;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled");

	}

	@Override
	public void onProviderDisabled(String provider) {
		updateWithNewLocation(null);
		Log.d(TAG, "onProviderDisabled");
	}
}
