package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import fr.unice.mbds.androiddevdiscoverlb.R;
import fr.unice.mbds.androiddevdiscoverlb.connexionActivity;

/**
 * Created by Thoma on 14/11/2016.
 */

public class CallAPI extends AsyncTask<String, String, String> {

    public static abstract class CallbackClass {
        public abstract void postCall(JSONObject result);
    }

    public CallAPI(String urlString, CallbackClass callback
                , HashMap<String, Object> postDataParam, Context context) {

        this.callback = callback;
        this.postDataParam = postDataParam;
        this.urlString = urlString;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    private Context context;
    private  CallbackClass callback;
    private HashMap<String, Object> postDataParam ;

    private String urlString = ""; // URL to call

    @Override
    protected String doInBackground(String... params) {

        final int d = Log.d("CallAPI","doInBackground");
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected)
        {

            Toast.makeText(context, R.string.ErreurConnexion, Toast.LENGTH_LONG).show();
            Log.d("doInBackground","Aucune connexion internet");
            return null;
        }

        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry : postDataParam.entrySet())
        {
            try {
                json.put(entry.getKey(),entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        return sendHTTPData(urlString,json);
    }

    public String sendHTTPData(String urlpath, JSONObject json) {
        HttpURLConnection connection = null;
        try {

            Log.d("sendHTTPData",urlpath);
            URL url=new URL(urlpath);
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
            if ( connection.getResponseCode()>=200 && connection.getResponseCode()<300 ){
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                Log.d("test", stringBuilder.toString());

                Log.d("sendHTTPData", stringBuilder.toString());
                return stringBuilder.toString();
            } else {
                Log.e("test", connection.getResponseMessage());
                return null;
            }
        } catch (Exception exception){
            Log.e("test", exception.toString());
            return null;
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            Log.d("onPostExecute"," "+result);
           callback.postCall(new JSONObject(result));
        } catch (Exception e) {

            callback.postCall(null);
            e.printStackTrace();
        }
    }
}