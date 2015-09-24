package com.tcnr14.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.tcnr14.example.providers.FriendsContentProvider;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Fragment_Query extends Fragment {
	private TextView tvTitle;
	private EditText edtid, edtname, edtbioup, edtbiodw, edthear, edtweg,
			edtdtime, edtdtext, edtmtime, edtmtext, edtcoent;
	private Button btnup, btndel;
	private View view;
	int tcount;
	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "name", "bioup", "biodw",
			"hear", "weg", "dtime", "dtext", "mtime", "mtext", "coent" };

	protected static final int BUTTON_POSITIVE = -1;
	protected static final int BUTTON_NEGATIVE = -2;

	List<Map<String, Object>> mList;
	private Spinner mspinner;
	String s_id;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_query, container, false);

		// 加上緒的讀取延遲 以保證撈取資料庫的動作順暢
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		setupWebView(view);
		return view;
	}

	private void setupWebView(View view) {
		// TODO Auto-generated method stub

		// edtid = (EditText) view.findViewById(R.id.edtid);
		edtname = (EditText) view.findViewById(R.id.edtname);
		edtbioup = (EditText) view.findViewById(R.id.edtbioup);
		edtbiodw = (EditText) view.findViewById(R.id.edtbiodw);
		edthear = (EditText) view.findViewById(R.id.edthear);
		edtweg = (EditText) view.findViewById(R.id.edtweg);
		edtdtime = (EditText) view.findViewById(R.id.edtdtime);
		edtdtext = (EditText) view.findViewById(R.id.edtdtext);
		edtmtime = (EditText) view.findViewById(R.id.edtmtime);
		edtmtext = (EditText) view.findViewById(R.id.edtmtext);
		edtcoent = (EditText) view.findViewById(R.id.edtcoent);

		btnup = (Button) view.findViewById(R.id.btnup);
		btndel = (Button) view.findViewById(R.id.btndel);

		edtdtime.setOnClickListener(dtimeon);
		edtmtime.setOnClickListener(mtimeon);
		btnup.setOnClickListener(btnupON);
		btndel.setOnClickListener(btndelON);

		tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		mspinner = (Spinner) this.view.findViewById(R.id.spiName);

		mContRes = getActivity().getContentResolver();
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		tcount = c.getCount();

		tvTitle.setTextColor(Color.BLUE);
		tvTitle.setText("顯示資料： 共" + tcount + " 筆");
		// edtid.setTextColor(Color.RED);

		// -------------------------
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item);
		for (int i = 0; i < tcount; i++) {
			c.moveToPosition(i);
			adapter.add(c.getString(0) + " | " + c.getString(1) + " | "
					+ c.getString(6) + " | " + c.getString(8));
		}
		c.close();

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mspinner.setAdapter(adapter);
		mspinner.setOnItemSelectedListener(spinneron);
	}

	View.OnClickListener dtimeon = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Calendar now = Calendar.getInstance();

			DatePickerDialog datePicDlg = new DatePickerDialog(getActivity(),
					datePicDlgbuy, now.get(Calendar.YEAR),
					now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
			datePicDlg.setTitle("選擇日期");
			datePicDlg.setMessage("請選擇就醫日期");
			datePicDlg.setIcon(android.R.drawable.ic_menu_month);
			datePicDlg.setCancelable(false);
			datePicDlg.show();

		}
	};

	private DatePickerDialog.OnDateSetListener datePicDlgbuy = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// String s = getString(R.string.tdtime);
			String myDate = String.valueOf(dayOfMonth);
			String myMonth = String.valueOf(monthOfYear);

			if (myDate.length() == 1) {
				myDate = "0" + dayOfMonth;
			}
			if (myMonth.length() == 1 && monthOfYear < 9) {
				myMonth = "0" + (monthOfYear + 1);
			} else {
				myMonth = String.valueOf((monthOfYear + 1));
			}
			// dtime.setText(s + year + " / " + myMonth + " / " + myDate);
			edtdtime.setText(year + " / " + myMonth + " / " + myDate);
		}
	};

	View.OnClickListener mtimeon = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Calendar now = Calendar.getInstance();

			TimePickerDialog timePicDlg = new TimePickerDialog(getActivity(),
					datePicDlgexpir, now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), true);
			timePicDlg.setTitle("選擇時間");
			timePicDlg.setMessage("請選擇用藥時間");
			timePicDlg.setIcon(android.R.drawable.ic_menu_month);
			timePicDlg.setCancelable(false);
			timePicDlg.show();

		}
	};

	private TimePickerDialog.OnTimeSetListener datePicDlgexpir = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String s = getString(R.string.tmtime);
			String mymin = String.valueOf(minute);
			String myhour = String.valueOf(hourOfDay);
			// mtime.setText(s + myhour + " : " + mymin);
			edtmtime.setText(myhour + " : " + mymin);
		}

	};

	private OnItemSelectedListener spinneron = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			int iSelect = mspinner.getSelectedItemPosition(); // 找到按何項
			String s = "資料：共" + tcount + " 筆," + "你按下  "
					+ Integer.toString(iSelect + 1) + "項"; // 起始為0
			tvTitle.setText(s);

			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
					MYCOLUMN, null, null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯
			c.moveToPosition(iSelect);

			s_id=c.getString(0);
			// edtid.setText(c.getString(0));
			edtname.setText(c.getString(1));
			edtbioup.setText(c.getString(2));
			edtbiodw.setText(c.getString(3));
			edthear.setText(c.getString(4));
			edtweg.setText(c.getString(5));
			edtdtime.setText(c.getString(6));
			edtdtext.setText(c.getString(7));
			edtmtime.setText(c.getString(8));
			edtmtext.setText(c.getString(9));
			edtcoent.setText(c.getString(10));

			c.close();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// edtid.setText("");
			edtname.setText("");
			edtdtime.setText("");
			edtmtime.setText("");
		}
	};

	private Button.OnClickListener btnupON = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri = FriendsContentProvider.CONTENT_URI;
			ContentValues contVal = new ContentValues();
			contVal = FillRec();
			String whereClause = "id='" + s_id + "'";
			String[] selectionArgs = null;
			mContRes.update(uri, contVal, whereClause, selectionArgs);
			Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
			setupWebView(view);
		}

	};

	private Button.OnClickListener btndelON = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri = FriendsContentProvider.CONTENT_URI;
			String whereClause = "id='" + s_id + "'";
			String[] selectionArgs = null;
			mContRes.delete(uri, whereClause, selectionArgs);
			Toast.makeText(getActivity(), "已刪除", Toast.LENGTH_SHORT).show();
			setupWebView(view);
		}
	};
	
	private ContentValues FillRec() { // user define method
		ContentValues contVal = new ContentValues();
		contVal.put("name", edtname.getText().toString());
		contVal.put("bioup", edtbioup.getText().toString());
		contVal.put("biodw", edtbiodw.getText().toString());
		contVal.put("hear", edthear.getText().toString());
		contVal.put("weg", edtweg.getText().toString());
		contVal.put("dtime", edtdtime.getText().toString());
		contVal.put("dtext", edtdtext.getText().toString());
		contVal.put("mtime", edtmtime.getText().toString());
		contVal.put("mtext", edtmtext.getText().toString());
		contVal.put("coent", edtcoent.getText().toString());
		return contVal;
		
	}

}