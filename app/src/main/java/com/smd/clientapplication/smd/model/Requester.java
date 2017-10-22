package com.smd.clientapplication.smd.model;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nikitos on 22.10.2017.
 */

public class Requester {
    private static Requester mRequester;
    private final String mHost = "http://127.0.0.1";
    private Requester(){

    }

    public static Requester getInstance(){
        if(mRequester==null){
            mRequester = new Requester();
        }
        return mRequester;
    }

    public void getPlotData(final DiagnosticCallBack callback){
       AsyncTask<Void,Void,Void> asyncTask =  new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                String requestUrl = "http://31.186.100.211:8000";
                try {
                    URL url = new URL(requestUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int code = connection.getResponseCode();
                    PlotDataJsonParser parser = new PlotDataJsonParser();
                    PlotDataResponse response = parser.readJsonStream(connection.getInputStream());
                    callback.onDataRecieved(response);

                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.execute();



    }
}
