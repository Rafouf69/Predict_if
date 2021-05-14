/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author louislombard
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author louislombard
 */
package metier.modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import metier.services.AstroNetApi;
import javax.persistence.*;


@Entity
public class Client {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    
    private String nom;
    private String prenom;
    private String telephone;
    private String zodiaque;
    private String signeChinois;
    private String couleur; 
    private String animalTotem;
    private String motDePasse;
    private Status status;
    
    @OneToMany(mappedBy="Client")
    private List<Consultation> listConsult;
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    
    public Client(){    
    }
    public Client(String nom,String prenom, String mail, Date date, String motDePasse, String telephone)throws Exception{
        this.nom=nom;
        this.prenom=prenom;
        this.mail=mail;
        this.dateNaissance=date;
        this.motDePasse=motDePasse;
        this.telephone=telephone;
        this.status=Status.FREE;
        try{
            ArrayList<String>  listeAstrale = (ArrayList<String>) new AstroNetApi().getProfil(prenom, date);
            this.zodiaque=listeAstrale.get(0);
            this.signeChinois=listeAstrale.get(1);
            this.couleur=listeAstrale.get(2);
            this.animalTotem=listeAstrale.get(3);
        }catch(Exception ex){
            throw new Exception("Error with AstroNetApi");
        }
    }
    
    //Getters
    public Long getId(){
        return id;
    }
    
    public String getNom(){
        return nom;
    }
    public String getPrenom(){
        return prenom;
    }
    public String getMail(){
        return mail;
    }
    public String getMotDePasse(){
        return motDePasse;
    }
    
    public Date getDate(){
        return dateNaissance;
    }
    
    public String getTelephone(){
        return telephone;
    }
    
    public String getZodiaque(){
        return zodiaque;
    }
    
    public String getSigneChinois(){
        return signeChinois;
    }
    
    public String getCouleur(){
        return couleur;
    }
    
    public String getAnimalTotem(){
        return animalTotem;
    }
    public Date getDateNaissance(){
        return dateNaissance;
    }
    public Status getStatus(){
        return status;
    }
    public List<Consultation>getList(){
        return this.listConsult;
    }
    //Setters
    public void setNom(String nom){
        this.nom= nom;
    }
    public void setPrenom(String prenom){
        this.prenom= prenom;
    }
    public void setMail(String mail){
        this.mail= mail;
    }
    public void setMotDePasse(String motDePasse){
        this.motDePasse= motDePasse;
    }
    public void setZodiaque(String zodiaque){
        this.zodiaque=zodiaque;
    }    
    public void setSigneChinois(String signeChinois){
        this.signeChinois=signeChinois;
    } 
    public void setCouleur(String couleur){
        this.couleur=couleur;
    }
    public void setAnimalTotem(String animalTotem){
        this.animalTotem=animalTotem;
    }
    public void setStatus(Status status){
        this.status=status;
    }
    public List<Consultation> addNewConsult(Consultation myConsulToAdd){
        this.listConsult.add(myConsulToAdd);
        return this.listConsult;
    }
    
    @Override
    public String toString()
    {
        String s = "";
        s += "Chèr(e)" + prenom + " " + nom + "\n";
        s += "Votre adresse e-mail : " + mail + "\n";
        s += "Votre signe du zodiaque : " + zodiaque + "\n";
        s += "Votre signe du zodiaque chinois : " + signeChinois + "\n";
        s += "Votre couleur porte-bonheur : " + couleur + "\n";
        s += "Votre animal totem : " + animalTotem + "\n\n";
/*
        if(listConsult.size() > 0)
        {
            s += "Vos dernières consultations :\n";
            for(Consultation c : listConsult)
            {
                s += c.getEndDate().toString() + " - " + c.getMedium().getDenomination() + "\n";
            }
            s += "\n";
        }
*/
        
        s += "Merci d'utiliser Predict\'IF ;)\n"; 
        s += "\n";
        
        return s;
    }
}
