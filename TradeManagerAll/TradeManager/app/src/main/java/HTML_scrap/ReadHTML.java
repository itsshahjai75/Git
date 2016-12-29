package HTML_scrap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadHTML extends AsyncTask<Void, Void, Void> {
    Context context;
    String address;// = "http://www.moneycontrol.com/rss/marketreports.xml";//"http://www.sciencemag.org/rss/news_current.xml";
    ProgressDialog progressDialog;
    ArrayList<Html_FeedItem> htmlFeedItems;
    RecyclerView recyclerView;
    URL url;
    public ReadHTML(Context context, RecyclerView recyclerView, String address) {
        this.recyclerView=recyclerView;
        this.context = context;
        this.address = address;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Html_Adapter adapter=new Html_Adapter(context, htmlFeedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(20));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected Void doInBackground(Void... params) {
        ProcessHtml(Getdata());

        return null;
    }

    private void ProcessHtml(Document data) {
        if (data != null) {
           /* htmlFeedItems =new ArrayList<>();
            //Element root = data.getDocumentElement();
           // Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    Html_FeedItem item=new Html_FeedItem();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")){
                            item.setTitle(cureent.getTextContent());
                        }else if (cureent.getNodeName().equalsIgnoreCase("description")){
                            String descript=cureent.getTextContent();
                            descript=descript.substring(descript.indexOf("/>")+2);
                            item.setDescription(descript);
                            String url=cureent.getTextContent()
                                    .substring(cureent.getTextContent().indexOf("src=")+5,cureent.getTextContent().indexOf("alt=")-2);
                            item.setThumbnailUrl(url);
                            //Log.d("imageur",url);
                        }else if (cureent.getNodeName().equalsIgnoreCase("pubDate")){
                            item.setPubDate(cureent.getTextContent());
                        }else if (cureent.getNodeName().equalsIgnoreCase("link")){
                            item.setLink(cureent.getTextContent());
                        }*//*else if (cureent.getNodeName().equalsIgnoreCase("media:thumbnail")){
                            //this will return us thumbnail url
                            String url=cureent.getAttributes().item(0).getTextContent();
                            item.setThumbnailUrl(url);
                        }*//*
                    }
                    htmlFeedItems.add(item);





                }
            }*/
        }
    }

    public Document Getdata() {
        try {
            url = new URL(address);
            Document document = Jsoup.connect(address).get();

            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
