package com.tcnr14.example;

import static com.tcnr14.example.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.tcnr14.example.CommonUtilities.EXTRA_MESSAGE;
import static com.tcnr14.example.CommonUtilities.SENDER_ID;
import static com.tcnr14.example.CommonUtilities.SERVER_URL;

import com.tcnr14.example.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		GCM();
	}

	private void GCM() {
		// TODO Auto-generated method stub
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.

		// GCMRegistrar.checkManifest(this);
		// setContentView(R.layout.fragment_6);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.

			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtilities.register(context,
								regId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		unregisterReceiver(mHandleMessageReceiver);
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
		}
	};

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new Fragment_Home();
			break;
		case 1:
			fragment = new Fragment_Instert();
			break;
		case 2:
			fragment = new Fragment_Query();
			break;
		case 3:
			fragment = new Fragment_PhotoSwipe();
			break;
		case 4:
			fragment = new Fragment_Video();
			break;
		case 5:
			fragment = new Fragment_Map();
			break;
		case 6:
			fragment = new Fragment_Map2();
			break;
		case 7:
			fragment = new Fragment_Map3();
			break;
		case 8:
			fragment = new Fragment_GCM();
			break;
		default: // 還沒製作的選項，fragment 是 null，直接返回 return;
			return;
		}

		fragmentTransaction.replace(R.id.container, fragment).commit();
		// mTitle = ListItem.DrawerText[position];
		mTitle = getResources().getStringArray(R.array.ListItem)[position];
		getActionBar().setTitle(mTitle);
	}

	// public void onSectionAttached(int number) {
	// switch (number) {
	// case 1:
	// mTitle = getString(R.string.title_section1);
	// break;
	// case 2:
	// mTitle = getString(R.string.title_section2);
	// break;
	// case 3:
	// mTitle = getString(R.string.title_section3);
	// break;
	// }
	// }

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent it = new Intent();
			Uri uri = Uri
					.parse("http://dbserver66.er-webs.com/project/index.php");
			it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// ((MainActivity)
			// activity).onSectionAttached(getArguments().getInt(
			// ARG_SECTION_NUMBER));
		}
	}

}
