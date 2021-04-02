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
    
    private String commentaire;
    
    @ManyToOne
    private Employee employee;
    
    @ManyToOne
    private Medium medium;
    
    @ManyToOne
    private Client client;
   
    private Date datedepart;
    private Date datefin;
    public Consultation(){    
    }
    public Consultation(Employee emp, Date datedeb, Client client, Medium medium ){
       this.client=client;
       this.medium=medium;
       this.employee=emp;
       this.datedepart=datedeb;
       
    }
   
   
}