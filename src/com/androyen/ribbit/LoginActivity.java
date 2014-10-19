package com.androyen.ribbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	protected TextView mSignUpTextView;
	
	protected EditText mUsername;
	protected EditText mPassword;
	protected Button mLoginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Set up progress indicator. Done BEFORE setContentView. Get window extended features
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_login);
	
		
		mSignUpTextView = (TextView) findViewById(R.id.signUpText);
		mSignUpTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
		
		mUsername =(EditText) findViewById(R.id.usernameField);
		mPassword = (EditText) findViewById(R.id.passwordField);
		mLoginButton = (Button) findViewById(R.id.loginButton);
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				
				
				//trim white space on user input
				username = username.trim();
				password = password.trim();
				
				Log.v(TAG, "Value of " + username + " and password " + password);
				
				
				//Check if username, password, or emails is not empty
				if (username.isEmpty() || password.isEmpty()) {
					
					//Set AlertDialog
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					builder.setMessage(R.string.login_error_message);
					builder.setTitle(R.string.login_error_title);
					
					//Set null to do nothing. No listener
					builder.setPositiveButton(android.R.string.ok, null);
					
					//Create dialog and show it
					AlertDialog dialog = builder.create();
					dialog.show();
				
				}
				else {
					
					//Logging in
					//Set progress indicator while logging in
					setProgressBarIndeterminateVisibility(true);
					
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						
						@Override
						public void done(ParseUser user, ParseException e) {
							//Get rid of progress indicator after sign in successful
							setProgressBarIndeterminateVisibility(false);
							
							//IF login is successful, should  be null
							if (e == null) {
								//Open the main mailbox activity
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								//Remove the login screen from the back stack
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
								
							}
							else {
								AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
								//Get message from this exception
								builder.setMessage(e.getMessage());
								builder.setTitle(R.string.login_error_title);
								builder.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
							
						}
					});
					
				}
				
				
			}
		});
	}



//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
