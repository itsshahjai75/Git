package billbook.smart.com.crickbat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.util.HashMap;
import java.util.List;

import billbook.smart.com.crickbat.Const;
import billbook.smart.com.crickbat.R;
import billbook.smart.com.crickbat.ScoreTable;
import billbook.smart.com.crickbat.model.UserModel;


public class ScoreTableAdapterTeamB extends RecyclerView.Adapter<ScoreTableAdapterTeamB.ScoreTable_RecyclerViewHolders> {


    private DatabaseReference databaseReferenceScore;
    String str_dateTime,str_venue,str_teamB,res;
    private List<UserModel> UserModel;
    protected Context context;
    SharedPreferences sharepref;


    public ScoreTableAdapterTeamB(Context context, List<UserModel> UserModel) {
        this.UserModel = UserModel;
        this.context = context;


        sharepref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        ScoreTable.tv_datetime.getText().toString();
        databaseReferenceScore = FirebaseDatabase.getInstance().getReference("score");
        //databaseReferenceScore.keepSynced(true);
        str_dateTime=ScoreTable.tv_datetime.getText().toString();
        str_venue=ScoreTable.tv_venue.getText().toString();
        str_teamB=ScoreTable.tv_temaB.getText().toString();

    }
    @Override
    public ScoreTable_RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        ScoreTable_RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score_table, parent, false);
        viewHolder = new ScoreTable_RecyclerViewHolders(layoutView, UserModel);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ScoreTable_RecyclerViewHolders holder, int position) {
        holder.tv_name.setText(UserModel.get(position).getName());
        holder.tv_number.setText(UserModel.get(position).getMob());
        holder.tv_email.setText(UserModel.get(position).getEmail());


        holder.et_player.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                HashMap<String, Object> score_update = new HashMap<>();
                score_update.put("datetime", str_dateTime);
                score_update.put("venue", str_venue);
                score_update.put("teamB", str_teamB);
                score_update.put("playerB", holder.et_player.getText().toString());
              //  score_update.put("runB", holder.et_runs.getText().toString());
                //=================================================================
                if(sharepref.getString(Const.PREF_USER_MOBILE_NO,"")!=null) {
                    databaseReferenceScore.child(str_dateTime).child(holder.tv_number.getText().toString()).updateChildren(score_update);
                }
            }
        });
        holder.et_runs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                HashMap<String, Object> score_update = new HashMap<>();
                score_update.put("datetime", str_dateTime);
                score_update.put("venue", str_venue);
                score_update.put("teamB", str_teamB);
                //score_update.put("playerB", holder.et_player.getText().toString());
                score_update.put("runB", holder.et_runs.getText().toString());
                //=================================================================
                if(sharepref.getString(Const.PREF_USER_MOBILE_NO,"")!=null) {
                    databaseReferenceScore.child(str_dateTime).child(holder.tv_number.getText().toString()).updateChildren(score_update);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return this.UserModel.size();
    }



    public class ScoreTable_RecyclerViewHolders extends RecyclerView.ViewHolder{

        public TextView tv_name;
        public TextView tv_number,tv_email;
        public EditText et_player,et_runs;

        public List<UserModel> UserModel;

        public ScoreTable_RecyclerViewHolders(final View itemView, final List<UserModel> UserModel) {
            super(itemView);
            this.UserModel = UserModel;
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_number = (TextView)itemView.findViewById(R.id.tv_number);
            tv_email= (TextView)itemView.findViewById(R.id.tv_email);


            et_player = (EditText)itemView.findViewById(R.id.et_player);
            et_runs = (EditText)itemView.findViewById(R.id.et_runs);
            databaseReferenceScore.child(ScoreTable.tv_datetime.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("Count B", "" + dataSnapshot.getChildrenCount());
                            //Log.e("objects B=====" ,""+dataSnapshot.toString());
                            if(tv_number.getText().toString().equals(singleSnapshot.getKey())){

                                if(singleSnapshot.hasChild("playerB")) {
                                    String playerB = singleSnapshot.child("playerB").getValue().toString();
                                    et_player.setText(playerB);
                                }if(singleSnapshot.hasChild("runB")) {
                                    String runB = singleSnapshot.child("runB").getValue().toString();
                                    et_runs.setText(runB);
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