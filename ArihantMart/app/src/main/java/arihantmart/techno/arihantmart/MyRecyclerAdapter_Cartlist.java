package arihantmart.techno.arihantmart;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

import java.util.ArrayList;
import java.util.List;

import me.himanshusoni.quantityview.QuantityView;

public class MyRecyclerAdapter_Cartlist extends RecyclerView
        .Adapter<MyRecyclerAdapter_Cartlist
        .DataObject_postHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject_CartItem> mDataset;
    private Context mContext;
    NotificationManager mNotifyManager;

    SharedPreferences sharepref;

    private int pricePerProduct = 180;
    String Price,res,str_itemid,str_itemname,str_useremail,str_cart_id;
    String str_qnty="0";


    public static class DataObject_postHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView itemname;
        TextView price,saving,addedtime;
        ImageView post_img;
        QuantityView qty_piker;
        TextView btn_addtocart,btn_remove;

        public DataObject_postHolder(final View itemView) {
            super(itemView);



            itemname = (TextView) itemView.findViewById(R.id.tv_cart_itemname);
            price = (TextView) itemView.findViewById(R.id.tv_cart_itemprice);
            saving = (TextView) itemView.findViewById(R.id.tv_cart_saving);
            addedtime = (TextView) itemView.findViewById(R.id.tv_cart_date_added);

            post_img = (ImageView) itemView.findViewById(R.id.img_post);

            qty_piker = (QuantityView) itemView.findViewById(R.id.number_picker);

            btn_addtocart = (TextView) itemView.findViewById(R.id.btn_addtocart);
            btn_remove = (TextView) itemView.findViewById(R.id.btn_remove);


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

    public MyRecyclerAdapter_Cartlist(ArrayList<DataObject_CartItem> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;


    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {



            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cartitems_list_cardview, parent, false);

        FontChangeCrawler fontChanger = new FontChangeCrawler(mContext.getAssets(), "fonts/ProductSans-Regular.ttf");
        //fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        fontChanger.replaceFonts((ViewGroup)view);

        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);

             mNotifyManager =
                (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);


        holder.qty_piker.setQuantity(Integer.parseInt(mDataset.get(position).getmquantity()));


        holder.itemname.setText(mDataset.get(position).getmitemname());
        holder.price.setText( "MRP: ₹ "+mDataset.get(position).getmitemprice());
        holder.saving.setText("Saving : ₹ "+mDataset.get(position).getmSaving());
        holder.addedtime.setText("Added on :"+mDataset.get(position).getmaddedtime());


        String str_imageresourse = mDataset.get(position).getmBitmap();
        int imageResourceId = mContext.getResources().getIdentifier(str_imageresourse, "drawable", mContext.getPackageName());

        try {

            String itemname=mDataset.get(position).getmitemname();
            itemname=itemname.replace(" ","");
			itemname=itemname.substring(0,itemname.indexOf("("));


            Picasso.with(mContext)
                    .load("http://arihantmart.com/androidapp/images/"+itemname+".jpg")
                    .placeholder(imageResourceId) // optional
                    .error(imageResourceId)         // optional
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into( holder.post_img);


        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }

        Price= mDataset.get(position).getmitemprice().toString();
        Price=Price.substring(Price.indexOf("Our Price: ₹ ")+13);


        pricePerProduct=Integer.parseInt(Price);


       /* holder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_itemid=mDataset.get(position).getmitem_id();
                str_itemname=mDataset.get(position).getmText1();
                str_useremail=sharepref.getString("key_useremail", "null");

                if(str_useremail.equalsIgnoreCase("demo@demo.com")){

                    Toast.makeText(mContext,"Please Register First, You are Demo user.",Toast.LENGTH_LONG).show();

                }else if(str_qnty.equalsIgnoreCase("0") || str_qnty.equalsIgnoreCase("null")){
                    Toast.makeText(mContext,"Please Add Quantity.",Toast.LENGTH_LONG).show();
                }else {
                    new Add_tocart().execute();
                }

                Toast.makeText(mContext,"quantity  is =="+str_qnty,Toast.LENGTH_LONG).show();
            }
        });*/

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                str_useremail=sharepref.getString("key_useremail", "null");
                if(str_useremail.equalsIgnoreCase("demo@demo.com")){

                    Toast.makeText(mContext,"Please Register First, You are Demo user.",Toast.LENGTH_LONG).show();

                }else {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setMessage("Item will be removed from your cart.");
                    alertbox.setTitle("Delete Item ?");
                    alertbox.setIcon(R.drawable.appicon);

                    alertbox.setNeutralButton("Delete",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,int arg1) {

                                    str_cart_id=mDataset.get(position).getmcartid();
                                    str_useremail=sharepref.getString("key_useremail", "null");
                                    new Delete_tocart().execute();
                                }
                            });
                    alertbox.show();

                }



                //Toast.makeText(mContext," Share Clicked ",Toast.LENGTH_LONG).show();
            }
        });



        holder.qty_piker.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {




               // Toast.makeText(mContext, "Quantity: " + newQuantity, Toast.LENGTH_LONG).show();
                if(newQuantity!=0 && newQuantity >=1){
                    str_qnty=Integer.toString(newQuantity);
                    str_itemid=mDataset.get(position).getmitemid();
                    str_itemname=mDataset.get(position).getmitemname();
                    str_useremail=sharepref.getString("key_useremail", "null");
                    new Edit_tocart().execute();
                }else if(newQuantity==0 && newQuantity <1){
                    Toast.makeText(mContext,"Item can not 0(Zero)",Toast.LENGTH_LONG).show();
                    holder.qty_piker.setQuantity(1);
                }
            }

            @Override
            public void onLimitReached() {
                //Log.d(getClass().getSimpleName(), "Limit reached");
            }
        });
        holder.qty_piker.setQuantityClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Change Quantity");

                View inflate = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_change_quantity, null, false);
                final EditText et = (EditText) inflate.findViewById(R.id.et_qty);
                final TextView tvPrice = (TextView) inflate.findViewById(R.id.tv_price);
                final TextView tvTotal = (TextView) inflate.findViewById(R.id.tv_total);

                et.setText(String.valueOf( holder.qty_piker.getQuantity()));
                tvPrice.setText("\u20B9" + pricePerProduct);
                tvTotal.setText("\u20B9 " +  holder.qty_piker.getQuantity() * pricePerProduct);


                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s)) return;

                        int intNewQuantity = Integer.parseInt(s.toString());
                        tvTotal.setText("\u20B9 " + intNewQuantity * pricePerProduct);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                builder.setView(inflate);
                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newQuantity = et.getText().toString();
                        if (TextUtils.isEmpty(newQuantity)) return;

                        int intNewQuantity = Integer.parseInt(newQuantity);

                        holder.qty_piker.setQuantity(intNewQuantity);
                    }
                }).setNegativeButton("Cancel", null);
                builder.show();
            }
        });





    }

    public void addItem(DataObject_CartItem dataObj, int index) {
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




    class Edit_tocart extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        ProgressDialog progressDialog;


        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "add tocart");
            progressDialog =  new ProgressDialog(mContext);
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


                //Log.i("RESPONSE", res);

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
            //Log.d("pre execute", "add tocart");
            progressDialog =  new ProgressDialog(mContext);
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


                //Log.i("RESPONSE", res);

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



}