package tcnr14.com.example.m1418;

import tcnr14.com.example.m1418.providers.FriendsContentProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class M1418query extends Activity {
	private Button b001, b002, b003, b004;
	private EditText e001, e002, e003, e004;
	// private FriendDbHelper dbHper;
	// private static final String DB_FILE = "friends.db";
	// private static final String DB_TABLE = "friends";
	// private static final int DBversion = 1;

	private static ContentResolver mContRes;
	String[] MYCOLUMN = new String[] { "id", "name", "sex", "address" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1418query);

		setupViewComponent();
	}

	private void setupViewComponent() {
		e001 = (EditText) findViewById(R.id.edtName);
		e002 = (EditText) findViewById(R.id.edtSex);
		e003 = (EditText) findViewById(R.id.edtAddr);
		e004 = (EditText) findViewById(R.id.edtList);

		mContRes = getContentResolver();

		b001 = (Button) findViewById(R.id.btnQuery);
		b001.setOnClickListener(b001on);
	}

	private Button.OnClickListener b001on = new Button.OnClickListener() {
		public void onClick(View v) {
			// 檢查使用哪一個欄位查詢, cursor 增加 where 條件.
			Cursor c = null;

			if (!e001.getText().toString().equals("")) {
				c = mContRes.query(FriendsContentProvider.CONTENT_URI,
						MYCOLUMN, "name=" + "\"" + e001.getText().toString()
								+ "\"", null, null);
			} else if (!e002.getText().toString().equals("")) {
				c = mContRes.query(FriendsContentProvider.CONTENT_URI,
						MYCOLUMN, "sex=" + "\"" + e002.getText().toString()
								+ "\"", null, null);
			} else if (!e003.getText().toString().equals("")) {
				c = mContRes.query(FriendsContentProvider.CONTENT_URI,
						MYCOLUMN, "address=" + "\"" + e003.getText().toString()
								+ "\"", null, null);
			}

			if (c == null)
				return;

			if (c.getCount() == 0) {
				e004.setText("");
				Toast.makeText(M1418query.this, "沒有這筆資料", Toast.LENGTH_LONG)
						.show();
			} else {
				c.moveToFirst();
				e004.setText(c.getString(0) + " " + c.getString(1) + " "
						+ c.getString(2));

				while (c.moveToNext())
					e004.append("\n" + c.getString(0) + " " + c.getString(1)
							+ " " + c.getString(2));
			}
			c.close();
		}
	};

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
			M1418query.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
