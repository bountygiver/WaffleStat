package com.altmining.wafflestat;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import api.API;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class StatsActivity extends Activity implements Observer {
	
	public API current;
	public Timer auto;
	public int thisHash = 0;
	public Boolean err = false;
	public NotificationManager mNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		Intent curr = this.getIntent();
		current = new API(curr.getStringExtra("addr").toString());
		current.addObserver(this);
		auto = new Timer();
		auto.scheduleAtFixedRate(new APIupdate(), 0, 30000);
	}
	
	public class APIupdate extends TimerTask {
	@Override
	public void run() {
		current.update();
	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}
	
	private void updateUI() {
		TextView tv = (TextView) findViewById(R.id.txtHash);
		tv.setText(current.getHash_str());
		if (current.getLastError().length() > 0) {
			tv.setText(current.getLastError());
			err = true;
		}
		tv = (TextView) findViewById(R.id.txtAddr);
		tv.setText(current.getBitaddress());
		tv = (TextView) findViewById(R.id.txtConverted);
		tv.setText(current.getConfirmed() + "");
		tv = (TextView) findViewById(R.id.txtPaid);
		tv.setText(current.getPaid() + "");
		tv = (TextView) findViewById(R.id.txtUnconverted);
		tv.setText(current.getUnconverted() + "");
		tv = (TextView) findViewById(R.id.txtLastChecked);
		tv.setText(current.getLastCheckedString());
		EditText eb = (EditText) findViewById(R.id.eTMinHash);
		CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
		int i = 0;
		try {
			i = Integer.parseInt(eb.getText().toString());
		} catch (Exception ex) {
			
		}
		if (cb.isChecked() && current.getHashrate() < i * 1000 && i > 0) {
			err = true;
		}
	}
	
	public void ToggleNotify(View view) {
		if (((CheckBox) view).isChecked()) { 
		}
		else {
			
		}
	}
	
	public class upTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			current.update();
			return null;
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.stat_refresh:
	        	new upTask().execute();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable(){
		    public void run(){
		    	updateUI();
		    }
		});
	}

}
