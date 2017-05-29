package com.example.grandma1;

import java.util.ArrayList;
import java.util.Locale;

import com.example.grandma1.SimpleGestureFilter.SimpleGestureListener;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PrepopSqliteDbActivity extends Activity implements
TextToSpeech.OnInitListener, SimpleGestureListener{
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(temp==0)
		finish();
	}

	private static final String DB_NAME = "Symptoms.sql";
	public static final String KEY_ID="_id";
	public static final String KEY_QUESTION="Question";
	public static final String KEY_YES="YES";
	public static final String KEY_NO="NO";
	public static final String TABLE_NAME="TABLE_NAME";
	protected static final int RESULT_SPEECH = 1234;
	private Button btnSpeak;
	//private TextView txtText;
	private TextToSpeech tts;
	Button No;
	 private SimpleGestureFilter detector;
	TextView text1,text2;
	ImageView iv1;
	String qid,str,tabname;
	int temp=0;
	
	private SQLiteDatabase database;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.act1);
	        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790B28")));
	        tts = new TextToSpeech(this, this);
	        detector = new SimpleGestureFilter(this,this);
	        //Our key helper
	        
	        ExternalDbOpenHelper db= new ExternalDbOpenHelper(this, DB_NAME);
	        database = db.openDataBase();
	        Intent in = getIntent();
	        qid = in.getStringExtra(KEY_ID);
	        tabname =in.getStringExtra(TABLE_NAME);
	        btnSpeak = (Button) findViewById(R.id.btnSpeak);
	        btnSpeak.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					temp=1;
					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
					
					
					try {
					
						startActivityForResult(intent, RESULT_SPEECH);
					//	txtText.setText("");
					} catch (ActivityNotFoundException a) {
						Toast t = Toast.makeText(getApplicationContext(),
								"Ops! Your device doesn't support Speech to Text",
								Toast.LENGTH_SHORT);
						t.show();
					}
				}
			});
	        
			No=(Button)findViewById(R.id.no);
			text1=(TextView)findViewById(R.id.textView1);
			text2=(TextView)findViewById(R.id.qid);
			iv1=(ImageView)findViewById(R.id.imageView1);
			//qid=text2.getText().toString();
	        if(qid.charAt(0) == 'A')
	        {
	        	String quest = getQuestion(qid);
	        	this.setTitle("DIAGNOSIS");
	        	detector.setEnabled(false);
	        	iv1.setVisibility(View.INVISIBLE);
	        	text1.setText(quest);
	        	No.setVisibility(View.INVISIBLE);
	        	btnSpeak.setText("CURE");
	        	btnSpeak.setVisibility(View.VISIBLE);
	        	btnSpeak.setAlpha(1);
	        	btnSpeak.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
				
	        	String qid1 = getID(qid,2);
				
	        
	        	Intent i = new Intent(v.getContext(),PrepopSqliteDbActivity.class);
	        	//text2.setText(qid1);
				i.putExtra(KEY_ID, qid1);
				i.putExtra("TABLE_NAME", tabname);
				startActivity(i);
					}});
			}
	        else if(qid.charAt(0) == 'C'||qid.charAt(0) == 'E')
	        {
	        	String quest = getQuestion(qid);
	        	text1.setText(quest);
	        	detector.setEnabled(false);
	        	No.setVisibility(View.INVISIBLE);
	        	iv1.setVisibility(View.INVISIBLE);
	        	this.setTitle("CURE");
	        	btnSpeak.setAlpha(1);
	        	btnSpeak.setVisibility(View.VISIBLE);
	        	btnSpeak.setText("HOME");
	        	btnSpeak.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
				
	        	String qid1 = getID(qid,2);
				//text1.setText(qid1);
				Intent i = new Intent(v.getContext(), MainActivity.class);
				
				startActivity(i);
					}});
	        	}
	        else
	        {
	        String quest = getQuestion(qid);
	        
	        
			text1.setText(quest);
		/*	Yes.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String qid1 = getID(qid,2);
					String que = getQuestion(qid1);
					qid=qid1;
				//	text1.setText(que);
					Intent in = new Intent(v.getContext(),PrepopSqliteDbActivity.class);
					in.putExtra(KEY_ID, qid1);
					in.putExtra("TABLE_NAME", tabname);
					startActivity(in);
					overridePendingTransition(R.anim.swipelr,R.anim.swipelr1);
				}
			});*/
			No.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String qid1 = getID(qid,3);
					String que = getQuestion(qid1);
					qid=qid1;
					//text1.setText(que);
					
					Intent in = new Intent(v.getContext(),PrepopSqliteDbActivity.class);
					in.putExtra(KEY_ID, qid1);
					in.putExtra("TABLE_NAME", tabname);
					startActivity(in);
					overridePendingTransition(R.anim.swiperl,R.anim.swiperl1);
				}
			});
	        }
		}
		
	        //That’s it, the database is open!
	        
	      
	    

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
	
	private String getID(String a,int n) {
		
		Cursor c= database.query(tabname, new String[] {KEY_ID,
		            KEY_QUESTION,KEY_YES,KEY_NO}, KEY_ID + "= '" + a + "'", null, null, null, null);
		if(c!=null)
		{
		c.moveToFirst();
		String name = c.getString(n);
		c.close();
		return name;
		
		}
		c.close();
		return null;
		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		temp=0;
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				str=text.get(0).toString();
				if(str.equals("no"))
				{
					String qid1 = getID(qid,3);
					String que = getQuestion(qid1);
					qid=qid1;
					//text1.setText(que);
					
					Intent in = new Intent(getApplicationContext(),PrepopSqliteDbActivity.class);
					in.putExtra(KEY_ID, qid1);
					in.putExtra("TABLE_NAME", tabname);
					startActivity(in);
					
					
					overridePendingTransition(R.anim.swiperl,R.anim.swiperl1);
					Toast t = Toast.makeText(getApplicationContext(),
							"You spoke "+str,
							
							Toast.LENGTH_SHORT);
					t.show();
					
				}
				else if(str.equals("yes")||str.equals("yeah")||str.equals("ya")||str.equals("yea"))
				{
					String qid1 = getID(qid,2);
					String que = getQuestion(qid1);
					qid=qid1;
				//	text1.setText(que);
					Intent in = new Intent(getApplicationContext(),PrepopSqliteDbActivity.class);
					in.putExtra(KEY_ID, qid1);
					
					in.putExtra("TABLE_NAME", tabname);
					
					startActivity(in);
					overridePendingTransition(R.anim.swipelr,R.anim.swipelr1);
					Toast t = Toast.makeText(getApplicationContext(),
							"You spoke "+str,
							
							Toast.LENGTH_SHORT);
					t.show();
					
				}
				else
				{
					Toast t = Toast.makeText(getApplicationContext(),
							"Oops! Cant recognize..Please Speak Again "+str,
							
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
			break;
		}

		}
	}
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				btnSpeak.setEnabled(true);
				speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}

	private void speakOut() {

		String text = text1.getText().toString();

		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		 switch (direction) {
		 
		  case SimpleGestureFilter.SWIPE_RIGHT : String qid1 = getID(qid,2);
			String que = getQuestion(qid1);
			qid=qid1;
		//	text1.setText(que);
			Intent in = new Intent(getApplicationContext(),PrepopSqliteDbActivity.class);
			in.putExtra(KEY_ID, qid1);
			in.putExtra("TABLE_NAME", tabname);
			startActivity(in);
			overridePendingTransition(R.anim.swipelr,R.anim.swipelr1);
		    break;
		  case SimpleGestureFilter.SWIPE_LEFT :
				String qid2 = getID(qid,3);
				String que2 = getQuestion(qid2);
				qid=qid2;
				//text1.setText(que);
				
				Intent in2 = new Intent(getApplicationContext(),PrepopSqliteDbActivity.class);
				in2.putExtra(KEY_ID, qid2);
				in2.putExtra("TABLE_NAME", tabname);
				
				startActivity(in2);
				overridePendingTransition(R.anim.swiperl,R.anim.swiperl1);
				break;
		
	}
	}
	   
	public boolean dispatchTouchEvent(MotionEvent me){
	    	// Call onTouchEvent of SimpleGestureFilter class
	         this.detector.onTouchEvent(me);
	       return super.dispatchTouchEvent(me);
	    }
	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}

}
		
