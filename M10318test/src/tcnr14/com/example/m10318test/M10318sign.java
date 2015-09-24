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
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class M10318sign extends Activity {
	private Button b001;
	private EditText e001, e002;

	String tname, tpass, taddress, code1, gup, id;
	int code;

	InputStream is = null;
	String result = null;
	String line = null;

	//private static ContentResolver mContRes;
	String[] MYCOLUMN = new String[] { "id", "username", "password", "email" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m10318sign);

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
		e002 = (EditText) findViewById(R.id.edtpassword);

		//mContRes = getContentResolver();

		b001 = (Button) findViewById(R.id.btnnx);
		b001.setOnClickListener(b001on);

	}

	private Button.OnClickListener b001on = new Button.OnClickListener() {
		public void onClick(View v) {
			// 查詢name跟在e001上打得是否有有此筆資料
			// Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
			// MYCOLUMN, null, null, null);
			// c.moveToFirst(); // 一定要寫，不然會出錯

			tname = e001.getText().toString().trim();
			tpass = e002.getText().toString().trim();

			dbinsert();

		}

		private void dbinsert() {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("email", tname));
			nameValuePairs.add(new BasicNameValuePair("password", tpass));
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://dbserverxu.byethost14.com/M10318TEST/android_sign_db.php");
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpResponse response;
			try {
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
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf8"), 8);
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
				Log.e("pass 2", result + "");
			} catch (Exception e) {
				Log.e("Fail 2", e.toString());
			}
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json_data = jsonArray.getJSONObject(i);
					code1 = (json_data.getString("state").toString());
					id = (json_data.getString("ID").toString());
					gup = (json_data.getString("gup").toString());
				}

				Log.e("2", code + gup);
				if (code == 0) {
					Intent it = new Intent();
					it.setClass(M10318sign.this, M10318spinner.class);
					it.putExtra("gupname", gup);
					it.putExtra("idname", id);
					startActivity(it);
					M10318sign.this.finish();
				} else {
					Toast.makeText(getBaseContext(), "Sorry,Try Again",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				Log.e("Fail 3", e.toString());
				Toast.makeText(getBaseContext(), "Sorry,Try Again",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

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
			M10318sign.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
