package com.iodapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Test extends Activity {

	private Button b1;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		b1 = (Button) findViewById(R.id.button1);
		
		
		b1.setOnClickListener(new OnClickListener() {
			
			

			private Dialog rankDialog;
			private RatingBar ratingBar;

			@Override
			public void onClick(View v) {
				rankDialog = new Dialog(Test.this, R.style.FullHeightDialog);
		        rankDialog.setContentView(R.layout.customer_dailog);
		        rankDialog.setCancelable(false);
		        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
		        ratingBar.setRating(2.0f);

		        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
		        text.setText("Dr.Alex Black");

		        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
		        updateButton.setTextColor(Color.parseColor("#00bfff"));
		        updateButton.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                rankDialog.dismiss();
		            }
		        });
		        //now that the dialog is set up, it's time to show it    
		        rankDialog.show();    
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
