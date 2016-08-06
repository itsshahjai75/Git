package arihantmart.techno.arihantmart;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerAdapter_Dailyoffres extends RecyclerView
        .Adapter<MyRecyclerAdapter_Dailyoffres
        .DataObject_postHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject_Dailyoffers> mDataset;
    private Context mContext;
    NotificationManager mNotifyManager;

    SharedPreferences sharepref;


    String offertitle,res,offermessage,offerdate;



    public static class DataObject_postHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView offer_title,offer_message,offer_date;


        TextView btn_share;

        public DataObject_postHolder(final View itemView) {
            super(itemView);



            offer_title = (TextView) itemView.findViewById(R.id.tv_offer_title);
            offer_message = (TextView) itemView.findViewById(R.id.tv_offer_messagae);
            offer_date = (TextView) itemView.findViewById(R.id.tv_offerpost_date);





            btn_share = (TextView) itemView.findViewById(R.id.btn_share_offer);


            // Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);





        }

        @Override
        public void onClick(View v) {
       //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerAdapter_Dailyoffres(ArrayList<DataObject_Dailyoffers> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;


    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {



            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dailyoffers_cardview, parent, false);

        FontChangeCrawler fontChanger = new FontChangeCrawler(mContext.getAssets(), "fonts/ProductSans-Regular.ttf");
        //fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        fontChanger.replaceFonts((ViewGroup)view);

        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);




        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);



        holder.offer_title.setText(mDataset.get(position).getmOffertitle());
        holder.offer_message.setText(mDataset.get(position).getmOffermessage());
        holder.offer_date.setText(mDataset.get(position).getmOfferdate());






        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentshare = new Intent(Intent.ACTION_SEND);
                intentshare.setType("text/plain");
                intentshare.putExtra(Intent.EXTRA_TEXT, "Offer : "+mDataset.get(position).getmOffertitle()
                        +"\nDetail: "+mDataset.get(position).getmOffermessage()
                        +"\n\nBotad's General Store Online. \"Arihant Mart\" all kind of products available just download and Enjoy.\n\n" + "https://play.google.com/store/apps/details?id=arihantmart.techno.arihantmart&hl=en");
                mContext.startActivity(Intent.createChooser(intentshare, "Share"));

            }
        });


    }

    public void addItem(DataObject_Dailyoffers dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }




    /*class Edit_tocart extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        ProgressDialog progressDialog;


        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "add tocart");
            progressDialog =  new ProgressDialog(mContext);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

                    //show(mContext.getApplicationContext(),"Loading...","Pleae wait !",true,false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/editcart.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("item_name",str_itemname));
                params.add(new BasicNameValuePair("item_id",str_itemid));
                params.add(new BasicNameValuePair("user_email",str_useremail));
                params.add(new BasicNameValuePair("item_quantity",str_qnty));


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
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("msg");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("quantity updatede!")){

                    Toast.makeText(mContext,"Updated !",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext,"Error!!!",Toast.LENGTH_LONG).show();
                }



                mContext.startActivity(new Intent(mContext,CartProducts.class));
                ((Activity)mContext).finish();






            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }







    }

    class Delete_tocart extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        ProgressDialog progressDialog;


        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "add tocart");
            progressDialog =  new ProgressDialog(mContext);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            //show(mContext.getApplicationContext(),"Loading...","Pleae wait !",true,false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/delete_cartitem.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("user_email",str_useremail));
                params.add(new BasicNameValuePair("cart_id",str_cart_id));


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
                Toast.makeText(mContext, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("msg");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("deleted")){

                    Toast.makeText(mContext,"Updated !",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext,"Error!!!",Toast.LENGTH_LONG).show();
                }



                mContext.startActivity(new Intent(mContext,CartProducts.class));
                ((Activity)mContext).finish();






            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }







    }
*/


}