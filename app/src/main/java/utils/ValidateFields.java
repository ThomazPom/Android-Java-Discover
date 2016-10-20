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

    public void verifyOnFocusChangeListener(final View view) {
        Log.d(this.getClass().getName(), "verifyOnFocusChangeListener");
        final EditText editText = (EditText) view;
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
                            editText.setError(null);
                            if (!validateName(editText.getText().toString())) {
                                editText.setError("Le nom renseigné n'est pas valide");
                                //Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un nom valide", Toast.LENGTH_SHORT).show();
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
                            editText.setError(null);
                            EditText mirrorPw = (EditText) v.getTag();
                            if (mirrorPw != null) {
                                if (!mirrorPw.getText().toString().equals(editText.getText().toString())) {
                                    // Toast.makeText(editText.getContext(), "Les mot de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                                    editText.setError("Les mot de passe ne correspondent pas");
                                }
                                else if (editText.getText().length()==0)
                                {

                                    editText.setError("Aucun mot de passe n'a été renseigné");
                                }

                            } else {
                                editText.setError("Aucun mot de passe n'a été renseigné");
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
                            editText.setError(null);
                            if(editText.getText().length() != 10) {
                                editText.setError("Merci de renseigner un numéro de téléphone français à 10 chiffres");
                            }//   Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un # de téléphone valide", Toast.LENGTH_SHORT).show();
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
                            editText.setError(null);
                            if (!isValidEmailAddress(editText.getText().toString())) {
                                editText.setError("L'email fourni n'est pas valide");
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
