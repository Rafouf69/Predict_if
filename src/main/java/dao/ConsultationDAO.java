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
        Client consultingClient= myConsult.getClient();
        Medium consultedMedium= myConsult.getMedium();
        Employee workingEmployee= myConsult.getEmployee();
        
        consultingClient.addnewconsult(myConsult);
        workingEmployee.addnewconsult(myConsult);
        workingEmployee.setStatus("Waiting");
        consultedMedium.addnewconsult(myConsult);
       
        JpaUtil.obtenirContextePersistance().persist(myConsult);
        JpaUtil.obtenirContextePersistance().merge(workingEmployee);
        JpaUtil.obtenirContextePersistance().merge(consultedMedium);
        JpaUtil.obtenirContextePersistance().merge(consultingClient);
        
        return myConsult;
    }
    
    
    
}
