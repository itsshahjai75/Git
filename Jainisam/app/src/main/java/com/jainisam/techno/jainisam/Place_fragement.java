package com.jainisam.techno.jainisam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Place_fragement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Place_fragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Place_fragement extends Fragment {


    List<ItemObject_gridview> items = new ArrayList<>();
    public Place_fragement() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.place_fragement, container, false);


        GridView gridview = (GridView)v.findViewById(R.id.gridview);

        final List<ItemObject_gridview> allItems = getAllItemObject();
        CustomAdapter_Gridview customAdapter = new CustomAdapter_Gridview(getActivity(), allItems);
        gridview.setAdapter(customAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = items.get(position).getContent().toString();


                selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
                selectedItem = selectedItem.toLowerCase();
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                Log.e("PLACeType", selectedItem);
                Intent result = new Intent(getActivity(), Place_results.class);
                result.putExtra("place_type_intent", selectedItem);
                startActivity(result);

            }
        });



        return v;
    }


    private List<ItemObject_gridview> getAllItemObject(){
        ItemObject_gridview itemObject = null;
        items.clear();
        items.add(new ItemObject_gridview("UPASRYAs", "upashrya"));
        items.add(new ItemObject_gridview("DERASARs", "derasar"));
        items.add(new ItemObject_gridview("PATHSHALAs", "pathshala"));
        items.add(new ItemObject_gridview("DHARAMSHALAs", "dharamshala"));
        items.add(new ItemObject_gridview("PANJARAPOLs", "panjarapol"));
        items.add(new ItemObject_gridview("HOSTELs", "hostell"));
        items.add(new ItemObject_gridview("MEDICAL-STOREs", "medicalstore"));
        items.add(new ItemObject_gridview("COLLEGEs", "college"));
         /*items.add(new ItemObject("Image One", "one"));
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
        return items;
    }




}
