package com.landsoft.jsonsearchgooglemap.DATA;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by TRANTUAN on 02-Dec-17.
 */

public class DownloadDataJson extends AsyncTask<String, Void, List> {
    TextView tvLatItude;
    TextView tvLongItude;
    public DownloadDataJson(TextView tvLatItude, TextView tvLongItude) {
        this.tvLatItude = tvLatItude;
        this.tvLongItude = tvLongItude;
    }

    @Override
    protected List doInBackground(String... strings) {
        List<String> list = null;
        String urlPath = strings[0];
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int conn = connection.getResponseCode();
            if (conn == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuffer jsonString = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    jsonString.append(line);
                }
                bufferedReader.close();
                reader.close();
                inputStream.close();
                JsonToString toString = new JsonToString(jsonString.toString());
               list =  toString.LogAndLat();
            }
           connection.disconnect();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
        tvLatItude.setText("LatItude: "+list.get(0).toString());
        tvLongItude.setText("LongItude: "+list.get(1).toString());

    }
}
