package tcnr14.com.example.m1401;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class M1401 extends Activity {

	private TextView t002, t003; // 宣告變數
	private ImageButton b001, b002, b003;
	private ImageView t001;
	private RelativeLayout layout;
	private MediaPlayer m001, m002, m003;

	int con = 0, pyw = 0, cow = 0, drow = 0;
	private Button b004, b005, b006, b007;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1401);

		setViewContent(); //
	}

	private void setViewContent() {

		t001 = (ImageView) findViewById(R.id.m1401_imv001); // 建立物件
		t002 = (TextView) findViewById(R.id.m1401_t005);
		t003 = (TextView) findViewById(R.id.m1401_t006);

		b001 = (ImageButton) findViewById(R.id.m1401_imb001); // 剪刀
		b002 = (ImageButton) findViewById(R.id.m1401_imb002); // 石頭
		b003 = (ImageButton) findViewById(R.id.m1401_imb003); // 布

		b004 = (Button) findViewById(R.id.m1401_b004);
		b005 = (Button) findViewById(R.id.m1401_b005);
		b006 = (Button) findViewById(R.id.m1401_b006);
		b007 = (Button) findViewById(R.id.m1401_b007);

		m001 = MediaPlayer.create(M1401.this, R.raw.m001); // 音樂物件
		m002 = MediaPlayer.create(M1401.this, R.raw.m002);
		m003 = MediaPlayer.create(M1401.this, R.raw.m003);

		layout = (RelativeLayout) findViewById(R.id.m1401_alayout);

		b001.setOnClickListener(b000on);
		b002.setOnClickListener(b000on);
		b003.setOnClickListener(b000on);
		b004.setOnClickListener(b004on);
		b005.setOnClickListener(b005on);
		b006.setOnClickListener(b006on);
		b007.setOnClickListener(b007on);// 宣告Button作動(監聽)事件
	}

	public Button.OnClickListener b000on = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {

			b001.getBackground().setAlpha(120); // 設定透明度 : 0~255
			b002.getBackground().setAlpha(120); // 0 透明 ; 255 不透明
			b003.getBackground().setAlpha(120);

			layout.setBackgroundResource(R.drawable.p001);

			switch (v.getId()) { // 擷取目前作動ID

			case (R.id.m1401_imb001): // 使用者輸入剪刀石頭布
				con++;
				str1(0); // 呼叫t002輸出函數
				b001.getBackground().setAlpha(250); // 作動按鈕變色
				math(1); // 呼叫判斷函數 丟入1(剪刀)
				break;

			case (R.id.m1401_imb002):
				con++;
				str1(1);
				b002.getBackground().setAlpha(250);
				math(2); // 呼叫判斷函數 丟入2(石頭)
				break;

			case (R.id.m1401_imb003):
				con++;
				str1(2);
				b003.getBackground().setAlpha(250);
				math(3); // 呼叫判斷函數 丟入3(布)
				break;
			}
		}
	};

	public void math(int n) { // 比較函數 接收int判斷值n

		int icp = (int) (Math.random() * 3 + 1); // 設定亂數語法 範圍為3 進位整數1
		// 1.剪刀 2.石頭 3.布

		switch (icp) { // 設定電腦亂數生成剪刀石頭布

		case 1:
			t001.setImageResource(R.drawable.scissors); // 輸出選擇 丟入1(剪刀)
			break;

		case 2:
			t001.setImageResource(R.drawable.stone); // 輸出選擇 丟入2(石頭)
			break;

		case 3:
			t001.setImageResource(R.drawable.net); // 輸出選擇 丟入3(布)
			break;
		}

		switch (icp - n) { // 進行比較

		case 0: // 相差為零 電人相同 故平手
			str2(2); // 呼叫t003輸出函數
			t003.setTextColor(getResources().getColor(R.drawable.Maroon));
			m003.start(); // 音樂播放
			drow++;
			showNotification("已經平手" + Integer.toString(drow) + "局");
			break;

		case -2: // 相差為-2 電剪刀 人布 故人輸
		case 1: // 相差為1 電石頭/布 人剪刀/石頭 故人輸
			str2(1);
			t003.setTextColor(getResources().getColor(R.drawable.Red));
			m002.start(); // 音樂播放
			cow++;
			showNotification("已經輸" + Integer.toString(cow) + "局");
			break;

		case -1: // 相差為-1 電剪刀/石頭 人石頭/布 故人贏
		case 2: // 相差為-2 電布 人剪刀 故人贏
			str2(0);
			t003.setTextColor(getResources().getColor(R.drawable.Lime));
			m001.start(); // 音樂播放
			pyw++;
			showNotification("已經贏" + Integer.toString(pyw) + "局");
			break;
		}
	}

	public void str1(int n) { // t002輸出
		t002.setText(getString(R.string.m1401_t005)
				+ getString(R.string.m1401_b001 + n)); // 輸出選擇
		t002.setBackgroundResource(R.drawable.box_grey);
	}

	public void str2(int n) { // t003輸出
		t003.setText(R.string.m1401_f001 + n);
		t003.setBackgroundResource(R.drawable.box_yellow);
		layout.setBackgroundResource(R.drawable.p002 + n);

		Toast t1 = null;
		t1 = Toast.makeText(getApplicationContext(),
				getResources().getString(R.string.m1401_f001 + n),
				Toast.LENGTH_LONG); // 利用Toast提示訊息

		t1.setGravity(Gravity.CENTER, 50, 350); // Toast提示視窗的顯示位置
		t1.show();
	}

	public Button.OnClickListener b004on = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent();
			it.setClass(M1401.this, M1401_2.class);

			Bundle bud = new Bundle();
			bud.putInt("KEY_COUNT_SET", con);
			bud.putInt("KEY_COUNT_PLAYER_WIN", pyw);
			bud.putInt("KEY_COUNT_COM_WIN", cow);
			bud.putInt("KEY_COUNT_DRAW", drow);

			it.putExtras(bud);

			startActivity(it);
		}
	};

	private void showNotification(String s) {
		Notification noti = new Notification(
				android.R.drawable.btn_star_big_on, s,
				System.currentTimeMillis());

		Intent it = new Intent();
		it.setClass(this, M1401_2.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putInt("KEY_COUNT_SET", con);
		bundle.putInt("KEY_COUNT_PLAYER_WIN", pyw);
		bundle.putInt("KEY_COUNT_COM_WIN", cow);
		bundle.putInt("KEY_COUNT_DRAW", drow);
		it.putExtras(bundle);

		PendingIntent penIt = PendingIntent.getActivity(this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT);

		noti.setLatestEventInfo(this, "遊戲結果", s, penIt);

		NotificationManager notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notiMgr.notify(0, noti);
	}

	private Button.OnClickListener b005on = new Button.OnClickListener() {
		public void onClick(View v) {
			SharedPreferences gameResultData = getSharedPreferences(
					"GAME_RESULT", 0);

			gameResultData.edit().putInt("COUNT_SET", con)
					.putInt("COUNT_PLAYER_WIN", pyw)
					.putInt("COUNT_COM_WIN", cow).putInt("COUNT_DRAW", drow)
					.commit();

			Toast.makeText(M1401.this, "儲存完成", Toast.LENGTH_LONG).show();
		}
	};

	private Button.OnClickListener b006on = new Button.OnClickListener() {
		public void onClick(View v) {
			SharedPreferences gameResultData = getSharedPreferences(
					"GAME_RESULT", 0);

			con = gameResultData.getInt("COUNT_SET", 0);
			pyw = gameResultData.getInt("COUNT_PLAYER_WIN", 0);
			cow = gameResultData.getInt("COUNT_COM_WIN", 0);
			drow = gameResultData.getInt("COUNT_DRAW", 0);

			Toast.makeText(M1401.this, "載入完成", Toast.LENGTH_LONG).show();
		}
	};

	private Button.OnClickListener b007on = new Button.OnClickListener() {
		public void onClick(View v) {
			SharedPreferences gameResultData = getSharedPreferences(
					"GAME_RESULT", 0);

			gameResultData.edit().clear().commit();

			Toast.makeText(M1401.this, "清除完成", Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1401, menu);
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
