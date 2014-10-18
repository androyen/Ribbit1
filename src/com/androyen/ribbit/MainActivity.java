package com.androyen.ribbit;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    //TAG constant
    private static final String TAG = MainActivity.class.getSimpleName();
    
    //Dialog interface constants for Camera intent requestCode. Used for startActivityForResult
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;
    
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    
    //uniform resource identifier. Path to a specific file in Android File system
    protected Uri mMediaUri;
    
    //Listener for the camera dialog options in ActionBar
    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			switch (which) {
				case 0: //Take picture
					Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					
					//Check to see if it is mMediaUri is null
					if (mMediaUri == null) {
						//display an error
						Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
					}
					else {
						takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
						startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
					}
					break;
				case 1: // Take video
					break;
				case 2:  //Choose picture
					break;
				case 3:  //Choose video
					break;
			}
			
		}

		private Uri getOutputMediaFileUri(int mediaType) {
			
			//To be safe you should check that external storage is mounted using Environment.getExternalStorageState()
			//before doing this
			if (isExternalStorageAvailable()) {
				//get the file URI
				
				//Get the app_name  application name
				String appName = MainActivity.this.getString(R.string.app_name);
				
				//1. Get the external storage directory   Creates subdirectory with app name Ribbit
				File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
				//2. Create our subdirectory
				if (! mediaStorageDir.exists()) {
				
					
					//Returns a boolean value if folder cannot create
					if (! mediaStorageDir.mkdirs()) {
						Log.e(TAG, "Failed to create directory");
					}
					
				}
				//3. Create the file name
				//4. Create the file
				
				//Append date/time stamp on the file
				File mediaFile;
				Date now = new Date(); //Get current date/time
				
				//Format the date 
				String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
				
				
				//Get string path of file for the media type
				String path = mediaStorageDir.getPath() + File.separator;
				if (mediaType == MEDIA_TYPE_IMAGE) {
					mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
				}
				else if (mediaType == MEDIA_TYPE_VIDEO) {
					mediaFile = new File(path + "VID_" + timestamp + ".mp4");
				}
				else {
					return null;
				}
				
				Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
				
				//5. Return the files Uri
				
				return Uri.fromFile(mediaFile);
			}
			else {
				return null;
			}
			
			
		}
		
		private boolean isExternalStorageAvailable() {
			//Gets the status of whether external storage is mounted
			String state = Environment.getExternalStorageState();
			
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				//If it is mounted
				return true;
			}
			else {
				
				return false;
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Set up Parse Analytics
        ParseAnalytics.trackAppOpened(getIntent());
        
        //Check to see if user is already logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        
        if (currentUser != null) {
        	
        }
        else {
        	
        	//Not currently logged in
        	navigateToLogin();
        	
        }
        
//        navigateToLogin();

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        
        //Adjusted parameter to pass this context to SectionsPagerAdapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


	private void navigateToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    
    	//Check which item is selected in Overflow
    	int itemId = item.getItemId();
    	
    	//Which items in Action Overflow is selected
    	
    	switch (itemId) {
    	
    	case R.id.action_logout:
    		ParseUser.logOut();
    		navigateToLogin();
    		
    	case R.id.action_edit_friends:
    		Intent intent = new Intent(this, EditFriendsActivity.class);
    		startActivity(intent);
    	case R.id.action_camera:
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		
    		//Add dialog listener in parameter
    		builder.setItems(R.array.camera_choices, mDialogListener);
    		AlertDialog dialog = builder.create();
    		dialog.show();
    	}
    		
    
    	
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    

}
