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

import java.util.List;
import javax.persistence.*;


@Entity
public class Medium {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy="Medium")
    private List<Consultation> listconsult;
    
    private String denomination;
    
    private String genre;
    
    private String presentation;
    
    private String support;
    
    private String formation;
    
    private String promotion;
    
    private int typeMedium;
    
    public Medium(){    
    }
    
    public Medium(String denomination,String genre, String presentation){
        this.denomination=denomination;
        this.genre=genre;
        this.presentation=presentation;
        this.typeMedium=1;
        this.support=null;
        this.formation=null;
        this.promotion=null;
    }
    
    public Medium(String denomination,String genre, String presentation, String support){
        this.denomination=denomination;
        this.genre=genre;
        this.presentation=presentation;
        this.typeMedium=2;
        this.support=support;
        this.formation=null;
        this.promotion=null;
    }
    
    public Medium(String denomination,String genre, String presentation, String formation, String promotion){
        this.denomination=denomination;
        this.genre=genre;
        this.presentation=presentation;
        this.typeMedium=1;
        this.support=null;
        this.formation=formation;
        this.promotion=promotion;
    }
    //Getters
    public Long getId(){
        return id;
    }
    
}

