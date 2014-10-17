package com.androyen.ribbit;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//Will hold inbox
public class FriendsFragment extends ListFragment {

	
	//Called when fragment is drawn for the first time
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState)
	 {
         View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
         return rootView;
         
	 }
	
}
