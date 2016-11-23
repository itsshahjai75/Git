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

public class dd extends Fragment{
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState); 
	      ArrayAdapter adapter = new ArrayAdapter<String>(view.getContext(),R.layout.activity_listview, diseaselist);
	      
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
					Intent in = new Intent(getActivity(),ddsqlite.class);
					in.putExtra("TABLE_NAME","Diseases");
					in.putExtra("_id",position+1 );
					
					
					startActivity(in);
				}
				});
				
				
	     

	}

	String KEY_ID = "_id";
	String[] diseaselist = {"Bird flu(Avian flu)",
			"Chikungunya",
			"Cholera",
			"Chicken Pox",
			"Diarrhoea",
			"Diptheria",
			"Dengue",
			"Dysentry",
			"Hepatitis A",
			"Hepatitis B",
			"Hepatitis C",
			"Hepatitis E",
			"Influenza(Flu)",
	"Jaundice",
	"Japanese Encephalitis",
	"Malaria",
	"Measles",
	"Mumps",
	"Plague",
	"Pneumonia",
	"Polio",
	"Rabies",
	"Rubella",
	"Yellow fever",
	"Tetanus",
	"Tuberculosis",
	"Tyhpoid",
	"Typhus",
	"Viral fever",
	"Syphilis",
	"Gonorrhea",
	"Swine flu"
};
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static dd newInstance(int sectionNumber) {
		dd fragment = new dd();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public dd() {
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
