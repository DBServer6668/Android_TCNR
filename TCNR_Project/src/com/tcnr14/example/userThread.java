package com.tcnr14.example;

public class userThread extends Thread {
	Fragment_Home activity;
	int what = 1;

	public userThread(Fragment_Home activity) {
		this.activity = activity;
	}

	@Override
	public void run() {
		while (true) {
			activity.myHandler.sendEmptyMessage((what++) % 4); // 回傳值0,1,2,3循環
			try {
				Thread.sleep(3000); // 休眠6000毫秒
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
