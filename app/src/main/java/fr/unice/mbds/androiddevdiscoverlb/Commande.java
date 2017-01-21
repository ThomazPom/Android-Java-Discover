package fr.unice.mbds.androiddevdiscoverlb;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import utils.CallAPI;

/**
 * Created by Thoma on 18/12/2016.
 */

public class Commande implements Serializable {
    private Person serveur;
    private List<Plats> plats;
    private String idcommande;
    public Commande(Person serveur, List<Plats> plats) {
        this.serveur = serveur;
        this.plats = plats;
    }



    public static void deleteCommande(final String idCommande)
    {
        new CallAPI("http://95.142.161.35:8080/menu/"+idCommande, new CallAPI.CallbackClass() {
            @Override
            public void postCall(JSONArray result) {
                Log.d("delcommande",idCommande);
            }
        },new HashMap<String, Object>(),null).execute("DELETE");
    }
    public Commande(JSONObject commande) {


        try {
            this.idcommande = (commande.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.serveur = new Person(commande.getJSONObject("server"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jarr = new JSONArray();
        try {
            jarr = commande.getJSONArray("items");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        plats = new ArrayList<Plats>();
        for (int i = 0;i<jarr.length();i++)
        {
            try {
                plats.add(new Plats(jarr.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public List<Plats> getPlats() {
        return plats;
    }

    public void setPlats(List<Plats> plats) {
        this.plats = plats;
    }

    public Person getServeur() {
        return serveur;
    }

    public void setServeur(Person serveur) {
        this.serveur = serveur;
    }

    public String getIdcommande() {
        return idcommande;
    }

    public void setIdcommande(String idcommande) {
        this.idcommande = idcommande;
    }
}
