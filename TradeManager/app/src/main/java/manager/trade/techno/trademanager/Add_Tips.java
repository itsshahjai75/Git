package manager.trade.techno.trademanager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import MyFirebase.Tips_dataobject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Tips extends Fragment {

    SharedPreferences sharepref;

    EditText et_tipstext, et_tipsurl, et_tipstitle;

    Button btn_change;
    ArrayAdapter<CharSequence> adapter;
    String img_string, str_tipstext, str_tipsurl, admin_email, res, str_tipstitle;
    private DatabaseReference databaseReference,databaseReference2;
    JSONArray jsonArray;
    OkHttpClient mClient;

    long child_count;


    public Add_Tips() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new OkHttpClient();

        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();//add your user refresh tokens who are logged in with firebase.

        jsonArray = new JSONArray();


        databaseReference2 = FirebaseDatabase.getInstance().getReference("users");
        databaseReference2.orderByValue().limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getAllTask(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getAllTask(DataSnapshot dataSnapshot){

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

            String token = singleSnapshot.child("token").getValue().toString();
            //Log.e("token" ,singleSnapshot.child("token").getValue().toString());
            jsonArray.put(token);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for convertView fragment
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/ProductSans-Regular.ttf");

        final View convertView = inflater.inflate(R.layout.fragment_add__tips, container, false);
        //==add convertView line to change all font to coustom font in fragments
        fontChanger.replaceFonts((ViewGroup) convertView);


        databaseReference = FirebaseDatabase.getInstance().getReference("tips");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                   // Log.e(snap.getKey(),snap.getChildrenCount() + "");
                }
                child_count=dataSnapshot.getChildrenCount();
                //Log.d("Count",Long.toString(child_count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        et_tipstext = (EditText) convertView.findViewById(R.id.et_tipstext);
        et_tipsurl = (EditText) convertView.findViewById(R.id.et_tipsurl);
        et_tipstitle = (EditText) convertView.findViewById(R.id.et_tipstitle);




        btn_change = (Button) convertView.findViewById(R.id.btn_submit_tips);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_tipstext = et_tipstext.getText().toString();
                str_tipsurl = et_tipsurl.getText().toString();
                str_tipstitle = et_tipstitle.getText().toString();

                if (str_tipstext.isEmpty() || str_tipstitle.isEmpty()) {

                    Toast.makeText(getContext(), "Some thing Missing", Toast.LENGTH_LONG).show();
                } else {
                    long timeInMillis = System.currentTimeMillis();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTimeInMillis(timeInMillis);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd-MM-yyyy hh:mm:ss a");
                    String time = dateFormat.format(cal1.getTime());


                    Map<String, String> tips_details = new HashMap<String, String>();
                    tips_details.put("title", et_tipstitle.getText().toString());
                    tips_details.put("details", et_tipstext.getText().toString());
                    tips_details.put("time", time);
                    //=================================================================

                    //You can use the single or the value.. depending if you want to keep track

                    databaseReference.child(Long.toString(child_count+1)).setValue(tips_details);
                    sendMessage(jsonArray, et_tipstitle.getText().toString(), et_tipstext.getText().toString(),
                            time, "Trade Manager");

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Tips()).commit();

                   /* et_tipstext.setText("");
                    et_tipstitle.setText("");
                    et_tipsurl.setText("");*/

                }

            }
        });



        /*FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder("990495455884"+ "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());*/


        return convertView;
    }


    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("Tips", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(getActivity(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {


         final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AAAA5p4hOow:APA91bHQw_BQOKRd8s-S2xJ_xRssr88ZxLvUWvi7WGZ_mJ21x29WbAJqCp3clUIbwUsMq1mJS3tT9CQnMYSGG-ToRkQROGbVMFnWZHEqyKq1esfAstgIZRy5c4LyuMmNRL2nRpR33iCPnHbSG9uK_iZs2WEjXJ0cSw")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

}
