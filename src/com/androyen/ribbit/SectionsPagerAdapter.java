package com.androyen.ribbit;

import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;




/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	protected Context mContext;

	//Add Context parameter to constructor
    public SectionsPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
    	
    	//Per tab click, switch to either Friends or Inbox fragment
    	switch(position) {
    	
	    	case 0:
	    		return new InboxFragment();
	    	case 1:
	    		return new FriendsFragment();
    	}
    	
        return null;
    }

    @Override
    public int getCount() {
        // Show 2 tabs
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
        
        //getString() is a context method. Need to specify context
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
            
        }
        return null;
    }
}

