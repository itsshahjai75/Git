package com.example.grandma1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class docaroundu extends Fragment implements LocationListener{
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TextView b1 = (TextView)getView().findViewById(R.id.textView1);
		b1.setText("Search for your location");
	}





	double lat,lon;

	private LocationManager locationManager;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState); 
		
}				
	  	@Override
		public void onLocationChanged(Location location) {

			lat=location.getLatitude();
			lon=location.getLongitude();
			String str = "Latitude: "+location.getLatitude()+" \nLongitude: "+location.getLongitude();
			Intent in = new Intent(getView().getContext(),CheckInActivity.class);
			in.putExtra("lat", lat);
			in.putExtra("lon", lon);
			
			startActivity(in);

			//Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
			// new GetPlaces(this, getListView(),lat,lon).execute();
		}

		@Override
		public void onProviderDisabled(String provider) {
			
			/******** Called when User off Gps *********/
			
			Toast.makeText(getView().getContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			
			/******** Called when User on Gps  *********/
			
			Toast.makeText(getView().getContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}


	

	
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static docaroundu newInstance(int sectionNumber) {
		docaroundu fragment = new docaroundu();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public docaroundu() {
	}
	TextView t;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final View rootView = inflater.inflate(R.layout.doc, container,false);
		ImageButton b1 = (ImageButton)rootView.findViewById(R.id.imageButton1);
		t=(TextView)rootView.findViewById(R.id.textView1);
		
	      b1.setOnClickListener(new OnClickListener() {
					
					// We know the View is a <extView so we can cast it
			
			
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						
						t.setText("Searching...");
						
						locationManager = (LocationManager) getView().getContext().getSystemService(Context.LOCATION_SERVICE);
						
						/*
						  Parameters :
						     First(provider)    :  the name of the provider with which to register 
						     Second(minTime)    :  the minimum time interval for notifications, in milliseconds. This field is only used as a hint to conserve power, and actual time between location updates may be greater or lesser than this value. 
						     Third(minDistance) :  the minimum distance interval for notifications, in meters 
						     Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location) method will be called for each location update 
				        */
						
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,   // 3 sec
								10, docaroundu.this);
						
						/********* After registration onLocationChanged method called periodically after each 3 sec ***********/
				//		Intent in = new Intent(getView().getContext(),CheckInActivity.class);
//						in.putExtra("lat", lat);
	//					in.putExtra("lon", lon);
						
					//	startActivity(in);

    


											}
						
					
				});

	     	      	
		return rootView;
	
	      }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}



}



