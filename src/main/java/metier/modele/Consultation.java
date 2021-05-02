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
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date askingDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date begginingDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    private String commentaire;
    private ConsultationStatus status;
    
    @ManyToOne
    private Employee employee;
    
    @ManyToOne
    private Medium medium;
    
    @ManyToOne
    private Client client;
   
    public Consultation(){    
    }
    public Consultation(Employee emp, Date askingDate, Client client, Medium medium ){
       this.client=client;
       this.medium=medium;
       this.employee=emp;
       this.askingDate=askingDate; 
       this.status=ConsultationStatus.WAITING;
       this.commentaire="aucun";
    }
    
    //Getters
    public Client getClient(){
        return this.client;
    }
    
    public Long getId(){
        return this.id;
    }
    public ConsultationStatus getStatus(){
        return this.status;
    }
    public Employee getEmployee(){
        return this.employee;
    }
    public Medium getMedium(){
        return this.medium;
    }
    public Date getEndDate()
    {
        return this.endDate;
    }
    public void setStatus(ConsultationStatus newStat){
        this.status=newStat;
    }
    public void setDateBegin(Date mydate){
        this.begginingDate=mydate;
    }
    public void setDateEnd(Date mydate){
        this.endDate=mydate;
    }
    public void setCommentaire(String comment){
        this.commentaire=comment;
    }
    
    @Override
    public String toString() {
        String beginningDateLocal = this.begginingDate==null ? "non fixé" : this.begginingDate.toString();
        String endDateLocal = this.endDate==null ? "non fixé" : this.endDate.toString();
        return "Consultation " + this.id + " : // Date de demande : " + this.askingDate+ " : // Date de début : " + beginningDateLocal + " : // Date de fin : " + endDateLocal + " // Commentaire : "+ this.commentaire +" // Employee : " +this.employee.getId()+  " // Medium : " + this.medium.getId()+ " // Client : "+ this.client.getId();
    }
}
    
    
   

