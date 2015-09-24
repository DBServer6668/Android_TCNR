package tcnr14.com.example.m10318test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class M10318map extends Activity {	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m10318);
        
        setViewCont();
    }

    private void setViewCont() {
		// TODO Auto-generated method stub
    	//建立WebView物件
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		//開啟HTML5 Web Storage
		webview.getSettings().setDomStorageEnabled(true);
		setContentView(webview);
		webview.loadUrl("file:///android_asset/map2/map.php");
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m10318s, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			M10318map.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
