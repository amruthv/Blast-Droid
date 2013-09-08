package com.example.blast2;
import java.io.IOException;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	final Context context = this;
	LocationManager locationManager;
    Location myLocation;
    
	private ArrayAdapter<Spanned> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		Context context = getApplicationContext();
		
		List<Spanned> starray = new ArrayList<Spanned>();
		starray.add(Html.fromHtml("hi"));
		ListView listView = (ListView) findViewById(R.id.list_view);
		arrayAdapter = new ArrayAdapter<Spanned>(this, android.R.layout.simple_list_item_1, starray);
		listView.setAdapter(arrayAdapter);
		
		updateBlasts(null);
 
//		Intent placesIntent = new Intent(this, BlastActivity.class);
//        new Thread(new Runnable() {
//            @Override
//            public void run()
//            {
//                // do the thing that takes a long time
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                    }
//                });
//            }
//        }).start(); 
//        startActivity(placesIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void updateBlasts(View view) {
		arrayAdapter.clear();
		
		String hardcodedURL = "http://ec2-54-200-7-142.us-west-2.compute.amazonaws.com:8000/getcontent?";

		locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Double lat = new Double(0);
        Double lon = new Double(0);
        if (location != null) {
        	lat = location.getLatitude();
        	lon = location.getLongitude();
        }
        
        List<Spanned> starray = new ArrayList<Spanned>();
        BlastParser blastFetcher = new BlastParser(String.format("%slat=%s&lon=%s", hardcodedURL, lat, lon));
        Blast[] blasts = null;
        try {
			blasts = blastFetcher.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for (Blast b: blasts) { 
        	arrayAdapter.insert(Html.fromHtml(b.formatted()), arrayAdapter.getCount());
        }
         
        arrayAdapter.notifyDataSetChanged();
	}
	
	public void newBlast(View view) {
		final String hardURL = "http://ec2-54-200-7-142.us-west-2.compute.amazonaws.com:8000/postcontent";
		
		
		//Timestamp
		Calendar c = Calendar.getInstance(); 
		int seconds = c.get(Calendar.SECOND);
		final String time = new Integer(seconds).toString();
		
		
		// Lat long
	    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Location location = lm
	    		.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Double lat = new Double(0);
	    Double lon = new Double(0);
		final String stringLat = ((Double) location.getLatitude()).toString();
		final String stringLong = ((Double) location.getLongitude()).toString();
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("What's going on?");
		
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText userBox = new EditText(this);
		userBox.setHint("User");
		layout.addView(userBox);

		final EditText descriptionBox = new EditText(this);
		descriptionBox.setHint("Event");
		layout.addView(descriptionBox);

		builder.setView(layout);
		
		
		/*final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		final EditText input2 = new EditText(this);
		input2.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input2);*/
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        String userName = userBox.getText().toString();
		        String event = descriptionBox.getText().toString();
		        
				String finalURL = String.format("%s?userid=%s&content=%s&lat=%s&lon=%s", hardURL, Uri.encode(userName), Uri.encode(event), stringLat, stringLong);
				System.out.println(finalURL);
				
				HttpClient httpclient= new DefaultHttpClient();
				HttpGet httpget = new HttpGet(finalURL);
				try {
					httpclient.execute(httpget);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
	}
}
	


