/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.services;

import dao.ClientDAO;
import dao.JpaUtil;
import java.util.List;
import metier.modele.Client;


/**
 *
 * @author louislombard
 */
public class ServiceClient {
    public Client creer(Client client) {
        ClientDAO monClientDAO= new ClientDAO();
        Client newclient;
        try{
            
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newclient= monClientDAO.creer(client);
            JpaUtil.validerTransaction();
            
            
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            newclient=null;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
        
        return newclient;
    }
    
     public Client trouverClientparId(Long id) {
        ClientDAO monClientDAO= new ClientDAO();
        try{
            JpaUtil.creerContextePersistance();
            return monClientDAO.chercherClientparID(id);
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            return null;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
    }
      public Client Authentifier(String mail, String mdp) {
        ClientDAO monClientDAO= new ClientDAO();
        try{
            JpaUtil.creerContextePersistance();
            return monClientDAO.chercherClient(mail, mdp) ;
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            return null;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        
    }
      public List<Client> ListeClients() {
        ClientDAO monClientDAO= new ClientDAO();
        try{
            JpaUtil.creerContextePersistance();
            return monClientDAO.chercherTous() ;
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            return null;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
      }

      
}
    
    
