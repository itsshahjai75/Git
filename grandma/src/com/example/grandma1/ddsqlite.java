package com.example.grandma1;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ddsqlite extends Activity {
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(temp==0)
		finish();
	}
	String[] qlist;
	private static final String DB_NAME = "dis.sql";
	public static final String KEY_ID="_id";
	public static final String KEY_NAME="Name";
	public static final String KEY_SYMP="Symptoms";
	public static final String KEY_AREA="Area";
	public static final String KEY_DIA="Diagnosis";
	public static final String KEY_CURES="Cures";
TextView symp,area,dia,cures;

	//private TextView txtText;


	 

	String key,qid,str,tabname;
	int temp=0;
	int id;
	private List<Category> catList;
	
	private SQLiteDatabase database;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.disdiclay);
	        symp=(TextView)findViewById(R.id.symptoms);
	        area=(TextView)findViewById(R.id.areas);
	        dia=(TextView)findViewById(R.id.diag);
	        cures=(TextView)findViewById(R.id.cure);
	        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790B28")));
	       
	       
	        
	        ExternalDbOpenHelper db= new ExternalDbOpenHelper(this, DB_NAME);
	        database = db.openDataBase();
	        Intent in = getIntent();
	        id=in.getIntExtra(KEY_ID,0);
	        
	        tabname =in.getStringExtra("TABLE_NAME");
	        String arr1[]=getTuple(id);
	        getActionBar().setTitle(arr1[0]);
	        symp.setText(arr1[1]);
	        area.setText(arr1[2]);
	        dia.setText(arr1[3]);
	        cures.setText(arr1[4]);
	        	        
	        
	       
	    }

    
	//Extracting elements from the database
	private String[] getTuple(int id) {
	
	Cursor c= database.query(tabname, new String[] {KEY_ID,
	            KEY_NAME,KEY_SYMP,KEY_AREA,KEY_DIA,KEY_CURES}, KEY_ID + " = " + id, null, null, null, null);
	if(c!=null)
	{
	c.moveToFirst();
	String[] arr=new String[5];
	arr[0] = c.getString(1);
	arr[1] = c.getString(2);
	arr[2] = c.getString(3);
	arr[3] = c.getString(4);
	arr[4] = c.getString(5);
	c.close();
	return arr;
	
	}
	c.close();
	return null;
	}
}