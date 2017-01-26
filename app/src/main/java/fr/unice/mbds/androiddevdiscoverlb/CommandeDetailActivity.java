package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.CallAPI;
import utils.PersonneAdapter;

/**
 * An activity representing a single Commande detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CommandeListActivity}.
 */
public class CommandeDetailActivity extends AppCompatActivity {

    private Boolean serverListViewVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final ListView platsListView = (ListView) findViewById(R.id.commande_plats_container);

        final ListView serversListView = (ListView) findViewById(R.id.commande_server);
        serversListView.setVisibility(serverListViewVisible?View.VISIBLE:View.GONE);
        platsListView.setVisibility(serverListViewVisible?View.GONE:View.VISIBLE);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_change_command_server);

        FloatingActionButton fab_delete_command = (FloatingActionButton) findViewById(R.id.fab_delete_command);

        if (fab_delete_command != null) {
            fab_delete_command.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Commande.deleteCommande(CommandeDetailFragment.myCommand.getIdcommande());
                   Snackbar sb=  Snackbar.make(view, "La commande a été supprimée", Snackbar.LENGTH_LONG);
                           sb.setAction("Action", null).show();
                    sb.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar snackbar) {
                            finish();
                        }
                    });

                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverListViewVisible = !serverListViewVisible;
                serversListView.setVisibility(serverListViewVisible?View.VISIBLE:View.GONE);
                platsListView.setVisibility(serverListViewVisible?View.GONE:View.VISIBLE);
                fab.setImageResource(serverListViewVisible?R.mipmap.rewind_button:R.mipmap.profile2);
                final Context CommandeDetailActivityContext = CommandeDetailActivity.this;
                Log.d("fabClicked","fab_change_command_server 1");
               // if(true)return;
                if(serverListViewVisible)
                {

                    Log.d("fabClicked","fab_change_command_server 2");
                    new CallAPI("http://95.142.161.35:8080/person/?connected=true", new CallAPI.CallbackClass() {
                        @Override
                        public void postCall(JSONArray result) {

                            Log.d("fabClicked","fab_change_command_server 3");
                            List<Person> persons = new ArrayList<Person>();
                            for (int i = 0; i < result.length(); i++) {
                                try {
                                    persons.add(new Person().construct(result.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.d("size", String.valueOf(persons.size()));

                             serversListView.setAdapter(new PersonneAdapter(CommandeDetailActivityContext, persons, false, new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    final Person p = (Person) v.getTag();
                                    Log.d("Change server command",p.getNom());
                                    CommandeDetailFragment.myCommand.setServeur(p);
                                    HashMap<String,Object> commande =new HashMap<>();
                                    JSONArray commandItems = new JSONArray();
                                    for (Plats plat : CommandeDetailFragment.myCommand.getPlats())
                                    {
                                        commandItems.put(plat.getJsonIdOfPlat());
                                    }
                                    commande.put("items",commandItems);

                                    commande.put("server",CommandeDetailFragment.myCommand.getServeur().getJsonIdOfPerson());
                                    new CallAPI("http://95.142.161.35:8080/menu/"+CommandeDetailFragment.myCommand.getIdcommande(),new CallAPI.CallbackClass() {
                                        @Override
                                        public void postCall(JSONArray result) {

                                            Snackbar.make(v, "La commande est assignée à " + p.getPrenom()+" "+p.getNom(), 4000)
                                                    .setAction("Action", null).show();
                                            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                                            if (appBarLayout != null) {
                                                appBarLayout.setTitle(p.getPrenom()+" "+p.getNom());
                                            }

                                        }
                                    },commande,CommandeDetailActivityContext).execute("PUT");


                                }
                            }));


                        }
                    },new HashMap<String, Object>(),CommandeDetailActivity.this).execute("GET");
                }

            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(CommandeDetailFragment.ARG_ITEM_ID,
                    getIntent().getSerializableExtra(CommandeDetailFragment.ARG_ITEM_ID));
            CommandeDetailFragment fragment = new CommandeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.commande_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, CommandeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
