package tcnr14.com.example.m1418;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tcnr14.com.example.m1418.providers.FriendsContentProvider;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class M1418list extends ListActivity {
	// private FriendDbHelper dbHper;
	// private static final String DB_FILE = "friends.db";
	// private static final String DB_TABLE = "friends";
	// private static final int DBversion = 1;
	// private ArrayList<String> recSet;
	// private int index = 0;

	String msg = null;
	private TextView tvTitle;

	List<Map<String, Object>> mList;

	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "name", "sex", "address" };

	int tcount;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1418list);

		setupViewContent();
	}

	private void setupViewContent() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		mContRes = getContentResolver();
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		tcount = c.getCount();

		tvTitle.setTextColor(Color.BLUE);
		tvTitle.setText("顯示資料： 共 " + tcount + " 筆");
		// ---------------------------------------------------------
		// string to int(字串轉整數)
		// 1. int intValue = Integer.valueOf("12345");
		// 2. int intValue = Integer.parseInt("12345");
		// int to String(整數轉字串)
		// 1. String stringValue = Integer.toString(12345);
		// 2. String stringValue = String.valueOf(12345);
		// 3. String stringValue = new String(""+12345);
		// 目前資料筆數 => recSet.size()
		// -----------------------------------------------------------------------------
		// Log.d("debug", "錯誤中斷點");

		mList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < tcount; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			c.moveToPosition(i);
			item.put("imgView", android.R.drawable.ic_menu_my_calendar);
			item.put(
					"txtView",
					c.getString(0) + c.getString(1) + c.getString(2)
							+ c.getString(3));
			mList.add(item);
		}
		c.close();

		SimpleAdapter adapter = new SimpleAdapter(this, mList,
				R.layout.list_item, new String[] { "imgView", "txtView" },
				new int[] { R.id.imgView, R.id.txtView });

		setListAdapter(adapter);

		ListView listview = getListView();
		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(listviewOnItemClkLis);
	}

	AdapterView.OnItemClickListener listviewOnItemClkLis = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// When clicked, show a toast with the TextView text
			String s = "你按下 "
					+ ((TextView) view.findViewById(R.id.txtView)).getText()
							.toString() + "listview " + "位於第"
					+ Integer.toString(position + 1) + "筆資料";
			tvTitle.setText(s);
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
			M1418list.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
