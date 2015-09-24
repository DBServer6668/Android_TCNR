package tcnr14.com.example.m1418;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import tcnr14.com.example.m1418.providers.FriendsContentProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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

public class M1418spinner extends Activity {
	private TextView tvTitle, txtTime;
	// private Button b001, b002, b003, b004;
	private EditText e001, e002, e003, e004;
	// private FriendDbHelper dbHper;
	// private static final String DB_FILE = "friends.db";
	// private static final String DB_TABLE = "friends";
	// private static final int DBversion = 1;

	protected static final int BUTTON_POSITIVE = -1;
	protected static final int BUTTON_NEGATIVE = -2;

	// private ArrayList<String> recSet;
	// private int index = 0;

	String msg = null;

	List<Map<String, Object>> mList;
	private Spinner mspinner;

	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "name", "sex", "address" };

	int tcount;

	String strTime = "";
	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 格式化資料時分秒
	ScheduledFuture<?> updateTask;
	ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1418spinner);

		// 加上執行緒作動，進行撈取資料庫動作
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		setupViewContent();

		// schedule(Runnable command,longdelay,TimeUnitunit);
		// setContinueExistingPeriodicTasksAfterShutdownPolicy(booleanvalue)
		// scheduleAtFixedRate(Runnablecommand,longinitialDelay,longperiod,TimeUnitunit);
		// scheduleWithFixedDelay(Runnablecommand,longinitialDelay,longdelay,TimeUnitunit);
		// setExecuteExistingDelayedTasksAfterShutdownPolicy(booleanvalue)
		updateTask = schedule.scheduleWithFixedDelay(updateData, 30, 30,
				TimeUnit.SECONDS);
	}

	private void setupViewContent() {
		e001 = (EditText) findViewById(R.id.edtName);
		e002 = (EditText) findViewById(R.id.edtSex);
		e003 = (EditText) findViewById(R.id.edtAddr);
		e004 = (EditText) findViewById(R.id.edtnum);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		mspinner = (Spinner) this.findViewById(R.id.spinner1);
		txtTime = (TextView) findViewById(R.id.txtTime);

		mContRes = getContentResolver();
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		tcount = c.getCount();

		tvTitle.setTextColor(Color.BLUE);
		tvTitle.setText("顯示資料： 共" + tcount + " 筆");
		e004.setTextColor(Color.RED);

		// -------------------------
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		for (int i = 0; i < tcount; i++) {
			c.moveToPosition(i);
			adapter.add(c.getString(0) + " | " + c.getString(1) + " | "
					+ c.getString(2) + " | " + c.getString(3));
		}
		c.close();

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mspinner.setAdapter(adapter);
		mspinner.setOnItemSelectedListener(spinneron);
	}

	Runnable updateData = new Runnable() {
		public void run() {
			schedule.purge();
			Log.e("thread", Thread.currentThread().getName() + " updateThread");
			dbmysql();
			Log.e("thread", Thread.currentThread().getName() + " update donec！");
			strTime += " ->" + df.format(System.currentTimeMillis());
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.e("thread", Thread.currentThread().getName()
							+ " UI refresh");
					setupViewContent();
					txtTime.setText(strTime);
				}
			});
		}
	};

	private OnItemSelectedListener spinneron = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			int iSelect = mspinner.getSelectedItemPosition(); // 找到按何項
			String s = "資料：共" + tcount + " 筆," + "你按下  "
					+ Integer.toString(iSelect + 1) + "項"; // 起始為0
			tvTitle.setText(s);
			e004.setTextColor(Color.RED);

			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
					MYCOLUMN, null, null, null);
			c.moveToFirst();
			c.moveToPosition(iSelect);

			e004.setText(c.getString(0));
			e001.setText(c.getString(1));
			e002.setText(c.getString(2));
			e003.setText(c.getString(3));

			c.close();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			e004.setText("");
			e001.setText("");
			e002.setText("");
			e003.setText("");
		}
	};

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ------------Android程式的生命週期------------------------------
	// 第一次執行程式時，進行起始啟動 OnCreate() > OnStart() > OnResume()
	// 暫停Pause
	// 按下HOME按鈕 OnPause() > OnStop()，程式只是暫停而停止，
	// 當再次啟動程式時 OnReStart() > OnStart() > OnResume()。
	// • 當呼叫Intent時，程式會先暫停(OnPause() > OnStop()，然後返回時 OnReStart() > OnStart() >
	// OnResume())。
	// 結束Destroy
	// 按下BACK按鈕或呼叫finish()方法，則會真正的結束程式 OnPause() > OnStop() > onDestroy()，
	// 當再次執行程式時,就如同起始啟動
	// • 當旋轉螢幕時，程式會先結束，然後再啟始起動(OnPause() > OnStop() > onDestroy() > OnCreate() >
	// OnStart() > OnResume())。
	// 機器休眠待機時
	// 此時可分為兩種情形
	// • 程式在執行狀態：經過多個Pause,Resume,Stop事件後，最後會Destory。當機器醒來時，程式會重新起始啟動OnCreate()
	// > OnStart() > OnResume()。
	// • 程式在暫停狀態：當機器醒來重新執行程式時，就如同從暫停模式回來一樣 OnReStart()>OnStart()>OnResume()。
	// //////////////////////////////////////////////////////////////////////////////////////////

	protected void onRestart() {
		super.onRestart();
		// Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show();
	}

	protected void onStart() {
		super.onStart();
		// Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show();
	}

	protected void onResume() {
		super.onResume();
		// Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
		// 在生命周期的onResume處置入撈取MySQL函式 即可實現自動更新效果
		dbmysql();

		onCreate(null); // 重構
	}

	protected void onPause() {
		super.onPause();
		// Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
	}

	protected void onStop() {
		super.onStop();
		// Toast.makeText(this, "onStop", Toast.LENGTH_LONG).show();
	}

	protected void onDestroy() {
		super.onDestroy();
		// Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();

		Log.e("thread", "before cancel " + updateTask.isCancelled());
		updateTask.cancel(true);
		Log.e("thread", "after cancel " + updateTask.isCancelled());
		schedule.shutdownNow();
		Log.e("thread", "after shutdown " + updateTask.isCancelled());
	}

	public void onBackPressed() { // 返回鍵
		super.onBackPressed();
		// Toast.makeText(this, "onBackPressed",Toast.LENGTH_SHORT).show();
	}

	public void closeContextMenu() {
		super.closeContextMenu();
		// Toast.makeText(this, "closeContextMenu",Toast.LENGTH_SHORT).show();
	}

	public void closeOptionsMenu() {
		super.closeOptionsMenu();
		// Toast.makeText(this, "closeOptionsMenu",Toast.LENGTH_SHORT).show();
	}

	private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
			switch (id) {
			case BUTTON_POSITIVE:
				Uri uri = FriendsContentProvider.CONTENT_URI;
				mContRes.delete(uri, null, null); // 刪除所有資料
				msg = "資料表已空 !";
				Toast.makeText(M1418spinner.this, msg, Toast.LENGTH_SHORT)
						.show();
				break;
			case BUTTON_NEGATIVE:
				msg = "放棄刪除所有資料 !";
				Toast.makeText(M1418spinner.this, msg, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			onCreate(null);
		}
	};

	// 撈取SQL
	private void dbmysql() {
		// 加上執行緒作動，進行撈取資料庫動作
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		mContRes = getContentResolver();
		Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI,
				MYCOLUMN, null, null, null);
		cur.moveToFirst();

		try {
			String result = DBConnector.executeQuery("SELECT * FROM friends");
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
			//DBConnector.httpstate = 0;
			JSONArray jsonArray = new JSONArray(result);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonData = jsonArray.getJSONObject(i);

				ContentValues newRow = new ContentValues();
				newRow.put("id", jsonData.getString("id").toString());
				newRow.put("name", jsonData.getString("name").toString());
				newRow.put("sex", jsonData.getString("sex").toString());
				newRow.put("address", jsonData.getString("address").toString());

				mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
			}
		} catch (Exception e) {
			// Log.e("log_tag", e.toString());
		}
		cur.close();
		//onCreate(null); // 重構(改用Thread,故無需重複onCreate)
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1418, menu);
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
			// 新增
			it.setClass(M1418spinner.this, M1418insert.class);
			startActivity(it);
			break;

		case R.id.action_settings02:
			// 查詢
			it.setClass(M1418spinner.this, M1418query.class);
			startActivity(it);
			break;

		case R.id.action_settings03:
			// 修改
			it.setClass(M1418spinner.this, M1418update.class);
			startActivity(it);
			break;

		case R.id.action_settings04:
			// 清空
			MyAlertDialog aldDial = new MyAlertDialog(M1418spinner.this);
			aldDial.setTitle("清空資料");
			aldDial.setMessage("確定將所有資料刪除嗎?");
			aldDial.setIcon(R.drawable.ic_launcher);
			aldDial.setCancelable(false);
			aldDial.setButton(BUTTON_POSITIVE, "確定清空", aldBtListener);
			aldDial.setButton(BUTTON_NEGATIVE, "取消清空", aldBtListener);
			aldDial.show();
			break;

		case R.id.action_settings05:
			// 批次增加
			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
					MYCOLUMN, null, null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯
			String msg = null;
			// -------------------------
			ContentValues newRow = new ContentValues();
			for (int i = 0; i < 10; i++) {
				newRow.put("name", "測試" + Integer.toString(i + 1));
				newRow.put("sex", "男");
				newRow.put("address", "台中市工業一路" + Integer.toString(i + 100) // Integer.toString(c.getCount()+1)
						+ "號");
				mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
			}
			// -------------------------
			c.moveToFirst(); // 一定要寫，不然會出錯
			tcount = c.getCount();
			// ---------------------------
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setText("顯示資料： 共" + tcount + " 筆");
			msg = "新增記錄  成功 ! ";
			Toast.makeText(M1418spinner.this, msg, Toast.LENGTH_SHORT).show();

			c.close();
			onCreate(null); // 重構
			break;

		case R.id.action_settings06:
			// 列表
			it.setClass(M1418spinner.this, M1418list.class);
			startActivity(it);

			break;

		/*
		 * case R.id.action_settings07: // 下拉式列表 it.setClass(M1418.this,
		 * M1418spinner.class); startActivity(it); break;
		 */

		case R.id.action_settings08:
			// 測試
			it.setClass(M1418spinner.this, M1430.class);
			startActivity(it);

			break;

		case R.id.action_settings09:
			// 匯入MySQL
			dbmysql();

			break;

		case R.id.action_settings:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
