package com.mls.mnote;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity implements
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
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
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		else if (id == R.id.action_save){
			NoteView.save();
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
			int viewIndex = getArguments().getInt(ARG_SECTION_NUMBER);
			if (viewIndex == 1){
				View rootView = inflater.inflate(R.layout.fragment_main, container,
						false);
				//			TextView textView = (TextView) rootView
				//					.findViewById(R.id.section_label);
				//			textView.setText(Integer.toString(getArguments().getInt(
				//					ARG_SECTION_NUMBER)));

				NoteButton [] buttons = new NoteButton[]{
						(NoteButton) rootView.findViewById(R.id.noteButton1),
						(NoteButton) rootView.findViewById(R.id.noteButton2),
						(NoteButton) rootView.findViewById(R.id.noteButton3),
						(NoteButton) rootView.findViewById(R.id.noteButton4),
						(NoteButton) rootView.findViewById(R.id.noteButton5),
						(NoteButton) rootView.findViewById(R.id.noteButton6),
						(NoteButton) rootView.findViewById(R.id.noteButton7)
				};

				final NoteButton [] valueButtons = new NoteButton[]{
						(NoteButton) rootView.findViewById(R.id.noteValueButton1),
						(NoteButton) rootView.findViewById(R.id.noteValueButton2),
						(NoteButton) rootView.findViewById(R.id.noteValueButton3),
						(NoteButton) rootView.findViewById(R.id.noteValueButton4),
						(NoteButton) rootView.findViewById(R.id.noteValueButton5),
						(NoteButton) rootView.findViewById(R.id.noteValueButton6)
				};

				for (int i = 0; i < buttons.length; ++i){
					buttons[i].noteNum = i;
					final int noteNum = i;

					buttons[i].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							NoteView.createNote(noteNum);
						}
					});
				}

				for (int i = 0; i < valueButtons.length; ++i){
					final double noteValue = 1. / Math.pow(2, i);
					valueButtons[i].noteValue = noteValue;

					valueButtons[i].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							NoteView.setNoteValue(noteValue);
						}
					});
				}

				return rootView;
			}
			else{
				View rootView = inflater.inflate(R.layout.fragment_list, container,
						false);
				
				LinearLayout notelistLayout = (LinearLayout) rootView.findViewById(R.id.notelist_layout);
				for (int i = 0; i < 10; ++i){
					notelistLayout.addView(new NoteView(notelistLayout.getContext(), null));
				}
				return rootView;
			}
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
