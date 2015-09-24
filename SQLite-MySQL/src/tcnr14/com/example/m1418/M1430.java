package tcnr14.com.example.m1418;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class M1430 extends Activity {
	private Button button_get_record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1430);

		findViews();
		setListeners();

		// 加上緒的讀取延遲 以保證撈取資料庫的動作順暢
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
	}

	private void findViews() {
		button_get_record = (Button) findViewById(R.id.get_record);
	}

	private void setListeners() {
		button_get_record.setOnClickListener(getDBRecord);
	}

	private Button.OnClickListener getDBRecord = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TableLayout user_list = (TableLayout) findViewById(R.id.user_list);
			user_list.setStretchAllColumns(true);
			TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TableRow.LayoutParams view_layout = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			try {
				String result = DBConnector
						.executeQuery("SELECT * FROM friends");
				/*
				 * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
				 * jsonData = new JSONObject(result);
				 */
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonData = jsonArray.getJSONObject(i);
					TableRow tr = new TableRow(M1430.this);
					tr.setLayoutParams(row_layout);
					tr.setGravity(Gravity.CENTER_HORIZONTAL);

					TextView user_na = new TextView(M1430.this);
					user_na.setText(jsonData.getString("Name"));
					user_na.setLayoutParams(view_layout);

					TextView user_se = new TextView(M1430.this);
					user_se.setText(jsonData.getString("Sex"));
					user_se.setLayoutParams(view_layout);

					TextView user_ad = new TextView(M1430.this);
					user_ad.setText(jsonData.getString("addres"));
					user_ad.setLayoutParams(view_layout);

					tr.addView(user_na);
					tr.addView(user_se);
					tr.addView(user_ad);
					user_list.addView(tr);
				}
			} catch (Exception e) {
				// Log.e("log_tag", e.toString());
			}
		}
	};

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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
