package com.example.grandma1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;















import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CheckInActivity extends ListActivity {// implements LocationListener {


    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
    	double lat,lon;
        super.onCreate(arg0);
        
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790B28")));
        getListView().setBackgroundResource(R.drawable.box);
        Intent in =getIntent();
        lat=in.getDoubleExtra("lat", 23.04937166666667);
        lon=in.getDoubleExtra("lon", 72.522833333333);
        new GetPlaces(this, getListView(),lat,lon).execute();
        getListView().setOnItemClickListener(new OnItemClickListener(){
        	
   
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView t = (TextView)view.findViewById(R.id.phone);
				String ref = t.getText().toString();
				Intent intent = new Intent(getBaseContext(), placedetails.class);
				intent.putExtra("reference", ref);
				startActivity(intent);
			}
        	
        });;
        
    }
	//	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		/*
		  Parameters :
		     First(provider)    :  the name of the provider with which to register 
		     Second(minTime)    :  the minimum time interval for notifications, in milliseconds. This field is only used as a hint to conserve power, and actual time between location updates may be greater or lesser than this value. 
		     Third(minDistance) :  the minimum distance interval for notifications, in meters 
		     Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location) method will be called for each location update 
        */
		
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,   // 3 sec
			//	10, this);
		
		/********* After registration onLocationChanged method called periodically after each 3 sec ***********/
	

       

    /*}


    

	private LocationManager locationManager;
	
		/********** get Gps location service LocationManager object ***********
	
	/************* Called after each 3 sec **********
	@Override
	public void onLocationChanged(Location location) {

		lat=location.getLatitude();
		lon=location.getLongitude();
		String str = "Latitude: "+location.getLatitude()+" \nLongitude: "+location.getLongitude();
		Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
		 new GetPlaces(this, getListView(),lat,lon).execute();
	}

	@Override
	public void onProviderDisabled(String provider) {
		
		/******** Called when User off Gps *********
		
		Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		
		/******** Called when User on Gps  *********
		
		Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}*/


    class GetPlaces extends AsyncTask<Void, Void, Void>{

    	
    	ArrayList<HashMap<String, String>> hosp;
        private ProgressDialog dialog;
        private Context context;
        private String[] placeName;
        private String[] imageUrl;
        private ListView listView;
        double lat,lon;
        public GetPlaces(Context context, ListView listView,double lat,double lon) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;
            this.lon=lon;
            this.lat=lat;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();

            //listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,placeName));
            ListAdapter adapter = new SimpleAdapter(
					CheckInActivity.this, hosp,
					R.layout.list_map, new String[] { "name", "vicinity","ref" }, new int[] {R.id.name,R.id.address,R.id.phone});
			// updating listview
			setListAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesServices service = new PlacesServices("AIzaSyApu_hTO6pztTZeJrHhG5P2RuINw4fx_Rs");
              List<Place> findPlaces = service.findPlaces(lat,lon,"hospital");  // hospiral for hospital
                                                                                        // atm for ATM
              hosp= new ArrayList<HashMap<String, String>>();
              HashMap<String, String> hos;
               // placeName = new String[findPlaces.size()];
                //imageUrl = new String[findPlaces.size()];

              for (int i = 0; i < findPlaces.size(); i++) {

            	  hos = new HashMap<String, String>();
                  Place placeDetail = findPlaces.get(i);
                  placeDetail.getIcon();
                  System.out.println(  placeDetail.getName());
                  
                  String placeName =placeDetail.getName();
                  String imageUrl =placeDetail.getIcon();
                  String vic = placeDetail.getVicinity();
                  String phone =placeDetail.getref();
                  
                  hos.put("name", placeName);
                  hos.put("vicinity", vic);
                  hos.put("ref", phone);
                  
                  hosp.add(hos);

            }
            return null;
        }

    }

}