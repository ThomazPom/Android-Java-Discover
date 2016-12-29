package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.unice.mbds.androiddevdiscoverlb.dummy.DummyContent;
import utils.CallAPI;
import utils.CommandeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * An activity representing a list of Commandes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CommandeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CommandeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (findViewById(R.id.commande_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        ListView recyclerView = (ListView) findViewById(R.id.command_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

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
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull final ListView recyclerView) {


        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        new CallAPI("http://95.142.161.35:8080/menu/", new CallAPI.CallbackClass() {
            @Override
            public void postCall(JSONArray jsonArray) {

                List<Commande> myCommands = new ArrayList<Commande>();
                Log.d("callpi", String.valueOf(jsonArray.length()));

                Log.d("callpi1", String.valueOf(myCommands.size()));
                for (int i =  0 ;i<jsonArray.length();i++)
                {
                    try {
                        //Commande.deleteCommande(jsonArray.getJSONObject(i).getString("id"));
                        myCommands.add(new Commande(jsonArray.getJSONObject(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d("callpi2", String.valueOf(myCommands.size()));
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                recyclerView.setAdapter(new CommandeAdapter(CommandeListActivity.this, myCommands,false, new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Log.d("event","Command clicked");
                                Commande tag = (Commande) v.getTag();
                                Intent intent = new Intent(CommandeListActivity.this,CommandeDetailActivity.class);
                                intent.putExtra(CommandeDetailFragment.ARG_ITEM_ID, tag );
                                startActivity(intent);
                            }
                        }
                        )
                );
            }
        },new HashMap<String, Object>(),CommandeListActivity.this).execute("GET");
    }

}
