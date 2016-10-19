package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class connexionActivity extends AppCompatActivity {
    EditText mailConnexionTF;
    EditText mdpConnexionTF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        mailConnexionTF = (EditText) this.findViewById(R.id.mailConnexionTF);
        mdpConnexionTF = (EditText) this.findViewById(R.id.mdpConnexionTF);
    }

    public void lost_focus_connexion(View v)
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onClickConnexionButton(View v) {

        Intent intent = new Intent(this, rightAccessActivity.class);
        intent.putExtra("MAIL_CONEXION", mailConnexionTF.getText().toString());
        intent.putExtra("MDP_CONEXION", mdpConnexionTF.getText().toString());
        startActivity(intent);
    }
}
