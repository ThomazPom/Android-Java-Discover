package utils;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.unice.mbds.androiddevdiscoverlb.R;

/**
 * Created by Thoma on 19/10/2016.
 */

public class ValidateFields {

   // Toast viewToast = Toast.makeText();

    public  void verifyOnFocusChangeListener(final View view)
    {
        Log.d(this.getClass().getName(),"verifyOnFocusChangeListener");
        final EditText editText = (EditText)view;
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText =(EditText) v;

                Toast.makeText(editText.getContext(), "Ce champ n'est pas de type reconnu par le validateur", Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getName(),"No event binded :"+editText.getInputType() +" " +editText.getId() );

            }
        };
        switch (editText.getInputType())
        {
            case InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME:
                    onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus)Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un nom valide", Toast.LENGTH_SHORT).show();
                        Log.d(this.getClass().getName()," event :"+InputType.TYPE_TEXT_VARIATION_PERSON_NAME+" "+InputType.TYPE_CLASS_TEXT);
 ;                   }
                };break;
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus)
                        {
                            EditText mirrorPw = (EditText)v.getTag();
                            if(mirrorPw != null)
                            {
                                EditText pw = (EditText)v;

                                if (!mirrorPw.getText().toString().equals(pw.getText().toString())) {
                                    if (!hasFocus) {
                                        Toast.makeText(editText.getContext(), "Les mot de passe ne correspondent pas", Toast.LENGTH_SHORT).show();

                                        pw.setError("Les mot de passe ne correspondent pas");
                                    }
                                    else {

                                    }
                                    }

                            }
                        }

                        Log.d(this.getClass().getName()," event :"+" "+InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                };break;
            case InputType.TYPE_CLASS_PHONE:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus)Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un # de téléphone valide", Toast.LENGTH_SHORT).show();
                        Log.d(this.getClass().getName()," event :"+InputType.TYPE_CLASS_PHONE);
                    }
                };break;
            case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus)Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un email valide", Toast.LENGTH_SHORT).show();
                        Log.d(this.getClass().getName()," event :"+InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    }
                };break;
        }
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }


/*
 View myView = (View)findViewById(R.id.my_view);
    myView.setOnLongClickListener(new OnLongClickListener() {
        @Override
        public void onLongClick(View v) {

        }
    });
  */



}
