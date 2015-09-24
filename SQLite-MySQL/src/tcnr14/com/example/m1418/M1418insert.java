package tcnr14.com.example.m1418;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import tcnr14.com.example.m1418.providers.FriendsContentProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class M1418insert extends Activity {
	private TextView count_t;
	private Button b001, b002, b003, b004;
	private EditText e001, e002, e003, e004;
	// private FriendDbHelper dbHper;
	// private static final String DB_FILE = "friends.db";
	// private static final String DB_TABLE = "friends";
	// private static final int DBversion = 1;

	String tname, tsex, taddress;
	InputStream is = null;
	String result = null;
	String line = null;
	int code;

	private static ContentResolver mContRes;
	String[] MYCOLUMN = new String[] { "id", "name", "sex", "address" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1418insert);

		setupViewComponent();

		// 加上緒的讀取延遲 以保證撈取資料庫的動作順暢
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
	}

	private void setupViewComponent() {
		e001 = (EditText) findViewById(R.id.edtName);
		e002 = (EditText) findViewById(R.id.edtSex);
		e003 = (EditText) findViewById(R.id.edtAddr);
		count_t = (TextView) findViewById(R.id.count_t);

		mContRes = getContentResolver();

		b001 = (Button) findViewById(R.id.btnAdd);
		b001.setOnClickListener(b001on);

	}

	private Button.OnClickListener b001on = new Button.OnClickListener() {
		public void onClick(View v) {
			// 查詢name跟在e001上打得是否有有此筆資料
			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
					MYCOLUMN, null, null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯

			tname = e001.getText().toString().trim();
			tsex = e002.getText().toString().trim();
			taddress = e003.getText().toString().trim();

			if (tname.equals("") || tsex.equals("")) {
				Toast.makeText(M1418insert.this, "資料空白無法新增 !",
						Toast.LENGTH_SHORT).show();
				c.close(); // cursor close
				return;
			}

			String msg = null;
			// -------------------------
			ContentValues newRow = new ContentValues();
			newRow.put("name", tname);
			newRow.put("sex", tsex);
			newRow.put("address", taddress);
			mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
			// -------------------------

			dbinsert(); // 呼叫MySQL寫入函式

			msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + (c.getCount() + 1) + " 筆記錄 !";
			Toast.makeText(M1418insert.this, msg, Toast.LENGTH_SHORT).show();
			count_t.setText("共計:" + Integer.toString(c.getCount() + 1) + "筆");
			c.close();
		}
	};

	private void dbinsert() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("name", tname));
		nameValuePairs.add(new BasicNameValuePair("sex", tsex));
		nameValuePairs.add(new BasicNameValuePair("address", taddress));

		try {
			Thread.sleep(500);// 延遲thread睡眠0.5秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://www.oldpa.tw/android/android_insert_db.php");
			// 避免JOSN中文亂碼
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.e("Pass 1", "Connection Success");
		} catch (Exception e) {
			Log.e("Fail 1", e.toString());
		}
		try {
			// 避免JOSN中文亂碼
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.e("Pass 2", "Connection Success");

		} catch (Exception e) {
			Log.e("Fail 2", e.toString());
		}
		try {
			JSONObject json_data = new JSONObject(result);
			code = (json_data.getInt("code"));

			if (code == 1) {
			}
		} catch (Exception e) {
			Log.e("Fail 3", e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1418s, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			M1418insert.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
