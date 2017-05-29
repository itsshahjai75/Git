package com.iodapp.activities;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.opentok.android.demo.opentoksamples.OpenTokSamples;
import com.opentok.android.demo.opentoksamples.UIActivity;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartVideoCallFragment extends Fragment {
//	Context c;
	public StartVideoCallFragment(){}
	Button btncall;
	 TextView txtLabel;
	 EditText etdocid;
	 PersonDetail person;
	 Button button;
	 Button button1;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 		
        View rootView = inflater.inflate(R.layout.fragment_start_video_call, container, false);
			
        return rootView;
        
    }
	
}
