package com.example.eventfinder.Model;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KeywordApi {

    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList = new ArrayList<String>();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://assignment8-377801.wl.r.appspot.com/suggest?");
            sb.append("keyword="+input);
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;
            char[] buffer = new char[1024];
            while ((read = inputStreamReader.read(buffer)) != -1){
                jsonResult.append(buffer, 0, read);
            }


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
        }

        try {

            JSONObject jsonobject = new JSONObject(jsonResult.toString());
            JSONObject data = jsonobject.getJSONObject("_embedded");
            JSONArray prediction = data.getJSONArray("attractions");

            Log.e("prodicaition", String.valueOf(prediction.length()));

            for(int i = 0; i < prediction.length(); i++) {
                arrayList.add(prediction.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return arrayList;
    }
}
