package com.example.rick.rickbakker_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the asynctask. It handles the retrieval of the data en putting it into the database.
 */


class MoodAsyncTask extends AsyncTask<String, Integer, String> {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Context context;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String uid = user.getUid();
    private DatabaseReference ref = database.getReference(uid);

    MoodAsyncTask(MoodsActivity mood) {
        this.context = mood.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, R.string.set_it, Toast.LENGTH_SHORT).show();
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
        Map<String, PaintingClass> paintingsmap = new HashMap<>();
        try {
            //Get the results
            JSONObject Mainobj = new JSONObject(result);
            JSONArray paintings = Mainobj.getJSONArray("artObjects");

            for (int i = 0; i < paintings.length(); i++) {
                JSONObject paintingsOBJ = paintings.getJSONObject(i);
                JSONObject urlobj = paintingsOBJ.getJSONObject("links");

                //Get all the data
                String title = paintingsOBJ.get("title").toString();
                String maker = paintingsOBJ.get("principalOrFirstMaker").toString();
                String paintingurl = urlobj.get("web").toString();

                //Make new map
                paintingsmap.put(String.valueOf(i), new PaintingClass(title, maker, paintingurl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Set map to database
        ref.setValue(paintingsmap);
    }
}