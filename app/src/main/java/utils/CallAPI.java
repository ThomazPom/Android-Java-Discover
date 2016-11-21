package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Joiner;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.unice.mbds.androiddevdiscoverlb.R;

/**
 * Created by Thoma on 14/11/2016.
 */

public class CallAPI extends AsyncTask<String, String, JSONArray> {

    public static abstract class CallbackClass {
        public abstract void postCall(JSONArray result);
    }


    public CallAPI(String urlString, CallbackClass callback
            , HashMap<String, Object> postDataParam, Context context) {

        this.callback = callback;
        this.postDataParam = postDataParam;
        this.urlString = urlString;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {

            Toast.makeText(context, R.string.internetConnexionError, Toast.LENGTH_LONG).show();
            Log.d("doInBackground", "Aucune connexion internet");

        }


    }


    private Context context;
    private CallbackClass callback;
    private HashMap<String, Object> postDataParam;

    private String urlString = ""; // URL to call

    @Override
    protected JSONArray doInBackground(String... params) {

        final int d = Log.d("CallAPI", "doInBackground");


        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry : postDataParam.entrySet()) {
            try {
                json.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ;


        if (params.length > 0) {
            switch (params[0]) {
                case "POST":
                    JSONArray jarr = new JSONArray();
                    jarr.put(sendHTTPData(urlString, json));
                    return jarr;
                case "DELETE":
                    return deleteFromURL(urlString);
            }
        }
        return getDataFromURL(urlString);
    }

    public JSONObject sendHTTPData(String urlpath, JSONObject json) {
        HttpURLConnection connection = null;
        try {

            Log.d("sendHTTPData", urlpath);
            URL url = new URL(urlpath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            streamWriter.write(json.toString());
            streamWriter.flush();
            StringBuilder stringBuilder = new StringBuilder();
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                Log.d("test", stringBuilder.toString());

                Log.d("sendHTTPData", stringBuilder.toString());
                return new JSONObject( stringBuilder.toString());
            } else {
                Log.e("test", connection.getResponseMessage());
                return null;
            }
        } catch (Exception exception) {
            Log.e("test", exception.toString());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(JSONArray result) {

        try {
            callback.postCall(result);
            Log.d("onPostExecute", " " + result);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static JSONArray deleteFromURL(String urlString) {

        HttpURLConnection con = null;
        try {
            URL obj = new URL(urlString);
            con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("DELETE");
            InputStreamReader inputStream = new InputStreamReader(con.getInputStream());
            BufferedReader bf = new BufferedReader(inputStream);
            String line;
            StringBuilder sb = new StringBuilder();
            JSONArray array = new JSONArray();
            String toParse = "";
            int i =0;
            while ((line = bf.readLine()) != null) {
                i++;
                String trim = line.trim();
                if (!trim.equals("[") && !trim.equals("]")) {

                    //  Log.d("line", line);
                    if (trim.equals("}") || trim.equals("},")) {
                        toParse+="}";
                        // Log.d("toparse"+i, toParse);
                        array.put(new JSONObject(toParse));
                        toParse = "";
                    }
                    else
                    {
                        toParse += line;
                    }
                }
            }
            return array;

            // return  IOUtils.toString(con.getInputStream(), "UTF8");
        } catch (Exception exception) {
            Log.e("DELETE FROM URL", exception.toString());

        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }
    public static JSONArray getDataFromURL(String urlString) {

        HttpURLConnection con = null;
        try {

            URL obj = new URL(urlString);
            con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");


            InputStreamReader inputStream = new InputStreamReader(con.getInputStream());

            BufferedReader bf = new BufferedReader(inputStream);
            String line;
            StringBuilder sb = new StringBuilder();
            JSONArray array = new JSONArray();
            String toParse = "";
            int i =0;
            while ((line = bf.readLine()) != null) {
                i++;
                String trim = line.trim();
                if (!trim.equals("[") && !trim.equals("]")) {

                  //  Log.d("line", line);
                    if (trim.equals("}") || trim.equals("},")) {
                        toParse+="}";
                       // Log.d("toparse"+i, toParse);
                        array.put(new JSONObject(toParse));
                        toParse = "";

                    }
                    else
                    {
                        toParse += line;
                    }

                }
            }
            return array;

            // return  IOUtils.toString(con.getInputStream(), "UTF8");
        } catch (Exception exception) {
            Log.e("getDataFromURL", exception.toString());

        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }


}