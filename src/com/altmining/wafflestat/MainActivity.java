package com.altmining.wafflestat;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void Enter(View view) {
		EditText addr = (EditText) findViewById(R.id.txtAddress);
		String bit_addr = addr.getText().toString();
		Intent d = new Intent(this, StatsActivity.class);
		d.putExtra("addr", bit_addr);	
		startActivity(d);
		addr.setText("");
	}
	
	public void GoSite(View view) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wafflepool.com/"));
		startActivity(browserIntent);
	}
	
	public void StartQR(View view) {
		try {
	        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	        startActivityForResult(intent, 0);
		}
		catch (ActivityNotFoundException e) {
	        Intent intent = new Intent(Intent.ACTION_VIEW,
	        		Uri.parse("market://details?id=com.google.zxing.client.android.SCAN") ); 
	        startActivity(intent);

		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            // Handle successful scan
	    		EditText addr = (EditText) findViewById(R.id.txtAddress);
	    		addr.setText(contents);
	        } else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
