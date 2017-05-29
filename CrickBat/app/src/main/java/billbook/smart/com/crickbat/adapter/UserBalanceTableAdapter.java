package billbook.smart.com.crickbat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

import billbook.smart.com.crickbat.R;
import billbook.smart.com.crickbat.UserScoreTable;
import billbook.smart.com.crickbat.model.UserModel;



public class UserBalanceTableAdapter extends RecyclerView.Adapter<UserBalanceTableAdapter.ScoreTable_RecyclerViewHolders> {


    private DatabaseReference databaseReferenceScore;
    private List<UserModel> UserModel;
    protected Context context;
    SharedPreferences sharepref;
    final Map<String, Integer> map = new HashMap<String, Integer>();


    public UserBalanceTableAdapter(Context context, List<UserModel> UserModel) {
        this.UserModel = UserModel;
        this.context = context;


        sharepref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);


        databaseReferenceScore = FirebaseDatabase.getInstance().getReference("score");
        //databaseReferenceScore.keepSynced(true);




    }
    @Override
    public ScoreTable_RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        ScoreTable_RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_balance_table, parent, false);
        viewHolder = new ScoreTable_RecyclerViewHolders(layoutView, UserModel);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ScoreTable_RecyclerViewHolders holder, final int position) {

        holder.tv_name.setText(UserModel.get(position).getName());
        holder.tv_number.setText(UserModel.get(position).getMob());
        holder.tv_email.setText(UserModel.get(position).getEmail());
        holder.et_totalprofit.setEnabled(false);
        holder.et_totalloss.setEnabled(false);

        databaseReferenceScore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    double loss=0,profit=0;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        //Log.e("Count A", "" + dataSnapshot.getChildrenCount());
                        //Log.e("objects A=====", "" + singleSnapshot.child(holder.tv_number.getText().toString()).getKey());

                        if (holder.tv_number.getText().toString().equals(singleSnapshot.child(holder.tv_number.getText().toString()).getKey())) {
                           if(singleSnapshot.child(holder.tv_number.getText().toString()).hasChild("loss")){
                                loss=loss+Double.parseDouble(singleSnapshot.child(holder.tv_number.getText().toString()).child("loss").getValue().toString());
                            }
                            if(singleSnapshot.child(holder.tv_number.getText().toString()).hasChild("profit")){
                                profit=profit+Double.parseDouble(singleSnapshot.child(holder.tv_number.getText().toString()).child("profit").getValue().toString());
                            }

                            holder.et_totalloss.setText(""+loss);
                            holder.et_totalprofit.setText(""+profit);


                            double final_balance = Double.parseDouble(holder.et_totalprofit.getText().toString()) - Double.parseDouble(holder.et_totalloss.getText().toString());
                            holder.tv_total_balance.setText(""+final_balance);
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

        public TextView tv_name ,tv_number,tv_email,tv_total_balance;
        public EditText et_totalloss,et_totalprofit;

        public List<UserModel> UserModel;

        public ScoreTable_RecyclerViewHolders(final View itemView, final List<UserModel> UserModel) {
            super(itemView);
            this.UserModel = UserModel;
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_number = (TextView)itemView.findViewById(R.id.tv_number);
            tv_email= (TextView)itemView.findViewById(R.id.tv_email);
            tv_number = (TextView)itemView.findViewById(R.id.tv_number);

            tv_total_balance= (TextView)itemView.findViewById(R.id.tv_total_balance);

            et_totalloss = (EditText)itemView.findViewById(R.id.et_totalloss);
            et_totalprofit = (EditText)itemView.findViewById(R.id.et_totalprofit);


        }
    }


}