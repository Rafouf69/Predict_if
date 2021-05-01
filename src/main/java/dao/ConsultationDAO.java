/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.util.Date;
import metier.modele.*;
/**
 *
 * @author louislombard
 */
public class ConsultationDAO {
    
    public Consultation creer(Consultation myConsult) {
        
        //obtenir les élements important de la consultation (clefs étrangères) 
        Client consultingClient= myConsult.getClient();
        Medium consultedMedium= myConsult.getMedium();
        Employee workingEmployee= myConsult.getEmployee();
        
        //Changer status client et ajouter consultation dans sa liste
        consultingClient.addNewConsult(myConsult);
        consultingClient.setStatus("Waiting");
        
        //Changer status employee et ajouter consultation dans sa liste
        workingEmployee.addNewConsult(myConsult);
        workingEmployee.setStatus("Waiting");
        
        //ajouter consultation dans la liste de consultaiton du medium
        consultedMedium.addnewconsult(myConsult);
       
        //Sauvegarder changement
        JpaUtil.obtenirContextePersistance().persist(myConsult);
        JpaUtil.obtenirContextePersistance().merge(workingEmployee);
        JpaUtil.obtenirContextePersistance().merge(consultedMedium);
        JpaUtil.obtenirContextePersistance().merge(consultingClient);
        
        return myConsult;
    }
    
    public Consultation modifier(Consultation consultation)
    {
        return JpaUtil.obtenirContextePersistance().merge(consultation);
    }
    
    public Consultation beginConsult(Consultation myConsult) {
        
        //obtenir les élements important de la consultation (clefs étrangères) 
        Client consultingClient= myConsult.getClient();
        Employee workingEmployee= myConsult.getEmployee();
        
        //Changer status client
        consultingClient.setStatus("Conversing");
        
        //Changer status employee
        workingEmployee.setStatus("Conversing");
        
         //Changer status conversation
        myConsult.setStatus("Running");
        myConsult.setDateBegin(new Date());
        
       
        //Sauvegarder changement
        JpaUtil.obtenirContextePersistance().merge(myConsult);
        JpaUtil.obtenirContextePersistance().merge(workingEmployee);
        JpaUtil.obtenirContextePersistance().merge(consultingClient);
        
        return myConsult;
    }
    public Consultation endConsult(Consultation myConsult, String Message) {
        
        //obtenir les élements important de la consultation (clefs étrangères) 
        Client consultingClient= myConsult.getClient();
        Employee workingEmployee= myConsult.getEmployee();
        
        //Changer status client
        consultingClient.setStatus("free");
        
        //Changer status employee
        workingEmployee.setStatus("free");
        
         //Changer status conversation
        myConsult.setStatus("Finish");
        myConsult.setDateEnd(new Date());
        myConsult.setCommentaire(Message);
        
       
        //Sauvegarder changement
        JpaUtil.obtenirContextePersistance().merge(myConsult);
        JpaUtil.obtenirContextePersistance().merge(workingEmployee);
        JpaUtil.obtenirContextePersistance().merge(consultingClient);
        
        return myConsult;
    }
    public Consultation chercherConsultparID(Long Id) {
        return JpaUtil.obtenirContextePersistance().find(Consultation.class, Id);
        
    }
    
    
    
}
