package utils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Thoma on 19/10/2016.
 */

public class ValidateFields {

    // Toast viewToast = Toast.makeText();

    List<View> views2LockList;

    List<ViewParent> viewsParent;
    HashMap<View, Boolean> validateViews;
    static int ERROR_TAG = 200;
    View lastTouch;


    public ValidateFields(Activity activity, List<View> views2Lock, boolean lockdefault) {
        activity.getWindow().getDecorView().setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        View view = ((Activity) v.getContext()).getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        }
                        return true;
                    }
                }
        );
/*
            activity.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = ((Activity)v.getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if(validateViews.containsValue(true)){

                    for(View viewcheck : views2LockList){
                        viewcheck.setEnabled(false);
                    }
                    return true;
                }
            });


  */

        this.validateViews = new HashMap<>();
        this.views2LockList = views2Lock;

        for (View v : views2Lock) {
            v.setEnabled(!lockdefault);
        }
    }


    private boolean lockviews(boolean lock, View currentView) {
        lastTouch= currentView;
        Log.d(this.getClass().getName(), views2LockList.toString());
        this.validateViews.put(currentView, lock);
        if (validateViews.containsValue(true)) {

            for (View v : views2LockList) {
                v.setEnabled(false);
            }
            return false; //return : are view enabled? true / false
        }
        for (View v : views2LockList) {
            v.setEnabled(true);
        }
        return true; //return : are view enabled? true / false


    }

    public void verifyOnFocusChangeListener(final View view) {
        Log.d(this.getClass().getName(), "verifyOnFocusChangeListener");


        try {
            verifyOnFocusChangeListener((EditText) view);


            lockviews(true, view);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "Type de vue en entrée non supporté");
        }
    }

    private void verifyOnFocusChangeListener(final EditText editText) {
        Log.d(this.getClass().getName(), "verifyOnFocusChangeListener/EditText");

        TextWatcher changeListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EditText editText = (EditText) s;
                Toast.makeText(editText.getContext(), "Ce champ n'est pas de type reconnu par le validateur", Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().getName(), "No event binded :" + editText.getInputType() + " " + editText.getId());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        switch (editText.getInputType()) {
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME:
                changeListener = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!validateName(editText.getText().toString())) {
                            editText.setError("Le nom renseigné n'est pas valide");
                            //Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un nom valide", Toast.LENGTH_SHORT).show();

                            lockviews(true, editText);
                        } else {

                            editText.setError(null);
                            lockviews(false, editText);
                        }

                        Log.d(this.getClass().getName(), " event :" + InputType.TYPE_TEXT_VARIATION_PERSON_NAME + " " + InputType.TYPE_CLASS_TEXT);
                        ;
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                break;
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD:
                changeListener = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        EditText mirrorPw = (EditText) editText.getTag();
                        if (mirrorPw != null) {
                            if (!mirrorPw.getText().toString().equals(editText.getText().toString())) {
                                // Toast.makeText(editText.getContext(), "Les mot de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                                editText.setError("Les mot de passe ne correspondent pas");
                                lockviews(true, editText);
                            } else if (editText.getText().toString().isEmpty()) {
                                lockviews(true, editText);
                                lockviews(true,mirrorPw);
                                editText.setError("Aucun mot de passe n'a été renseigné");
                            } else {
                                editText.setError(null);
                                lockviews(true, editText);
                                lockviews(true, mirrorPw);
                            }

                        } else if (editText.getText().toString().isEmpty()) {
                            lockviews(true, editText);
                            editText.setError("Aucun mot de passe n'a été renseigné");
                        } else {
                            editText.setError(null);
                            lockviews(false, editText);
                        }


                        Log.d(this.getClass().getName(), " event :" + " " + InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                break;
            case InputType.TYPE_CLASS_PHONE:
                changeListener = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (editText.getText().length() != 10) {
                            editText.setError("Merci de renseigner un numéro de téléphone français à 10 chiffres");
                            lockviews(true, editText);
                            //   Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un # de téléphone valide", Toast.LENGTH_SHORT).show();

                        } else {
                            editText.setError(null);
                            lockviews(false, editText);
                        }

                        Log.d(this.getClass().getName(), " event :" + InputType.TYPE_CLASS_PHONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                break;
            case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                changeListener = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (!isValidEmailAddress(editText.getText().toString())) {
                            lockviews(true, editText);
                            editText.setError("L'email fourni n'est pas valide");
                        } else {

                            editText.setError(null);
                            lockviews(false, editText);
                        }

                        //Toast.makeText(editText.getContext(), "Il est obligatoire de fournir un email valide", Toast.LENGTH_SHORT).show();

                        Log.d(this.getClass().getName(), " event :" + InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                break;
        }
        editText.addTextChangedListener(changeListener);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean validateName(String name) {
        return name.matches("[a-zA-z]+([ '-][a-zA-Z]+)*") && name.length() > 1;
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
