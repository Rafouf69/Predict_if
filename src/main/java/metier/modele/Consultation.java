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

import java.util.Date;

@Entity
public class Consultation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private Date askingdate;
    private Date begginignedate;
    private Date enddate;
    private String commentaire;
    private String status;
    
    @ManyToOne
    private Employee employee;
    
    @ManyToOne
    private Medium medium;
    
    @ManyToOne
    private Client client;
   
    public Consultation(){    
    }
    public Consultation(Employee emp, Date askingdate, Client client, Medium medium ){
       this.client=client;
       this.medium=medium;
       this.employee=emp;
       this.askingdate=askingdate; 
       this.status="Waiting";
    }
    
    //Getters
    public Client getClient(){
        return this.client;
    }
    
    public Long getId(){
        return this.id;
    }
    public String getStatus(){
        return this.status;
    }
    public Employee getEmployee(){
        return this.employee;
    }
    public Medium getMedium(){
        return this.medium;
    }
    public void setStatus(String newStat){
        this.status=newStat;
    }
    public void setDateBegin(Date mydate){
        this.begginignedate=mydate;
    }
    public void setDateEnd(Date mydate){
        this.enddate=mydate;
    }
    public void setCommentaire(String comment){
        this.commentaire=comment;
    }
    public String toString() {
        return "Consultation " + this.id + " : // Date de demande : " + this.askingdate+ " : // Date de début : " + this.begginignedate+ " : // Date de fin : " + this.enddate + " // Commentaire : "+ this.commentaire +" // Employee : " +this.employee.getId()+  " // Medium : " + this.medium.getId()+ " // Client : "+ this.client.getId();
    }
}
    
    
   

