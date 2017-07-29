package com.example.savi.rssfeedaplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class Connector {
    public static Object connect(String urlAddress) {
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            return httpConnection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ErrorTracker.WRONG_URL_FORMAT;
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorTracker.CONNECTION_ERROR;
        }
    }
}
