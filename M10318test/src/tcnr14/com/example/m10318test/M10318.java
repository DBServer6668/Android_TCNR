package tcnr14.com.example.m10318test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class M10318 extends Activity {	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m10318);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m10318, menu);
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
			// 登入及編輯
			it.setClass(M10318.this, M10318spinner.class);
			startActivity(it);
			break;

		case R.id.action_settings02:
			// 關於我
			
			Uri uri = Uri.parse("http://dbserver66.er-webs.com/members/index.php");
			it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);

			break;
			
		case R.id.action_settings07:			
			// jQuery Mobile - map3
			it.setClass(M10318.this, M10318map.class);
			startActivity(it);
			break;

		case R.id.action_settings03:			
			// 結束
			M10318.this.finish();
			
			break;
		}

        return super.onOptionsItemSelected(item);
    }
}
