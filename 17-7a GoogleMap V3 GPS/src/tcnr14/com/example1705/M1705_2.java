package tcnr14.com.example1705;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class M1705_2 extends Activity {
	private static final String MAP_URL="file:///android_asset/map/map.php";
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1705_2);
		
		setupViewContent();
	}

	private void setupViewContent() {
		webview=(WebView)findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(MAP_URL);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1705, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent it = new Intent();
		switch (item.getItemId()) {
		
		case R.id.action_settings01:
			it.setClass(M1705_2.this, M1705.class);
			startActivity(it);
			break;
			
		case R.id.action_settings02:
			it.setClass(M1705_2.this, M1705_2.class);
			startActivity(it);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
