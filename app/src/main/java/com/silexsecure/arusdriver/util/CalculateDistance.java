package com.silexsecure.arusdriver.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class CalculateDistance extends AsyncTask<String, String, String> {

    public CalculateDistance() {

    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        URL url = null;
        String forecastJsonStr = null;
        try {
            url = new URL("http://maps.google.com/maps/api/directions/json?origin="
                    + params[0] + "," + params[1] + "&destination=" + params[2]
                    + "," + params[3] + "&sensor=false&units=metric");
            //params[0] : Source latitude
            //params[1] : Source longitude
            //params[2] : Destination latitude
            //params[3] : Destination longitude
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            return forecastJsonStr;
        } catch (IOException e) {
            Log.e("Exception", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Exception", "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject object = new JSONObject(s);
            if (object.get("status").equals("OK")) {
                JSONArray routesArry = object.optJSONArray("routes");
                if (routesArry != null && routesArry.length() > 0) {
                    JSONObject routesObject = routesArry.optJSONObject(0);
                    if (routesObject != null) {
                        JSONArray legArrry = routesObject.optJSONArray("legs");
                        if (legArrry != null && legArrry.length() > 0) {
                            JSONObject legObject = legArrry.optJSONObject(0);
                            if (legObject != null) {
                                JSONObject distanceObject = legObject.optJSONObject("distance");
                                if (distanceObject != null) {
                                    String totalDistance = distanceObject.optString("text");
                                    Log.e("TotalDistance", totalDistance);

                                    //Distance may come in meter or kilometer, If distance between source and destination is less than 1KM it shows distane in meter
                                    double dis;
                                    if (totalDistance.contains("km")) {
                                        totalDistance = totalDistance.replace(" km", "");
                                        dis = Double.parseDouble(totalDistance);
                                    } else {
                                        totalDistance = totalDistance.replace(" m", "");
                                        dis = Double.parseDouble(totalDistance) / 1000;
                                    }

                                    totalDistance = String.valueOf(dis);
                                    Log.e("TotalDistance in km/m", totalDistance);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}