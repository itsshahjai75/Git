package com.opentok.android.demo.multiparty;



import com.iodapp.activities.DashboardActivity;
import com.iodapp.activities.DoctorAvailableActivity;
import com.iodapp.activities.Feedback;
import com.iodapp.activities.ParentForFragment;
import com.iodapp.activities.R;
import com.iodapp.activities.ScheduleAppintment;
import com.iodapp.activities.Sharedpref;
import com.iodapp.activities.TransMoreRemove;
import com.iodapp.util.SetDoctoreAvailable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.Connection;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.SubscriberKit;
import com.opentok.android.demo.config.OpenTokConfig;
import com.opentok.android.demo.opentoksamples.MultipartyActivity;
import com.opentok.android.demo.ui.MeterView;

public class MySession extends Session {

	Context mContext;
	String callaccepted;
	private String singnal;
	Stream streamvalue;
	// Interface
	ViewPager mSubscribersViewContainer;
	ViewGroup mPreview;
	TextView mMessageView;
	ScrollView mMessageScroll;
	public static Publisher mPublisher;
	// Players status
	ArrayList<MySubscriber> mSubscribers = new ArrayList<MySubscriber>();
	HashMap<Stream, MySubscriber> mSubscriberStream = new HashMap<Stream, MySubscriber>();
	HashMap<String, MySubscriber> mSubscriberConnection = new HashMap<String, MySubscriber>();
	private String errorUrl = "";
	
	
	public MySession(Context context) {
		super(context, OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID);
		this.mContext = context;
		setSystemFont();
	}

	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return ((MySubscriber) arg1).getView() == arg0;
		}

		@Override
		public int getCount() {
			return mSubscribers.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position < mSubscribers.size()) {
				return mSubscribers.get(position).getName();
			} else {
				return null;
			}
		}
		
		

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			MySubscriber p = mSubscribers.get(position);
			container.addView(p.getView());
			return p;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			for (MySubscriber p : mSubscribers) {
				if (p == object) {
					if (!p.getSubscribeToVideo()) {
						p.setSubscribeToVideo(true);
					}
				} else {
					if (p.getSubscribeToVideo()) {
						p.setSubscribeToVideo(false);
					}
				}
			}
		}
		

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			MySubscriber p = (MySubscriber) object;
			container.removeView(p.getView());
		}

		@Override
		public int getItemPosition(Object object) {
			for (int i = 0; i < mSubscribers.size(); i++) {
				if (mSubscribers.get(i) == object) {
					return i;
				}
			}
			return POSITION_NONE;
		}

	};


	private boolean isCallAccepted=false;

	private Typeface sysFont;


	




	
	
	// public methods
	public void setPlayersViewContainer(ViewPager container) {
		this.mSubscribersViewContainer = container;
		this.mSubscribersViewContainer.setAdapter(mPagerAdapter);
		mPagerAdapter.notifyDataSetChanged();
	}

	public void setMessageView(TextView et, ScrollView scroller) {
		this.mMessageView = et;
		this.mMessageScroll = scroller;
	}

	public void setPreviewView(ViewGroup preview) {
		this.mPreview = preview;
	}

	public void connect() {
		this.connect(OpenTokConfig.TOKEN);
	}

	public void sendChatMessage(String message) {

//		Toast.makeText(mContext, "You have sendMsg To chat...",Toast.LENGTH_SHORT ).show();
		String name = Sharedpref.getfirstname() + " "
				+ Sharedpref.getlastname();
		// String str = mPublisher.getStream().getStreamId() + "|"
		// + mPublisher.getStream().getName() + "|" + 0;
		String str = mPublisher.getStream().getStreamId() + "|" + name + "|"
				+ 0;
		JSONObject json = new JSONObject();

		// try {
		// // json.put("name", mPublisherName);
		// json.put("streamId", str);
		sendSignal("begincall", str);
		presentMessage("Me", message);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// sendSignal("begincall",str );
		// presentMessage("Me", message);
	}
	
	
	

	public void SendEndCallSignal(String message) {

		String name = Sharedpref.getfirstname() + " "
				+ Sharedpref.getlastname();
		// String str = mPublisher.getStream().getStreamId() + "|"
		// + mPublisher.getStream().getName() + "|" + 0;
		String str = mPublisher.getStream().getStreamId() + "|" + name;
//		JSONObject json = new JSONObject();

		// try {
		// // json.put("name", mPublisherName);
		// json.put("streamId", str);
		sendSignal("endcall", str);
		presentMessage("Me", message);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// sendSignal("begincall",str );
		// presentMessage("Me", message);
	}

	// callbacks
	@Override
	protected void onConnected() {
		mPublisher = new Publisher(mContext, Sharedpref.getfirstname()+" "+Sharedpref.getlastname());
		publish(mPublisher);
		MultipartyActivity.progress_circle.setVisibility(View.GONE);
		// Add video preview
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPreview.addView(mPublisher.getView(), lp);
		mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
				BaseVideoRenderer.STYLE_VIDEO_FILL);
		presentText("Welcome to OpenTok Chat.");
		Log.d("OnCnnectMethid", "Welcome To open talk chat.......");
//		Toast.makeText(mContext, "You Connect With your cemera..",Toast.LENGTH_SHORT ).show();
		// // set start button enable/visible
		// MultipartyActivity.btncall.setEnabled(true);
//		mPublisher.setCameraId(CameraInfo.CAMERA_FACING_FRONT);
//		mPublisher.swapCamera();
		
		
		

	}
	
	
	
	
	

	@Override
	protected void onStreamReceived(Stream stream) {

//		Toast.makeText(mContext, "StreamRecieved",Toast.LENGTH_SHORT ).show();
		// streamvalue=stream;
		MySubscriber p = new MySubscriber(mContext, stream);
		
		
		p.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
		        BaseVideoRenderer.STYLE_VIDEO_FILL);
		// we can use connection data to obtain each user id
		p.setUserId(stream.getConnection().getData());
		
		// Subscribe audio only if we have more than one player
		if (mSubscribers.size() != 0) {
			p.setSubscribeToVideo(false);
			
		}

		// Subscribe to this player
//		 this.subscribe(p);

		
		
		mSubscribers.add(p);
		mSubscriberStream.put(stream, p);
		mSubscriberConnection.put(stream.getConnection().getConnectionId(), p);
		mPagerAdapter.notifyDataSetChanged();
		
//		MultipartyActivity.starttime();
		MultipartyActivity.btncall.setVisibility(View.VISIBLE);
//		MultipartyActivity.btncall.setEnabled(true);
		MultipartyActivity.btncall.setEnabled(true);
		presentText("\n" + p.getName() + " has joined the chat");
		Log.d("OnstreamRecieve", p.getName()+"has Joined the chat...");
		
	}

	
	@Override
	protected void onStreamDropped(Stream stream) {
		MySubscriber p = mSubscriberStream.get(stream);
		if (p != null) {
			mSubscribers.remove(p);
			mSubscriberStream.remove(stream);
			mSubscriberConnection.remove(stream.getConnection()
					.getConnectionId());
			mPagerAdapter.notifyDataSetChanged();

			presentText("\n" + p.getName() + " has left the chat");
			if (MultipartyActivity.countDownTimer != null) {
				MultipartyActivity.countDownTimer.cancel();
				MultipartyActivity.countDownTimer = null;
			}
		}
		
		if(!singnal.equalsIgnoreCase("endcall") && !MultipartyActivity.userEnd.equalsIgnoreCase("end") && !callaccepted.equalsIgnoreCase("no"))
		{
//			MultipartyActivity.starttime();
			
			ErrorDialog();
		}
	}
	
	private void ErrorDialog()

	{
		
//		MultipartyActivity.countDownTimer.cancel();
		
		
		String set_str = " http://jsoncdn.webcodeplus.com/CallWatingData.svc/EndCallonConnectionDestroy/"+Sharedpref.getcustomerid()+"/"+MultipartyActivity.token_id_oncreat_multi;
		SetDoctoreAvailable SetDA = new SetDoctoreAvailable();
		SetDA.execute(set_str);
		
		final Dialog rankDialog = new Dialog(mContext, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.error_dialog);
        rankDialog.setCancelable(false);
       
        TextView text3 = (TextView) rankDialog.findViewById(R.id.error_desc);
        text3.setTypeface(sysFont);
        TextView text = (TextView) rankDialog.findViewById(R.id.link1);
        text.setText(Html.fromHtml("<a href = \" CLICK HERE \"> CLICK HERE </a>  to Video call or"));
        text.setTypeface(sysFont);
        text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(Feedback.this, "You Click On HyperLInk", Toast.LENGTH_SHORT).show();
				
				Sharedpref.setTokenId("");
				Sharedpref.setTokenkey("");
				Sharedpref.setsessionkey("");
				Intent it_login = new Intent(mContext,
						DoctorAvailableActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						mContext, R.anim.animation,
						R.anim.animation2).toBundle();
				
				it_login.putExtra("Fragment_Code", R.id.link1);
				mContext.startActivity(it_login, bndlanimation);
				((Activity)mContext).finish();
				
			rankDialog.dismiss();
				
				
			}
		});
        
        
        
        TextView text2 = (TextView) rankDialog.findViewById(R.id.link2);
        text2.setText(Html.fromHtml("<a href = \" CLICK HERE \"> CLICK HERE </a>  to Scheduale Appointment."));
        text2.setTypeface(sysFont);
        text2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(Feedback.this, "You Click On HyperLInk", Toast.LENGTH_SHORT).show();
				
				
				Intent it_login = new Intent(mContext,
						ScheduleAppintment.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						mContext, R.anim.animation,
						R.anim.animation2).toBundle();
				
				it_login.putExtra("Fragment_Code", R.id.link2);
				mContext.startActivity(it_login, bndlanimation);
				((Activity)mContext).finish();
				
			rankDialog.dismiss();
				
				
			}
		});
        
        

        
        Button home = (Button) rankDialog.findViewById(R.id.Home);
        home.setTextColor(Color.parseColor("#00bfff"));
        home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent it_login = new Intent(mContext,
						DashboardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						mContext, R.anim.animation,
						R.anim.animation2).toBundle();
				mContext.startActivity(it_login, bndlanimation);
				
				((Activity)mContext).finish();
//				Toast toast=Toast.makeText(getApplicationContext(), "Success - Redorect to Dashboared", Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
//				toast.show();
//				hidePDialog();
				
            	
			}
		});
        
        //now that the dialog is set up, it's time to show it    
        rankDialog.show();    
	}

	@Override
	protected void onSignalReceived(String type, String data,
			Connection connection) {

		singnal = type;
		
		if (type != null && "begincall".equals(type)) {
			String mycid = this.getConnection().getConnectionId();
			String cid = connection.getConnectionId();
			if (!cid.equals(mycid)) {
				MySubscriber p = mSubscriberConnection.get(cid);
				if (p != null) {
					presentMessage(p.getName(), data);
					Log.d("pp", "" + data);
//					Toast.makeText(mContext, "You has connetct only doctor..",Toast.LENGTH_SHORT ).show();
					
				}
			}
		} else if (type != null && "acceptcall".equals(type)) {
			String mycid = this.getConnection().getConnectionId();
			String cid = connection.getConnectionId();
			if (!cid.equals(mycid)) {
				MySubscriber p = mSubscriberConnection.get(cid);
				if (p != null) {
					JSONObject json;
					String streamid;
					String text = "";

					try {
						json = new JSONObject(data);
						text = json.getString("callaccepted");
						// String name = json.getString("name");
						// p.setmName(name);

						presentMessage(p.getName(), text);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// presentMessage(p.getName(), data);
					String[] data1 = text.trim().split("[|]");
					streamid = data1[0];
					String name = data1[1];
				   callaccepted = data1[2];
					if (callaccepted.equals("yes")) {
						//
						Toast.makeText(mContext, "Doctor Accepted your call...",Toast.LENGTH_SHORT ).show();
						isCallAccepted = true;
						this.subscribe(p);
						// MultipartyActivity.timervalue="1";
						Sharedpref.settimerval("1");

						// Toast.makeText(mContext, "Yes",
						// Toast.LENGTH_LONG).show();
						MultipartyActivity.btncall.setVisibility(View.GONE);
						MultipartyActivity.btnendcall
								.setVisibility(View.VISIBLE);
						MultipartyActivity.starttime();
					} else if (callaccepted.equals("no")) {
						isCallAccepted = false;
						Toast.makeText(mContext, "Doctor Dont't Accepted your call...",Toast.LENGTH_SHORT ).show();
						p.destroy();
						
						String set_str = " http://jsoncdn.webcodeplus.com/TokenData.svc/CheckIfTokenAvailable/"+MultipartyActivity.token_id_oncreat_multi;
						SetDoctoreAvailable SetDA = new SetDoctoreAvailable();
						SetDA.execute(set_str);
						
						RejectCallDialog();
						
					}
					Log.d("pp12", "" + data);
				}
			}
		} else if (type != null && "endcall".equals(type)) {
			// mSession.disconnect();
			String mycid = this.getConnection().getConnectionId();
			String cid = connection.getConnectionId();
			
			if (!cid.equals(mycid)) {
				MySubscriber p = mSubscriberConnection.get(cid);
				
				
				
				MultipartyActivity.isEnd = true;
				if (p != null) {
					p.destroy();
				}
			}
			disconnect();
			if(!MultipartyActivity.userEnd.equalsIgnoreCase("end"))
			{
				MultipartyActivity.starttime();
				
				RatingDialog();
			}
			
		}

	}
	
	
	public void oncancelcall(){
		
		String mycid = this.getConnection().getConnectionId();
		String cid = connection.getConnectionId();
		
		Log.d("seesion_cenntionID=============",mycid);
		
		if (!cid.equals(mycid)) {
			MySubscriber p = mSubscriberConnection.get(cid);
			if(p!=null){
				p.destroy();
				disconnect();
			}
		}
		
	}
	

	private void RatingDialog()
	{
		final Dialog rankDialog = new Dialog(mContext, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.customer_dailog);
        rankDialog.setCancelable(false);
       RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(2.0f);
        ratingBar.setVisibility(View.GONE);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText("Dr. "+MultipartyActivity.str_drName+" ended this Video Call.");

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setTextColor(Color.parseColor("#00bfff"));
        updateButton.setOnClickListener(new View.OnClickListener() {
           
        	@Override
            public void onClick(View v) {
            	
            	
        		Sharedpref.setTokenId("");
        		Sharedpref.setTokenkey("");
        		Sharedpref.setsessionkey("");
            	
            	Intent it_login = new Intent(mContext,
						DashboardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						mContext, R.anim.animation,
						R.anim.animation2).toBundle();
				mContext.startActivity(it_login, bndlanimation);
				((Activity)mContext).finish();
				
				
				
//				Toast toast=Toast.makeText(getApplicationContext(), "Success - Redorect to Dashboared", Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
//				toast.show();
//				hidePDialog();
				
            	
            	
            	rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it    
        rankDialog.show();    
	}
	private void presentMessage(String who, String message) {
		presentText("\n" + who + ": " + message);
	}

	private void presentText(String message) {
		mMessageView.setText(mMessageView.getText() + message);
		mMessageScroll.post(new Runnable() {
			@Override
			public void run() {
				int totalHeight = mMessageView.getHeight();
				mMessageScroll.smoothScrollTo(0, totalHeight);
			}
		});
	}
	//
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(mContext.getAssets(),"font/Leelawadee.ttf");
	}
	
	private void RejectCallDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Doctore Reject Your Call").setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						Intent it_login = new Intent(mContext,
								DoctorAvailableActivity.class);
						Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
								mContext, R.anim.animation,
								R.anim.animation2).toBundle();
						
						it_login.putExtra("Fragment_Code", R.id.link1);
						mContext.startActivity(it_login, bndlanimation);
						((Activity)mContext).finish();
						
						dialog.cancel();
						
					}
				  });
			
 
			
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
//				alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
				//alertDialog.getButton(1).setBackgroundColor(Color.BLACK);
			
				
 
				// show it
				alertDialog.show();
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
				
	}

}
