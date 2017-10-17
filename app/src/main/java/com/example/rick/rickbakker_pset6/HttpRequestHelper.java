package com.example.rick.rickbakker_pset6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rick on 17-10-2017.
 */

public class HttpRequestHelper {

    protected static synchronized String downloadFromServer(String... params) throws
            MalformedURLException {
        String result = "";
        String color = params[0];

        URL url = new URL("https://www.rijksmuseum.nl/api/nl/collection?key=pOArRxv7&format=json"
                + "&type=schilderij&f.normalized32Colors.hex=" + color +
                "&imgonly=True&s=relevance");
        HttpURLConnection connect;

        try {
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");

            Integer responseCode = connect.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connect
                        .getInputStream()));
                String line;
                while ((line = bReader.readLine()) != null) {
                    result += line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
