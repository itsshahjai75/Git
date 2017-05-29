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

public class SymptomC extends Fragment{
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState); 
	      ArrayAdapter adapter = new ArrayAdapter<String>(view.getContext(),R.layout.activity_listview, SymptomsArray);
	      
	      ListView listView=(ListView)view.findViewById(R.id.symlist);
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
					Intent in = new Intent(getActivity(),HelpOverlay.class);
					in.putExtra(KEY_ID,"Q1");
					switch(position)
					{
					case 0:in.putExtra("TABLE_NAME","ABDOPAINLT");
					   break;	
					case 1:in.putExtra("TABLE_NAME","ABDOPAINST");
					   break;	
					case 2:in.putExtra("TABLE_NAME","ANKLE");
					   break;						   
					case 3:in.putExtra("TABLE_NAME","BREAST_PRO_M");
					   break;	
					case 4:in.putExtra("TABLE_NAME","BREAST_PRO_W");
					   break;	
					case 5:in.putExtra("TABLE_NAME","CHESTINFANTS");
					   break;	
					case 6:in.putExtra("TABLE_NAME","CHESTPACUTE");
					   break;	
					case 7:in.putExtra("TABLE_NAME","CHESTPCHRONIC");
					   break;	
					case 8:in.putExtra("TABLE_NAME","COLDFLU");
					   break;	
					case 9:in.putExtra("TABLE_NAME","COUGH");
					   break;	
					case 10:in.putExtra("TABLE_NAME","DIAR");
					   break;	
					case 11:in.putExtra("TABLE_NAME","EAR_PRO");
					   break;	
					case 12:in.putExtra("TABLE_NAME","ELIM");
					   break;	
					case 13:in.putExtra("TABLE_NAME","ELIMINF");
					   break;	
					case 14:in.putExtra("TABLE_NAME","EYE_PRO");
					   break;	
					case 15:in.putExtra("TABLE_NAME","FACIAL");
					   break;	
					case 16:in.putExtra("TABLE_NAME","FEEDINGPRO");
					   break;	
					case 17:in.putExtra("TABLE_NAME","FEVER");
					   break;	
					case 18:in.putExtra("TABLE_NAME","FEVER_INFANTS");
					   break;	
					case 19:in.putExtra("TABLE_NAME","FOOT");
					   break;				 
					case 20:in.putExtra("TABLE_NAME","GENIINFMA");
					   break;	   
					case 21:in.putExtra("TABLE_NAME","GENINFFEM");
						   break;	
					case 22:in.putExtra("TABLE_NAME","GEN_PRO_M");
					   break;	
					case 23:in.putExtra("TABLE_NAME","GEN_PRO_W");
					   break;	
					case 24:in.putExtra("TABLE_NAME","HAIRLOSS");
					   break;	
					case 25:in.putExtra("TABLE_NAME","HAND");
					   break;	
					case 26:in.putExtra("TABLE_NAME","HEADACHES");
						   break;
					case 27:in.putExtra("TABLE_NAME","HEARINGPRO");
					   break;	
					case 28:in.putExtra("TABLE_NAME","HIP");
					   break;
					case 29:in.putExtra("TABLE_NAME","KNEE");
					   break;   
					case 30:in.putExtra("TABLE_NAME","LEG");
					   break;	
					case 31:in.putExtra("TABLE_NAME","LOWERBACKPAIN");
					   break;
					case 32:in.putExtra("TABLE_NAME","MENSTRUAL");
					   break;
					case 33:in.putExtra("TABLE_NAME","MOUTH");
					   break;
					case 34:in.putExtra("TABLE_NAME","MOUTHINF");
					   break;	
					case 35:in.putExtra("TABLE_NAME","NAUSEA");
					   break;	
					case 36:in.putExtra("TABLE_NAME","NAUSEA_INFANTS");
					   break;
					case 37:in.putExtra("TABLE_NAME","NECK_PAIN");
					   break;
					case 38:in.putExtra("TABLE_NAME","NECK_SWELL");
					   break;	
					case 39:in.putExtra("TABLE_NAME","SHORTBREATH");
					   break;	
					case 40:in.putExtra("TABLE_NAME","SHORT_BREATH_INFANTS");
					   break;	
					case 41:in.putExtra("TABLE_NAME","SHOULDER");
					   break;	
					case 42:in.putExtra("TABLE_NAME","SKIN");
					   break;	
					case 43:in.putExtra("TABLE_NAME","THROATPRO");
					   break;
					case 44:in.putExtra("TABLE_NAME","TOOTH");
					   break;
					case 45:in.putExtra("TABLE_NAME","URINATION");
					   break;
			 
					}
					startActivity(in);
				}
				});
				
				
	     

	}

	String KEY_ID = "_id";
	String[] SymptomsArray = {
			"Abdominal Pain, Long-term",

			"Abdominal Pain, Short-term",

			"Ankle Problems",

			"Breast Problems in Men",

			"Breast Problems in Women",

			"Chest Pain in Infants and Children",

			"Chest Pain, Acute",

			"Chest Pain, Chronic",

			"Cold and Flu",

			"Cough",

			"Diarrhea",

			"Ear Problems",

			"Elimination Problems",

			"Elimination Problems in Infants and Children",

			"Eye Problems",

			"Facial Swelling",
			"Feeding Problems in Infants and Children",

			"Fever",

			"Fever in Infants and Children",

			"Foot Problems",

			"Genital Problems in Infants(Male)",

			"Genital Problems in Infants(Female)",

			"Genital Problems in Men",

			"Genital Problems in Women",

			"Hair Loss",

			"Hand/Wrist/Arm Problems",

			"Headaches",

			"Hearing Problems",

			"Hip Problems",

			"Knee Problems",

			"Leg Problems",

			"Lower Back Pain",

			"Menstrual Cycle Problems",

			"Mouth Problems",

			"Mouth Problems in Infants and Children",

			"Nausea and Vomiting",

			"Nausea and Vomiting in Infants and Children",

			"Neck Pain",

			"Neck Swelling",

			"Shortness of Breath",

			"Shortness of Breath in Infants and Children",

			"Shoulder Problems",

			"Skin Rashes and Other Changes",

			"Throat Problems",

			"Tooth Problems",

			"Urination Problems"
 
};
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SymptomC newInstance(int sectionNumber) {
		SymptomC fragment = new SymptomC();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public SymptomC() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.symptomchecker, container,false);
		
	     	      	
		return rootView;
	
	      }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}



}
