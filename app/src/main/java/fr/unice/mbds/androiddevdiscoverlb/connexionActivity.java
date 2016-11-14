package fr.unice.mbds.androiddevdiscoverlb;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.CallAPI;
import utils.ValidateFields;

public class connexionActivity extends AppCompatActivity {
    EditText mailConnexionTF;
    EditText mdpConnexionTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        mailConnexionTF = (EditText) this.findViewById(R.id.mailConnexionTF);
        mdpConnexionTF = (EditText) this.findViewById(R.id.mdpConnexionTF);

        ValidateFields validate = new ValidateFields(this, Arrays.asList(new View[]{this.findViewById(R.id.SaveLogin)}), true);

        validate.verifyOnFocusChangeListener(this.findViewById(R.id.mailConnexionTF));

        validate.verifyOnFocusChangeListener(this.findViewById(R.id.mdpConnexionTF));
    }

    public void onClickConnexionButton(View v) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(connexionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {

            Person c1 = new Person(
                    ((EditText) connexionActivity.this.findViewById(R.id.mailConnexionTF)).getText().toString(),
                    ((EditText) connexionActivity.this.findViewById(R.id.mdpConnexionTF)).getText().toString());

            Log.d("onClickConnexionButton","onClickConnexionButton");
            loginPerson(c1);
            //new RegisterTask().execute(ctab);
        } else {
            Toast.makeText(connexionActivity.this, R.string.internetConnexionError, Toast.LENGTH_SHORT).show();
        }
    }

    ProgressDialog progressDialog;

    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        progressDialog = null;
                    }
                });
                progressDialog.show();
            }
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }


    private void loginPerson(Person person)
    {
        HashMap<String,Object> params = new HashMap<>();
        params.put("email", person.getEmail());
        params.put("password", person.getPassword());
        final int d = Log.d("loginPerson","loginPerson");
        new CallAPI("http://95.142.161.35:1337/person/login/", new CallAPI.CallbackClass(){
            @Override
            public void postCall(JSONObject result) {


                if(result!=null)
                {

                    Log.d("postCall", String.valueOf(result));
                    try {
                        if( result.getBoolean("success"))
                        {

                            Toast.makeText(connexionActivity.this, R.string.connexion_ok, Toast.LENGTH_LONG).show();
                            final int d = Log.d("postCall(String result)",result.toString());
                            Intent i = new Intent(connexionActivity.this, rightAccessActivity.class);

                            i.putExtra("MAIL_CONNEXION", result.getJSONObject("user").getString("email"));
                            i.putExtra("NOM_CONNEXION", result.getJSONObject("user").getString("nom"));
                            i.putExtra("PRENOM_CONNEXION", result.getJSONObject("user").getString("prenom"));
                            i.putExtra("NUMTEL_CONNEXION",  result.getJSONObject("user").getString("telephone"));
                            Log.d("postCall",result.getJSONObject("user").getString("email"));
                            startActivity(i);


                            return;

                         }
                        else
                        {

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

                Toast.makeText(connexionActivity.this, R.string.ErreurConnexion, Toast.LENGTH_LONG).show();
            }
        },params, getApplicationContext()).execute();

    }
}