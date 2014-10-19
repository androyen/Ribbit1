package com.androyen.ribbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	
	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText mEmail;
	protected Button mSignUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Set progess indicator
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_sign_up);
		
		mUsername =(EditText) findViewById(R.id.usernameField);
		mPassword = (EditText) findViewById(R.id.passwordField);
		mEmail = (EditText) findViewById(R.id.emailField);
		mSignUpButton = (Button) findViewById(R.id.signupButton);
		mSignUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				String email = mEmail.getText().toString();
				
				//trim white space on user input
				username = username.trim();
				password = password.trim();
				email = email.trim();
				
				//Check if username, password, or emails is not empty
				if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
					
					//Set AlertDialog
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.sign_up_error_message);
					builder.setTitle(R.string.sign_up_error_title);
					
					//Set null to do nothing. No listener
					builder.setPositiveButton(android.R.string.ok, null);
					
					//Create dialog and show it
					AlertDialog dialog = builder.create();
					dialog.show();
				
				}
				else {
					
					
					//Create new user
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					
					//Set progress bar indicator
					setProgressBarIndeterminateVisibility(true);
					
					//Sign up user in background thread
					newUser.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {
							//When sign up is done from Parse.com
							
							//Remove progress bar indicator
							setProgressBarIndeterminateVisibility(false);
							
							if (e == null) {
								//Successful sign up
								
								//Open the main mailbox activity
								Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
								
								//Need to clear the sign up activity out of back stack
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
							}
							else {
								//If there are errors signing up
								AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
								//Get message from this exception
								builder.setMessage(e.getMessage());
								builder.setTitle(R.string.sign_up_error_title);
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


//
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
