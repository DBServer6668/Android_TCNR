package tcnr14.com.example.m1401;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class M1401_2 extends Activity {

	private TextView t001, t002, t003, t004;
	private Button b001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1401_2);

		setViewContent();
		show();
	}

	private void setViewContent() {

		t001 = (TextView) findViewById(R.id.m1401_2_t002); // 建立物件
		t002 = (TextView) findViewById(R.id.m1401_2_t004);
		t003 = (TextView) findViewById(R.id.m1401_2_t006);
		t004 = (TextView) findViewById(R.id.m1401_2_t008);

		b001 = (Button) findViewById(R.id.m1401_2_b001);

		b001.setOnClickListener(b001on);
	}

	public Button.OnClickListener b001on = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			M1401_2.this.finish();
		}
	};

	public void show() {
		Bundle bun = this.getIntent().getExtras();
		
		int con = bun.getInt("KEY_COUNT_SET");
		int pyw = bun.getInt("KEY_COUNT_PLAYER_WIN");
		int cow = bun.getInt("KEY_COUNT_COM_WIN");
		int drow = bun.getInt("KEY_COUNT_DRAW");
		
		t001.setText(Integer.toString(con));
		t002.setText(Integer.toString(pyw));
		t003.setText(Integer.toString(cow));
		t004.setText(Integer.toString(drow));

	}

}
