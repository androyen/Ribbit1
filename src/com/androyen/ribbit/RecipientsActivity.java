package com.androyen.ribbit;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecipientsActivity extends ListActivity {
	
public static final String TAG = RecipientsActivity.class.getSimpleName();
	
	//Get list of ParseUsers from query
		protected List<ParseUser> mFriends;
		
		//Relations of the ParseUser to currentUser
		protected ParseRelation<ParseUser> mFriendsRelation;
		
		protected ParseUser mCurrentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipients);
		
		//Show the up button in the Action Bar.
//		setupActionBar();
		
		//In onCreate, set checkmark in list view of Friends. Allow to checkmark multiple items in ListView
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
//	private void setupActionBar() {
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
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
	
	
	@Override
	public void onResume() {
		
		super.onResume();
		
		
		//Display list of friends in Friends fragment tab
		//Initialize current user and the relation in onResume()
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		//Inside Fragment.  Getting activity method to call this progress bar
		setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		
		//Sort friends in ascending order in Friends fragment tab
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);

		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				
				setProgressBarIndeterminateVisibility(false);
				
				if (e == null) {
					//Get list and set as Friends
					mFriends = friends;
					
					String[] usernames = new String[mFriends.size()];
					int i = 0;
					for (ParseUser user: mFriends) {
						usernames[i] = user.getUsername();
						i++;
					}
					
					//Get Context
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipientsActivity.this, android.R.layout.simple_list_item_checked, usernames);
					setListAdapter(adapter);
					
					}
				
				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
					builder.setMessage(e.getMessage());
					builder.setTitle(R.string.error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				
			}
			
			
				
			
			
		});
		
		
	}
}
