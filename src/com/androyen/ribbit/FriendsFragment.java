package com.androyen.ribbit;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

//Will hold inbox
public class FriendsFragment extends ListFragment {
	
	public static final String TAG = FriendsFragment.class.getSimpleName();
	
	//Get list of ParseUsers from query
		protected List<ParseUser> mFriends;
		
		//Relations of the ParseUser to currentUser
		protected ParseRelation<ParseUser> mFriendsRelation;
		
		protected ParseUser mCurrentUser;

	
	//Called when fragment is drawn for the first time
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState)
	 {
         View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
         return rootView;
         
	 }
	 
	 @Override
		public void onResume() {
			super.onResume();
			
	
			
			
			//Display list of friends in Friends fragment tab
			//Initialize current user and the relation in onResume()
			mCurrentUser = ParseUser.getCurrentUser();
			mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
			
			//Inside Fragment.  Getting activity method to call this progress bar
			getActivity().setProgressBarIndeterminateVisibility(true);
			
			ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
			
			//Sort friends in ascending order in Friends fragment tab
			query.addAscendingOrder(ParseConstants.KEY_USERNAME);

			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> friends, ParseException e) {
					
					getActivity().setProgressBarIndeterminateVisibility(false);
					
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
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, usernames);
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
