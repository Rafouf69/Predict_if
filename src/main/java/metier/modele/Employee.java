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
import java.util.List;
import javax.persistence.*;


@Entity
public class Employee implements Comparable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private String nom;
    private String prenom;
    private String genre;
    private String telephone;
    private String motDePasse;
    private String status;//enumeration
    
    @Column(nullable = false, unique = true)
    private String mail;
   
    @OneToMany(mappedBy="Employee")
    private List<Consultation> listConsult;
    
    public Employee(){    
    }
    public Employee(String nom,String prenom, String mail,String genre, String modDePasse, String telephone){
        this.nom=nom;
        this.genre=genre;
        this.prenom=prenom;
        this.mail=mail;
        this.motDePasse=modDePasse;
        this.telephone=telephone;
        this.status="free";
    }
    
    //Getters
    public Long getId(){
        return id;
    }
    public String getNom(){
        return nom;
    }
    public String getStatus(){
        return status;
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
    public List<Consultation> getList(){
        return listConsult;
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
    public void setStatus(String status){
        this.status= status;
    }
    public List<Consultation> addNewConsult(Consultation myConsulToAdd){
        this.listConsult.add(myConsulToAdd);
        return this.listConsult;
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Employee))
        {
            throw new NullPointerException("Un employee n'est comparable qu'avec un autre employee");
        }
        Employee e2 = (Employee) o;
        return this.listConsult.size() - e2.getList().size();
    }
     
   
}
