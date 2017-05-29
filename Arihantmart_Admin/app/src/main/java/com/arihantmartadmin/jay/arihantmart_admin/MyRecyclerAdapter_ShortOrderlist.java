package com.arihantmartadmin.jay.arihantmart_admin;

        import android.app.Activity;
        import android.app.NotificationManager;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.os.SystemClock;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Adapter;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.protocol.HTTP;
        import org.apache.http.util.EntityUtils;
        import org.json.JSONObject;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.TimeZone;
        import java.util.Timer;
        import java.util.TimerTask;
        import java.util.concurrent.TimeUnit;


public class MyRecyclerAdapter_ShortOrderlist extends RecyclerView.Adapter<MyRecyclerAdapter_ShortOrderlist.ViewHolder> {

    private Context context;
    private final List<ViewHolder> lstHolders;
    public List<DataObject_ShortOrderlist> lst;
    SharedPreferences sharepref;

    Calendar  end_date,Date1=null;
    String order_time,str_useremail,str_ordernumber,res;

    long different;


    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining();
                }
            }
        }
    };

    public MyRecyclerAdapter_ShortOrderlist(List<DataObject_ShortOrderlist> lst, Context context){
        super();
        this.lst = lst;
        this.context = context;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_order_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(lst.get(position));
        synchronized (lstHolders) {
            lstHolders.add(holder);
        }
        //holder.updateTimeRemaining(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderno,tv_counter_order,btn_return_wholeorder;
        TextView order_date,useremail;

        DataObject_ShortOrderlist mDatasetObject;

        public void setData(DataObject_ShortOrderlist item) {
            mDatasetObject = item;
            sharepref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);



            orderno.setText(item.getmorderno());
            order_date.setText(item.getmorderdate());
            useremail.setText(item.getmorderuseremail());
            if(item.getmorderuseremail().equalsIgnoreCase("1")){

                tv_counter_order.setText("00:00:00");
                btn_return_wholeorder.setText("Delivered.");
                btn_return_wholeorder.setEnabled(false);

            }else if(item.getmorderuseremail().equalsIgnoreCase("2")){

                tv_counter_order.setText("00:00:00");
                btn_return_wholeorder.setText("Canceled.");
                btn_return_wholeorder.setEnabled(false);

            }







        }

        public void updateTimeRemaining() {



            try {


                order_time = mDatasetObject.getmorderdate();
                // Log.v("order_time==", order_time);



                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US);
                Date1 = Calendar.getInstance();
                Date1.setTime(format.parse(order_time));

                end_date = Calendar.getInstance();
                end_date.setTime(format.parse(order_time));
                end_date.add(Calendar.HOUR,4);


                final Calendar today = Calendar.getInstance();
                different = end_date.getTimeInMillis() - today.getTimeInMillis();


                if(different>0){

                    int seconds = (int) (different / 1000) % 60;
                    int minutes = (int) ((different / (1000 * 60)) % 60);
                    int hours = (int) ((different / (1000 * 60 * 60)) % 24);

                    tv_counter_order.setText(hours + " hrs " + minutes + " mins " + seconds + " sec");
                }else{

                    tv_counter_order.setText("is Expired !!");
                    btn_return_wholeorder.setEnabled(false);

                }


            }catch (Exception datecp){
                datecp.printStackTrace();
            }





        }

        public ViewHolder(final View itemView) {
            super(itemView);
            orderno = (TextView) itemView.findViewById(R.id.tv_order_no);
            order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            useremail = (TextView) itemView.findViewById(R.id.tv_useremailorder);
            tv_counter_order = (TextView) itemView.findViewById(R.id.tv_timer_return);
            btn_return_wholeorder = (TextView) itemView.findViewById(R.id.btn_returnorder_wholeorder);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(itemView.getContext(),"itemcode="+mDataset.get(getAdapterPosition()).getmitemid().toString(),Toast.LENGTH_LONG).show();

                    Intent order = new Intent(v.getContext(),All_ordres.class);
                    order.putExtra("orderno",mDatasetObject.getmorderno().toString());
                    itemView.getContext().startActivity(order);


                }
            });

            btn_return_wholeorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    str_useremail=sharepref.getString("key_useremail", "null");
                    if(str_useremail.equalsIgnoreCase("demo@demo.com")){

                        Toast.makeText(context,"Please Register First, You are Demo user.",Toast.LENGTH_LONG).show();

                    }else {
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                        alertbox.setMessage("Order is Delivered to Customer?");
                        alertbox.setTitle("Order Delivered.");
                        alertbox.setIcon(R.mipmap.ic_launcher);

                        alertbox.setNeutralButton("YES,Delivered",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,int arg1) {

                                        str_ordernumber=mDatasetObject.getmorderno();
                                        str_useremail=sharepref.getString("key_useremail", "null");
                                        new Deliveredorder().execute();
                                    }
                                });
                        alertbox.show();

                    }


                }
            });





        }
    }


    class Deliveredorder extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        ProgressDialog progressDialog;


        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "add tocart");
            progressDialog =  new ProgressDialog(context);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            //show(mContext.getApplicationContext(),"Loading...","Pleae wait !",true,false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/deliveredorderbyadmin.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("email_address",str_useremail));
                params.add(new BasicNameValuePair("order_number",str_ordernumber));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(context, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                //Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("msg");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("Updated")){

                    Toast.makeText(context,"Updated !",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"Error!!!",Toast.LENGTH_LONG).show();
                }



                context.startActivity(new Intent(context,Orders_short.class));
                ((Activity)context).finish();






            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }







    }


}