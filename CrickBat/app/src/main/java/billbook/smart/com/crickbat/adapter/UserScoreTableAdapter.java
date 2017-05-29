package billbook.smart.com.crickbat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import billbook.smart.com.crickbat.Const;
import billbook.smart.com.crickbat.R;
import billbook.smart.com.crickbat.UserScoreTable;
import billbook.smart.com.crickbat.UserScoreTable;
import billbook.smart.com.crickbat.model.UserModel;

import static billbook.smart.com.crickbat.UserScoreTable.tv_winner_usrescore;


public class UserScoreTableAdapter extends RecyclerView.Adapter<UserScoreTableAdapter.ScoreTable_RecyclerViewHolders> {


    private DatabaseReference databaseReferenceScore;
    String str_dateTime,str_venue,res;
    private List<UserModel> UserModel;
    protected Context context;
    SharedPreferences sharepref;
    final Map<String, Integer> map = new HashMap<String, Integer>();


    public UserScoreTableAdapter(Context context, List<UserModel> UserModel) {
        this.UserModel = UserModel;
        this.context = context;


        sharepref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);

        UserScoreTable.tv_datetime.getText().toString();

        databaseReferenceScore = FirebaseDatabase.getInstance().getReference("score");
        //databaseReferenceScore.keepSynced(true);

        str_dateTime=UserScoreTable.tv_datetime.getText().toString();
        str_venue=UserScoreTable.tv_venue.getText().toString();


    }
    @Override
    public ScoreTable_RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        ScoreTable_RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_score_table, parent, false);
        viewHolder = new ScoreTable_RecyclerViewHolders(layoutView, UserModel);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ScoreTable_RecyclerViewHolders holder, int position) {

        holder.tv_name.setText(UserModel.get(position).getName());
        holder.tv_number.setText(UserModel.get(position).getMob());
        holder.tv_email.setText(UserModel.get(position).getEmail());

       /* databaseReferenceScore.child(UserScoreTable.tv_datetime.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if(singleSnapshot.hasChild("loss")) {
                            String loss = singleSnapshot.child("loss").getValue().toString();
                            profit = profit + Double.parseDouble(loss);
                            Log.d("profit is ",Double.toString(profit));
                        }
                    }

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if (!singleSnapshot.hasChild("loss") && holder.tv_number.getText().toString().equalsIgnoreCase(singleSnapshot.getKey())) {
                            holder.tv_profitloss.setText("Profit : " + profit);
                            HashMap<String, Object> prof_obj = new HashMap<>();
                            prof_obj.put("profit", profit);
                            //databaseReferenceScore.child(UserScoreTable.tv_datetime.getText().toString()).child(singleSnapshot.getKey()).updateChildren(prof_obj);
                        }
                    }
                }catch (Exception ecxe){
                    ecxe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        /*if(Integer.parseInt(holder.tv_total.getText().toString())<winnerTotal){
            double pro_loss = (double)(winnerTotal-Integer.parseInt(holder.tv_total.getText().toString())*0.20);
            holder.tv_profitloss.setText("Loss : "+pro_loss);
        }*/

    }
    @Override
    public int getItemCount() {
        return this.UserModel.size();
    }



    public class ScoreTable_RecyclerViewHolders extends RecyclerView.ViewHolder{

        public TextView tv_name ,tv_number,tv_email,tv_teamAname ,tv_teamBname,tv_total,tv_profitloss;
        public EditText et_playerA,et_runA,et_playerB ,et_runB;

        public List<UserModel> UserModel;

        public ScoreTable_RecyclerViewHolders(final View itemView, final List<UserModel> UserModel) {
            super(itemView);
            this.UserModel = UserModel;
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_number = (TextView)itemView.findViewById(R.id.tv_number);
            tv_email= (TextView)itemView.findViewById(R.id.tv_email);
            tv_number = (TextView)itemView.findViewById(R.id.tv_number);

            tv_teamAname= (TextView)itemView.findViewById(R.id.tv_teamAname);
            tv_teamBname = (TextView)itemView.findViewById(R.id.tv_teamBname);
            tv_total= (TextView)itemView.findViewById(R.id.tv_total_user);
            tv_profitloss= (TextView)itemView.findViewById(R.id.tv_profitloss);

            et_playerA = (EditText)itemView.findViewById(R.id.et_playerA);
            et_runA = (EditText)itemView.findViewById(R.id.et_runA);
            et_playerB = (EditText)itemView.findViewById(R.id.et_playerB);
            et_runB = (EditText)itemView.findViewById(R.id.et_runB);

            et_playerA.setEnabled(false);
            et_runA.setEnabled(false);
            et_playerB.setEnabled(false);
            et_runB.setEnabled(false);

           /* databaseReferenceScore.child(UserScoreTable.tv_datetime.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("Count A", "" + dataSnapshot.getChildrenCount());
                            if (tv_number.getText().toString().equals(singleSnapshot.getKey())) {
                                //Log.e("objects A=====", "" + singleSnapshot.toString());

                                tv_teamAname.setText(singleSnapshot.child("teamA").getValue().toString());
                                tv_teamBname.setText(singleSnapshot.child("teamB").getValue().toString());

                                String playerA = singleSnapshot.child("playerA").getValue().toString();
                                String runA = singleSnapshot.child("runA").getValue().toString();
                                String playerB = singleSnapshot.child("playerB").getValue().toString();
                                String runB = singleSnapshot.child("runB").getValue().toString();

                                String total_score = singleSnapshot.child("total_score").getValue().toString();
                                String winner_total = singleSnapshot.child("winner_total").getValue().toString();

                                et_playerA.setText(playerA);
                                et_runA.setText(runA);
                                et_playerB.setText(playerB);
                                et_runB.setText(runB);
                                if (!runA.isEmpty() && !runB.isEmpty()) {
                                    int total = Integer.parseInt(runA) + Integer.parseInt(runB);
                                    map.put(tv_number.getText().toString(), total);
                                    //Log.d("map",map.toString()+map.size());
                                    tv_total.setText(""+total);

                                    double prof_loss = (Integer.parseInt(winner_total) - Integer.parseInt(total_score)) * 0.20;


                                    if(Integer.parseInt(total_score)<Integer.parseInt(winner_total)){
                                        tv_profitloss.setText("Loss : "+prof_loss);
                                        HashMap<String, Object> loss_obj = new HashMap<>();
                                        loss_obj.put("loss", prof_loss);
                                        databaseReferenceScore.child(UserScoreTable.tv_datetime.getText().toString()).child(singleSnapshot.getKey()).updateChildren(loss_obj);


                                    } else{
                                        //tv_profitloss.setText("Profit : "+profit);
                                    }
                                }
                            }
                        }

                        if(map.size()>0) {
                            int maxValueInMap=(Collections.max(map.values()));  // This will return max value in the Hashmap
                            for (final Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                                if (entry.getValue()==maxValueInMap) {
                                    //System.out.println("MAX==="+entry.getKey());     // Print the key with max value

                                    FirebaseDatabase.getInstance().getReference("users").child(entry.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String winerName= dataSnapshot.child("name").getValue().toString();
                                            winnerTotal = entry.getValue();
                                            tv_winner_usrescore.setText("Winner is "+winerName+",Total "+entry.getValue()+" runs.");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }
                    }catch (Exception ecxe){
                        ecxe.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
*/


            databaseReferenceScore.child(UserScoreTable.tv_datetime.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("Count A", "" + dataSnapshot.getChildrenCount());
                            if (tv_number.getText().toString().equals(singleSnapshot.getKey())) {
                                //Log.e("objects A=====", "" + singleSnapshot.toString());

                                tv_teamAname.setText(singleSnapshot.child("teamA").getValue().toString());
                                tv_teamBname.setText(singleSnapshot.child("teamB").getValue().toString());

                                String playerA = singleSnapshot.child("playerA").getValue().toString();
                                String runA = singleSnapshot.child("runA").getValue().toString();
                                String playerB = singleSnapshot.child("playerB").getValue().toString();
                                String runB = singleSnapshot.child("runB").getValue().toString();

                                String total_score = singleSnapshot.child("total_score").getValue().toString();
                               // String winner_total = singleSnapshot.child("winner_total").getValue().toString();

                                et_playerA.setText(playerA);
                                et_runA.setText(runA);
                                et_playerB.setText(playerB);
                                et_runB.setText(runB);
                                tv_total.setText(total_score);
                                map.put(singleSnapshot.getKey().toString(), Integer.parseInt(total_score));

                                if(singleSnapshot.hasChild("loss") &&
                                        !singleSnapshot.child("loss").getValue().toString().equalsIgnoreCase("0")){
                                    tv_profitloss.setText("Loss : "+singleSnapshot.child("loss").getValue().toString());
                                    tv_profitloss.setBackgroundResource(R.color.md_red_700);
                                    tv_profitloss.setTextColor(Color.parseColor("#ffffff"));
                                }else if(singleSnapshot.hasChild("profit")){
                                    tv_profitloss.setText("Profit : "+singleSnapshot.child("profit").getValue().toString());
                                    tv_profitloss.setBackgroundResource(R.color.md_green_700);
                                    tv_profitloss.setTextColor(Color.parseColor("#ffffff"));
                                }

                            }
                        }

                        if(map.size()>0){
                            int maxValueInMap=(Collections.max(map.values()));  // This will return max value in the Hashmap
                            for (final Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                                if (entry.getValue()==maxValueInMap) {
                                    //System.out.println("MAX==="+entry.getKey());     // Print the key with max value

                                    FirebaseDatabase.getInstance().getReference("users").child(entry.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String winerName= dataSnapshot.child("name").getValue().toString();
                                            tv_winner_usrescore.setText("Winner is "+winerName+",Total "+entry.getValue()+" runs");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                    }catch (Exception ecxe){
                        ecxe.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }
    }


}