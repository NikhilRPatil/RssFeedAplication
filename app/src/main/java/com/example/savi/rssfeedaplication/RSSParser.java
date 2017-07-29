package com.example.savi.rssfeedaplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RSSParser extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private InputStream inputStream;
    private ListView listView;
    private ProgressDialog progressDialog;
    private ArrayList<Article> articles = new ArrayList<>();
    private OnOperationCompleteListener mOnOperationCompleteListener;

    public void setOnOperationCompleteListener(OnOperationCompleteListener onOperationCompleteListener) {
        mOnOperationCompleteListener = onOperationCompleteListener;
    }

    public RSSParser(Context context, InputStream inputStream, ListView listView) {
        this.context = context;
        this.inputStream = inputStream;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Parse RSS");
        progressDialog.setMessage("Parsing...Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseRSS();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);
        progressDialog.dismiss();
        if (mOnOperationCompleteListener != null) {
            if (isParsed) {
                mOnOperationCompleteListener.onOperationComplete(articles);
            } else {
                mOnOperationCompleteListener.onOperationFailed();
            }
        }

    }

    private Boolean parseRSS() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);
            int event = parser.getEventType();
            String tagValue = null;
            Boolean isSiteMeta = true;
            articles.clear();
            Article article = null;
            do {
                String tagName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            article = new Article();
                            isSiteMeta = false;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        tagValue = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (!isSiteMeta) {
                            if (tagName.equalsIgnoreCase("title")) {
                                article.setTitle(tagValue);
//                            } else if (tagName.equalsIgnoreCase("description")) {
//                                article.setDescription(tagValue);
                            } else if (tagName.equalsIgnoreCase("pubDate")) {
                                article.setDate(tagValue);
                            } else if (tagName.equalsIgnoreCase("link")) {
                                article.setLink(tagValue);
                                article.setImageUrl(getImageUrl(article.getLink()));
                                article.setDescription(getDescription(article.getLink()));
                            }
                        }
                        if (tagName.equalsIgnoreCase("item")) {
                            articles.add(article);
                            isSiteMeta = true;
                        }
                        break;
                }
                event = parser.next();
            } while (event != XmlPullParser.END_DOCUMENT);
            return true;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getImageUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementById("content");
            Elements element2 = element.select("img");
            return element2.attr("src");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDescription(String url) {

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Element element = document.getElementById("content");
            Elements elements = element.select("p");
            return elements.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
