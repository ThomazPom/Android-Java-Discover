package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
        ValidateFields validator = new ValidateFields();

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
}
