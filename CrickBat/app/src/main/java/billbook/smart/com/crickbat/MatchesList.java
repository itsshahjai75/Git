package billbook.smart.com.crickbat;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import billbook.smart.com.crickbat.adapter.TimeTableRecyclerViewAdapter;
import billbook.smart.com.crickbat.model.TimeTableModel;

public class MatchesList extends AppCompatActivity {

    private static final String TAG = "index fragment";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TimeTableRecyclerViewAdapter recyclerViewAdapter;
    private DatabaseReference databaseReference;
    private List<TimeTableModel> allindex;


    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView tv_balancesheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);



        mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipe_refrash_home);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    // Internet Connection is Present
                    databaseReference.orderByValue().limitToLast(100).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            getAllTask(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }
        });

        tv_balancesheet=(TextView)this.findViewById(R.id.tv_balancesheet);
        tv_balancesheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MatchesList.this,UserBalanceSheet.class));
            }
        });
//----------------------------------------------------------------------------------------------------------------------------------------------

        allindex = new ArrayList<TimeTableModel>();
        databaseReference = FirebaseDatabase.getInstance().getReference("matches");
        //databaseReference.keepSynced(true);
        recyclerView = (RecyclerView)this.findViewById(R.id.indexRecyclerView);
        linearLayoutManager = new LinearLayoutManager(MatchesList.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new TimeTableRecyclerViewAdapter(MatchesList.this, allindex);

        databaseReference.orderByValue().limitToLast(100).addValueEventListener(new ValueEventListener() {
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

        recyclerView.setAdapter(recyclerViewAdapter);
        allindex.clear();
        recyclerViewAdapter.notifyDataSetChanged();
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());

            TimeTableModel tips = singleSnapshot.getValue(TimeTableModel.class);
            /*System.out.println("title: " + tips.getTitle());
            System.out.println("diff: " + tips.getDiff());*/
            allindex.add(new TimeTableModel(tips.getDate(),tips.getDetails(),tips.getTime(),tips.getVenue()));

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
