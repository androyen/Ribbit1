package com.androyen.ribbit;

import java.util.Timer;
import java.util.TimerTask;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ViewImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		
		//Get uri from the inbox fragment intent
		Uri imageUri = getIntent().getData();
		
		//Need to load the image direct from remote Parse server. Using a Picasso library to load images from Parse
		Picasso.with(this).load(imageUri.toString()).into(imageView);
		
		//Set up Timer to go back to the Inbox
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//This run method is called after the 10*1000 seconds complete
				
				//End Activity
				finish();
				
			}
		}, 10*1000);
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
}
