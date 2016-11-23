package com.iodapp.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.iodapp.adapter.HistoryAdapter;
import com.iodapp.adapter.OrderAdatper;
import com.iodapp.model.DateFormater;
import com.iodapp.model.HistoryBean;
import com.iodapp.model.OrderHistoryBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class OrderHistory extends Activity {

	private ListView order_list;
	private OrderAdatper schAdapter;
	private List<OrderHistoryBean> historyItems = new ArrayList<OrderHistoryBean>();
	private ProgressDialog pDialog;
	private String url = "http://jsoncdn.webcodeplus.com/OrderData.svc/GetOrderSummary/30/"+Sharedpref.getcustomerid();
	private ImageButton o_backbtn;
	private TextView title;
	private Typeface sysFont;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.activity_order_history);
		
		
		
		o_backbtn = (ImageButton) findViewById(R.id.order_backbtn);
		title = (TextView) findViewById(R.id.o_title);
		
		order_list = (ListView) findViewById(R.id.order_h_list);
		schAdapter = new OrderAdatper(OrderHistory.this, historyItems);
		order_list.setAdapter(schAdapter);
		
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		getOrderHistoryList();
		
		 setSystemFont();

		 o_backbtn.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
		 // TODO Auto-generated method stub

		 finish();
		 }
		 });
		 }

		 private void setSystemFont()
		 {
		 sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");


		 title.setTypeface(sysFont);


		 }

		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_history, menu);
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
	
	public void getOrderHistoryList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d("Order History", response.toString());
						hidePDialog();
						String str = "";
						Bitmap mBitmap = null;
						// Parsing json
						historyItems.clear();
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								OrderHistoryBean sData = new OrderHistoryBean();
								
								sData.setOrd_date(DateFormater.getDate(obj.getString("OrderDate")));
								sData.setDay(DateFormater.getDay(obj.getString("OrderDate")));
								sData.setMonth(DateFormater.formateMonth(obj.getString("OrderDate")));
								sData.setOrd_id(obj.getString("OrderID"));
								sData.setPlan(obj.getString("ProductName"));
								sData.setPaid(obj.getString("OrderTotal"));
								
								
								
								historyItems.add(sData);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						schAdapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Order History", "Error: " + error.getMessage());
						hidePDialog();

					}
				});

		// Adding request to request queue
	Sharedpref.getInstance().addToRequestQueue(movieReq);

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
}
