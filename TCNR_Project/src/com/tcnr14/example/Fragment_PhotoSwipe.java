package com.tcnr14.example;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

public class Fragment_PhotoSwipe extends WebViewFragment {
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
		webView.loadUrl("file:///android_asset/PhotoSwipe.html");
	}
}