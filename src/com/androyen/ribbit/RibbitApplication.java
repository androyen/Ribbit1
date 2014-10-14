package com.androyen.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


//Very first class that opens on this Ribbit application. Initializing setup
public class RibbitApplication extends Application {

	public void onCreate() {
		
		super.onCreate();
		Parse.initialize(this, "ugvYQBY9jyQYtUt0NHyMnPxqJjb5bzPtkBVyKJd1", "B22JHeyItOl71u7eE5kXv65N8C5c1O0CVk2HrePf");
		  
		  
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		
		}
	
}
