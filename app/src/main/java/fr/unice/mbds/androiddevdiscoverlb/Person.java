package fr.unice.mbds.androiddevdiscoverlb;

import java.util.HashMap;

/**
 * Created by Clem on 21/10/2016.
 */
public class Person {

    //public static HashMap<String,HashMap<String,String>> clients = new  HashMap<String,HashMap<String,String>>();
    private String email;
    private Person createdBy;
    private String password;
    private String nom;
    private String prenom;
    private String sexe;
    private String telephone;

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

    public Person(String email, String nom, String prenom, String sexe, String tel, String password,Person p){

        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.telephone = tel;
        this.email = email;
        this.password = password;
        this.createdBy = p;


        // clients.put(mail,h);

    }

}