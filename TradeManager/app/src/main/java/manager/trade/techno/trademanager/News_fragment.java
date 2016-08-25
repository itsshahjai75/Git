package manager.trade.techno.trademanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import RSS.ReadRss;


/**
 * A simple {@link Fragment} subclass.
 */
public class News_fragment extends Fragment {

    RecyclerView recyclerView;
    public News_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_news_fragment, container, false);


        recyclerView= (RecyclerView)convertView.findViewById(R.id.recyclerview);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        ReadRss readRss=new ReadRss(getContext(),recyclerView,"http://www.moneycontrol.com/rss/MCtopnews.xml");
        readRss.execute();


        return convertView;

    }

}
