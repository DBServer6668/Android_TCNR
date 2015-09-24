package com.tcnr14.example;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

public class Fragment_GCM extends WebViewFragment {
	private static final String GCM_URL = "http://dbserver66.er-webs.com/android/index.php";
	private WebView webView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setwebView();
	}

	private void setwebView() {
		webView = getWebView();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(GCM_URL);

	}
}