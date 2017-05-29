package com.jainisam.techno.jainisam;

import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Pachkhaan extends Fragment {

    public Pachkhaan() {
        // Required empty public constructor
    }

    MyTextView tv_translate,tv_chovihar,tv_navkarshi,tv_ekashna,tv_ayambil,tv_upvas,tv_paadvanapchkhaan,
            tv_chovihar_sub,tv_navkarshi_sub,tv_ekashna_sub,tv_ayambil_sub,tv_upvas_sub,tv_paadvanapchkhaan_sub;

    TextView web_chovihar,web_navkarshi,web_ekashna,web_aayumbil,web_upvas,web_paadvana,tv_deravasi,tv_video;



    int ik= 1;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pachkhaan, container, false);


        tv_deravasi = (MyTextView)view.findViewById(R.id.tv_deravasi_format);
        tv_deravasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentweb = new Intent(getActivity(),Webview_pachkhan.class);
                startActivity(intentweb);
            }
        });
        tv_video = (MyTextView)view.findViewById(R.id.tv_videoformat);
        tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=hjQfH_odKTs&list=PL1IIOngjkSNHVSe1-tcEzID9kTkuELx08"));
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=hjQfH_odKTs&list=PL1IIOngjkSNHVSe1-tcEzID9kTkuELx08"));
                    startActivity(intent);
                }


            }
        });


        tv_chovihar = (MyTextView)view.findViewById(R.id.tv_chovihar);
        tv_navkarshi = (MyTextView)view.findViewById(R.id.tv_navkarshi);
        tv_ekashna = (MyTextView)view.findViewById(R.id.tv_ekashna);
        tv_ayambil = (MyTextView)view.findViewById(R.id.tv_aayumbil);
        tv_upvas = (MyTextView)view.findViewById(R.id.tv_upvaas);
        tv_paadvanapchkhaan = (MyTextView)view.findViewById(R.id.tv_paadvanapchkaan);

        tv_chovihar_sub = (MyTextView)view.findViewById(R.id.tv_chovihar_sub);
        tv_navkarshi_sub = (MyTextView)view.findViewById(R.id.tv_navkarshi_sub);
        tv_ekashna_sub = (MyTextView)view.findViewById(R.id.tv_ekashna_sub);
        tv_ayambil_sub = (MyTextView)view.findViewById(R.id.tv_aayumbil_sub);
        tv_upvas_sub = (MyTextView)view.findViewById(R.id.tv_upvaas_sub);
        tv_paadvanapchkhaan_sub = (MyTextView)view.findViewById(R.id.tv_paadvanapchkaan_sub);

        web_chovihar =(TextView)view.findViewById(R.id.web_chovihar);
        web_navkarshi =(TextView)view.findViewById(R.id.web_navkarsi);
        web_ekashna =(TextView)view.findViewById(R.id.web_ekashna);
        web_aayumbil =(TextView)view.findViewById(R.id.web_ayumbil);
        web_upvas =(TextView)view.findViewById(R.id.web_upvas);
        web_paadvana =(TextView)view.findViewById(R.id.web_paadvana);




        tv_translate=(MyTextView) view.findViewById(R.id.tv_language);
        tv_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity().getBaseContext(),"Clicked----",Toast.LENGTH_LONG).show();


                ik=ik+1;

                if(ik%2==0){

                    tv_translate.setText("In English.");
                    tv_chovihar_sub.setVisibility(View.GONE);
                    tv_navkarshi_sub.setVisibility(View.GONE);
                    tv_ekashna_sub.setVisibility(View.GONE);
                    tv_ayambil_sub.setVisibility(View.GONE);
                    tv_upvas_sub.setVisibility(View.GONE);
                    tv_paadvanapchkhaan_sub.setVisibility(View.GONE);

                    web_chovihar.setVisibility(View.VISIBLE);
                    web_navkarshi.setVisibility(View.VISIBLE);
                    web_ekashna.setVisibility(View.VISIBLE);
                    web_aayumbil.setVisibility(View.VISIBLE);
                    web_upvas.setVisibility(View.VISIBLE);
                    web_paadvana.setVisibility(View.VISIBLE);


                    tv_chovihar.setText(" ચોવિહાર ");
                    tv_navkarshi.setText(" નવકારશી / પોરસી / દોઢ પોરસી ");
                    tv_ekashna.setText(" એકાશના ");
                    tv_ayambil.setText(" આયંબીલ ");
                    tv_upvas.setText(" ઉપવાસ ");
                    tv_paadvanapchkhaan.setText(" ગમે તે પછ્ખાણ પાળવાના ");

                    web_chovihar.setText("ધાર્યા પ્રમાણે ચૌવીહામ્પી આહારમ પછ્ખાન્મ, અસનમ , પાનમ , ખાઈમ્મ , સાઈમ્મ, અનથ્હા ભોગેન્મ સહ્હ-સગારેન્મ , સવ્વ સમાહી વાત્યા ગારેન્મ , અપ્પ્વાન્મ વોસીરામી .");
                    web_navkarshi.setText("(નવકારશી / પોરસી / દોઢ પોરસી / 2 પોરસી) ના પછ્ખાણ )ધાર્યા પ્રમાણે ચૌવીહામ્પી આહારમ પછ્ખાન્મ, અસનમ , પાનમ , ખાઈમ્મ , સાઈમ્મ, અનથ્હા ભોગેન્મ સહ્હ-સગારેન્મ , સવ્વ સમાહી વાત્યા ગારેન્મ , અપ્પ્વાન્મ વોસીરામી .");
                    web_ekashna.setText("એકટાણા ઉપરાંત તીવીહમ પી આહારમ પછ્ખામી, અસન્ન, ખાઈમ્મ, સઈમમ , અન્ન્થાભોગેન્મ, સહ્હ સાગરેન્મ ,સવ્વ સમાહી વ્ત્ય્યા ગારેન્મ, અપ્પ્વાન્મ વોસીરામી ");
                    web_aayumbil.setText("આયંબીલ  તીવીહામ્પી આહારમ  પછ્ખાણ , અસન્મ   , ખાઈમ્મ , સાઈમમ , અન્નાથ્હા ભોગેનન્મ, સહ -સગારેન્મ, લેવા લેવ્ણ  ગીત્છ -સામસા-ઠેન્મ ,  પદ્દુચ્ખાનેઅમ , ગુરુ -અબ  -ભુથાયેનામ   ઉક્કીત્ત - વિવેનામ , સવ્વ -સમાહી -વાત્તિયા -ગારેનામ , અપ્પાનામ  , વોસીરામી .");
                    web_upvas.setText("આવતી  કાલે  સૂર્ય  ઉદય  થાય  ત્યાં  સુધી  ના  પછ્ખાણ  , અસનમ  , પાનામ , ખાઈમમ , સાઈમમ, અન્નાથ્હા -ભોગેનામ , સહ્હ -સગારેનામ , સવ્વ -સમાહી -વાત્તિયા -ગારેનામ , અપ્પાનામ  , વોસીરામી .(તીવીહારો  ઉપવાસ  માં  \\'\\'પાનમ \\'\\' બોલવું  નહિ .)");
                    web_paadvana.setText("કરેલા  પચખાણ  નું  નામે  લઇ --પચખાણ  કાર્ય  હતા  તે  પુરા  થતા  પાડું  છુ , સમ -કાયેણ્મ, ફાસીયમ , પાલીયમ , તીરીયમ , કીત્ત્તીયમ , આરાહીયમ  , આનાયે  અનુપાલીયમ , ના  ભ્હાવીએ  તસ્સ  મિચ્છામી  દુકડમ .( 3 વાર  નવકાર  મંત્ર  બોલવા ).");



                }else{


                    tv_translate.setText("In Gujarati.");

                    tv_chovihar_sub.setVisibility(View.VISIBLE);
                    tv_navkarshi_sub.setVisibility(View.VISIBLE);
                    tv_ekashna_sub.setVisibility(View.VISIBLE);
                    tv_ayambil_sub.setVisibility(View.VISIBLE);
                    tv_upvas_sub.setVisibility(View.VISIBLE);
                    tv_paadvanapchkhaan_sub.setVisibility(View.VISIBLE);

                    web_chovihar.setVisibility(View.GONE);
                    web_navkarshi.setVisibility(View.GONE);
                    web_ekashna.setVisibility(View.GONE);
                    web_aayumbil.setVisibility(View.GONE);
                    web_upvas.setVisibility(View.GONE);
                    web_paadvana.setVisibility(View.GONE);

                    tv_chovihar.setText("Chovihar");
                    tv_navkarshi.setText("Navkarshi / Porsi etc");
                    tv_ekashna.setText("Ekashana");
                    tv_ayambil.setText("Aayambil");
                    tv_upvas.setText("Upvas");
                    tv_paadvanapchkhaan.setText("All Pachkhaan Padvana");


                }




            }
        });


        /*Typeface cFont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
        Typeface cFont2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");


        tv_chovihar.setTypeface(cFont2);
        tv_navkarshi.setTypeface(cFont2);
        tv_ekashna.setTypeface(cFont2);
        tv_ayambil.setTypeface(cFont2);
        tv_upvas.setTypeface(cFont2);
        tv_paadvanapchkhaan.setTypeface(cFont2);

        tv_chovihar_sub.setTypeface(cFont);
        tv_navkarshi_sub.setTypeface(cFont);
        tv_ekashna_sub.setTypeface(cFont);
        tv_ayambil_sub.setTypeface(cFont);
        tv_upvas_sub.setTypeface(cFont);
        tv_paadvanapchkhaan_sub.setTypeface(cFont);*/

        return view;


    }


}
