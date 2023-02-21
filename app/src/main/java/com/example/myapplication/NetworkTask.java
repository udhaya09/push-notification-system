package com.example.myapplication;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkTask extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... params) {
        String email = params[0];

        String urlString = "<YOUR-API-END-POINT>/api/v1/user-details/mobile-register/" + email;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String result = "Error Occurred";
            try {
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // Handle the response data
                    return response.toString();
                } else {
                    System.out.println("Error occurred");
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error Occurred";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error Occurred";
        }
    }

    protected void onPostExecute(String result) {
        // Update UI with the result
    }
}
