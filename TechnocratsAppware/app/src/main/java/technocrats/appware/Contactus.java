package technocrats.appware;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Contactus extends AppCompatActivity {

    private Toolbar mToolbar;


    LinearLayout website_layout,email_layout,call_layout;
    TextView txt_website,tv_onlinform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txt_website =(TextView)findViewById(R.id.txt_website);
        txt_website.setClickable(true);
        txt_website.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.technocratsappware.com'> www.technocratsappware.com </a>";
        txt_website.setText(Html.fromHtml(text));


        tv_onlinform =(TextView)findViewById(R.id.tv_online_reg);
        tv_onlinform.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent i = new Intent(Contactus.this, Reg_form.class);
                startActivity(i);

            }
        });

        // website_layout =(LinearLayout)rootView.findViewById(R.id.layout_website);
        email_layout=(LinearLayout)findViewById(R.id.layout_mail);
        call_layout =(LinearLayout)findViewById(R.id.layout_call);


        email_layout.setClickable(true);
        call_layout.setClickable(true);


        email_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "technocratsappware@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Inquery Email from Application.");
                intent.putExtra(Intent.EXTRA_TEXT, "Please Fill your details.\nName:\nCo.No:\nHow May i help you?\n\n\n\nThank you");
                intent.putExtra(Intent.EXTRA_BCC, "shah.jai75@gmail.com");
                startActivity(intent);

            }
        });

        call_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+919033228796"));
                startActivity(callIntent);


            }
        });

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

}
