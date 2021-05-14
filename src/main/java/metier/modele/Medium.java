/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

/**
 *
 * @author vigno
 */

import java.util.Comparator;
import java.util.List;
import javax.persistence.*;


@Entity
public class Medium implements Comparator<Medium> {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy="Medium")
    private List<Consultation> listconsult;
    
    @Column(nullable = false, unique = true)
    private String denomination;
   
    
    private String genre;
    
    private String presentation;
    
    private String support;
    
    private String formation;
    
    private String promotion;
    
    private String typeMedium;
    
    public Medium(){    
    }
    
    public Medium(String denomination,String genre, String presentation){
        this.denomination=denomination;
        this.genre=genre;
        this.presentation=presentation;
        this.typeMedium="Cartomancien";
        this.support=null;
        this.formation=null;
        this.promotion=null;
    }
    
    public Medium(String denomination,String genre, String presentation, String support){
        this.denomination=denomination;
        this.genre=genre;
        this.presentation=presentation;
        this.typeMedium="Spirite";
        this.support=support;
        this.formation=null;
        this.promotion=null;
    }
    
    public Medium(String denomination,String genre, String presentation, String formation, String promotion){
        this.denomination=denomination;
        this.genre=genre;
        this.presentation=presentation;
        this.typeMedium="Astrologue";
        this.support=null;
        this.formation=formation;
        this.promotion=promotion;
    }
    //Getters
    public Long getId(){
        return id;
    }
    public String getType(){
        return typeMedium;
    }
    public String getSupport(){
        return support;
    }
    public String getformation(){
        return formation;
    }
    public String getpresentation(){
        return presentation;
    }
    public String getpromotion(){
        return promotion;
    }
    public String getGender(){
        return this.genre;
    }
    public String getDenomination(){
        return this.denomination;
    }
    
    public int getConsultNumber()
    {
        return listconsult.size();
    }
    public List<Consultation> addnewconsult(Consultation myconsulToAdd){
        this.listconsult.add(myconsulToAdd);
        return this.listconsult;
    }

    @Override
    public int compare(Medium o1, Medium o2) {
        return o2.listconsult.size() - o1.listconsult.size();
    }
    
    @Override
    public String toString()
    {
        String newLine = System.getProperty("line.separator");
        String s = "Id du médium : "+id+newLine+"Denomination : "+denomination+newLine+"Type de Medium : "+typeMedium+newLine+"Genre : "+genre;
        switch(typeMedium)
        {
            case "Spirite":
                s=s+newLine+"Support : "+support;
                break;
            case "Astrologue":
                s=s+newLine+"formation : "+formation+newLine+"Promotion : "+promotion;
        }
        return s+newLine+"Presentation : "+presentation+newLine;
    }
}

