package tcnr14.com.example.m10318test;

import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import tcnr14.com.example.m10318test.providers.FriendsContentProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class M10318spinner extends Activity {

	// AsyncTask 緒
	private class Downloaddb extends AsyncTask<Void, Void, Void> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute() {
			listrefresh();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			dbmysql();
			Log.e("thread", Thread.currentThread().getName()
					+ " xxxxxxxxxxxxxxxxxxxxxdoInBackground");
			return null;
		}
	}

	private TextView tvTitle;
	private EditText e001, e002, e003, e004, e005, e006, e007;
	int tcount;
	String msg = null;

	private Intent intent;
	String gup, idname;

	protected static final int BUTTON_POSITIVE = -1;
	protected static final int BUTTON_NEGATIVE = -2;

	List<Map<String, Object>> mList;
	private Spinner mspinner;

	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "username", "password",
			"email", "gup", "address1", "address2" };

	// ScheduledFuture<?> updateTask;
	// ScheduledThreadPoolExecutor schedule = new
	// ScheduledThreadPoolExecutor(1);

	private Handler handler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m10318spinner);

		// 加上執行緒作動，進行撈取資料庫動作
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		setupViewContent();

		handler.removeCallbacks(updateData);
		handler.post(updateData);

		// updateTask = schedule.scheduleWithFixedDelay(updateData, 30, 30,
		// TimeUnit.SECONDS);
	}

	private void setupViewContent() {
		e001 = (EditText) findViewById(R.id.edtid);
		e002 = (EditText) findViewById(R.id.edtName);
		e003 = (EditText) findViewById(R.id.edtpassword);
		e004 = (EditText) findViewById(R.id.edtemail);
		e005 = (EditText) findViewById(R.id.edtgup);
		e006 = (EditText) findViewById(R.id.edtadd1);
		e007 = (EditText) findViewById(R.id.edtadd2);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		mspinner = (Spinner) this.findViewById(R.id.spiName);

		intent = this.getIntent();
		gup = intent.getStringExtra("gupname");
		idname = intent.getStringExtra("idname");
	}

	private void listrefresh() {
		mContRes = getContentResolver();
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				"gup=" + "\"" + gup + "\"", null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		tcount = c.getCount();

		tvTitle.setTextColor(Color.BLUE);
		tvTitle.setText("顯示資料： 共" + tcount + " 筆");
		e001.setTextColor(Color.RED);

		// -------------------------
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		for (int i = 0; i < tcount; i++) {
			c.moveToPosition(i);
			// adapter.add(c.getString(1));
			adapter.add(c.getString(0) + " | " + c.getString(1) + " | "
					+ c.getString(2) + " | " + c.getString(3) + " | "
					+ c.getString(4) + " | " + c.getString(5) + " | "
					+ c.getString(6));
		}
		c.close();

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mspinner.setAdapter(adapter);
		mspinner.setOnItemSelectedListener(spinneron);
	}

	// Runnable updateData = new Runnable() {
	// public void run() {
	// schedule.purge();
	// Log.e("thread", Thread.currentThread().getName() + " updateThread");
	// dbmysql();
	// Log.e("thread", Thread.currentThread().getName() + " update donec！");

	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// Log.e("thread", Thread.currentThread().getName()
	// + " UI refresh");
	// setupViewContent();
	// }
	// });
	// }
	// };

	Runnable updateData = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new Downloaddb().execute(null, null, null);
			new Downloaddb().onPostExecute();
			handler.postDelayed(this, 60000);
		}
	};

	public void onResume() {
		super.onResume();
		Log.e("1", "msg");
	}

	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(updateData);
		// Log.e("thread", "before cancel " + updateTask.isCancelled());
		// updateTask.cancel(true);
		// Log.e("thread", "after cancel " + updateTask.isCancelled());
		// schedule.shutdownNow();
		// Log.e("thread", "after shutdown " + updateTask.isCancelled());
	}

	public void onPause() {
		super.onPause();

	}

	private OnItemSelectedListener spinneron = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			int iSelect = mspinner.getSelectedItemPosition(); // 找到按何項
			String s = "資料：共" + tcount + " 筆," + "你按下  "
					+ Integer.toString(iSelect + 1) + "項"; // 起始為0
			tvTitle.setText(s);

			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
					MYCOLUMN, "gup=" + "\"" + gup + "\"", null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯
			c.moveToPosition(iSelect);

			e001.setText(c.getString(0));
			e002.setText(c.getString(1));
			e003.setText(c.getString(2));
			e004.setText(c.getString(3));
			e005.setText(c.getString(4));
			e006.setText(c.getString(5));
			e007.setText(c.getString(6));

			c.close();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			e001.setText("");
			e002.setText("");
			e003.setText("");
			e004.setText("");
		}
	};

	// ============================= 撈取SQL
	private void dbmysql() {
		// 加上執行緒作動，進行撈取資料庫動作
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads().detectDiskWrites().detectNetwork()
		// .penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
		// .build());

		mContRes = getContentResolver();
		Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI,
				MYCOLUMN, null, null, null);
		cur.moveToFirst();

		try {
			ContentValues newRow = new ContentValues();
			String result = DBConnector.executeQuery("SELECT * FROM members");
			// SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
			// jsonData = new JSONObject(result);

			// 接收MySQL前先清空SQLite(用是否有資料來判斷)
			/*
			 * if (result != null) { Uri uri =
			 * FriendsContentProvider.CONTENT_URI; mContRes.delete(uri, null,
			 * null); }
			 */
			// 接收MySQL前先清空SQLite(用連線狀況(httpcode)來判定)
			if (DBConnector.httpstate == 200) {
				Uri uri = FriendsContentProvider.CONTENT_URI;
				mContRes.delete(uri, null, null);
			} else {
				Toast.makeText(getBaseContext(), "伺服器無回應，請稍後再試",
						Toast.LENGTH_SHORT).show();
			}
			// DBConnector.httpstate = 0;
			JSONArray jsonArray = new JSONArray(result);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonData = jsonArray.getJSONObject(i);

				newRow.put("id", jsonData.getString("ID").toString());
				newRow.put("username", jsonData.getString("username")
						.toString());
				newRow.put("password", jsonData.getString("password")
						.toString());
				newRow.put("email", jsonData.getString("email").toString());
				newRow.put("gup", jsonData.getString("gup").toString());
				newRow.put("address1", jsonData.getString("address1")
						.toString());
				newRow.put("address2", jsonData.getString("address2")
						.toString());

				mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
			}
		} catch (Exception e) {
			Log.e("log_tag", e.toString());
		}
		cur.close();
		// onCreate(null); // 重構(改用Thread,故無需重複onCreate)
	}

	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m10318spinner, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		Intent it = new Intent();
		switch (item.getItemId()) {
		case R.id.action_settings04:
			// 登入
			it.setClass(M10318spinner.this, M10318sign.class);
			startActivity(it);

			break;

		case R.id.action_settings05:
			// 登出
			idname = null;
			gup = null;
			
			handler.removeCallbacks(updateData);
			// Log.e("thread", "before cancel " + updateTask.isCancelled());
			// updateTask.cancel(true);
			// Log.e("thread", "after cancel " + updateTask.isCancelled());
			// schedule.shutdownNow();
			// Log.e("thread", "after shutdown " + updateTask.isCancelled());
			
			it.setClass(M10318spinner.this, M10318.class);
			M10318spinner.this.finish();

			break;

		case R.id.action_settings06:
			// 修改
			it.putExtra("idname", idname);
			it.setClass(M10318spinner.this, M10318update.class);
			startActivity(it);

			break;

		}
		return super.onOptionsItemSelected(item);
	}
}
