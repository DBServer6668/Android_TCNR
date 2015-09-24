package tcnr14.com.example1705;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class M1705 extends Activity implements LocationListener, Listener {
	private static String[][] locations = { { "我的位置", "0, 0" },
			{ "中區職訓", "24.172127,120.610313" },
			{ "東海大學", "24.179051,120.600610" },
			{ "台中公園", "24.144671,120.683981" },
			{ "秋紅谷", "24.1674900,120.6398902" },
			{ "台中火車站", "24.136829,120.685011" },
			{ "台中科博館", "24.1579361,120.6659828" } };
	private Spinner mSpnLocation;

	private static final String MAP_URL = "file:///android_asset/GoogleMAP.html";
	private WebView webview;
	private String Lat, Lon, Title;
	private TextView txtOutput;

	/** GPS */
	private LocationManager locationMgr;
	private String provider; // 提供資料
	private final String TAG = "+MapV3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1705);

		setupViewContent();
	}

	private void setupViewContent() {
		mSpnLocation = (Spinner) this.findViewById(R.id.spnLocation);
		// mSpnLocation
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);

		for (int i = 0; i < locations.length; i++)
			adapter.add(locations[i][0]);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnLocation.setAdapter(adapter);
		mSpnLocation.setOnItemSelectedListener(mSpnLocationOnItemSelLis);

		webview = (WebView) findViewById(R.id.webview);
		txtOutput = (TextView) findViewById(R.id.txtOutput);
		// lat = "0.0";// 南北緯
		// lon = "0.0";// 東西經
		//
		// webview.addJavascriptInterface(M1705.this, "AndroidFunction");
		// webview.getSettings().setJavaScriptEnabled(true);
		// webview.loadUrl(MAP_URL);
	}

	private OnItemSelectedListener mSpnLocationOnItemSelLis = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			setMapLocation();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1705, menu);
		return true;
	}

	public void setMapLocation() {
		int iSelect = mSpnLocation.getSelectedItemPosition();

		String[] sLocation = locations[iSelect][1].split(",");

		Lat = sLocation[0];// 南北緯
		Lon = sLocation[1];// 東西經
		Title = locations[iSelect][0];

		webview.getSettings().setJavaScriptEnabled(true);
		webview.addJavascriptInterface(M1705.this, "AndroidFunction");
		webview.loadUrl(MAP_URL);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent it = new Intent();
		switch (item.getItemId()) {

		case R.id.action_settings01:
			it.setClass(M1705.this, M1705.class);
			startActivity(it);
			break;

		case R.id.action_settings02:
			it.setClass(M1705.this, M1705_2.class);
			startActivity(it);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@JavascriptInterface
	public String GetLat() {
		return Lat;
	}

	@JavascriptInterface
	public String GetLon() {
		return Lon;
	}

	@JavascriptInterface
	public String GetTitle() {
		return Title;
	}

	// 開啟前先檢查是否有啟動GPS精緻定位
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		if (initLocationProvider()) {
			nowaddress();
		} else {
			txtOutput.setText("GPS未開啟,請先開啟定位！");
		}
	}

	@Override
	protected void onStop() {
		locationMgr.removeUpdates(this);
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// 檢查GPS是否開啟
	private boolean initLocationProvider() {
		locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			return true;
		}
		return false;
	}

	/* 建立位置改變偵聽器 預先顯示上次的已知位置 */
	private void nowaddress() {
		// 取得上次已知的位置
		Location location = locationMgr.getLastKnownLocation(provider);
		updateWithNewLocation(location);

		// 監聽 GPS Listener
		locationMgr.addGpsStatusListener(this);
		// Location Listener
		long minTime = 2000;// ms
		float minDist = 2.0f;// meter
		locationMgr.requestLocationUpdates(provider, minTime, minDist,
				this);
	}

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
			locations[0][1] = lat + "," + lng;// 用GPS找到的位置更換陣列的目前位置

		} else {
			where = "*位置訊號消失*";
		}
		// 位置改變顯示
		// float zoomlevel = map.getCameraPosition().zoom;
		txtOutput.setText(where);
		// onCameraChange(map.getCameraPosition());
	}

	// ------------------------------------------
	private String getTimeString(long timeInMilliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(timeInMilliseconds);
	}

	// ------------------------------------------
	// implements LocationListener
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		updateWithNewLocation(location);
		Log.d(TAG, "onLocationChanged");
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		Log.d(TAG, "onProviderEnabled");
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		updateWithNewLocation(null);
		Log.d(TAG, "onProviderDisabled");
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
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
		
	

	// ------------------------------------------
//	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
//		/* 監聽GPS 狀態 */
//		@Override
//		public void onGpsStatusChanged(int event) {
//			switch (event) {
//			case GpsStatus.GPS_EVENT_STARTED:
//				Log.d(TAG, "GPS_EVENT_STARTED");
//				break;
//			case GpsStatus.GPS_EVENT_STOPPED:
//				Log.d(TAG, "GPS_EVENT_STOPPED");
//				break;
//			case GpsStatus.GPS_EVENT_FIRST_FIX:
//				Log.d(TAG, "GPS_EVENT_FIRST_FIX");
//				break;
//			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//				Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
//				break;
//			}
//		}
//	};
//
//	LocationListener locationListener = new LocationListener() {
//		/* 位置變更狀態監視 */
//		@Override
//		public void onLocationChanged(Location location) {
//			updateWithNewLocation(location);
//			Log.d(TAG, "onLocationChanged");
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//			updateWithNewLocation(null);
//			Log.d(TAG, "onProviderDisabled");
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//			Log.d(TAG, "onProviderEnabled");
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//			switch (status) {
//			case LocationProvider.OUT_OF_SERVICE:
//				Log.v(TAG, "Status Changed: Out of Service");
//				break;
//			case LocationProvider.TEMPORARILY_UNAVAILABLE:
//				Log.v(TAG, "Status Changed: Temporarily Unavailable");
//				break;
//			case LocationProvider.AVAILABLE:
//				Log.v(TAG, "Status Changed: Available");
//				break;
//			}
//		}
//	};
}
