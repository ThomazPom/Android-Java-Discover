package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Arrays;

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

    public void lost_focus_connexion(View v)
    {
    }

    public void onClickConnexionButton(View v) {

        Intent intent = new Intent(this, rightAccessActivity.class);
        intent.putExtra("MAIL_CONEXION", mailConnexionTF.getText().toString());
        intent.putExtra("MDP_CONEXION", mdpConnexionTF.getText().toString());
        startActivity(intent);
    }
}
