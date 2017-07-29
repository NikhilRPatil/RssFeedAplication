package com.example.savi.rssfeedaplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class Downloader extends AsyncTask<Void, Void, Object> {
    private Context c;
    private String urlAddress;
    private ListView lv;
    private ProgressDialog pd;
    private OnOperationCompleteListener mOnOperationCompleteListener;

    public Downloader(Context c, String urlAddress) {
        this.c = c;
        this.urlAddress = urlAddress;
    }

    public void setOnOperationCompleteListener(OnOperationCompleteListener onOperationCompleteListener) {
        mOnOperationCompleteListener = onOperationCompleteListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Fetch Articles");
        pd.setCancelable(false);
        pd.setMessage("Fetching...Please wait");
        pd.show();
    }

    @Override
    protected Object doInBackground(Void... params) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        pd.dismiss();
        if (data.toString().startsWith("Error")) {
            Toast.makeText(c, data.toString(), Toast.LENGTH_SHORT).show();
        } else {
            //PARSE RSS
            RSSParser rssParser = new RSSParser(c, (InputStream) data, lv);
            rssParser.setOnOperationCompleteListener(mOnOperationCompleteListener);
            rssParser.execute();
        }
    }

    private Object downloadData() {
        Object connection = Connector.connect(urlAddress);
        if (connection.toString().startsWith("Error")) {
            return connection.toString();
        }
        try {
            HttpURLConnection con = (HttpURLConnection) connection;
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return new BufferedInputStream(con.getInputStream());
            }
            return ErrorTracker.RESPONSE_EROR + con.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorTracker.IO_EROR;
        }
    }
}
