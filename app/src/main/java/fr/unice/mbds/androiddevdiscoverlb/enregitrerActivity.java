package fr.unice.mbds.androiddevdiscoverlb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

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

        ValidateFields validator = new ValidateFields(this,lockviews,true);
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextNom));
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextPrenom));
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextTelephone));
        validator.verifyOnFocusChangeListener(this.findViewById(R.id.editTextEmail));
        validator.verifyOnFocusChangeListener(mdp1);
        validator.verifyOnFocusChangeListener(mdp2);
    }

    public void lost_focus_enregistrer(View v)
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void enregistrerOnCick(View view){

    }
    ProgressDialog progressDialog;
    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if(progressDialog==null) {
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
        }
        else {
            if(progressDialog!=null) {
                progressDialog.dismiss();
            }
        }
    }

    class RegisterTask extends AsyncTask<Person,Void,Person> {

         protected Person doInBackground(Person... people) {
             String url = "http://95.142.161.35:1337/person/";
             for (int i = 0; i < people.length; i++) {
                 Person person = people[1];
                 try {
                     HttpClient client = new DefaultHttpClient();
                     HttpPost post = new HttpPost(url);

                     // add header

                     post.setHeader("Content-Type", "application/json");
                     JSONObject obj = new JSONObject();
                     obj.put("prenom", person.getPrenom());
                     obj.put("nom", person.getNom());
                     obj.put("sexe", person.getSexe());
                     obj.put("telephone", person.getTelephone());
                     obj.put("email", person.getEmail());
                     obj.put("createdby", person.getCreatedBy());
                     obj.put("password", person.getPassword());
                     StringEntity entity = new StringEntity(obj.toString());

                     post.setEntity(entity);

                     HttpResponse response = client.execute(post);
                     System.out.println("\nSending 'POST' request to URL : " + url);
                     System.out.println("Post parameters : " + post.getEntity());
                     System.out.println("Response Code : " +
                             response.getStatusLine().getStatusCode());

                     BufferedReader rd = new BufferedReader(
                             new InputStreamReader(response.getEntity().getContent()));

                     StringBuffer result = new StringBuffer();
                     String line = "";
                     while ((line = rd.readLine()) != null) {
                         result.append(line);
                     }

                     System.out.println(result.toString());
                     return person;
                 } catch (Exception e) {

                 }
             }
             return null;
         }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(Person person) {
            super.onPostExecute(person);
            showProgressDialog(false);
            Toast.makeText(enregitrerActivity.this,R.string.inscription_ok, Toast.LENGTH_LONG).show();
        }
     }

}
