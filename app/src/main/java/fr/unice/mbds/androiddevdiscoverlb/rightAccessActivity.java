package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import utils.ValidateFields;

public class rightAccessActivity extends AppCompatActivity {
    TextView nomTV;
    TextView emailTV;
    TextView numTelTv;
    Button buttonServeurs;
    Button buttonCommandes;
    static String nomConnecte;
    static String preConnecte;
    static String numTelConnecte;
    static String emailConnecte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_access);

        emailTV = (TextView) this.findViewById(R.id.emailTV);

        nomTV = (TextView) this.findViewById(R.id.nomTV);

        numTelTv = (TextView) this.findViewById(R.id.numTelTV);
        buttonCommandes = (Button) this.findViewById(R.id.buttonviewrstos);
        buttonServeurs = (Button) this.findViewById(R.id.buttonviewserv);


        ValidateFields validate = new ValidateFields(this, Arrays.asList(new View[]{}), true);

/*
*                             i.putExtra("MAIL_CONNEXION", result.getJSONObject("user").getString("email"));
                            i.putExtra("NOM_CONNEXION", result.getJSONObject("user").getString("nom"));
                            i.putExtra("PRENOM_CONNEXION", result.getJSONObject("user").getString("prenom"));
                            i.putExtra("NUMTEL_CONNEXION",  result.getJSONObject("user").getString("telephone"));

* */

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            emailTV.setText(emailConnecte= extras.getString("MAIL_CONNEXION"));
            nomTV.setText( (nomConnecte = extras.getString("NOM_CONNEXION"))+" "+ (preConnecte = extras.getString("PRENOM_CONNEXION")));
            numTelTv.setText( numTelConnecte = extras.getString("NUMTEL_CONNEXION"));
        }
        else
        {

            emailTV.setText(emailConnecte);
            nomTV.setText(nomConnecte +" "+preConnecte);
            numTelTv.setText(numTelConnecte);

        }
    }
    public void onClickCommandButton(View v) {

        Intent i = new Intent(rightAccessActivity.this, ActivityCommande.class);
        startActivity(i);
    }

    public void onClickServerButton(View v) {

        Intent i = new Intent(rightAccessActivity.this, ServeursActivity.class);
        startActivity(i);
    }

}