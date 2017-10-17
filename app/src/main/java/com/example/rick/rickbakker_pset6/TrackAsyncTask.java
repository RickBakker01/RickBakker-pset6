package com.example.rick.rickbakker_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by Rick on 17-10-2017.
 */

public class TrackAsyncTask extends AsyncTask<String, Integer, String> {


    Context context;
    MoodsActivity moodsActivity;

    public TrackAsyncTask(MoodsActivity mood) {
        this.moodsActivity = mood;
        this.context = this.moodsActivity.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Setting the mood!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpRequestHelper.downloadFromServer(params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        HashMap<String, String> paintingOBJ2 = new HashMap<>();
        try {
            JSONObject Mainobj = new JSONObject(result);
            Log.d("MAIN", Mainobj.toString());
            JSONArray paintings = Mainobj.getJSONArray("artObjects");


            for (int i = 0; i < paintings.length(); i++) {
                JSONObject paintingsOBJ = paintings.getJSONObject(i);
                JSONObject urlobj = paintingsOBJ.getJSONObject("links");
                JSONObject imageobj = paintingsOBJ.getJSONObject("webImage");

                //JSONObject paintingOBJ2 = new JSONObject();

                String title = paintingsOBJ.get("title").toString();
                String maker = paintingsOBJ.get("principalOrFirstMaker").toString();
                String paintingurl = urlobj.get("web").toString();
                String imageurl = imageobj.get("url").toString();

                paintingOBJ2.put("title", title);
                paintingOBJ2.put("maker", maker);
                paintingOBJ2.put("paintingurl", paintingurl);
                paintingOBJ2.put("imageurl", imageurl);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.moodsActivity.moodStartIntent(paintingOBJ2);
    }
}