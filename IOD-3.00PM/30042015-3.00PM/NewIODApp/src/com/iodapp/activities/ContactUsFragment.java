package com.iodapp.activities;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.util.URLSuppoter;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.RootElement;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ContactUsFragment extends Fragment {
	
	
	private ProgressBar progressBar;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private TextView text4;
	private TextView text5;
	private TextView text6;
	private Typeface sysFont;
	
	
	public ContactUsFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
       
        text1 = (TextView) rootView.findViewById(R.id.addtitel);
        text2 = (TextView) rootView.findViewById(R.id.bus);
        text3 = (TextView) rootView.findViewById(R.id.emaititle);
        text4 = (TextView) rootView.findViewById(R.id.address);
        text5 = (TextView) rootView.findViewById(R.id.time);
        text6 = (TextView) rootView.findViewById(R.id.email);
        
        setSystemFont();
        
        

        return rootView;
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		
		text1.setTypeface(sysFont);
		text2.setTypeface(sysFont);
		text3.setTypeface(sysFont);
		
		text4.setTypeface(sysFont);
		text5.setTypeface(sysFont);
		text6.setTypeface(sysFont);
		
		
	}
	
	
	
	
}