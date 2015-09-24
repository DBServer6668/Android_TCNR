package tcnr14.com.example.m10318test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import tcnr14.com.example.m10318test.providers.FriendsContentProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
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

public class M10318update extends Activity {
	private TextView tvTitle;
	private Button b001, b002;
	private EditText e000, e001, e002, e003, e004, e005, e006;

	private int index = 0;
	String msg = null;
	int tcount;

	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "username", "password",
			"email", "gup", "address1", "address2" };

	String s_id, tname, tpass, temail, tgup, tadd1, tadd2, idname;
	InputStream is = null;
	String result = null;
	String line = null;
	int code;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m10318update);
		setupViewComponent();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
	}

	private void showRec(int index) {
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				"id=" + "\"" + idname + "\"", null, null); 
		if (c == null)
			return;

		if (c.getCount() != 0) {
			String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + c.getCount()
					+ " 筆";
			tcount = c.getCount();
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setText(stHead);
			c.moveToPosition(index);

			e000.setTextColor(Color.RED);
			e000.setText(c.getString(0));
			e001.setText(c.getString(1));
			e002.setText(c.getString(2));
			e003.setText(c.getString(3));
			e004.setText(c.getString(4));
			e005.setText(c.getString(5));
			e006.setText(c.getString(6));
			//
		} else {
			String stHead = "顯示資料：0 筆";
			tvTitle.setTextColor(Color.BLUE);
			tvTitle.setText(stHead);
			e000.setText("");
			e001.setText("");
			e002.setText("");
			e003.setText("");
		}
		c.close();

	}

	private void setupViewComponent() {
		e000 = (EditText) findViewById(R.id.edtid);
		e001 = (EditText) findViewById(R.id.edtName);
		e002 = (EditText) findViewById(R.id.edtpassword);
		e003 = (EditText) findViewById(R.id.edtemail);
		e004 = (EditText) findViewById(R.id.edtgup);
		e005 = (EditText) findViewById(R.id.edtadd1);
		e006 = (EditText) findViewById(R.id.edtadd2);

		tvTitle = (TextView) findViewById(R.id.m1407_tit);

		b001 = (Button) findViewById(R.id.btnup);
		b002 = (Button) findViewById(R.id.btndel);

		b001.setOnClickListener(b001on);
		b002.setOnClickListener(b002on);

		intent = this.getIntent();
		idname = intent.getStringExtra("idname");
		mContRes = getContentResolver();
		showRec(index);

	}

	private Button.OnClickListener b001on = new Button.OnClickListener() {

		public void onClick(View v) {
			Uri uri = FriendsContentProvider.CONTENT_URI;
			ContentValues contVal = new ContentValues();
			contVal = FillRec();
			s_id = e000.getText().toString().trim();
			tname = e001.getText().toString().trim();
			tpass = e002.getText().toString().trim();
			temail = e003.getText().toString().trim();
			tgup = e004.getText().toString().trim();
			tadd1 = e005.getText().toString().trim();
			tadd2 = e006.getText().toString().trim();

			String whereClause = "id='" + s_id + "'";
			String[] selectionArgs = null;
			int rowsAffected = mContRes.update(uri, contVal, whereClause,
					selectionArgs);

			mysql_update();

			if (rowsAffected == -1) {
				msg = "資料表已空，無法修改!";
			} else {
				msg = "第 " + (index + 1) + " 筆記錄  已修改 ! \n" + "共 "
						+ rowsAffected + " 筆記錄   被修改 !";
				showRec(index);
			}
			Toast.makeText(M10318update.this, msg, Toast.LENGTH_SHORT).show();
		}
	};

	private Button.OnClickListener b002on = new Button.OnClickListener() {
		public void onClick(View v) {
			Uri uri = FriendsContentProvider.CONTENT_URI;
			s_id = e001.getText().toString().trim();
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
			Toast.makeText(M10318update.this, msg, Toast.LENGTH_SHORT).show();
		}
	};

	private ContentValues FillRec() { 
		ContentValues contVal = new ContentValues();
		contVal.put("id", e000.getText().toString());
		contVal.put("username", e001.getText().toString());
		contVal.put("password", e002.getText().toString());
		contVal.put("email", e003.getText().toString());
		contVal.put("gup", e004.getText().toString());
		contVal.put("address1", e005.getText().toString());
		contVal.put("address2", e006.getText().toString());
		return contVal;
	}

	protected void mysql_update() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", s_id));
		nameValuePairs.add(new BasicNameValuePair("name", tname));
		nameValuePairs.add(new BasicNameValuePair("password", tpass));
		nameValuePairs.add(new BasicNameValuePair("email", temail));
		nameValuePairs.add(new BasicNameValuePair("gup", tgup));
		nameValuePairs.add(new BasicNameValuePair("add1", tadd1));
		nameValuePairs.add(new BasicNameValuePair("add2", tadd2));
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://dbserverxu.byethost14.com/M10318TEST/android_update_db.php");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			HttpResponse response;
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			try {
				is = entity.getContent();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("pass 1", "connection success");

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf8"), 8);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();

			Log.e("pass 2", "connection success");
		} catch (Exception e) {
			Log.e("Fail 2", e.toString());
		}
		try {
			JSONObject json_data = new JSONObject(result);
			code = (json_data.getInt("success"));
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

	private void dbdelete() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", s_id));

		try {
			Thread.sleep(500);// 延遲thread睡眠0.5秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://dbserverxu.byethost14.com/M10318TEST/android_delete_db.php");
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
			M10318update.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
