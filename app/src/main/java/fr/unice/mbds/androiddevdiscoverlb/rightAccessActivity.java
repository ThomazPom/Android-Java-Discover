package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Arrays;

import utils.ValidateFields;

public class rightAccessActivity extends AppCompatActivity {
    TextView loginTV;
    TextView mdpTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_access);
        loginTV = (TextView) this.findViewById(R.id.loginTV);
        mdpTV = (TextView) this.findViewById(R.id.mdpTV);

        ValidateFields validate = new ValidateFields(this, Arrays.asList(new View[]{}), true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String  mailConnexionFromParent = extras.getString("MAIL_CONEXION");
            String  mdpConnexionFromParent = extras.getString("MDP_CONEXION");

            loginTV.setText(mailConnexionFromParent);
            mdpTV.setText(mdpConnexionFromParent);

        }
    }

}