package com.jainisam.techno.jainisam;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment {

    MyTextView tv_call,tv_email;

    public AboutUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        tv_call = (MyTextView)view.findViewById(R.id.tv_call_mobile);
        tv_email = (MyTextView)view.findViewById(R.id.tv_email_contactus);

        tv_call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+919033228796"));
                startActivity(callIntent);


            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "technocratsappware@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Inquery Email from Jainism Application.");
                intent.putExtra(Intent.EXTRA_TEXT, "Please Fill your details.\nName:\nCo.No:\n\n\n Feel free to message us Here.\n\n\n\nThank you");
                intent.putExtra(Intent.EXTRA_BCC, "shah.jai75@gmail.com");
                startActivity(intent);

            }
        });


        return view;
    }


}
