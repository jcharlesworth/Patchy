package com.thrivingidea.android.patchy;

import winterwell.jtwitter.Twitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Patchy extends Activity {
	
	private EditText status = null;
	private SharedPreferences prefs = null;
	private Twitter client = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        
        status = (EditText)findViewById(R.id.status);
        
        Button send = (Button)findViewById(R.id.send);
        
        send.setOnClickListener(onSend);
    }
    
    private void updateStatus() {
    	try {
    		getClient().updateStatus(status.getText().toString());
    	} catch (Throwable t) {
    		Log.e("Patchy", "Exception in updateStatus()", t);
    		goBlooey(t);
    	}
    }
    
    private void goBlooey(Throwable t) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder
    		.setTitle("Exception!")
    		.setMessage(t.toString())
    		.setPositiveButton("OK", null)
    		.show();
    }
    
    private View.OnClickListener onSend = new View.OnClickListener() {
		public void onClick(View v) {
			updateStatus();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication()).inflate(R.menu.option, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.prefs){
			startActivity(new Intent(this, EditPreferences.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener =
		new SharedPreferences.OnSharedPreferenceChangeListener() { 
			
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {
			if (key.equals("user") || key.equals("password")) {
				resetClient();
			}
		}
	};
	
	synchronized private Twitter getClient() {
		if (client == null) {
			client = new Twitter(prefs.getString("user", ""),
					prefs.getString("password", ""));
			
			client.setAPIRootUrl("https://identi.ca/api");
		}
		
		return client;
	}
	
	synchronized private void resetClient() {
		client = null;
	}
		
}