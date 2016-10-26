package fr.unice.mbds.androiddevdiscoverlb;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

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

        ValidateFields validate = new ValidateFields(this,Arrays.asList(new View[]{this.findViewById(R.id.SaveLogin)}),true);

        validate.verifyOnFocusChangeListener(this.findViewById(R.id.mailConnexionTF));

        validate.verifyOnFocusChangeListener(this.findViewById(R.id.mdpConnexionTF));
    }


    public void onClickConnexionButton(View v) {

        Person c1 = new Person(
                ((EditText) connexionActivity.this.findViewById(R.id.mailConnexionTF)).getText().toString(),
                ((EditText) connexionActivity.this.findViewById(R.id.mdpConnexionTF)).getText().toString());

        Person[] ctab = new Person[1];
        ctab[0] = c1;

        new RegisterTask().execute(ctab);
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

    class RegisterTask extends AsyncTask<Person, Void, Person> {

        private String resultat = "";
        @Override
        protected Person doInBackground(Person... people) {
            String url = "http://95.142.161.35:1337/person/login/";
            Person person = people[0];
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);

                // add header

                post.setHeader("Content-Type", "application/json");
                JSONObject obj = new JSONObject();
                obj.put("email", person.getEmail());
                obj.put("password", person.getPassword());
                StringEntity entity = new StringEntity(obj.toString());

                post.setEntity(entity);
                post.addHeader("content-type","application/json");
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

                resultat= result.toString();
                System.out.println(result.toString());
                return person;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
            Toast.makeText(connexionActivity.this, R.string.please_wait, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Person person) {
            super.onPostExecute(person);
            //enlever le loading
            showProgressDialog(false);
            //Enlever la person

            System.out.println(person.toString());
            StringTokenizer token1 = new StringTokenizer(resultat, ",");
            String save = token1.nextToken();

            StringTokenizer token2 = new StringTokenizer(save, ":");
            String sucess = token2.nextToken();


            if (sucess.equals(" true")) {

                Intent i = new Intent(connexionActivity.this, rightAccessActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(connexionActivity.this, R.string.ErreurRegister, Toast.LENGTH_LONG).show();
            }
        }
    }
}