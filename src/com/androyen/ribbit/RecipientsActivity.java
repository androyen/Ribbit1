package com.androyen.ribbit;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RecipientsActivity extends ListActivity {
	
public static final String TAG = RecipientsActivity.class.getSimpleName();
	
	//Get list of ParseUsers from query
		protected List<ParseUser> mFriends;
		
		//Relations of the ParseUser to currentUser
		protected ParseRelation<ParseUser> mFriendsRelation;
		
		protected ParseUser mCurrentUser;
		
		//Send menu item
		protected MenuItem mSendMenuItem;
		
		//get path of the file
		protected Uri mMediaUri;
		
		//file type
		protected String mFileType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipients);
		
		//Show the up button in the Action Bar.
//		setupActionBar();
		
		//In onCreate, set checkmark in list view of Friends. Allow to checkmark multiple items in ListView
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		//Get Uri of file
		mMediaUri = getIntent().getData();
		
		//Get the extra of type of either Image of Video
		mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);
	}
	
//	private void setupActionBar() {
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
		
		//Get Send menu item object  Only 1 item in menu bar. Set parameter to 0
		mSendMenuItem = menu.getItem(0);
		
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
			case R.id.action_send:
				//Send message to Parse
				//Create message
				ParseObject message = createMessage();
				
				if (message == null) {
					//error sending
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(R.string.error_selecting_file)
					.setTitle(R.string.error_selecting_file_title)
					.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					
					send(message);
					//Once successful close this Activity and go to the Inbox
					finish();
				}
				
		}
			
		return super.onOptionsItemSelected(item);
	}
	
	//Setting up Send button in Action Bar to display if friends are checkmark
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		
	
		
		//When items are deselected, remove the Send action bar icon
		
		if (l.getCheckedItemCount() > 0) {
			
			//Show Send item
			mSendMenuItem.setVisible(true);
		}
		else {
			mSendMenuItem.setVisible(false);
		}
		
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
	
	//helper methods for sending message
	protected ParseObject createMessage() {
		ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
		//Add data to messages
		message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
		//Get selected friends
		message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
		//Get Image or Video type and send to Parse
		message.put(ParseConstants.KEY_FILE_TYPE, mFileType);
		
		//Convert Message data to byte[] array
		byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
		
		//null check for errors
		if (fileBytes == null) {
			return null;
		}
		else {
			//Create the Parse File
			
			//If it is an Image file
			if (mFileType.equals(ParseConstants.TYPE_IMAGE)) {
				//Reduce the file size to a PNG file
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);
			}
			
			//Set filename from the Uri  Appends either PNG or use the video file extension
			String filename = FileHelper.getFileName(this, mMediaUri, mFileType);
			ParseFile file = new ParseFile(filename, fileBytes);
			message.put(ParseConstants.KEY_FILE, file);
			return message;
		}
		
	}
	
	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		
		//loop through the list of friends. Add friends that are checked
		for (int i = 0; i < getListView().getCount(); i++) {
			if (getListView().isItemChecked(i)) {
				recipientIds.add(mFriends.get(i).getObjectId());
			}
		}
		
		return recipientIds;
	}
	
	//save messages to Parse.com
	protected void send(ParseObject message) {
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				
				
				//If error on save
				if (e == null) {
					//Success
					Toast.makeText(RecipientsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
					
				}
				else {
					//Error
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
					builder.setMessage(R.string.error_sending_message)
					.setTitle(R.string.error_selecting_file_title)
					.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				
			}
		});
	}
}
