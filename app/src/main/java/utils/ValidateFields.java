package utils;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.unice.mbds.androiddevdiscoverlb.R;

/**
 * Created by Thoma on 19/10/2016.
 */

public class ValidateFields {

    // Toast viewToast = Toast.makeText();

    List<View>views2Lock;
    HashMap<View,Boolean>validateViews;
    static int ERROR_TAG = 200;
    public ValidateFields(List<View> views2Lock,boolean lockdefault)
    {
        this.validateViews =new HashMap<>();
        this.views2Lock=views2Lock;

        for(View v : views2Lock){
            v.setEnabled(!lockdefault);
        }
    }
    private boolean lockviews(boolean lock, View currentView)
    {
        Log.d(this.getClass().getName(),views2Lock.toString());
        this.validateViews.put(currentView,lock);
        if(validateViews.containsValue(true)){

                for(View v : views2Lock){
                    v.setEnabled(false);
                }
            return false; //return : are view enabled? true / false
            }
        for(View v : views2Lock){
            v.setEnabled(true);
        }
        return  true; //return : are view enabled? true / false


    }

    public void verifyOnFocusChangeListener(final View view) {
        Log.d(this.getClass().getName(), "verifyOnFocusChangeListener");
        try {
            verifyOnFocusChangeListener((EditText) view);
        }
        catch(Exception e)
        {
            Log.d(this.getClass().getName(), "Type de vue en entrée non supporté");
        }
    }
    public void verifyOnFocusChangeListener(final EditText editText) {
        Log.d(this.getClass().getName(), "verifyOnFocusChangeListener/EditText");

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;

                Toast.makeText(editText.getContext(), "Ce champ n'est pas de type reconnu par le validateur", Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getName(), "No event binded :" + editText.getInputType() + " " + editText.getId());

            }
        };
        switch (editText.getInputType()) {
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if (!validateName(editText.getText().toString())) {
                                editText.setError("Le nom renseigné n'est pas valide");
                                //Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un nom valide", Toast.LENGTH_SHORT).show();

                                lockviews(true,editText);
                            }
                            else
                            {

                                editText.setError(null);
                                lockviews(false,editText);
                            }
                        }
                        Log.d(this.getClass().getName(), " event :" + InputType.TYPE_TEXT_VARIATION_PERSON_NAME + " " + InputType.TYPE_CLASS_TEXT);
                        ;
                    }
                };
                break;
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus) {
                            EditText mirrorPw = (EditText) v.getTag();
                            if (mirrorPw != null) {
                                if (!mirrorPw.getText().toString().equals(editText.getText().toString())) {
                                    // Toast.makeText(editText.getContext(), "Les mot de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                                    editText.setError("Les mot de passe ne correspondent pas");
                                    lockviews(true,editText);
                                }
                                else if (editText.getText().toString().isEmpty())
                                {
                                    lockviews(true,editText);
                                    editText.setError("Aucun mot de passe n'a été renseigné");
                                }
                                else
                                {
                                    editText.setError(null);
                                    lockviews(false,editText);
                                }

                            } else if(editText.getText().toString().isEmpty()) {
                                lockviews(true,editText);
                                editText.setError("Aucun mot de passe n'a été renseigné");
                            }
                            else
                            {
                                editText.setError(null);
                                lockviews(false,editText);
                            }
                        }

                        Log.d(this.getClass().getName(), " event :" + " " + InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                };
                break;
            case InputType.TYPE_CLASS_PHONE:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if(editText.getText().length() != 10) {
                                editText.setError("Merci de renseigner un numéro de téléphone français à 10 chiffres");
                                lockviews(true,editText);
                                //   Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un # de téléphone valide", Toast.LENGTH_SHORT).show();

                            }

                            else
                            {
                                editText.setError(null);
                                lockviews(false,editText);
                            }
                        }
                        Log.d(this.getClass().getName(), " event :" + InputType.TYPE_CLASS_PHONE);
                    }
                };
                break;
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus)
                        {
                            if (!isValidEmailAddress(editText.getText().toString())) {
                                lockviews(true,editText);
                                editText.setError("L'email fourni n'est pas valide");
                            }
                            else {

                                editText.setError(null);
                                lockviews(false,editText);
                            }

                            //Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un email valide", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(this.getClass().getName(), " event :" + InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    }
                };
                break;
        }
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean validateName( String name )
    {
        return name.matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" ) && name.length()>1;
    } // end method validateLastName

/*
 View myView = (View)findViewById(R.id.my_view);
    myView.setOnLongClickListener(new OnLongClickListener() {
        @Override
        public void onLongClick(View v) {

        }
    });
  */


}
