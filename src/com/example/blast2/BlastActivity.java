package com.example.blast2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.location.Location;



public class BlastActivity extends ListActivity {
    private LocationManager locationManager;
    final Context context = this;
    Location myLocation;
    
    
    
	String hardcodedURL = "http://ec2-54-200-7-142.us-west-2.compute.amazonaws.com:8000/getcontent/";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
        	System.out.println(location.getLatitude());
        	System.out.println(location.getLongitude());
        }
        
        List<Spanned> starray = new ArrayList<Spanned>();
        BlastParser blastFetcher = new BlastParser(hardcodedURL + lat + "/" + lon);
        Blast[] blasts = null;
        try {
			blasts = blastFetcher.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for (Blast b: blasts) {
        	starray.add(Html.fromHtml(b.toString()));
        }
        
        setListAdapter(new ArrayAdapter<Spanned>(this, R.layout.blastlist, starray));
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
