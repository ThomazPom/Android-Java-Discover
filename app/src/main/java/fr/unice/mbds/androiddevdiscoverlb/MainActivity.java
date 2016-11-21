package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickEnregistrer(View v) {
        Intent intent = new Intent(this, enregitrerActivity.class);
        startActivity(intent);
    }

    public void onClickConnexion(View v) {
        Intent intent = new Intent(this, connexionActivity.class);
        startActivity(intent);
    }
}
