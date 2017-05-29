package com.techno.jay.codingcontests;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Setting_app extends AppCompatActivity {



    private RadioGroup radioGroup;
    private RadioButton rb15,rb30,rb45,rb60,rb90,rb120;
    private RadioButton radioButton;

    SharedPreferences sharepref;
    String min_before;

    TextView tv_showreminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_app);


        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        min_before=sharepref.getString("reminder_min", "60");
        if(min_before.isEmpty() || min_before.equalsIgnoreCase("null")){
            min_before="60";
        }


        radioGroup = (RadioGroup) findViewById(R.id.radioChoice);
        rb15=(RadioButton)this.findViewById(R.id.rb_15min);
        rb30=(RadioButton)this.findViewById(R.id.rb_30min);
        rb45=(RadioButton)this.findViewById(R.id.rb_45min);
        rb60=(RadioButton)this.findViewById(R.id.rb_60min);
        rb90=(RadioButton)this.findViewById(R.id.rb_90min);
        rb120=(RadioButton)this.findViewById(R.id.rb_120min);


        if(min_before.equalsIgnoreCase("15")){
            rb15.setChecked(true);
        }else if(min_before.equalsIgnoreCase("30")){
            rb30.setChecked(true);
        }else if(min_before.equalsIgnoreCase("45")){
            rb45.setChecked(true);
        }else if(min_before.equalsIgnoreCase("60")){
            rb60.setChecked(true);
        }else if(min_before.equalsIgnoreCase("90")){
            rb90.setChecked(true);
        }else if(min_before.equalsIgnoreCase("120")){
            rb120.setChecked(true);
        }



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                String reminder_min = radioButton.getText().toString();
                reminder_min =reminder_min.substring(0,reminder_min.indexOf("min"));
                Toast.makeText(Setting_app.this,
                        "Before "+reminder_min+" min", Toast.LENGTH_SHORT).show();
                sharepref.edit().putString("reminder_min",reminder_min).commit();

            }
        });

        tv_showreminders=(TextView)this.findViewById(R.id.tv_my_reminders);
        tv_showreminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Setting_app.this,
                        Show_Set_Reminders.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Setting_app.this,
                Home.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
