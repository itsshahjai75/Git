package manager.trade.techno.trademanager;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import RSS.ReadRss;


/**
 * A simple {@link Fragment} subclass.
 */
public class Market_reports extends Fragment {
    RecyclerView recyclerView;

    public Market_reports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/ProductSans-Regular.ttf");
        // fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_market_reports, container, false);
        //==add this line to change all font to coustom font in fragments
        fontChanger.replaceFonts((ViewGroup)convertView);


        FloatingActionButton fab = (FloatingActionButton)convertView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerView= (RecyclerView)convertView.findViewById(R.id.recyclerview);
        ReadRss readRss=new ReadRss(getContext(),recyclerView,"http://www.moneycontrol.com/rss/marketreports.xml");
        readRss.execute();


        return convertView;

    }

}
