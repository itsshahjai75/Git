package com.jainisam.techno.jainisam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Products_fragment extends Fragment {



    List<ItemObject_gridview> items1 = new ArrayList<>();
    public Products_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v2= inflater.inflate(R.layout.products_fragment, container, false);




        GridView gridview = (GridView)v2.findViewById(R.id.gridview);

        final List<ItemObject_gridview> allItems = getAllItemObject();
        CustomAdapter_Gridview customAdapter = new CustomAdapter_Gridview(getActivity(), allItems);
        gridview.setAdapter(customAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = items1.get(position).getContent().toString();


                selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
                selectedItem = selectedItem.toLowerCase();
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext(), R.style.AppTheme);
                builder.setTitle("Mail Us!");
                builder.setMessage("Are you Jain Product SalLer?\n\nWe are now promoting your product free of charge, if its Jain base product.\n\nExample.(Nasta, Pooja samgri, Food items, Prabhavna Items etc)\n\nMail us we will add your things HERE!\n\nThank You");
                builder.setPositiveButton("Send e-mail", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "technocratsappware@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Products add from Jainism Application.");
                        intent.putExtra(Intent.EXTRA_TEXT, "Please Fill your details.\nName:\nContact.No:\nProduct Name?\n\n" +
                                "Product Details:" +
                                "\n\n" +
                                "Address for Receive product: \n\n\nThank you");
                        intent.putExtra(Intent.EXTRA_BCC, "shah.jai75@gmail.com");
                        startActivity(intent);
                    }
                })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });


                // show it
                builder.show();

            }
        });


        return v2;
    }



    private List<ItemObject_gridview> getAllItemObject(){
        ItemObject_gridview itemObject = null;
        items1.clear();
        items1.add(new ItemObject_gridview("BOOKs", "books"));
        items1.add(new ItemObject_gridview("SNACKs", "snacks"));
        items1.add(new ItemObject_gridview("FOODs", "foods"));
        /*items.add(new ItemObject_gridview("DHARAMSHALAs", "dharamshala"));
        items.add(new ItemObject_gridview("PANJRAPOLs", "panjarapol"));
        items.add(new ItemObject_gridview("HOSTELs", "hostell"));
        items.add(new ItemObject_gridview("MEDICAL STOREs", "medicalstore"));
        items.add(new ItemObject_gridview("COLLEGEs", "college"));
        items.add(new ItemObject("Image One", "one"));
        items.add(new ItemObject("Image Two", "two"));
        items.add(new ItemObject("Image Three", "three"));
        items.add(new ItemObject("Image Four", "four"));
        items.add(new ItemObject("Image Five", "five"));
        items.add(new ItemObject("Image Six", "six"));
        items.add(new ItemObject("Image Seven", "seven"));
        items.add(new ItemObject("Image Eight", "eight"));
        items.add(new ItemObject("Image One", "one"));
        items.add(new ItemObject("Image Two", "two"));
        items.add(new ItemObject("Image Three", "three"));
        items.add(new ItemObject("Image Four", "four"));
        items.add(new ItemObject("Image Five", "five"));
        items.add(new ItemObject("Image Six", "six"));
        items.add(new ItemObject("Image Seven", "seven"));
        items.add(new ItemObject("Image Eight", "eight"));*/
        return items1;
    }




}
