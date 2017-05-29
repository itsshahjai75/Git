package billbook.smart.com.crickbat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import billbook.smart.com.crickbat.adapter.ScoreTableAdapterTeamA;
import billbook.smart.com.crickbat.adapter.ScoreTableAdapterTeamB;
import billbook.smart.com.crickbat.model.UserModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScoreTable extends AppCompatActivity {


    OkHttpClient mClient;
    public static TextView tv_temaA,tv_temaB,tv_datetime,tv_venue,tv_winner;

    RecyclerView rv_teamA,rv_teamB;
    SharedPreferences sharepref;

    private static final String TAG = "index fragment";
    private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    private ScoreTableAdapterTeamA adapter_rv_teamA;
    private ScoreTableAdapterTeamB adapter_rv_teamB;
    private DatabaseReference databaseReference,databaseReferenceScore;
    private ArrayList<UserModel> allindex_teamA,allindex_teamB;

    String res;
    Boolean isInternetPresent = false;
    Button btn_update;
    JSONArray jsonArray;

    int winner_score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Score Table");


        //-------------------------------------------------
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);



        jsonArray = new JSONArray();
        mClient = new OkHttpClient();


        tv_winner =(TextView)this.findViewById(R.id.tv_winner);

        tv_temaA =(TextView)this.findViewById(R.id.tv_temaA);
        tv_temaB =(TextView)this.findViewById(R.id.tv_temaB);
        tv_datetime =(TextView)this.findViewById(R.id.tv_datetime);
        tv_venue =(TextView)this.findViewById(R.id.tv_venue);
        rv_teamA =(RecyclerView) this.findViewById(R.id.rv_teamA);
        rv_teamB =(RecyclerView)this.findViewById(R.id.rv_teamB);

        btn_update =(Button) this.findViewById(R.id.btn_update);

        if(sharepref.getString(Const.PREF_USER_MOBILE_NO,"").equalsIgnoreCase("9924079199") ||
                sharepref.getString(Const.PREF_USER_MOBILE_NO,"").equalsIgnoreCase("9033228796")){
            btn_update.setEnabled(true);
        }else{
            btn_update.setEnabled(false);
        }
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long timeInMillis = System.currentTimeMillis();
                Calendar cal1 = Calendar.getInstance();
                cal1.setTimeInMillis(timeInMillis);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy hh:mm:ss a");
                String time = dateFormat.format(cal1.getTime());

                sendMessage(jsonArray, "Score Details Updated",tv_winner.getText().toString(),time, "CricBat");



            }
        });



        if(getIntent().hasExtra("match_name") && getIntent().hasExtra("date_time"))
        {
            String teamsAll =getIntent().getExtras().getString("match_name");
            String s[] = teamsAll.split("v ");
            String str_teamA = s[0];
            String str_teamB = s[1];

            tv_temaA.setText(str_teamA);
            tv_temaB.setText(str_teamB);
        }

        tv_venue.setText("Venue\n"+getIntent().getStringExtra("venue"));
        tv_datetime.setText(getIntent().getStringExtra("date_time"));



//----------------------------------------------------------------------------------------------------------------------------------------------

        allindex_teamA = new ArrayList<UserModel>();
        allindex_teamB = new ArrayList<UserModel>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //databaseReference.keepSynced(true);

        databaseReferenceScore = FirebaseDatabase.getInstance().getReference("score");
        databaseReferenceScore.keepSynced(false);


        linearLayoutManager = new LinearLayoutManager(ScoreTable.this);
        rv_teamA.setLayoutManager(linearLayoutManager);
        linearLayoutManager2 = new LinearLayoutManager(ScoreTable.this);
        rv_teamB.setLayoutManager(linearLayoutManager2);

        adapter_rv_teamA = new ScoreTableAdapterTeamA(ScoreTable.this, allindex_teamA);
        adapter_rv_teamB = new ScoreTableAdapterTeamB(ScoreTable.this, allindex_teamB);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    //Log.e("objects =====" ,""+dataSnapshot.toString());

                    String name = singleSnapshot.child("name").getValue().toString();
                    String mob = singleSnapshot.child("mobno").getValue().toString();
                    String email = singleSnapshot.child("email").getValue().toString();

                    try {

                        UserModel obj12 = new UserModel(name, mob, email);
                        //Log.i("DURATION",DURATION);
                        allindex_teamA.add(obj12);
                        allindex_teamB.add(obj12);

                        String token = singleSnapshot.child("token").getValue().toString();
                        //Log.e("token" ,singleSnapshot.child("token").getValue().toString());
                        jsonArray.put(token);


                    }catch (Exception ecxe){
                        ecxe.printStackTrace();
                    }

                }
                adapter_rv_teamA.notifyDataSetChanged();
                adapter_rv_teamB.notifyDataSetChanged();
                rv_teamA.setAdapter(adapter_rv_teamA);
                rv_teamB.setAdapter(adapter_rv_teamB);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        final Map<String, Integer> map = new HashMap<String, Integer>();

        databaseReferenceScore.child(tv_datetime.getText().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    map.clear();
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("objects =====" ,""+dataSnapshot.toString());
                                String runA = null, runB = null;
                                if (singleSnapshot.hasChild("runA") && singleSnapshot.child("runA").getValue()!=null) {
                                    runA = singleSnapshot.child("runA").getValue().toString();
                                }
                                if (singleSnapshot.hasChild("runB")) {
                                    runB = singleSnapshot.child("runB").getValue().toString();
                                }
                                String MobNo = singleSnapshot.getKey();
                                if (runA != null && runB != null && runA.length()>0 && runB.length()>0) {
                                 //Log.d("RUNAB", runA + runB);
                                    int total = Integer.parseInt(runA) + Integer.parseInt(runB);
                                    map.put(MobNo, total);
                                    HashMap<String, Object> user_total = new HashMap<>();
                                    user_total.put("total_score", total);
                                        databaseReferenceScore.child(tv_datetime.getText().toString()).child(MobNo).updateChildren(user_total);
                                    }
                                    //Log.d("map",map.toString()+map.size());
                        }
                        if (map.size() > 0) {
                            int maxValueInMap = (Collections.max(map.values()));  // This will return max value in the Hashmap
                            for (final Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                                if (entry.getValue() == maxValueInMap) {
                                    //System.out.println("MAX==="+entry.getKey());     // Print the key with max value
                                    FirebaseDatabase.getInstance().getReference("users").child(entry.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String winerName = dataSnapshot.child("name").getValue().toString();
                                            tv_winner.setText("Winner is " + winerName + ",Total " + entry.getValue() + " runs.");
                                            winner_score = entry.getValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                        double loss = 0;
                        for (DataSnapshot singleSnapshot1 : dataSnapshot.getChildren()) {
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());
                            HashMap<String, Object> winner_totalObj = new HashMap<>();
                            winner_totalObj.put("winner_total", winner_score);
                            databaseReferenceScore.child(tv_datetime.getText().toString()).updateChildren(winner_totalObj);

                            if (singleSnapshot1.hasChild("total_score") && dataSnapshot.hasChild("winner_total")) {

                                double total_score = Double.parseDouble(singleSnapshot1.child("total_score").getValue().toString());
                                double winner_total = Double.parseDouble(dataSnapshot.child("winner_total").getValue().toString());
                                //Log.d("total score",Double.toString(total_score));
                                if (total_score < winner_total) {
                                    loss = (winner_total - total_score) * 0.20;
                                    HashMap<String, Object> lossObj = new HashMap<>();
                                    lossObj.put("loss", loss);

                                    if (singleSnapshot1.hasChild("profit")) {
                                        databaseReferenceScore.child(tv_datetime.getText().toString()).child(singleSnapshot1.getKey()).child("profit").removeValue();
                                    }
                                    //Log.d("datasnap key",singleSnapshot.getKey());
                                    databaseReferenceScore.child(tv_datetime.getText().toString()).child(singleSnapshot1.getKey()).updateChildren(lossObj);
                                }
                            }
                        }

                        double profit = 0;
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());

                            if (singleSnapshot.hasChild("total_score") && dataSnapshot.hasChild("winner_total")) {
                                if (singleSnapshot.hasChild("loss")) {
                                    double user_loss = Double.parseDouble(singleSnapshot.child("loss").getValue().toString());
                                    //Log.d("loss",Double.toString(user_loss));
                                    profit = profit + user_loss;
                                }
                            }
                        }

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());

                            if (singleSnapshot.hasChild("total_score") && dataSnapshot.hasChild("winner_total")) {

                                double total_score = Double.parseDouble(singleSnapshot.child("total_score").getValue().toString());
                                double winner_total = Double.parseDouble(dataSnapshot.child("winner_total").getValue().toString());

                                if (total_score == winner_total) {
                                    if (singleSnapshot.hasChild("loss")) {
                                        databaseReferenceScore.child(tv_datetime.getText().toString()).child(singleSnapshot.getKey()).child("loss").removeValue();
                                    }
                                    HashMap<String, Object> profit_total = new HashMap<>();
                                    profit_total.put("profit", profit);
                                    databaseReferenceScore.child(tv_datetime.getText().toString()).child(singleSnapshot.getKey()).updateChildren(profit_total);
                                }
                            }
                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

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
                    //Log.d("score", "Result: " + result);
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
                    Toast.makeText(ScoreTable.this, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();

                    //Uri pictureUri = Uri.parse(AUDIO_FILE_PATH);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"IPL fantasy 2017\nMatch: "+tv_datetime.getText().toString()+"\n"+tv_temaA.getText().toString()+" vs "+tv_temaB.getText().toString()+"\nTop Score:\n"
                            +tv_winner.getText().toString()
                            +"\n-via Crickbet Mobile App.(for download APP "+"https://drive.google.com/open?id=0B0BmnqB6oJfRaFlMMEVfZHpsRUU");
                    shareIntent.setType("text/plain");
                    //shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share images..."));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ScoreTable.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
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
                .addHeader("Authorization", "key=" + "AAAAdD_wdq8:APA91bFhpuEvkPrwlfeU_OJtVqXAI6xxOVcL0u1LY3pW8avGBzp3dFEaQYOHEaE8R4RXI-VaTm2UXHNIJKtFx_9jAlI7XfV4dbJpiXF6kQyIDCvqk-jBt1Xfqr9iasThMk5y7ivhdWs-")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

        /*    case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        databaseReferenceScore.onDisconnect();
        databaseReferenceScore.goOffline();

        finish();
    }

}
