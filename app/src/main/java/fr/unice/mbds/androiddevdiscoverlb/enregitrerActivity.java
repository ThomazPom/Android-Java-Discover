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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import utils.CallAPI;
import utils.ValidateFields;

public class enregitrerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregitrer);

        View mdp1 = this.findViewById(R.id.editTextMDP1);
        View mdp2 = this.findViewById(R.id.editTextMDP2);

        mdp1.setTag(mdp2);
        mdp2.setTag(mdp1);


        ArrayList<View> lockviews = new ArrayList<>();

        lockviews.add(this.findViewById(R.id.Save));

        ValidateFields validator = new ValidateFields(this, lockviews, true);
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextNom));
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextPrenom));
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextTelephone));
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextEmail));
        validator.verifyOnFocusChangeListener(mdp1);
        validator.verifyOnFocusChangeListener(mdp2);
    }


    public void enregistrerOnCick(View view) {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(enregitrerActivity.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {

            String sexe = "";

            sexe = ((RadioButton) enregitrerActivity.this.findViewById(((RadioGroup) enregitrerActivity.this.findViewById(R.id.radiogroupsexe)).getCheckedRadioButtonId())).getText().toString();


            Person c1 = new Person(
                    ((EditText) enregitrerActivity.this.findViewById(R.id.editTextEmail)).getText().toString(),
                    ((EditText) enregitrerActivity.this.findViewById(R.id.editTextNom)).getText().toString(),
                    ((EditText) enregitrerActivity.this.findViewById(R.id.editTextPrenom)).getText().toString(),
                    sexe,
                    ((EditText) enregitrerActivity.this.findViewById(R.id.editTextTelephone)).getText().toString(),
                    ((EditText) enregitrerActivity.this.findViewById(R.id.editTextMDP1)).getText().toString()
                    , null);

            registerPerson(c1);
        } else {
            Toast.makeText(enregitrerActivity.this, R.string.internetConnexionError, Toast.LENGTH_SHORT).show();
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


    private void registerPerson(Person person) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("nom", person.getNom());
        params.put("prenom", person.getPrenom());
        params.put("sexe", person.getSexe());
        params.put("telephone", person.getTelephone());
        params.put("email", person.getEmail());
        params.put("createdby", person.getCreatedBy());
        params.put("password", person.getPassword());
        final int d = Log.d("loginPerson", "loginPerson");
        new CallAPI("http://95.142.161.35:8080/person/", new CallAPI.CallbackClass() {
            @Override
            public void postCall(JSONArray resultarray) {
                if (resultarray != null) {
                    try {
                        JSONObject result = resultarray.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {


                    }

                    Intent i = new Intent(enregitrerActivity.this, connexionActivity.class);
                    startActivity(i);
                    Toast.makeText(enregitrerActivity.this, R.string.inscription_ok, Toast.LENGTH_LONG).show();
                    return;

                }
                Toast.makeText(enregitrerActivity.this, R.string.ErreurRegister, Toast.LENGTH_LONG).show();

            }
        }, params, getApplicationContext()).execute("POST");

    }
}
