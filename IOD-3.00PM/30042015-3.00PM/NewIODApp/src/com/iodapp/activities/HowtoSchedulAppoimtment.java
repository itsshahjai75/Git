package com.iodapp.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.util.URLSuppoter;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HowtoSchedulAppoimtment extends Fragment {
	
	
	
	
	
	TextView etResponse;
	private ProgressBar progressBar;
	private Typeface sysFont;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.howisschuduleappointment, container, false);
        etResponse = (TextView) rootView.findViewById(R.id.etResponse);
        setSystemFont();
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		new HttpAsyncTask()
				.execute("http://jsoncdn.webcodeplus.com/ContentData.svc/ContentDetailsFromContentID/30/1425");
        return rootView;
    }
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		
		etResponse.setTypeface(sysFont);
		
		
	}
	
//	public static String GET(String url) {
//		InputStream inputStream = null;
//		String result = "";
//		try {
//
//			// create HttpClient
//			HttpClient httpclient = new DefaultHttpClient();
//
//			// make GET request to the given URL
//			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//			// receive response as inputStream
//			inputStream = httpResponse.getEntity().getContent();
//
//			// convert inputstream to string
//			if (inputStream != null)
//				result = convertInputStreamToString(inputStream);
//			else
//				result = "Did not work!";
//
//		} catch (Exception e) {
//			Log.d("InputStream", e.getLocalizedMessage());
//		}
//
//		return result;
//	}

//	private static String convertInputStreamToString(InputStream inputStream)
//			throws IOException {
//		BufferedReader bufferedReader = new BufferedReader(
//				new InputStreamReader(inputStream));
//		String line = "";
//		String result = "";
//		while ((line = bufferedReader.readLine()) != null)
//			result += line;
//
//		inputStream.close();
//		return result;
//
//	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private URLSuppoter supporter;

		@Override
		protected String doInBackground(String... urls) {

			supporter = new URLSuppoter();
			return supporter.GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			progressBar.setVisibility(View.INVISIBLE);
			try {
				JSONObject json = new JSONObject(result);
				// etResponse.setText(json.toString(1));
				// JSONObject json = new JSONObject(result);
				String str = "";

				//str += json.getString("ContentTitle");
				str += "<br><br><br>";
			str += json.getString("FullDescription");
				 
				Spanned sp = Html.fromHtml(str);
				etResponse.setText(sp);
				// etResponse.setText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
			Toast toast = Toast.makeText(getActivity(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();

		}
	}

	public static MovementMethod getInstance() {
		// TODO Auto-generated method stub
		return null;
	}
}


