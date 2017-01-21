package fr.unice.mbds.androiddevdiscoverlb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

import fr.unice.mbds.androiddevdiscoverlb.dummy.DummyContent;
import utils.CallAPI;
import utils.PlatAdapter;

/**
 * A fragment representing a single Commande detail screen.
 * This fragment is either contained in a {@link CommandeListActivity}
 * in two-pane mode (on tablets) or a {@link CommandeDetailActivity}
 * on handsets.
 */
public class CommandeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    public static Commande myCommand;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommandeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //          mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            myCommand = (Commande) getArguments().getSerializable(ARG_ITEM_ID);


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(myCommand.getServeur().getPrenom() + " " + myCommand.getServeur().getNom());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.commande_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (myCommand != null) {

            final PlatAdapter adapter = new PlatAdapter(this.getActivity().getApplicationContext(), myCommand.getPlats(), false, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            final FragmentActivity activity = this.getActivity();
            adapter.buttonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    HashMap<String, Object> commande = new HashMap<>();
                    JSONArray commandItems = new JSONArray();
                    for (Plats p : adapter.plats) {
                        commandItems.put(p.reConstructJson());
                    }
                    commande.put("items", commandItems);

                    commande.put("server", myCommand.getServeur().getJsonOfPerson());
                    new CallAPI("http://95.142.161.35:8080/menu/" + myCommand.getIdcommande(), new CallAPI.CallbackClass() {
                        @Override
                        public void postCall(JSONArray result) {

                            Snackbar.make(activity.findViewById(R.id.layout_commande_detail), "Plat retiré de la commande !", 4000)
                                    .setAction("Action", null).show();

                        }
                    }, commande, getActivity().getApplicationContext()).execute("PUT");
                }
            };

            ListView listePlatsCommande = (ListView) this.getActivity().findViewById(R.id.commande_plats_container);
            listePlatsCommande.setAdapter(adapter);
            // ((TextView) rootView.findViewById(R.id.commande_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
