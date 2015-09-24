package com.tcnr14.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tcnr14.example.providers.FriendsContentProvider;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Fragment_Instert extends Fragment {
	private Button btnadd;
	private EditText edtname, edtbioup, edtbiodw, edthear, edtweg, edtdtext,
			edtmtext, edtcoent;
	private TextView dtime, mtime;
	private View view;
	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "name", "bioup", "biodw",
			"hear", "weg", "dtime", "dtext", "mtime", "mtext", "coent" };
	String tname, tbioup, tbiodw, thear, tweg, tdtime, tdtext, tmtime, tmtext,
			tcoent;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_insert, container, false);

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
		dtime = (TextView) view.findViewById(R.id.dtime);
		mtime = (TextView) view.findViewById(R.id.mtime);

		edtname = (EditText) view.findViewById(R.id.edtname);
		edtbioup = (EditText) view.findViewById(R.id.edtbioup);
		edtbiodw = (EditText) view.findViewById(R.id.edtbiodw);
		edthear = (EditText) view.findViewById(R.id.edthear);
		edtweg = (EditText) view.findViewById(R.id.edtweg);
		edtdtext = (EditText) view.findViewById(R.id.edtdtext);
		edtmtext = (EditText) view.findViewById(R.id.edtmtext);
		edtcoent = (EditText) view.findViewById(R.id.edtcoent);

		btnadd = (Button) view.findViewById(R.id.btnadd);

		dtime.setOnClickListener(dtimeon);
		mtime.setOnClickListener(mtimeon);

		btnadd.setOnClickListener(btnaddon);

		mContRes = getActivity().getContentResolver();
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
			dtime.setText(year + " / " + myMonth + " / " + myDate);
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
			mtime.setText(myhour + " : " + mymin);
		}

	};

	private Button.OnClickListener btnaddon = new Button.OnClickListener() {
		public void onClick(View v) {
			// 查詢name跟在e001上打得是否有有此筆資料
			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI,
					MYCOLUMN, null, null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯

			tname = edtname.getText().toString().trim();
			tbioup = edtbioup.getText().toString().trim();
			tbiodw = edtbiodw.getText().toString().trim();
			thear = edthear.getText().toString().trim();
			tweg = edtweg.getText().toString().trim();
			tdtime = dtime.getText().toString().trim();
			tdtext = edtdtext.getText().toString().trim();
			tmtime = mtime.getText().toString().trim();
			tmtext = edtmtext.getText().toString().trim();
			tcoent = edtcoent.getText().toString().trim();

			if (tname.equals("") || tbioup.equals("") || tbiodw.equals("")) {
				Toast.makeText(getActivity(), "資料空白無法新增 !", Toast.LENGTH_SHORT)
						.show();
				c.close(); // cursor close
				return;
			}

			String msg = null;
			// -------------------------
			ContentValues newRow = new ContentValues();
			newRow.put("name", tname);
			newRow.put("bioup", tbioup);
			newRow.put("biodw", tbiodw);
			newRow.put("hear", thear);
			newRow.put("weg", tweg);
			newRow.put("dtime", tdtime);
			newRow.put("dtext", tdtext);
			newRow.put("mtime", tmtime);
			newRow.put("mtext", tmtext);
			newRow.put("coent", tcoent);

			mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
			// -------------------------

			// dbinsert(); // 呼叫MySQL寫入函式

			msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + (c.getCount() + 1) + " 筆記錄 !";
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			// count_t.setText("共計:" + Integer.toString(c.getCount() + 1) +
			// "筆");
			c.close();
		}
	};

}