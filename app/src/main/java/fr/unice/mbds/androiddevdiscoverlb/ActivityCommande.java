package fr.unice.mbds.androiddevdiscoverlb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utils.CallAPI;
import utils.PersonneAdapter;
import utils.PlatAdapter;

public class ActivityCommande extends AppCompatActivity {

    Boolean addPlatVisible = false;
    private RelativeLayout reListPlatsLayout;
    private PlatAdapter adapterListCommande;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final FloatingActionButton fab_valid = (FloatingActionButton) findViewById(R.id.fab_valid);

        reListPlatsLayout = (RelativeLayout) findViewById(R.id.reListPlatsLayout);

        reListPlatsLayout.setVisibility(addPlatVisible ? View.VISIBLE : View.GONE);
        fab.setImageDrawable(null);
        fab.setImageResource(R.mipmap.button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlatVisible = !addPlatVisible;

                reListPlatsLayout.setVisibility(addPlatVisible ? View.VISIBLE : View.GONE);
                fab_valid.setVisibility(addPlatVisible ?   View.GONE:View.VISIBLE);
                FloatingActionButton me = (FloatingActionButton) view;
                me.setImageResource(addPlatVisible ? R.mipmap.rewind_button : R.mipmap.button_add);


            }
        });

        fab_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(ActivityCommande.this)
                        .setMessage("Voulez vous envoyer la commande aux serveurs?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                HashMap<String,Object> commande =new HashMap<>();
                                JSONArray commandItems = new JSONArray();
                                for (Plats p : adapterListCommande.plats)
                                {
                                    commandItems.put(p.getJsonOfPlat());
                                }
                                commande.put("items",commandItems);
                                commande.put("server",connexionActivity.userConnected.getJsonOfPerson());
                                new  CallAPI("http://95.142.161.35:8080/menu/",new CallAPI.CallbackClass() {
                                    @Override
                                    public void postCall(JSONArray result) {

                                            Snackbar.make(view, "La commande est envoyée !", 4000)
                                                    .setAction("Action", null).show();
                                            adapterListCommande.plats.removeAll(adapterListCommande.plats);
                                            adapterListCommande.notifyDataSetChanged();

                                    }
                                },commande,ActivityCommande.this).execute("POST");
                            }
                        })
                        .setNegativeButton("Non, annuler", null)
                        .show();
                Log.d("clicked", view.getId() + "");


            }
        });

        ListView lst = (ListView) findViewById(R.id.listViewPlatCommande);
        adapterListCommande = new PlatAdapter(ActivityCommande.this, new ArrayList<Plats>(), false, new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("ButtonRem", "Clicked");
                Snackbar.make(v, "Retiré !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        lst.setAdapter(adapterListCommande);
        fillListAdd();
    }


    public void fillListAdd() {
        final View.OnClickListener buttonClickListenerAddPlat = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonAdd", "Clicked");
                Snackbar.make(v, "Ajouté !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                adapterListCommande.plats.add((Plats) v.getTag());
                adapterListCommande.notifyDataSetChanged();
            }
        };
        HashMap<String, Object> params = new HashMap<String, Object>();
        new CallAPI("http://95.142.161.35:8080/product/", new CallAPI.CallbackClass() {

            public PlatAdapter adapter;

            @Override
            public void postCall(JSONArray result) {


                List<Plats> plats = new ArrayList<>();
                ListView lst = (ListView) findViewById(R.id.listViewAddPlat);
                if (result != null) {
                    Log.d("fillList", result.toString());
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            plats.add(new Plats(result.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.d("forplat", "size" + plats.size());
                    adapter = new PlatAdapter(ActivityCommande.this, plats, true, buttonClickListenerAddPlat);
                    lst.setAdapter(adapter);
                    return;
                }


                Toast.makeText(ActivityCommande.this, R.string.ErreurConnexion, Toast.LENGTH_LONG).show();
            }
        }, params, getApplicationContext()).execute();
    }
}
