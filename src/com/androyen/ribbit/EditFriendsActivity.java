package com.androyen.ribbit;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditFriendsActivity extends ListActivity {
	
	public static final String TAG = EditFriendsActivity.class.getSimpleName();
	
	//Get list of ParseUsers from query
	protected List<ParseUser> mUsers;
	
	//Relations of the ParseUser to currentUser
	protected ParseRelation<ParseUser> mFriendsRelation;
	
	protected ParseUser mCurrentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);
		setupActionBar();
		
		//In onCreate, set checkmark in list view of Friends
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	private void setupActionBar() {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	//Setting up ParseQuery to retrieve users in onResume
	@Override
	protected void onResume() {
		super.onResume();
		
		//Initialize current user and the relation in onResume()
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		//Show progress bar
		setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		
		//Sort in ascending order of usernames
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		
		//Set limits of users to retrieve and display to 1000
		query.setLimit(1000);
		
		//Execute query
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				//Hide progress bar
				setProgressBarIndeterminateVisibility(false);
				
				//If e is null, there is users
				if (e == null) {
					//Get list of ParseUsers from Done method
					mUsers = users;
					
					//What data do we want to display on screen
					//Display usernames
					String[] usernames = new String[mUsers.size()];
					
					//Loop through list of ParseUsers and extract username
					int i = 0;
					for(ParseUser user: mUsers) {
						usernames[i] = user.getUsername();
						i++;
					}
					//Adapt list of usernames to the ListView screen
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this, android.R.layout.simple_list_item_checked, usernames);
					setListAdapter(adapter);
				}
				else {
					//Errors on querying users
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
					//Get message from this exception
					builder.setMessage(e.getMessage());
					builder.setTitle(R.string.error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_friends, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	//When ListView item is clicked
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		//Conditional
		//See if item friend is checkmarked
		if (getListView().isItemChecked(position)) {
			//add friend
			
			//Get the user position that was tapped on the list
			mFriendsRelation.add(mUsers.get(position));
			
			//Save the user selected in list to Parse.com backend in background asynchronously
			mCurrentUser.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					
					//If there is an error, log message
					if (e != null) {
						Log.e(TAG, e.getMessage());
					}
					
				}
			});
		}
		else {
			//remove him
		}
		
		
	}
}
