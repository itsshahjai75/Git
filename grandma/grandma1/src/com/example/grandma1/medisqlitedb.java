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


public class medisqlitedb extends Activity {
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(temp==0)
		finish();
	}
	String[] qlist;
	private static final String DB_NAME = "medimod1.sql";
	public static final String KEY_ID="_id";
	public static final String KEY_QUESTION="mediname";
	public static final String KEY_YES="content";
	public static final String KEY_NO="description";
	public static final String TABLE_NAME="TABLE_NAME";


	//private TextView txtText;


	 

	String key,qid,str,tabname;
	int temp=0;
	private List<Category> catList;
	
	private SQLiteDatabase database;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.expand);
	        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790B28")));
	       
	        ExpandableListView exList = (ExpandableListView) findViewById(R.id.expandableListView1);
	        ExternalDbOpenHelper db= new ExternalDbOpenHelper(this, DB_NAME);
	        database = db.openDataBase();
	        Intent in = getIntent();
	        key = in.getStringExtra("Keyword");
	        tabname =in.getStringExtra(TABLE_NAME);
	        getID(key);
	        initData();
	        ExpandableAdapter exAdpt = new ExpandableAdapter(catList, this);
	           exList.setAdapter(exAdpt);
	           
	    
    }

    private void initData() {
    	catList = new ArrayList<Category>();
        
        int i=0;
    	while(i<qlist.length)
    	{
    	
    	String med=getQuestion(qlist[i]);	
    	String con=getcontent(qlist[i]);
    	String desc = getdescription(qlist[i]);
    	Category cat1 = createCategory( med, i);
    	cat1.setItemList(createItems(med, con, desc,1));
    	catList.add(cat1);
    	i++;
    	}
    }
    
    private Category createCategory(String name, long id) {
    	return new Category(id, name);
    }
    
    
    private List<ItemDetail> createItems(String name, String content, String descr, int num) {
    	List<ItemDetail> result = new ArrayList<ItemDetail>();
    	
    	for (int i=0; i < num; i++) {
    		ItemDetail item = new ItemDetail(i, name, content, descr);
    		result.add(item);
    	}
    	
    	return result;
    }     //That’s it, the database is open!
	        
	      
	    

	//Extracting elements from the database
	private String getQuestion(String qid) {
	
	Cursor c= database.query(tabname, new String[] {KEY_ID,
	            KEY_QUESTION,KEY_YES,KEY_NO}, KEY_ID + "= '" + qid + "'", null, null, null, null);
	if(c!=null)
	{
	c.moveToFirst();
	String name = c.getString(1);
	c.close();
	return name;
	
	}
	c.close();
	return null;
	}
	private String getcontent(String qid) {
		
		Cursor c= database.query(tabname, new String[] {KEY_ID,
		            KEY_QUESTION,KEY_YES,KEY_NO}, KEY_ID + "= '" + qid + "'", null, null, null, null);
		if(c!=null)
		{
		c.moveToFirst();
		String name = c.getString(2);
		c.close();
		return name;
		
		}
		c.close();
		return null;
		}
	private String getdescription(String qid) {
		
		Cursor c= database.query(tabname, new String[] {KEY_ID,
		            KEY_QUESTION,KEY_YES,KEY_NO}, KEY_ID + "= '" + qid + "'", null, null, null, null);
		if(c!=null)
		{
		c.moveToFirst();
		String name = c.getString(3);
		c.close();
		return name;
		
		}
		c.close();
		return null;
		}
	 

	private void getID(String a) {
		try{
		Cursor c= database.query(tabname, new String[] {KEY_ID,
		            KEY_QUESTION,KEY_YES,KEY_NO}, KEY_ID + " LIKE '" + a + "%'", null, null, null, null);
		c.moveToFirst();
		int i=0;
		qlist=new String[c.getCount()];
		while(c!=null)
		{
		String name = c.getString(0);
		qlist[i]=name;
		i++;
		c.moveToNext();
		}
		c.close();
		}
		catch(Exception e)
		{Toast.makeText(this, "Something is up here!",100);}
		}

}
		
