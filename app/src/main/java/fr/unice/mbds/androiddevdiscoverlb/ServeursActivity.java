package fr.unice.mbds.androiddevdiscoverlb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.CallAPI;
import utils.PersonneAdapter;

public class ServeursActivity extends AppCompatActivity {

    ListView listeServeurs;
    Boolean deleteModeOn = false;
    private ImageButton btDelAll;
    private TextView tvDelAll;
    private ImageButton btDelmode;
    private TextView tvDelMode;
    private ImageButton btBackDel;
    public PersonneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serveurs);
        listeServeurs = (ListView) findViewById(R.id.listViewServ);
        fillList();
        LinearLayout lLayoutDeleteBarServ = (LinearLayout) findViewById(R.id.lLayoutDeleteBarServ);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_RIGHT);

        RelativeLayout.LayoutParams lb = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(30, 40, 0, 0);

        btDelmode = new ImageButton(ServeursActivity.this);
        // btDelAll.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1000f));
        btDelmode.setImageResource(R.mipmap.deletemode);
        btDelmode.setBackgroundColor(Color.TRANSPARENT);

        tvDelMode = new TextView(ServeursActivity.this);
        tvDelMode.setTextSize(20);
        tvDelMode.setText("Mode supression");
        tvDelMode.setLayoutParams(layoutParams);

        //tvDelAll.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));

        btDelAll = new ImageButton(ServeursActivity.this);
        btDelAll.setImageResource(R.mipmap.delete);
        btDelAll.setBackgroundColor(Color.TRANSPARENT);

        tvDelAll = new TextView(ServeursActivity.this);
        tvDelAll.setTextSize(20);
        tvDelAll.setText("Supprimer tous");
        tvDelAll.setLayoutParams(layoutParams);


        btBackDel = new ImageButton(ServeursActivity.this);
        btBackDel.setImageResource(R.mipmap.valid);
        btBackDel.setBackgroundColor(Color.TRANSPARENT);

        RelativeLayout backbuttonRLLayout = new RelativeLayout(ServeursActivity.this);
        backbuttonRLLayout.setLayoutParams(lp);
        btBackDel.setLayoutParams(lb);
        backbuttonRLLayout.addView(btBackDel);
        //  backbuttonRLLayout.setBackgroundColor(Color.CYAN);
        // btDelmode.setClickable(true);
        // btDelAll.setClickable(true);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelMode(!deleteModeOn);
                Log.d("clicked", v.getId() + "");

            }
        };
        View.OnClickListener deleteAllClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ServeursActivity.this)
                        .setMessage("Voulez vous vraiment supprimer TOUT les utilisateurs?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Person.deleteAll(ServeursActivity.this, adapter.person);
                                adapter.person.removeAll(adapter.person);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Non, annuler", null)
                        .show();
                Log.d("clicked", v.getId() + "");

            }
        };
        btDelmode.setOnClickListener(onClickListener);
        //  btDelAll.setOnClickListener(onClickListener);
        btBackDel.setOnClickListener(onClickListener);
        //  tvDelAll.setOnClickListener(onClickListener);
        tvDelMode.setOnClickListener(onClickListener);
        tvDelAll.setOnClickListener(deleteAllClickListener);
        btDelAll.setOnClickListener(deleteAllClickListener);


        setDelMode(deleteModeOn);
        lLayoutDeleteBarServ.addView(btDelAll);
        lLayoutDeleteBarServ.addView(tvDelAll);
        lLayoutDeleteBarServ.addView(btDelmode);
        lLayoutDeleteBarServ.addView(tvDelMode);
        lLayoutDeleteBarServ.addView(backbuttonRLLayout);
    }

    public void setDelMode(Boolean delmode) {
        deleteModeOn = delmode;
        if (adapter != null) {
            adapter.hideButtons(deleteModeOn);
        }

        btDelAll.setVisibility(deleteModeOn ? View.VISIBLE : View.GONE);
        btBackDel.setVisibility(deleteModeOn ? View.VISIBLE : View.GONE);
        tvDelAll.setVisibility(deleteModeOn ? View.VISIBLE : View.GONE);
        tvDelMode.setVisibility(deleteModeOn ? View.GONE : View.VISIBLE);
        btDelmode.setVisibility(deleteModeOn ? View.GONE : View.VISIBLE);

    }

    public void fillList() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        new CallAPI("http://95.142.161.35:8080/person/", new CallAPI.CallbackClass() {

            @Override
            public void postCall(JSONArray result) {


                List<Person> persons = new ArrayList<>();
                ListView lst = (ListView) findViewById(R.id.listViewServ);
                if (result != null) {
                    Log.d("fillList", result.toString());
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            persons.add(new Person(result.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.d("forperson", "size" + persons.size());
                    adapter = new PersonneAdapter(ServeursActivity.this, persons, deleteModeOn);
                    lst.setAdapter(adapter);
                    return;
                }


                Toast.makeText(ServeursActivity.this, R.string.ErreurConnexion, Toast.LENGTH_LONG).show();
            }
        }, params, getApplicationContext()).execute();
    }
}
