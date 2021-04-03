/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
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
        consultingClient.addnewconsult(myConsult);
        consultingClient.setStatus("Waiting");
        
        //Changer status employee et ajouter consultation dans sa liste
        workingEmployee.addnewconsult(myConsult);
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
    
    
    
}
