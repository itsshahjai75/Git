package com.opentok.android.demo.opentoksamples;

import com.iodapp.activities.R;
import com.iodapp.activities.Sharedpref;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethod.SessionCallback;
import android.view.inputmethod.InputMethodSession;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.opentok.android.demo.services.ClearNotificationService;
import com.opentok.android.demo.config.OpenTokConfig;
import com.opentok.android.demo.services.ClearNotificationService.ClearBinder;
import com.opentok.android.demo.ui.AudioLevelView;
import com.opentok.android.demo.ui.fragments.PublisherControlFragment;
import com.opentok.android.demo.ui.fragments.PublisherStatusFragment;
import com.opentok.android.demo.ui.fragments.SubscriberControlFragment;
import com.opentok.android.demo.ui.fragments.SubscriberQualityFragment;
import com.opentok.android.demo.ui.fragments.SubscriberQualityFragment.CongestionLevel;

public class UIActivity extends Activity implements Session.SessionListener,
       	Session.ArchiveListener,Session.ConnectionListener,
        Session.StreamPropertiesListener, Publisher.PublisherListener,
        Subscriber.VideoListener, Subscriber.SubscriberListener,
        SubscriberControlFragment.SubscriberCallbacks,
        PublisherControlFragment.PublisherCallbacks {

    private static final String LOGTAG = "demo-UI";
    private static final int ANIMATION_DURATION = 3000;

//    WebView webView;
    private Session mSession;
    protected Publisher mPublisher;
    protected Subscriber mSubscriber;
    private ArrayList<Stream> mStreams = new ArrayList<Stream>();
    protected Handler mHandler = new Handler();

    private boolean mSubscriberAudioOnly = false;
    private boolean archiving = false;
    private boolean resumeHasRun = false;

    // View related variables
    private RelativeLayout mPublisherViewContainer;
    private RelativeLayout mSubscriberViewContainer;
    private RelativeLayout mSubscriberAudioOnlyView;

    // Fragments
    private SubscriberControlFragment mSubscriberFragment;
    private PublisherControlFragment mPublisherFragment;
    private PublisherStatusFragment mPublisherStatusFragment;
    private SubscriberQualityFragment mSubscriberQualityFragment;
    private FragmentTransaction mFragmentTransaction;

    public HashMap<String, Connection> connectionCollection;
    // Spinning wheel for loading subscriber view
    private ProgressBar mLoadingSub;
    
    private AudioLevelView mAudioLevelView;
    
    private CongestionLevel congestion = CongestionLevel.Low;
    
	private boolean mIsBound = false;
	private NotificationCompat.Builder mNotifyBuilder;
	NotificationManager mNotificationManager;
	ServiceConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showDialog();
//        loadInterface();
//
//        if (savedInstanceState == null) {
//            mFragmentTransaction = getFragmentManager().beginTransaction();
//            initSubscriberFragment();
//            initPublisherFragment();
//            initPublisherStatusFragment();
//            initSubscriberQualityFragment();
//            mFragmentTransaction.commitAllowingStateLoss();
//        }
//
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        mPublisherFragment.showPublisherWidget(true);
////        mPublisherFragment.initPublisherUI();
////        attachPublisherView(mPublisher);
//        sessionConnect();
//        sessionConnectedHandler();
//        mSubscriber.setSubscribeToVideo(true); 
//        mSubscriber.setSubscribeToAudio(true);
//        mPublisher.setPublishVideo(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_settings:
            if (mSubscriber != null) {
                onViewClick.onClick(null);
            }
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Remove publisher & subscriber views because we want to reuse them
        if (mSubscriber != null) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());

            if (mSubscriberFragment != null) {
                getFragmentManager().beginTransaction()
                        .remove(mSubscriberFragment).commit();

                initSubscriberFragment();
                if (mSubscriberQualityFragment != null ){
                	getFragmentManager().beginTransaction()
                    .remove(mSubscriberQualityFragment).commit();
                	initSubscriberQualityFragment();
                }
            }
        }
        if (mPublisher != null) {
            mPublisherViewContainer.removeView(mPublisher.getView());

            if (mPublisherFragment != null) {
                getFragmentManager().beginTransaction()
                        .remove(mPublisherFragment).commit();

                initPublisherFragment();
            }

            if (mPublisherStatusFragment != null) {
                getFragmentManager().beginTransaction()
                        .remove(mPublisherStatusFragment).commit();

                initPublisherStatusFragment();
            }
        }

//        loadInterface();
    }

    public void loadInterface() {
        setContentView(R.layout.layout_ui_activity);

        mLoadingSub = (ProgressBar) findViewById(R.id.loadingSpinner);
//         webView = (WebView) findViewById(R.id.webView1);
        mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherView);
        mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberView);
        mSubscriberAudioOnlyView = (RelativeLayout) findViewById(R.id.audioOnlyView);

        //Initialize 
        mAudioLevelView = (AudioLevelView)findViewById(R.id.subscribermeter);
        mAudioLevelView.setIcons(BitmapFactory.decodeResource(getResources(),
     					R.drawable.headset));
//        showDialog();
        // Attach running video views
        if (mPublisher != null) {
            attachPublisherView(mPublisher);
        }
         
        // show subscriber status
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    attachSubscriberView(mSubscriber);

                    if (mSubscriberAudioOnly) {
                        mSubscriber.getView().setVisibility(View.GONE);
                        setAudioOnlyView(true);
                        congestion = CongestionLevel.High;
                    }
                }
            }
        }, 0);

        loadFragments();
    }

    public void loadFragments() {
        // show subscriber status
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    mSubscriberFragment.showSubscriberWidget(true);
                    mSubscriberFragment.initSubscriberUI();
                    
                    if (congestion != CongestionLevel.Low) {
                    	mSubscriberQualityFragment.setCongestion(congestion);
                    	mSubscriberQualityFragment.showSubscriberWidget(true);
                    }
                }
            }
        }, 0);

        // show publisher status
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPublisher != null) {
                    mPublisherFragment.showPublisherWidget(true);
                    mPublisherFragment.initPublisherUI();

                    if (archiving) {
                        mPublisherStatusFragment.updateArchivingUI(true);
                        setPubViewMargins();
                    }
                }
            }
        }, 0);
     
    }

    public void initSubscriberFragment() {
        mSubscriberFragment = new SubscriberControlFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_sub_container, mSubscriberFragment).commit();
    }

    public void initPublisherFragment() {
        mPublisherFragment = new PublisherControlFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_pub_container, mPublisherFragment).commit();
    }

    public void initPublisherStatusFragment() {
        mPublisherStatusFragment = new PublisherStatusFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_pub_status_container,
                        mPublisherStatusFragment).commit();
    }
    
    public void initSubscriberQualityFragment() {
    	 mSubscriberQualityFragment = new SubscriberQualityFragment();
         getFragmentManager()
                 .beginTransaction()
                 .add(R.id.fragment_sub_quality_container,
                		 mSubscriberQualityFragment).commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSession != null) {
            mSession.onPause();

            if (mSubscriber != null) {
                mSubscriberViewContainer.removeView(mSubscriber.getView());
            }
        }

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(this.getTitle())
                .setContentText(getResources().getString(R.string.notification))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(true);

        Intent notificationIntent = new Intent(this, UIActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        mNotifyBuilder.setContentIntent(intent);
        if(mConnection == null){	    
	    	mConnection = new ServiceConnection() {
	    		@Override
	    		public void onServiceConnected(ComponentName className,IBinder binder){
	    			((ClearBinder) binder).service.startService(new Intent(UIActivity.this, ClearNotificationService.class));
	    			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);					
	    			mNotificationManager.notify(ClearNotificationService.NOTIFICATION_ID, mNotifyBuilder.build());
	    		}

	    		@Override
	    		public void onServiceDisconnected(ComponentName className) {
	    			mConnection = null;
	    		}

	    	};
	    }

		if(!mIsBound){
			Log.d(LOGTAG, "mISBOUND GOT CALLED");
			bindService(new Intent(UIActivity.this,
					ClearNotificationService.class), mConnection,
					Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}

    }

    @Override
    public void onResume() {
        super.onResume();
        
        if(mIsBound){
			unbindService(mConnection);
			mIsBound = false;
		}
        
        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        } else {
            if (mSession != null) {
                mSession.onResume();
            }
        }

        mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);

        reloadInterface();
    }

    @Override
    public void onStop() {
        super.onStop();
        
        if(mIsBound){
			unbindService(mConnection);
			mIsBound = false;
		}
        if (isFinishing()) {
            mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
            if (mSession != null) {
                mSession.disconnect();
            }
        }
    }
    
    @Override
    public void onDestroy() {
    	mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
    	if(mIsBound){
			unbindService(mConnection);
			mIsBound = false;
		}
    	
    	if (mSession != null)  {
    		mSession.disconnect();
    	}
    	
    	restartAudioMode();
    	
    	super.onDestroy();
    	finish();
    }

    @Override
    public void onBackPressed() {
        if (mSession != null) {
            mSession.disconnect();
        }
        
        restartAudioMode();
        
        super.onBackPressed();
    }

    public void reloadInterface() {
     	mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {	
                    attachSubscriberView(mSubscriber);
                    if (mSubscriberAudioOnly) {
                        mSubscriber.getView().setVisibility(View.GONE);
                        setAudioOnlyView(true);
                        congestion = CongestionLevel.High;
                    }
                }
            }
        }, 500);

        loadFragments();
    }

    public void restartAudioMode() {
    	AudioManager Audio =  (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
    	Audio.setMode(AudioManager.MODE_NORMAL);
    	this.setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
    }
    
    private void sessionConnect() {
        if (mSession == null) {
            mSession = new Session(this, "45002332",
            		Sharedpref.getsessionkey());
            mSession.setSessionListener(this);
            mSession.setArchiveListener(this);
            mSession.setStreamPropertiesListener(this);
            mSession.connect(Sharedpref.getTokenkey());
            Log.d("coonectval", ""+mSession.getConnection());
            //       OpenTokConfig.API_KEY=45002332
//             OpenTokConfig.SESSION_ID="2_khkh....."
//            OpenTokConfig.TOKEN="T1==sdgsd"
            
        }
    }

    private void attachPublisherView(Publisher publisher) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mPublisherViewContainer.addView(publisher.getView(), layoutParams);
        mPublisherViewContainer.setDrawingCacheEnabled(true);
        publisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        publisher.getView().setOnClickListener(onViewClick);
    }

    @Override
    public void onMuteSubscriber() {
        if (mSubscriber != null) {
            mSubscriber.setSubscribeToAudio(!mSubscriber.getSubscribeToAudio());
//            mSubscriber.setSubscribeToVideo(!mSubscriber.getSubscribeToVideo());
        }
    }

    @Override
    public void onMutePublisher() {
        if (mPublisher != null) {
            mPublisher.setPublishAudio(!mPublisher.getPublishAudio());
//            mPublisher.setPublishVideo(!mPublisher.getPublishVideo());
//            mPublisher.setPublishAudio(true);
          
        }
        
    }

    @Override
    public void onSwapCamera() {
        if (mPublisher != null) {
            mPublisher.swapCamera();
        }
       
    }

    @Override
    public void onEndCall() {
        if (mSession != null) {
            mSession.disconnect();
        }
        restartAudioMode();
        
        finish();
    }

    private void attachSubscriberView(Subscriber subscriber) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        
        mSubscriberViewContainer.addView(subscriber.getView(), layoutParams);
        subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        subscriber.getView().setOnClickListener(onViewClick);
    }

    private void subscribeToStream(Stream stream) {
        mSubscriber = new Subscriber(this, stream);
        mSubscriber.setSubscriberListener(this);
        mSubscriber.setVideoListener(this);
//        mSession.subscribe(mSubscriber);
//        
//        if ( mSubscriber.getSubscribeToVideo() ) {
//        	// start loading spinning
//        	mLoadingSub.setVisibility(View.VISIBLE);
//        }
    	showDialogcall();
    }

    private void unsubscriberFromStream(Stream stream) {
        mStreams.remove(stream);
        if (mSubscriber.getStream().equals(stream)) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());
            mSubscriber = null;
            if (!mStreams.isEmpty()) {
                subscribeToStream(mStreams.get(0));
            }
        }
    }

    private void setAudioOnlyView(boolean audioOnlyEnabled) {
    	mSubscriberAudioOnly = audioOnlyEnabled;

        if (audioOnlyEnabled) {
            mSubscriber.getView().setVisibility(View.GONE);
            mSubscriberAudioOnlyView.setVisibility(View.VISIBLE);
            mSubscriberAudioOnlyView.setOnClickListener(onViewClick);

            // Audio only text for subscriber
            TextView subStatusText = (TextView) findViewById(R.id.subscriberName);
            subStatusText.setText(R.string.audioOnly);
            AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
            aa.setDuration(ANIMATION_DURATION);
            subStatusText.startAnimation(aa);
            
            
            mSubscriber
			.setAudioLevelListener(new SubscriberKit.AudioLevelListener() {
				@Override
				public void onAudioLevelUpdated(
						SubscriberKit subscriber, float audioLevel) {
					mAudioLevelView.setMeterValue(audioLevel);
				}
			});
            
//            mSubscriber
//			.setAudioLevelListener(new SubscriberKit.AudioLevelListener() {
//				@Override
//				public void onAudioLevelUpdated(
//						SubscriberKit subscriber, float audioLevel) {
//					mAudioLevelView.setMeterValue(audioLevel);
//				}
//			});
        } else {
            if (!mSubscriberAudioOnly) {
                mSubscriber.getView().setVisibility(View.VISIBLE);
                mSubscriberAudioOnlyView.setVisibility(View.GONE);

    			mSubscriber.setAudioLevelListener(null);
            }
        }
    }

    private OnClickListener onViewClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        	boolean visible = false;
 
        	if (mPublisher != null) {
        		 // check visibility of bars
            	if (!mPublisherFragment.isMPublisherWidgetVisible()) {
            		visible = true;
            	}
				mPublisherFragment.publisherClick();
				if (archiving) {
					mPublisherStatusFragment.publisherClick();
				}
                setPubViewMargins();
            	if (mSubscriber != null) {
            		mSubscriberFragment.showSubscriberWidget(visible);
            		mSubscriberFragment.initSubscriberUI();
                }
        	}
        }
    };

    public Publisher getmPublisher() {
        return mPublisher;
    }

    public Subscriber getmSubscriber() {
        return mSubscriber;
    }

    public Handler getmHandler() {
        return mHandler;
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOGTAG, "Connected to the session.");
        if (mPublisher == null) {
            mPublisher = new Publisher(this, "Publisher");
            mPublisher.setPublisherListener(this);
            attachPublisherView(mPublisher);
            mPublisher.setPublishVideo(true);
            mSession.publish(mPublisher);
//            mSession.sendSignal("begincall", Sharedpref.getfirstname(),mSession.getConnection());
         
//            mPublisherFragment.showPublisherWidget(true);
//            mPublisherFragment.initPublisherUI();
//            mPublisherStatusFragment.showPubStatusWidget(true);
//            mPublisherStatusFragment.initPubStatusUI();
        }
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOGTAG, "Disconnected to the session.");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        if (!OpenTokConfig.SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

//    public void sessionConnectedHandler() {
//		 
//    	sessionConnect();
//        //show('disconnectLink');
//        //hide('connectLink');
//    }
//
//    
//   
    @Override
    public void onStreamDropped(Session session, Stream stream) {
        mStreams.remove(stream);
        if (!OpenTokConfig.SUBSCRIBE_TO_SELF) {
            if (mSubscriber != null
                    && mSubscriber.getStream().getStreamId()
                            .equals(stream.getStreamId())) {
                mSubscriberViewContainer.removeView(mSubscriber.getView());
                mSubscriber = null;
                findViewById(R.id.avatar).setVisibility(View.GONE);
                mSubscriberAudioOnly = false;
                if (!mStreams.isEmpty()) {
                    subscribeToStream(mStreams.get(0));
                }
            }
        }
    }

    
    @Override
    public void onStreamCreated(PublisherKit publisher, Stream stream) {

        if (OpenTokConfig.SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
//                mPublisher.setPublishVideo(true);
//                mPublisherStatusFragment.setUserVisibleHint(true);
                
            }
           
        }
        mPublisherFragment.showPublisherWidget(true);
        mPublisherFragment.initPublisherUI();
        mPublisherStatusFragment.showPubStatusWidget(true);
        mPublisherStatusFragment.initPubStatusUI();
        
//        addButton(stream); 
//        mPublisher.setPublishVideo(true);
//        publisher.setPublishVideo(true);
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisher, Stream stream) {

        if (OpenTokConfig.SUBSCRIBE_TO_SELF && mSubscriber != null) {
            unsubscriberFromStream(stream);
        }
    }

    @Override
    public void onError(Session session, OpentokError exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void setPubViewMargins() {
        RelativeLayout.LayoutParams pubLayoutParams = (LayoutParams) mPublisherViewContainer
                .getLayoutParams();
        int bottomMargin = 0;
        boolean controlBarVisible = mPublisherFragment
                .isMPublisherWidgetVisible();
        boolean statusBarVisible = mPublisherStatusFragment
                .isMPubStatusWidgetVisible();
        RelativeLayout.LayoutParams pubControlLayoutParams = (LayoutParams) mPublisherFragment
                .getMPublisherContainer().getLayoutParams();
        RelativeLayout.LayoutParams pubStatusLayoutParams = (LayoutParams) mPublisherStatusFragment
                .getMPubStatusContainer().getLayoutParams();

        // setting margins for publisher view on portrait orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (statusBarVisible && archiving) {
                // height of publisher control bar + height of publisher status
                // bar + 20 px
                bottomMargin = pubControlLayoutParams.height
                        + pubStatusLayoutParams.height + dpToPx(20);
            } else {
                if (controlBarVisible) {
                    // height of publisher control bar + 20 px
                    bottomMargin = pubControlLayoutParams.height + dpToPx(20);
                } else {
                    bottomMargin = dpToPx(20);
                }
            }
        }

        // setting margins for publisher view on landscape orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (statusBarVisible && archiving) {
                bottomMargin = pubStatusLayoutParams.height + dpToPx(20);
            } else {
                bottomMargin = dpToPx(20);
            }
        }

        pubLayoutParams.bottomMargin = bottomMargin;
        pubLayoutParams.leftMargin = dpToPx(20);

        mPublisherViewContainer.setLayoutParams(pubLayoutParams);

        if (mSubscriber != null) {
            if (mSubscriberAudioOnly) {
                RelativeLayout.LayoutParams subLayoutParams = (LayoutParams) mSubscriberAudioOnlyView
                        .getLayoutParams();
                int subBottomMargin = 0;
                subBottomMargin = pubLayoutParams.bottomMargin;
                subLayoutParams.bottomMargin = subBottomMargin;
                mSubscriberAudioOnlyView.setLayoutParams(subLayoutParams);
            }
            
            setSubQualityMargins();
        }
    }

//    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//        Log.i( TAG, action );
//        // TB Methods
//        if( action.equals("initPublisher")){
//          mPublisher = new RunnablePublisher( args );
//        }else if( action.equals( "destroyPublisher" )){
//        if( myPublisher != null ){
//           cordova.getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                  myPublisher.destroyPublisher();
//                  myPublisher = null;
//               }
//           });
//
//           callbackContext.success();
//           return true;
//        }
//        }
    public void setSubQualityMargins(){
		RelativeLayout.LayoutParams subQualityLayoutParams = (LayoutParams) mSubscriberQualityFragment
				.getSubQualityContainer().getLayoutParams();
		boolean pubControlBarVisible = mPublisherFragment
				.isMPublisherWidgetVisible();
		boolean pubStatusBarVisible = mPublisherStatusFragment
				.isMPubStatusWidgetVisible();
		RelativeLayout.LayoutParams pubControlLayoutParams = (LayoutParams) mPublisherFragment
				.getMPublisherContainer().getLayoutParams();
		RelativeLayout.LayoutParams pubStatusLayoutParams = (LayoutParams) mPublisherStatusFragment
				.getMPubStatusContainer().getLayoutParams();
		RelativeLayout.LayoutParams audioMeterLayoutParams = (LayoutParams) mAudioLevelView.getLayoutParams();
				
		int bottomMargin = 0;

		// control pub fragment
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (pubControlBarVisible) {
				bottomMargin = pubControlLayoutParams.height + dpToPx(10);
			}
			if (pubStatusBarVisible && archiving) {
				bottomMargin = pubStatusLayoutParams.height + dpToPx(10);
			}
			if (bottomMargin == 0) {
				bottomMargin = dpToPx(10);
			}
			subQualityLayoutParams.rightMargin = dpToPx(10);
		}

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (!pubControlBarVisible) {
				subQualityLayoutParams.rightMargin = dpToPx(10);
				bottomMargin = dpToPx(10);
				audioMeterLayoutParams.rightMargin = 0;
				mAudioLevelView.setLayoutParams(audioMeterLayoutParams);
			
			}else {
				subQualityLayoutParams.rightMargin = pubControlLayoutParams.width;
				bottomMargin = dpToPx(10);
				audioMeterLayoutParams.rightMargin = pubControlLayoutParams.width;
			}
			if (pubStatusBarVisible && archiving) {
				bottomMargin = pubStatusLayoutParams.height + dpToPx(10);
			}
			mAudioLevelView.setLayoutParams(audioMeterLayoutParams);
		}

		subQualityLayoutParams.bottomMargin = bottomMargin;
		
		mSubscriberQualityFragment.getSubQualityContainer().setLayoutParams(
				subQualityLayoutParams);

    }


    @Override
    public void onError(PublisherKit publisher, OpentokError exception) {
        Log.i(LOGTAG, "Publisher exception: " + exception.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriber) {
    	mLoadingSub.setVisibility(View.GONE);
        mSubscriberFragment.showSubscriberWidget(true);
        mSubscriberFragment.initSubscriberUI();
    }

    @Override
    public void onDisconnected(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Subscriber disconnected.");
    }

    @Override
    public void onVideoDataReceived(SubscriberKit subscriber) {
        Log.i(LOGTAG, "First frame received");

        // stop loading spinning
        mLoadingSub.setVisibility(View.GONE);
        attachSubscriberView(mSubscriber);
    }

    @Override
    public void onError(SubscriberKit subscriber, OpentokError exception) {
        Log.i(LOGTAG, "Subscriber exception: " + exception.getMessage());
    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriber, String reason) {
        Log.i(LOGTAG, "Video disabled:" + reason);
        if (mSubscriber == subscriber) {
            setAudioOnlyView(true);
        }
        
        if (reason.equals("quality")) {
        	mSubscriberQualityFragment.setCongestion(CongestionLevel.High);
        	congestion = CongestionLevel.High;
        	setSubQualityMargins();
        	mSubscriberQualityFragment.showSubscriberWidget(true);
        }
    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriber, String reason) {
        Log.i(LOGTAG, "Video enabled:" + reason);
        if (mSubscriber == subscriber) {
            setAudioOnlyView(false);
        }
        if (reason.equals("quality")) {
        	mSubscriberQualityFragment.setCongestion(CongestionLevel.Low);
        	congestion = CongestionLevel.Low;
            mSubscriberQualityFragment.showSubscriberWidget(false);
        } 
    }

    @Override
    public void onStreamHasAudioChanged(Session session, Stream stream,
            boolean audioEnabled) {
        Log.i(LOGTAG, "Stream audio changed");
    }

    @Override
    public void onStreamHasVideoChanged(Session session, Stream stream,
            boolean videoEnabled) {
        Log.i(LOGTAG, "Stream video changed");
    }

    @Override
    public void onStreamVideoDimensionsChanged(Session session, Stream stream,
            int width, int height) {
        Log.i(LOGTAG, "Stream video dimensions changed");
    }

    @Override
    public void onArchiveStarted(Session session, String id, String name) {
    	Log.i(LOGTAG, "Archiving starts");
        mPublisherFragment.showPublisherWidget(false);

        archiving = true;
        mPublisherStatusFragment.updateArchivingUI(true);
        mPublisherFragment.showPublisherWidget(true);
        mPublisherFragment.initPublisherUI();
        setPubViewMargins();

        if (mSubscriber != null) {
            mSubscriberFragment.showSubscriberWidget(true);
        }
    }

    @Override
    public void onArchiveStopped(Session session, String id) {
    	Log.i(LOGTAG, "Archiving stops");
        archiving = false;

        mPublisherStatusFragment.updateArchivingUI(false);
        setPubViewMargins();
        
        if (mSubscriber != null) {
            setSubQualityMargins();
        }
    }
   
    /**
     * Converts dp to real pixels, according to the screen density.
     * 
     * @param dp
     *            A number of density-independent pixels.
     * @return The equivalent number of real pixels.
     */
    public int dpToPx(int dp) {
        double screenDensity = getResources().getDisplayMetrics().density;
        return (int) (screenDensity * (double) dp);
    }

    @Override
	public void onVideoDisableWarning(SubscriberKit subscriber) {
		Log.i(LOGTAG, "Video may be disabled soon due to network quality degradation. Add UI handling here.");	
		mSubscriberQualityFragment.setCongestion(CongestionLevel.Mid);
		congestion = CongestionLevel.Mid;
		setSubQualityMargins();
		mSubscriberQualityFragment.showSubscriberWidget(true);
	}

	@Override
	public void onVideoDisableWarningLifted(SubscriberKit subscriber) {
		Log.i(LOGTAG, "Video may no longer be disabled as stream quality improved. Add UI handling here.");
		mSubscriberQualityFragment.setCongestion(CongestionLevel.Low);
		congestion = CongestionLevel.Low;
		mSubscriberQualityFragment.showSubscriberWidget(false);
	}

	public void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				UIActivity.this);
 
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Wants to use your Camera").setCancelable(false)
				.setPositiveButton("Allow",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						 // Attach running video views
						 loadInterface();

//					        if (savedInstanceState == null) {
					            mFragmentTransaction = getFragmentManager().beginTransaction();
					            initSubscriberFragment();
					            initPublisherFragment();
					            initPublisherStatusFragment();
					            initSubscriberQualityFragment();
					            mFragmentTransaction.commitAllowingStateLoss();
//					        }

					        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					        
					        sessionConnect();

					       
					}
				  })
				.setNegativeButton("Deny",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						try
						{
							finish();
							dialog.cancel();
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
	public void showDialogcall()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				UIActivity.this);
 
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Connect To Call",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						 // Attach running video views
						Log.d("conn1", ""+mSession.getConnection());
//						Connection connection;
//						connection=mSession.getConnection();
//						if(connection == null)
//						{
//							  mSession.sendSignal("begincall", "hello");
////						}else
//						{
							
							 
//						}
						try{
						 mSession.sendSignal("begincall", Sharedpref.getfirstname(),mSession.getConnection());
					        mSession.subscribe(mSubscriber);
//					        mConnection.add();
//					        String mycid = mSession.getConnection().getConnectionId();
					} catch (Throwable t) {
						Log.d("SenSignal",t.getMessage(),t);
					}
//					       
					        if ( mSubscriber.getSubscribeToVideo() ) {
					        	// start loading spinning
					        	mLoadingSub.setVisibility(View.VISIBLE);
					        }
					}
				  });
//				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//						dialog.cancel();
//					}
//				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	@Override
	public void onPubliserStreamingStarted() {
		// TODO Auto-generated method stub
		mPublisher.setPublishVideo(true);
		mSession.publish(mPublisher);
		
	}

	@Override
	public void onPubliserStreamingStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionCreated(Session session, Connection connection) {
		// TODO Auto-generated method stub
		  session.getConnection().getConnectionId();
		  connection.getConnectionId();
	}

	@Override
	public void onConnectionDestroyed(Session arg0, Connection arg1) {
		// TODO Auto-generated method stub
		
	}

	

//	@Override
//	public void onSignalReceived(Session session, String type, String data,
//			Connection connection) {
//		// TODO Auto-generated method stub
////		type="begincall";
////		connection=mStreams.add();
//		  String mycid = session.getConnection().getConnectionId();
//		    String cid = connection.getConnectionId();
//		    if (!cid.equals(mycid)) {
//		        // Signal received from another client
//		    }
//		    type="begincall";
//			data=Sharedpref.getfirstname();
//		
//	}
	
//	public void sendSignal(String type,String data)
//	{
//		type="begincall";
//		data=Sharedpref.getfirstname();
//	}

//@Override
//public void onConnectionCreated(Session session, Connection connection) {
//	// TODO Auto-generated method stub
//	session.getConnection().getConnectionId();
//	connection.getConnectionId();
//}
//
//@Override
//public void onConnectionDestroyed(Session arg0, Connection arg1) {
//	// TODO Auto-generated method stub
//	
//}
}
