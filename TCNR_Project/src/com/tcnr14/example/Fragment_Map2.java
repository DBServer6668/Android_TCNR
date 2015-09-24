package com.tcnr14.example;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.Toast;

public class Fragment_Map2 extends WebViewFragment implements LocationListener {

	private LocationManager locationManager;
	private WebView webView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		LocationManager status = (LocationManager) (getActivity()
				.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			// 如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			initview();
		} else {
			Toast.makeText(getActivity(), "請開啟定位服務", Toast.LENGTH_LONG).show();
			// 開啟設定頁面
		}
	}

	private void initview() {
		webView = getWebView();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/GoogleMAP2.html");

		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 5000, 1.0f,
				Fragment_Map2.this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 1.0f, Fragment_Map2.this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Double longitude = location.getLongitude();
		Double latitude = location.getLatitude();
		webView.loadUrl("javascript:initialize('" + latitude + "','"
				+ longitude + "')");
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(Fragment_Map2.this);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initview();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
