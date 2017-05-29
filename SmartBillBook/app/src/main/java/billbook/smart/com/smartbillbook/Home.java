package billbook.smart.com.smartbillbook;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.HashMap;

import billbook.smart.com.smartbillbook.utils.Const;

public class Home extends AppCompatActivity {

    EditText et_billdate,et_billnumber ,et_partyname,et_email
            ,et_mobno ,et_amount,et_note;
    CheckBox  cb_terms;
    Button btn_add,btn_report;

    String str_billdate,str_billnumber,str_partyname,str_mobileno,str_email,str_amount,str_note,str_paid,res;

    private DatabaseReference databaseReference;
    SharedPreferences sharepref;

    final Calendar myCalendar = Calendar.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //==firebase configration
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.keepSynced(true);
        //-------------------------------------------------
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        et_billdate=(EditText)this.findViewById(R.id.et_billdate);
        et_billnumber=(EditText)this.findViewById(R.id.et_billnumber);
        et_partyname=(EditText)this.findViewById(R.id.et_partyname);
        et_email=(EditText)this.findViewById(R.id.et_email);
        et_mobno=(EditText)this.findViewById(R.id.et_mobno);
        et_amount=(EditText)this.findViewById(R.id.et_amount);
        et_note=(EditText)this.findViewById(R.id.et_note);

        cb_terms=(CheckBox)this.findViewById(R.id.cb_terms);

        btn_add=(Button)this.findViewById(R.id.btn_add);
        btn_report=(Button)this.findViewById(R.id.btn_report);
       btn_report.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Home.this,ReportsList.class));
           }
       });


        et_billdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Home.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                et_billdate.setText( dayOfMonth+ "-" + (monthOfYear + 1) + "-" +  year);
                                //str_date=et_date.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()/*+2*1000*24*3600*/);
                datePickerDialog.show();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_billdate.getText().toString()==null ||
                        et_billnumber.getText().toString()==null ||
                        et_partyname.getText().toString()==null ||
                        et_email.getText().toString()==null ||
                        et_mobno.getText().toString()==null ||
                        et_amount.getText().toString()==null){

                }else{

                    str_billdate= et_billdate.getText().toString();
                    str_billnumber= et_billnumber.getText().toString();
                    str_partyname =  et_partyname.getText().toString();
                    str_email = et_email.getText().toString();
                    str_mobileno= et_mobno.getText().toString();
                    str_amount =et_amount.getText().toString();
                    str_note=et_note.getText().toString();
                    if(cb_terms.isChecked()){
                        str_paid="yes";
                    }else{
                        str_paid="no";
                    }
                    new AddBillDetails().execute();
                }
            }
        });
    }

    class AddBillDetails extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

       @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
        }

        @Override
        protected String doInBackground(Object... parametros) {
            System.out.println("On do in back ground----done-------");

            HashMap<String, Object> user_details = new HashMap<>();
            user_details.put("billnumber", str_billnumber);
            user_details.put("partyname", str_partyname);
            user_details.put("email",str_email);
            user_details.put("mobileno", str_mobileno);
            user_details.put("amount", str_amount);
            user_details.put("note", str_note);
            user_details.put("paid",str_paid);
            user_details.put("timestamp", ServerValue.TIMESTAMP);

            //=================================================================
            databaseReference.child(sharepref.getString(Const.PREF_MOBILE_NO,"")).child("Bills").child(Long.toString(Calendar.getInstance().getTimeInMillis())).setValue(user_details);
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
            Intent intent = new Intent(Home.this, Home.class);
            startActivity(intent);
            finish(); // call this to finish the current activity



        }







    }
}
