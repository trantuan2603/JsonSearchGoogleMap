package com.landsoft.jsonsearchgooglemap.DATA;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TRANTUAN on 02-Dec-17.
 */

public class JsonToString {
    private String jsonString;

    public JsonToString(String jsonString) {
        this.jsonString = jsonString;
    }

    public void getDataJson(){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++){
                JSONObject resultsObject = results.getJSONObject(i);
              //  Log.d("results", "getDataJson: " + results.toString());
                JSONArray addressComponents = resultsObject.getJSONArray("address_components");
                for (int j = 0; j < addressComponents.length(); j++){
                    if (j==2) {
                        JSONObject addressComponentsObject = addressComponents.getJSONObject(j);
                        Log.d("address_components",addressComponentsObject.toString());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> LogAndLat(){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray("results");
            //Log.d("geometryObject", "LogAndLat: " + results.toString());
            JSONObject resultObject = results.getJSONObject(0);
            JSONObject geometryObject = new JSONObject(resultObject.get("geometry").toString());
            JSONObject locationObject = new JSONObject(geometryObject.get("location").toString());
            String mLat = locationObject.getString("lat");
            String mLng = locationObject.getString("lng");
            // Log.d("location",mLat+mLog);
            List<String> list = new ArrayList<>();
            list.add(mLat);
            list.add(mLng);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
