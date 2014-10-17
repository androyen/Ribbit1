package com.androyen.ribbit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EditFriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_friends);
		setupActionBar();
	}
	
	private void setupActionBar() {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	//Setting up ParseQuery to retrieve users in onResume
	@Override
	protected void onResume() {
		super.onResume();
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		
		//Sort in ascending order of usernames
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		
		//Set limits of users to retrieve and display to 1000
		query.setLimit(1000);
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
}
