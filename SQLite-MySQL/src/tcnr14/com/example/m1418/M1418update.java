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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class M1418update extends Activity {

	private TextView tvTitle;
	private Button b001, b002, b003, b004;
	private EditText e001, e002, e003, e004;
	// private FriendDbHelper dbHper;
	// private static final String DB_FILE = "friends.db";
	// private static final String DB_TABLE = "friends";
	// private static final int DBversion = 1;

	// private ArrayList<String> recSet;
	private int index = 0;
	String msg = null;
	int tcount;

	String s_id, tname, tsex, taddress;
	InputStream is = null;
	String result = null;
	String line = null;
	int code;

	private static ContentResolver mContRes;
	String[] MYCOLUMN = new String[] { "id", "name", "sex", "address" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1418update);

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
		e004 = (EditText) findViewById(R.id.edtnum);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		b001 = (Button) findViewById(R.id.btnBrowse1);
		b002 = (Button) findViewById(R.id.btnBrowse2);
		b003 = (Button) findViewById(R.id.btnup);
		b004 = (Button) findViewById(R.id.btndel);

		b001.setOnClickListener(b001on);
		b002.setOnClickListener(b002on);
		b003.setOnClickListener(b003on);
		b004.setOnClickListener(b004on);

		mContRes = getContentResolver();
		showRec(index);
	}

	private Button.OnClickListener b001on = new Button.OnClickListener() {
		public void onClick(View v) {
			index--;
			if (index < 0)
				index = tcount - 1;
			showRec(index);
		}
	};

	private Button.OnClickListener b002on = new Button.OnClickListener() {
		public void onClick(View v) {
			index++;
			if (index >= tcount)
				index = 0;
			showRec(index);
		}
	};

	private Button.OnClickListener b003on = new Button.OnClickListener() {
		public void onClick(View v) {
			Uri uri = FriendsContentProvider.CONTENT_URI;
			ContentValues contVal = new ContentValues();
			contVal = FillRec();
			
			s_id = e004.getText().toString().trim();
			tname = e001.getText().toString().trim();
			tsex = e002.getText().toString().trim();
			taddress = e003.getText().toString().trim();
			
			String whereClause = "id='" + s_id + "'";
			String[] selectionArgs = null;
			int rowsAffected = mContRes.update(uri, contVal, whereClause,
					selectionArgs);

			dbupdate(); // 呼叫MySQL修改函式

			if (rowsAffected == -1) {
				msg = "資料表已空, 無法修改 !";
			} else if (rowsAffected == 0) {
				msg = "找不到欲修改的記錄, 無法修改 !";
			} else {
				msg = "第 " + (index + 1) + " 筆記錄  已修改 ! \n" + "共 "
						+ rowsAffected + " 筆記錄   被修改 !";
				showRec(index);
			}
			Toast.makeText(M1418update.this, msg, Toast.LENGTH_SHORT).show();
		}
	};

	private void dbupdate() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", s_id));
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
					"http://www.oldpa.tw/android/android_update_db.php");
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
				Toast.makeText(getBaseContext(), "updata Successfully",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getBaseContext(), "Sorry,Try Again",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("Fail 3", e.toString());
		}
	}

	private Button.OnClickListener b004on = new Button.OnClickListener() {
		public void onClick(View v) {
			Uri uri = FriendsContentProvider.CONTENT_URI;
			s_id = e004.getText().toString().trim();
			String whereClause = "id='" + s_id + "'";
			String[] selectionArgs = null;
			int rowsAffected = mContRes.delete(uri, whereClause, selectionArgs);

			dbdelete(); // 呼叫MySQL修改函式

			if (rowsAffected == -1) {
				msg = "資料表已空, 無法刪除 !";
			} else if (rowsAffected == 0) {
				msg = "找不到欲刪除的記錄, 無法刪除 !";
			} else {
				msg = "第 " + (index + 1) + " 筆記錄  已刪除 ! \n" + "共 "
						+ rowsAffected + " 筆記錄   被刪除 !";
				showRec(0);
			}
			Toast.makeText(M1418update.this, msg, Toast.LENGTH_SHORT).show();
		}
	};

	private void dbdelete() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 nameValuePairs.add(new BasicNameValuePair("id", s_id));
		// nameValuePairs.add(new BasicNameValuePair("name", tname));
		// nameValuePairs.add(new BasicNameValuePair("sex", tsex));
		// nameValuePairs.add(new BasicNameValuePair("address", taddress));

		try {
			Thread.sleep(500);// 延遲thread睡眠0.5秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://www.oldpa.tw/android/android_delete_db.php");
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
				Toast.makeText(getBaseContext(), "updata Successfully",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getBaseContext(), "Sorry,Try Again",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("Fail 3", e.toString());
		}
	}

	private void showRec(int index) {
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				null, null, null); // 全選
		if (c == null)
			return;

		if (c.getCount() != 0) {
			String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + c.getCount()
					+ "筆";
			tcount = c.getCount();
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setText(stHead);
			c.moveToPosition(index);

			e004.setTextColor(Color.RED);
			e004.setText(c.getString(0));
			e001.setText(c.getString(1));
			e002.setText(c.getString(2));
			e003.setText(c.getString(3));
		} else {
			String stHead = "顯示資料：0 筆";
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setText(stHead);
			e004.setText("");

			e001.setText("");
			e002.setText("");
			e003.setText("");
		}
		c.close();
	}

	private ContentValues FillRec() {
		ContentValues contVal = new ContentValues();
		contVal.put("id", e004.getText().toString());
		contVal.put("name", e001.getText().toString());
		contVal.put("sex", e002.getText().toString());
		contVal.put("address", e003.getText().toString());
		return contVal;
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
			M1418update.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
