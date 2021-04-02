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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import metier.services.AstroNetApi;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    
    private String nom;
    private String prenom;
    
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    
    private String telephone;
    
    private ArrayList<String> listeAstrale;
    
    private String motDePasse;
    
    public Client(){    
    }
    public Client(String nom,String prenom, String mail, Date date, String modDePasse){
        this.nom=nom;
        this.prenom=prenom;
        this.mail=mail;
        this.dateNaissance=date;
        this.motDePasse=modDePasse;
        AstroNetApi astroNetApi = new AstroNetApi();
        try {
            listeAstrale = (ArrayList<String>) astroNetApi.getProfil(prenom, date);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public String getTelephone(){
        return telephone;
    }
    
    public List<String> getListeAstrale(){
        return listeAstrale;
    }
    
    public Date getDateNaissance(){
        return dateNaissance;
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
    
}
