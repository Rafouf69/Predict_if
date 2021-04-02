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
import javax.persistence.*;


@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private String nom;
    private String prenom;
    private String genre; 
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    private int nbconsultation;
    
    private String telephone;
    
    private String motDePasse;
    
    public Employee(){    
    }
    public Employee(String nom,String prenom, String mail,String genre, String modDePasse, String telephone){
        this.nom=nom;
        this.genre=genre;
        this.prenom=prenom;
        this.mail=mail;
        this.motDePasse=modDePasse;
        this.telephone=telephone;
        this.nbconsultation=0;
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
