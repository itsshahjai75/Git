package billbook.smart.com.crickbat;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import billbook.smart.com.crickbat.adapter.UserBalanceTableAdapter;
import billbook.smart.com.crickbat.model.UserModel;
import okhttp3.OkHttpClient;

public class UserBalanceSheet extends AppCompatActivity {


    OkHttpClient mClient;
    public static TextView tv_scoreTable,tv_datetime,tv_venue,tv_winner_usrescore;

    RecyclerView rv_userscores;
    SharedPreferences sharepref;

    private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    private UserBalanceTableAdapter adapter_rv_usersore;
    private DatabaseReference databaseReference;
    private ArrayList<UserModel> allindex_teamA;

    String res;


    int Count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_balance_sheet);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Balance");

        mClient = new OkHttpClient();

        tv_winner_usrescore =(TextView)this.findViewById(R.id.tv_winner);

        tv_scoreTable =(TextView)this.findViewById(R.id.tv_temaA);
        tv_datetime =(TextView)this.findViewById(R.id.tv_datetime);
        tv_venue =(TextView)this.findViewById(R.id.tv_venue);
        rv_userscores =(RecyclerView) this.findViewById(R.id.rv_balance_sheet);

        //-------------------------------------------------
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        allindex_teamA = new ArrayList<UserModel>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //databaseReference.keepSynced(true);

        linearLayoutManager2 = new LinearLayoutManager(UserBalanceSheet.this);
        rv_userscores.setLayoutManager(linearLayoutManager2);

        adapter_rv_usersore = new UserBalanceTableAdapter(UserBalanceSheet.this, allindex_teamA);

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

                        String token = singleSnapshot.child("token").getValue().toString();
                        //Log.e("token" ,singleSnapshot.child("token").getValue().toString());



                    }catch (Exception ecxe){
                        ecxe.printStackTrace();
                    }

                }
                adapter_rv_usersore.notifyDataSetChanged();
                rv_userscores.setAdapter(adapter_rv_usersore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
    }


}
