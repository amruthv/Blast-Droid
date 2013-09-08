package com.example.blast2;

import android.content.Context;
import android.content.Intent;
import android.app.ListActivity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.Location;
import android.widget.Toast;

public class ShowGPS extends ListActivity{
	final Context context = this;
	/**
	 * @param args
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        Context context = getApplicationContext();
        CharSequence text = location.toString();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

