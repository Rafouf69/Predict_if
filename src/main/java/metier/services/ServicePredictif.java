/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.services;

import dao.EmployeDAO;
import dao.ClientDAO;
import dao.JpaUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.*;


/**
 *
 * @author louislombard
 */
public class ServicePredictif {
    public Client creer(Client client) {
        ClientDAO monClientDAO= new ClientDAO();
        Client newClient;
        
        Message envoyer = new Message();
        
        ArrayList<String> listeAstrale;
        AstroNetApi astroNetApi = new AstroNetApi();
        
        try {
            listeAstrale = (ArrayList<String>) astroNetApi.getProfil(client.getPrenom(), client.getDate());
            client.setZodiaque(listeAstrale.get(0));
            client.setSigneChinois(listeAstrale.get(1));
            client.setCouleur(listeAstrale.get(2));
            client.setAnimalTotem(listeAstrale.get(3));
        } catch (IOException ex) {
            System.out.println("Error creating liste Astral");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newClient= monClientDAO.creer(client);
            JpaUtil.validerTransaction();
            envoyer.envoyerMail("contact@predict.if", newClient.getMail(), "Bienvenue chez PREDICT’IF", "Bonjour "+ newClient.getPrenom()+", nous vous confirmons votre inscription au service PREDICT’IF.Rendez-vous  vite  sur  notre  site  pour  consulter  votre profil  astrologique  et  profiter  des  dons incroyables de nos mediums.");
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            envoyer.envoyerMail("contact@predict.if", client.getMail(), "Echec de l’inscription chez PREDICT’IF", "Bonjour "+ client.getPrenom()+", votre inscription au service PREDICT’IF a malencontreusement échoué... Merci de recommencer ultérieurement.");

            newClient=null;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
        
        
        return newClient;
    }
     public Consultation DemandedeConsultation(Client client,Medium medium, Date date) throws Exception{
         EmployeDAO myDAOemp= new EmployeDAO();
         List<Employee> MatchingEmployees;
         try{
             MatchingEmployees=myDAOemp.chercherEmployeDispo(medium.getGender());
         }catch(Exception Ex){
            System.out.println("ERREUR DAO.EMPLOYEE.chercherEmployedispo: " + Ex);
            return null;
         }
         
         if (MatchingEmployees.isEmpty()){
             throw new Exception("Sorry, " + medium.getDenomination() + " is unavailable for the moment, please come back later");
         }
         else{
             Employee employeChose = MatchingEmployees.get(0); //A faire: Algorithm pour trouver l'employee qui est le moins prix et pas le premier de la liste pour essayer de rééquilibrer le nombre d'apparition des demployee
             
         }
         
         return null;
     
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
    
    
