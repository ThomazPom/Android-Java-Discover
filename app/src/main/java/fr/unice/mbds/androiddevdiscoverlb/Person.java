package fr.unice.mbds.androiddevdiscoverlb;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import utils.CallAPI;

/**
 * Created by Clem on 21/10/2016.
 */
public class Person  implements Serializable{

    //public static HashMap<String,HashMap<String,String>> clients = new  HashMap<String,HashMap<String,String>>();
    private String email = "[NO EMAIL]";
    private Person createdBy = null;
    private String password = "";
    private String nom = "[User]";
    private String prenom = "[Deleted]";
    private String sexe = "[NOT SET]";
    private String telephone = "[NO PHONE]";
    private Boolean connected = false;
    private Boolean buzze = false;
    private String id = "";
    private transient JSONObject jsonOfPerson;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



   /*public static void createClientsDefault(){

        addClient("kevin@gmail.com"  ,"LAVOISIER"  , "Clément","Masculin" ,"0650686074","clem" );
        addClient("vaki@gmail.com","BENHAMOU","Thomas","Masculin","0650686074","Thom");

    }*/

    public Person(String email, String nom, String prenom, String sexe, String tel, String password, Person p) {

        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.telephone = tel;
        this.email = email;
        this.password = password;
        this.createdBy = p;


        // clients.put(mail,h);

    }


    public void pleaseComplete(final CallAPI.Callback callback) {
        final Person self = this;
        if (jsonOfPerson ==null || !jsonOfPerson.has("createdAt")) {

            new CallAPI("http://95.142.161.35:8080/person/" + this.id, new CallAPI.CallbackClass() {
                @Override
                public void postCall(JSONArray result) {
                    try {
                        self.construct(result.getJSONObject(0));
                        callback.postCall(self);
                    } catch (JSONException e) {
                    }
                }
            }, new HashMap<String, Object>(), null).execute("GET");

        }

    }
    public Person()
    {

    }
    public Person construct(JSONObject input) {
/*
    "prenom": "test",
    "nom": "test",
    "sexe": "f�minin",
    "telephone": "123",
    "email": "test@test.ru",
    "createdby": "IrinaSergei",
    "password": "4445",
    "connected": false,
    "createdAt": "2016-11-16T09:32:36.448Z",
    "updatedAt": "2016-11-16T09:32:36.448Z",
    "id": "582c27b4b549726b71281a52"
*/

        Log.d("JSON2PERSON", input.toString());
        setJsonOfPerson(input);


        try {
            this.id = jsonOfPerson.getString("id");
        } catch (JSONException e) {

            Log.d("jsonperson", "id ,no value");
            // e.printStackTrace();
        }

        if(!this.jsonOfPerson.has("createdAt"))
        {
            return this;
        }
            /*
        if (!jsonOfPerson.has("createdAt"))
        {
            try {
                jsonOfPerson = CallAPI.getDataFromURL("http://95.142.161.35:8080/person/"+this.id).getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
*/
        try {
            this.prenom = jsonOfPerson.getString("prenom");
        } catch (JSONException e) {
            Log.d("jsonperson", "prenom ,no value");
            //e.printStackTrace();
        }
        try {
            this.nom = jsonOfPerson.getString("nom");
        } catch (JSONException e) {

            Log.d("jsonperson", "nom ,no value");
            //  e.printStackTrace();
        }
        try {
            this.sexe = jsonOfPerson.getString("sexe");
        } catch (JSONException e) {

            Log.d("jsonperson", "sex ,no value");
            // e.printStackTrace();
        }

        try {
            this.email = jsonOfPerson.getString("email");
        } catch (JSONException e) {

            Log.d("jsonperson", "mail ,no value");
            //   e.printStackTrace();
        }
        try {
            this.telephone = jsonOfPerson.getString("telephone");
        } catch (JSONException e) {

            Log.d("jsonperson", "tel ,no value");
            // e.printStackTrace();
        }

        try {
            this.setConnected(jsonOfPerson.getBoolean("connected"));
        } catch (JSONException e) {

            Log.d("jsonperson", "connected ,no value");
            //  e.printStackTrace();
        }

        try {
            this.setConnected(jsonOfPerson.getBoolean("buzze"));
        } catch (JSONException e) {

            Log.d("buzze", "connected ,no value");
            //  e.printStackTrace();
        }
        try {
            this.id = jsonOfPerson.getString("id");
        } catch (JSONException e) {

            Log.d("jsonperson", "id ,no value");
            // e.printStackTrace();
        }
        try {
            this.password = jsonOfPerson.getString("password");
        } catch (JSONException e) {

            Log.d("jsonperson", "pw ,no value");
            //    e.printStackTrace();
        }
        // clients.put(mail,h);
        return this;
    }

    public Person(String email, String password) {

        this.email = email;
        this.password = password;

    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void delete(final Context context) {
        new CallAPI("http://95.142.161.35:8080/person/" + id, new CallAPI.CallbackClass() {
            @Override
            public void postCall(JSONArray result) {
                if (result != null) {

                    Toast.makeText(context, R.string.userdeleted, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }, new HashMap<String, Object>(), context).execute("DELETE");
    }

    public static void deleteAll(final Context context, List<Person> persons) {


        for (Person p : persons) {
            new CallAPI("http://95.142.161.35:8080/person/" + p.id, new CallAPI.CallbackClass() {
                @Override
                public void postCall(JSONArray result) {
                    if (result != null) {

                    }
                }
            }, new HashMap<String, Object>(), context).execute("DELETE");
        }
        Toast.makeText(context, R.string.alluserdeleted, Toast.LENGTH_LONG).show();
        return;
    }

    public Boolean getBuzze() {
        return buzze;
    }

    public void setBuzze(Boolean buzze) {
        this.buzze = buzze;
    }

    public JSONObject getJsonOfPerson() {
        return jsonOfPerson;
    }
    public JSONObject getJsonIdOfPerson() {
        JSONObject returnJson = new JSONObject();
        try {
            returnJson.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJson;
    }
    public void setJsonOfPerson(JSONObject jsonOfPerson) {
        this.jsonOfPerson = jsonOfPerson;
    }
}