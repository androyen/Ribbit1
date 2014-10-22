package com.androyen.ribbit;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

//Will hold inbox
public class InboxFragment extends ListFragment {
	
	protected List<ParseObject> mMessages;

	
	//Called when fragment is drawn for the first time
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState)
	 {
         View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
         return rootView;
         
	 }
	 
	 //Get the messages from Parse  Refreshed once displayed
	 public void onResume() {
		 super.onResume();
		 getActivity().setProgressBarIndeterminate(true);
		 
		 //Get message of the logged in user  Specify the class in Parse we are querying
		 ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
		 query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		 
		 //Sort messages by newest
		 query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		 query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				
				getActivity().setProgressBarIndeterminate(false);
				
				if(e == null) {
					//Success. Found messages
					mMessages = messages;
					
					String[] usernames = new String[mMessages.size()];
					int i = 0;
					for (ParseObject message: mMessages) {
						usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
						i++;
					}
					
					//Get Context
					MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
					setListAdapter(adapter);
					
					}
				}
				
			});
		
		}
	 
	 	@Override
	 	public void onListItemClick(ListView l, View v, int position, long id) {
	 		super.onListItemClick(l, v, position, id);
	 		
	 		ParseObject message = mMessages.get(position);
	 		String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
	 		
	 		//View files from Parse from the URL
	 		ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
	 		
	 		//Set it to Uri
	 		Uri fileUri = Uri.parse(file.getUrl());
	 		
	 		if(messageType.equals(ParseConstants.TYPE_IMAGE)) {
	 			//View image
	 			Intent intent = new Intent(getActivity(), ViewImageActivity.class);
	 			//Add data
	 			intent.setData(fileUri);
	 			startActivity(intent);
	 		}
	 		else {
	 			//View video
	 			
	 			//Implicit intent to allow user to choose app to view video
	 			Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
	 			intent.setDataAndType(fileUri, "video/*");
	 			startActivity(intent);
	 		}
	 	}
	 }
	

