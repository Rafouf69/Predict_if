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
        
        JpaUtil.obtenirContextePersistance().persist(myConsult);
        return myConsult;
    }
    
    
    
}
