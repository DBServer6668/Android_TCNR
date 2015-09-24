package com.tcnr14.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/** 開場歡迎動畫 */
public class Welcome extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 全屏顯示welcome畫面
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		
		// 延遲5.0秒後執行run方法中的換頁動作
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(Welcome.this, MainActivity.class);
				startActivity(intent);
				Welcome.this.finish();
			}
		}, 3000);
	}
}