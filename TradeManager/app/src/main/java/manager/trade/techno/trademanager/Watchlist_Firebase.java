package manager.trade.techno.trademanager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DatabaseHelper;
import DB.DatabaseHelper_Compnies;
import MyFirebase.RecyclerViewAdapter;
import MyFirebase.Stockindex;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Watchlist_Firebase extends Fragment {

    SharedPreferences sharepref;

    Boolean isInternetPresent = false;
    private Paint p = new Paint();

    private AutoCompleteTextView autoComplete;
    Button btn_add;
    DatabaseHelper dbh;
    SQLiteDatabase db;

    DatabaseHelper_Compnies myDbHelper;
    Cursor c,mCursor;

    String securitycode,longname;
    private ArrayAdapter<String> adapter;



    String res1,str_whatchlist_shares="";



    private static final String TAG = "watchlist fragment";


    private DatabaseReference databaseReference;
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ArrayList results;



    public Watchlist_Firebase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/ProductSans-Regular.ttf");
        // fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_my_watch_list, container, false);
        //==add this line to change all font to coustom font in fragments
        fontChanger.replaceFonts((ViewGroup)convertView);


        myDbHelper = new DatabaseHelper_Compnies(getContext());

        List<String> compnies=new ArrayList<String>();




        sharepref = getContext().getSharedPreferences("MyPref", getContext().MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("watchlist");
        databaseReference.keepSynced(true);




//=====this code is for search edittext for add watchlist company names=========================
        try{

            myDbHelper.openDataBase();

            c=myDbHelper.query("listingcompanies",new String[]{"SecurityCode",
                            "SecurityId",
                            "SecurityName",
                            "Status",
                            "Group_name",
                            "FaceValue",
                            "ISINNo",
                            "Industry",
                            "Instrument"} ,
                    null ,null, null,null, null);

            if(c.moveToFirst()) {
                do {

                    String company_securitcode = c.getString(0).toString();
                    String comapny_short_code=c.getString(1).toString();
                    String comapny_fullname=c.getString(2).toString();

                    compnies.add(comapny_fullname +"\n("+company_securitcode+")");

                } while (c.moveToNext());
            }

            myDbHelper.close();


        }catch(SQLException sqle){

            throw sqle;

        }catch(Exception excmain){

            excmain.printStackTrace();
        }

//==============================================================================




        String[] companies_arry = new String[compnies.size()];
        companies_arry = compnies.toArray(companies_arry);

        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,companies_arry);

        autoComplete = (AutoCompleteTextView)convertView.findViewById(R.id.et_companyname);
        btn_add = (Button) convertView.findViewById(R.id.btn_add);

        // set adapter for the auto complete fields
        autoComplete.setAdapter(adapter);
        autoComplete.setThreshold(1);

        // when the user clicks an item of the drop-down list
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {





                longname=arg0.getItemAtPosition(arg2).toString();
                longname=longname.substring(0,longname.indexOf("\n")-1);

                securitycode=arg0.getItemAtPosition(arg2).toString();
                securitycode=securitycode.substring(securitycode.indexOf("\n")+1);

                autoComplete.setText(longname+securitycode);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(autoComplete.getText().toString().isEmpty()
                        || autoComplete.getText().equals("null")){
                    autoComplete.setError("Value Missing...");

                }else{
                    securitycode=securitycode.substring(1,securitycode.length()-1);
                    str_whatchlist_shares=securitycode;
                    new GetShareIndex().execute();




                    //=================================================================




                }

            }
        });

//==========================================================================================================================




        // Initialize recycler view
        mRecyclerView = (RecyclerView)convertView.findViewById(R.id.post_recycler_view);



        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        results = new ArrayList<DataObject_Watchlist>();
        mAdapter = new MyRecyclerAdapter_watchlist(results);




        databaseReference.child(sharepref.getString("key_usermobno",null)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String security_code = singleSnapshot.getKey();
                            Log.e("childcode",""+security_code);

                            str_whatchlist_shares=str_whatchlist_shares+",bom:"+security_code;

                        }

                        new GetShareIndex_all().execute();



                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });




        return convertView;
    }



   /* private void taskDeletion(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for(int i = 0; i < allindex.size(); i++){
                if(allindex.get(i).getClass().equals(taskTitle)){
                    allindex.remove(i);
                }
            }
            Log.d(TAG, "Task tile " + taskTitle);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), allindex);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }*/


    class GetShareIndex extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");




        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");

// should be a singleton
            OkHttpClient client = new OkHttpClient();
            /*HttpUrl.Builder urlBuilder = HttpUrl.parse("https://ajax.googleapis.com/ajax/services/search/images").newBuilder();
            urlBuilder.addQueryParameter("v", "1.0");
            urlBuilder.addQueryParameter("q", "android");
            urlBuilder.addQueryParameter("rsz", "8");
            String url = urlBuilder.build().toString();*/

            Log.d("url",str_whatchlist_shares);

            Request request = new Request.Builder()
                    .url("http://finance.google.com/finance/info?client=ig&q="+str_whatchlist_shares)
                    .build();



            try{
                //request mate nicheno code
                Response response = client.newCall(request).execute();

                res1=response.body().string();
                // Log.d("okhtp==",res1);

            }catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res1;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res1 == null || res1.equals("")) {



                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }


            try {


                mRecyclerView.setAdapter(mAdapter);
                // Log.d("result string",res1);
                res1=res1.substring(3);
                JSONArray array_res = new JSONArray(res1);

                for(int a =0;a<array_res.length();a++){


                    //  Log.i("RESPONSE", res1);
                    JSONObject obj = array_res.getJSONObject(a);

                    String company_code = obj.getString("t");
                    String current_index = obj.getString("l_cur");
                    current_index= Html.fromHtml((String) current_index).toString();
                    String diff_index = obj.getString("c");
                    String diff_per_index = obj.getString("cp");
                    String time_index = obj.getString("lt");
                    String preivous_close = obj.getString("pcls_fix");


                  /*  DataObject_Watchlist obj12 = new DataObject_Watchlist(company_code, current_index, diff_index, diff_per_index, time_index, preivous_close);
                    // Log.d("object",EVENT);
                    results.add(obj12);
                    mAdapter.notifyDataSetChanged();*/

                    Map<String, String> stock_index_details = new HashMap<String, String>();
                    stock_index_details.put("full_name", longname);
                    stock_index_details.put("current_index", current_index);
                    stock_index_details.put("diff_index", diff_index);
                    stock_index_details.put("diff_per_index", diff_per_index);
                    stock_index_details.put("time_index", time_index);
                    stock_index_details.put("preivous_close", preivous_close);

                    //=================================================================


                    databaseReference.child(sharepref.getString("key_usermobno",null)).child(company_code).setValue(stock_index_details);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Watchlist_Firebase()).commit();


                }

            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //pg_bar.setVisibility(View.GONE);





        }
    }



    class GetShareIndex_all extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");




        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");

// should be a singleton
            OkHttpClient client = new OkHttpClient();
            /*HttpUrl.Builder urlBuilder = HttpUrl.parse("https://ajax.googleapis.com/ajax/services/search/images").newBuilder();
            urlBuilder.addQueryParameter("v", "1.0");
            urlBuilder.addQueryParameter("q", "android");
            urlBuilder.addQueryParameter("rsz", "8");
            String url = urlBuilder.build().toString();*/

            Log.d("url",str_whatchlist_shares);

            Request request = new Request.Builder()
                    .url("http://finance.google.com/finance/info?client=ig&q="+str_whatchlist_shares)
                    .build();



            try{
                //request mate nicheno code
                Response response = client.newCall(request).execute();

                res1=response.body().string();
                // Log.d("okhtp==",res1);

            }catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res1;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res1 == null || res1.equals("")) {



                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }


            try {


                mRecyclerView.setAdapter(mAdapter);
                // Log.d("result string",res1);
                res1=res1.substring(3);
                JSONArray array_res = new JSONArray(res1);

                for(int a =0;a<array_res.length();a++){


                    //  Log.i("RESPONSE", res1);
                    JSONObject obj = array_res.getJSONObject(a);

                    String company_code = obj.getString("t");
                    String current_index = obj.getString("l_cur");
                    current_index= Html.fromHtml((String) current_index).toString();
                    String diff_index = obj.getString("c");
                    String diff_per_index = obj.getString("cp");
                    String time_index = obj.getString("lt");
                    String preivous_close = obj.getString("pcls_fix");


                    DataObject_Watchlist obj12 = new DataObject_Watchlist(company_code, current_index, diff_index, diff_per_index, time_index, preivous_close);
                    // Log.d("object",EVENT);
                    results.add(obj12);
                    mAdapter.notifyDataSetChanged();


                }

            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //pg_bar.setVisibility(View.GONE);





        }
    }

}
