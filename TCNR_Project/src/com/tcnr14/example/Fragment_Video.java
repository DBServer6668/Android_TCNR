package com.tcnr14.example;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

public class Fragment_Video extends WebViewFragment {
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
		webView.getSettings().setPluginState(PluginState.ON);//撥放YouTube影片
		webView.setWebChromeClient(new WebChromeClient());

		webView.loadUrl("file:///android_asset/Video.html");

	}
	
}
