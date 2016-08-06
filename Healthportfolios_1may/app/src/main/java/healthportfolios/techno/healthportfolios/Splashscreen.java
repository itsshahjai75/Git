package healthportfolios.techno.healthportfolios;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class Splashscreen extends AppCompatActivity {

    final int SPLASH_TIME_OUT = 3500;
    SharedPreferences sharepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        //==1) for activity====
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);



        // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);


        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


             if(sharepref.getString("key_login", "no").equals("yes")){

                 startActivity(new Intent(Splashscreen.this,
                         Show_reports.class));
                 overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                 finish();

             }else{

                 startActivity(new Intent(Splashscreen.this,
                         HomeScreen.class));
                 overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                 finish();

             }

            }
        }, SPLASH_TIME_OUT);

    }
}
