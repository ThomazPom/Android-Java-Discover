package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.mbds.androiddevdiscoverlb.R;

import static org.apache.commons.io.IOUtils.copy;

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

        if(context != null) {
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


        JSONArray jarr = new JSONArray();
        if (params.length > 0) {
            switch (params[0]) {
                case "POST":
                    jarr.put(sendHTTPData(urlString, json,"POST"));
                    return jarr;
                case "PUT":
                    jarr.put(sendHTTPData(urlString, json,"PUT"));
                    return jarr;
                case "DELETE":
                    return deleteFromURL(urlString);
                case "GETIMAGE":
                    jarr.put(getImageFromUrl(urlString));
                    return jarr;
            }
        }
        return getDataFromURL(urlString);
    }

    public Bitmap getImageFromUrl(String urlString)
    {
        try {
            Log.d("url",urlString);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public JSONObject sendHTTPData(String urlpath, JSONObject json,String method) {
        HttpURLConnection connection = null;
        try {

            Log.d("sendHTTPData", urlpath);
            URL url = new URL(urlpath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(method);
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

                Log.d("sendHTTPData1", stringBuilder.toString());

                Log.d("sendHTTPData2", stringBuilder.toString());
                return new JSONObject( stringBuilder.toString());
            } else {
                Log.e("sendHTTPData3", connection.getResponseMessage());
                return null;
            }
        } catch (Exception exception) {
            Log.e("sendHTTPData4", exception.toString());

            Log.e("sendHTTPData4", json.toString());
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

            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            ja.put(jo);
            return ja;

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


        //Other lines of code
        URL seatURL = null;
        try {
            seatURL = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JsonObject json =  new JsonObject();
        //Return the JSON Response from the API
        BufferedReader br = null;
        try {
            br = new BufferedReader(new
                    InputStreamReader(seatURL.openStream(),
            Charset.forName("UTF-8")),8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String readAPIResponse = " ";
        StringBuffer jsonString = new StringBuffer();
        try {
            while((readAPIResponse = br.readLine()) != null){
                   jsonString.append(readAPIResponse+"\n");
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = null;

        JSONArray root=null;
        try {
            root = new JSONArray(jsonString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERR",e.getMessage());
            return null;
        }

        return root;


    }


}