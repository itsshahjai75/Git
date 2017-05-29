package billbook.smart.com.crickbat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText et_name,et_mob ,et_email,et_password;
    Button btn_submit;
    String name,email,mobno,res,refreshedToken,password;

    private DatabaseReference databaseReference;
    SharedPreferences sharepref;
    private static FirebaseDatabase fbDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //==firebase configration
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //databaseReference.keepSynced(true);
        //-------------------------------------------------
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

//        Log.d("Token==========",refreshedToken);

        if(sharepref.getString(Const.PREF_LOGINKEY,"no").equalsIgnoreCase("yes")){
            startActivity(new Intent(Login.this,MatchesList.class));
            finish();
        }

        et_name = (EditText)this.findViewById(R.id.et_name);
        et_mob = (EditText)this.findViewById(R.id.et_mob);
        et_email = (EditText)this.findViewById(R.id.et_email);
        et_password=(EditText)this.findViewById(R.id.et_password);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email_txt = et_email.getText().toString().trim();

        et_email .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (email_txt.matches(emailPattern) && s.length() > 0 )
                {
                    ///                et_email.setCompoundDrawables(null,null,android.graphics.drawable.,null);
                    //                  Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                    // or
                    //textView.setText("valid email");
                }
                else
                {
//                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    //or
                    // textView.setText("invalid email");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        btn_submit=(Button)this.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String before_attherate="";
                String after_attherate="";
                if(et_email.getText().toString().matches(emailPattern)){

                    before_attherate=et_email.getText().toString();
                    before_attherate=before_attherate.substring(0,before_attherate.indexOf("\u0040"));

                    after_attherate=et_email.getText().toString();
                    after_attherate=after_attherate.substring(after_attherate.indexOf("\u0040")+1);
                    after_attherate=after_attherate.substring(0,after_attherate.indexOf("."));


                }else{
                    et_email.setError("Enter Valid Email Address!");
                }
                if(et_name.getText().toString().length()==0){
                    et_name.setError("Enter Name!");
                }else if(et_mob.getText().toString().length()==0
                        || et_mob.getText().toString().length() < 10
                        || et_mob.getText().toString().equalsIgnoreCase("1234567890")){
                    et_mob.setError("Enter Correct Mobile Number!");
                }else if(et_email.getText().toString().length()==0 || !et_email.getText().toString().matches(emailPattern)
                        || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")
                        || et_email.getText().toString().equalsIgnoreCase("abc@abc.com")
                        || before_attherate.equalsIgnoreCase(after_attherate)) {
                    et_email.setError("Enter Valid Email Address!");
                }else if(before_attherate.length()<=4 || before_attherate.equalsIgnoreCase("12345")
                        || before_attherate.equalsIgnoreCase("12345456789")
                        ||before_attherate.equalsIgnoreCase("1234567890") ||
                        before_attherate.equalsIgnoreCase("abcde") ||
                        before_attherate.equalsIgnoreCase("abcdef") ||
                        before_attherate.equalsIgnoreCase("qwert") ||
                        before_attherate.equalsIgnoreCase("qwerty") ||
                        before_attherate.equalsIgnoreCase("asdfg")||
                        before_attherate.equalsIgnoreCase("asdfgh")){
                    et_email.setError("Try other email please!!!");
                }else if(after_attherate.length()<=4 || after_attherate.equalsIgnoreCase("12345")
                        || after_attherate.equalsIgnoreCase("12345456789")
                        ||after_attherate.equalsIgnoreCase("1234567890") ||
                        after_attherate.equalsIgnoreCase("abcde") ||
                        after_attherate.equalsIgnoreCase("abcdef") ||
                        after_attherate.equalsIgnoreCase("qwert") ||
                        after_attherate.equalsIgnoreCase("qwerty") ||
                        after_attherate.equalsIgnoreCase("asdfg")||
                        after_attherate.equalsIgnoreCase("asdfgh")||
                        after_attherate.equalsIgnoreCase("email")){
                    et_email.setError("Try other email please!!!");
                }else if(et_password.getText().toString().length()<7){
                    et_password.setError("Password must be 7 digit and unique.");
                }else {
                    name = et_name.getText().toString();
                    email = et_email.getText().toString();
                    mobno = et_mob.getText().toString();
                    password=et_password.getText().toString();
                    refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    // Internet Connection is Present
                    // make HTTP requests

                    databaseReference.child(mobno).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value

                                    if(dataSnapshot.hasChild("mobno")){
                                        String mobile_no = dataSnapshot.child("mobno").getValue(String.class).toString();
                                        String pwd = dataSnapshot.child("pwd").getValue().toString();
                                        String email = dataSnapshot.child("email").getValue(String.class);
                                        //============================================
                                        if(mobno.equals(mobile_no) && pwd.equals(password)){

                                            //=================================================================
                                            new Signup_async().execute();

                                        }else if(!mobile_no.equalsIgnoreCase(mobno) || !pwd.equalsIgnoreCase(password)){
                                            Snackbar snackbar = Snackbar
                                                    .make(findViewById(android.R.id.content), " Sorry! Email OR Password not matched!!!", Snackbar.LENGTH_LONG);

                                            // Changing message text color
                                            snackbar.setActionTextColor(Color.BLUE);

                                            // Changing action button text color
                                            View sbView = snackbar.getView();
                                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.YELLOW);
                                            snackbar.show();

                                            Toast.makeText(Login.this, " Sorry! Email OR Password not matched!!!", Toast.LENGTH_LONG).show();

                                        }
                                    }else {
                                        new Signup_async().execute();
                                    }

                                    // ...
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //Log.w("TAG", "getUser:onCancelled", databaseError.toException());
                                    new Signup_async().execute();
                                }
                            });
                }
            }
        });
    }

    class Signup_async extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Login.this, "Loading", "Please Wait--------.", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");

            HashMap<String, Object> user_details = new HashMap<>();
            user_details.put("name", name);
            user_details.put("mobno", mobno);
            user_details.put("pwd",password);
            user_details.put("email", email);
            user_details.put("timestamp", ServerValue.TIMESTAMP);
            user_details.put("token", refreshedToken);
            //Log.d("token===",refreshedToken);

            //=================================================================

            databaseReference.child(mobno).updateChildren(user_details);


            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            progressDialog.dismiss();
            Intent intent = new Intent(Login.this, MatchesList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // call this to finish the current activity

            sharepref.edit().putString(Const.PREF_LOGINKEY,"yes").apply();
            sharepref.edit().putString(Const.PREF_USER_MOBILE_NO,mobno).apply();


        }







    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

