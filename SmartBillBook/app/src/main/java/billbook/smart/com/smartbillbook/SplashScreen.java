package billbook.smart.com.smartbillbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import billbook.smart.com.smartbillbook.utils.Const;

public class SplashScreen extends AppCompatActivity {
    final int SPLASH_TIME_OUT = 2500;
    SharedPreferences sharepref;
    Boolean isInternetPresent = false;
    String res,res2;


    private static FirebaseDatabase fbDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(fbDatabase == null) {
            fbDatabase = FirebaseDatabase.getInstance();
            fbDatabase.setPersistenceEnabled(true);
        }
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);



        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(sharepref.getString(Const.PREF_LOGINKEY, "no").equals("yes")){

                    startActivity(new Intent(SplashScreen.this,
                            Home.class));
                    finish();

                }else{

                    startActivity(new Intent(SplashScreen.this,
                            Login.class));

                    finish();

                }






            }
        }, SPLASH_TIME_OUT);










    }


}

