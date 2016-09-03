package manager.trade.techno.trademanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import DB.DatabaseHelper_Compnies;

public class SplashScreen extends AppCompatActivity {


    final int SPLASH_TIME_OUT = 3500;
    SharedPreferences sharepref;
    Boolean isInternetPresent = false;
    String res,res2;


    DatabaseHelper_Compnies myDbHelper;

    private static FirebaseDatabase fbDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(fbDatabase == null) {
            fbDatabase = FirebaseDatabase.getInstance();
            fbDatabase.setPersistenceEnabled(true);
        }


        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);



        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        myDbHelper = new DatabaseHelper_Compnies(SplashScreen.this);

        try {

            myDbHelper.createDataBase();
            myDbHelper.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }catch (Exception exp) {

            exp.printStackTrace();

        }


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {




                    startActivity(new Intent(SplashScreen.this,
                            Login.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();





                }
            }, SPLASH_TIME_OUT);






        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {





                    startActivity(new Intent(SplashScreen.this,
                            Login.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();





                }
            }, SPLASH_TIME_OUT);


        }





    }


}

