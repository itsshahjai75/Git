package com.example.grandma1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Medi extends Fragment{
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState); 
	      ArrayAdapter adapter = new ArrayAdapter<String>(view.getContext(),R.layout.activity_listview, Medic);
	      
	      ListView listView=(ListView)view.findViewById(R.id.conditionlist);
	      listView.setAdapter(adapter);
	      listView.setClickable(true);
	      listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
						long id) {
					
					
					// We know the View is a <extView so we can cast it
					TextView clickedView = (TextView) view;
					Toast t = Toast.makeText(view.getContext(),
							"U clicked" + position,
							Toast.LENGTH_SHORT);
					t.show();
					Intent in = new Intent(getActivity(),medisqlitedb.class);
					
					switch(position)
					{
					case 0:in.putExtra("Keyword","ALZ");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 1:in.putExtra("Keyword","ANA");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 2:in.putExtra("Keyword","ALL");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 3:in.putExtra("Keyword","ACA");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 4:in.putExtra("Keyword","AIN");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 5:in.putExtra("Keyword","ABI");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 6:in.putExtra("Keyword","ADI");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 7:in.putExtra("Keyword","AFU");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 8:in.putExtra("Keyword","AMA");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 9:in.putExtra("Keyword","APA");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 10:in.putExtra("Keyword","APS");
						in.putExtra("TABLE_NAME","A");
					   break;	
					case 11:in.putExtra("Keyword","ANX");
						in.putExtra("TABLE_NAME","A");
					   break;	
					case 12:in.putExtra("Keyword","ART");
						in.putExtra("TABLE_NAME","A");
					   break;	
					case 13:in.putExtra("Keyword","AYU");
						in.putExtra("TABLE_NAME","A");
					   break;	
					case 14:in.putExtra("Keyword","AVI");
					in.putExtra("TABLE_NAME","A");
					   break;	
					case 15:in.putExtra("Keyword","BLP");
						in.putExtra("TABLE_NAME","B");
					   break;				 
					case 16:in.putExtra("Keyword","BLT");
						in.putExtra("TABLE_NAME","B");
						   break;	
					case 17:in.putExtra("Keyword","CHO");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 18:in.putExtra("Keyword","DEP");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 19:in.putExtra("Keyword","DRR");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 20:in.putExtra("Keyword","ERE");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 21:in.putExtra("Keyword","EYE");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 22:in.putExtra("Keyword","GOU");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 23:in.putExtra("Keyword","HAI");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 24:in.putExtra("Keyword","HEA");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 25:in.putExtra("Keyword","HER");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 26:in.putExtra("Keyword","HOM");
						in.putExtra("TABLE_NAME","B");
					   break;	
					case 27:in.putExtra("Keyword","MEN");
						in.putExtra("TABLE_NAME","C");
					   break;	
					case 28:in.putExtra("Keyword","MIG");
						in.putExtra("TABLE_NAME","C");
					   break;	
					case 29:in.putExtra("Keyword","MUS");
						in.putExtra("TABLE_NAME","C");
					   break;	
					case 30:in.putExtra("Keyword","NES");
						in.putExtra("TABLE_NAME","C");
					   break;	
					case 31:in.putExtra("Keyword","NEU");
						in.putExtra("TABLE_NAME","C");
					   break;	

					case 32:in.putExtra("Keyword","OST");
						in.putExtra("TABLE_NAME","C");
					   break;		  
					case 33:in.putExtra("Keyword","PAI");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 34:in.putExtra("Keyword","PUL");
					in.putExtra("TABLE_NAME","C");
				   break;	
					case 35:in.putExtra("Keyword","SMO");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 36:in.putExtra("Keyword","SEX");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 37:in.putExtra("Keyword","SKI");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 38:in.putExtra("Keyword","THY");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 39:in.putExtra("Keyword","URI");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 40:in.putExtra("Keyword","VAG");
					in.putExtra("TABLE_NAME","C");
				   break;		  
					case 41:in.putExtra("Keyword","WLO");
					in.putExtra("TABLE_NAME","C");
				   break;

					case 42:in.putExtra("Keyword","WOS");
						in.putExtra("TABLE_NAME","C");
					   break;		  
			 
					}
					
					startActivity(in);
				}
				});
				
				
	     

	}

	String KEY_ID = "_id";
	String[] Medic = {"Alzheimer",
		    "Analgesics & antipyretic",
		    "Anti allergic",
		    "Anti cancer drugs",
		    "Antiasthmatic-inhalers",
		    "Antibiotics",
		    "Antidiabetic/ hypoglycemics",
		    "Antifungal",
		    "Antimalarial",
		    "Antiparkinson agents",
		    "Antipsychotic & antimanics",
		    "Antivirals",
		    "Anxiety",
		    "Arthritis",
		    "Ayurvedic section",
		    "Blood pressure",
		    "Blood thinner",
		    "Cholestrol care",
		    "Depression care",
		    "Dr reckeweg & co",
		    "Erectile dysfunction",
		    "Eye drops/ ointments",
		    "Gout pain",
		    "Hair care",
		    "Heartburn",
		    "Herbal preprations",
		    "Homeopathic section",
		    "Men special",																
		    "Migrane",																	
		    "Muscle relaxant",															
		    "Nesal spray",																
		    "Neurologiical disorders",													
		    "Osteoporosis",																	
		    "Pain reliever",															
		    "Pulmonary fibrosis (ipf)",													
		    "Quit smoking",																 
		    "Sexual health",															
		    "Skin /acne care",                                                          
		    "Thyroid/ anti thyroid",													
		    "Urinary anti infectives",													
		    "Vaginal creams/inserts",															
		    "Weight loss / fat burner",														
		    "Women special"		
			

};
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Medi newInstance(int sectionNumber) {
		Medi fragment = new Medi();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public Medi() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.act2, container,false);
		
	     	      	
		return rootView;
	
	      }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}



}
