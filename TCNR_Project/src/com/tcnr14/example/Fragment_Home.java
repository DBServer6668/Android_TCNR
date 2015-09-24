package com.tcnr14.example;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragment_Home extends Fragment {
	private ImageView ivFlow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		setupView(view);
		return view;
	}

	private void setupView(View view) {
		// TODO Auto-generated method stub
		// mTextView1 = (TextView)view.findViewById(R.id.textView1);
		ivFlow = (ImageView) view.findViewById(R.id.ividflow);
		userThread myThread = new userThread(this);

		myThread.start();
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ivFlow.setImageResource(R.drawable.f001);
				break;
			case 1:
				ivFlow.setImageResource(R.drawable.f002);
				break;
			case 2:
				ivFlow.setImageResource(R.drawable.f003);
				break;
			case 3:
				ivFlow.setImageResource(R.drawable.f004);
				break;
			case 4:
				ivFlow.setImageResource(R.drawable.f005);
				break;
			case 5:
				ivFlow.setImageResource(R.drawable.f006);
				break;
			}
			super.handleMessage(msg);
		}
	};
}
