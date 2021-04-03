/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.services;

import dao.ConsultationDAO;
import dao.EmployeDAO;
import dao.ClientDAO;
import dao.MediumDAO;
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
    public Client creerClient(Client client) throws Exception{
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
            throw ex;
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
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }     
        return newClient;
    }
    public Employee creerEmployee(Employee Employee) throws Exception{
        EmployeDAO monEmpDAO= new EmployeDAO();
        Employee newEmp;
        
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newEmp= monEmpDAO.creer(Employee);
            JpaUtil.validerTransaction();
            Message.envoyerMail("contact@predict.if", Employee.getMail(), "Bienvenue chez PREDICT’IF", "Bonjour "+ Employee.getPrenom()+", nous vous confirmons votre inscription au service PREDICT’IF.Rendez-vous  vite  sur  notre  site  pour  consulter  votre profil  astrologique  et  profiter  des  dons incroyables de nos mediums.");
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            Message.envoyerMail("contact@predict.if", Employee.getMail(), "Echec de l’inscription chez PREDICT’IF", "Bonjour "+ Employee.getPrenom()+", votre inscription au service PREDICT’IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }     
        return newEmp;
    }
    public Medium creerMedium(Medium newmedium) throws Exception{
        MediumDAO monMedDAO= new MediumDAO();
        Medium newmed;
        
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newmed= monMedDAO.creer(newmedium);
            JpaUtil.validerTransaction();
            
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }     
        return newmed;
    }
     public Consultation DemandedeConsultation(long idclient,long idmedium, Date date) throws Exception{
        
         //Etape 1: Retrouver le client demandé
         Client myclient;
         try {
             myclient= trouverClientparId(idclient);
         }catch(Exception ex){
             System.out.println("Desolé, cette utilisateur n'existe pas");
             throw ex;
         };
         //Etape 1:Vérifier que le client demandé n'a pas déja une consultation en attente
         if (myclient.getStatus()!="free"){
             throw new Exception("Sorry, " + myclient.getPrenom() +" "+ myclient.getNom() + " already have a consultation reserved");
         }
         
         
         //Etape 3: Retrouver le medium demandée
         Medium mymedium;
         try {
             mymedium= trouverMediumparId(idmedium);
         }catch(Exception ex){
             System.out.println("Desolé, ce medium n'existe pas");
             throw ex;
         };
         
         //Etape 4: Trouver la liste des employée pouvant executer le role
         List<Employee> MatchingEmployees;
         Consultation myConsult;
         try{
             JpaUtil.creerContextePersistance();
             EmployeDAO myDAOemp= new EmployeDAO();
             MatchingEmployees=myDAOemp.chercherEmployeDispo(mymedium.getGender());
         }catch(Exception Ex){
            System.out.println("ERREUR DAO.EMPLOYEE.chercherEmployedispo: " + Ex);
            throw Ex;
         }
         finally{
            JpaUtil.fermerContextePersistance();
         }
         
         //Etape 5: Vérifier que la liste d'employée pouvant faire le role n'est pas vide
         if (MatchingEmployees.isEmpty()){
             throw new Exception("Sorry, " + mymedium.getDenomination() + " is unavailable for the moment, please come back later");
         }
         else{
             //Etape 6: créer la consultation
             try{
                 Employee employeChose = MatchingEmployees.get(0); //A faire: Algorithm pour trouver l'employee qui est le moins prix et pas le premier de la liste pour essayer de rééquilibrer le nombre d'apparition des demployee
                 myConsult= new Consultation(employeChose,date,myclient, mymedium);
                 JpaUtil.creerContextePersistance();
                 JpaUtil.ouvrirTransaction();
                 ConsultationDAO myConsultationDAO= new ConsultationDAO();
                 myConsultationDAO.creer(myConsult);
                 JpaUtil.validerTransaction();
                 
             }catch(Exception Ex){
                  System.out.println("ERREUR creating consultation: " + Ex);
                 throw Ex;
             }
             finally{
                 JpaUtil.fermerContextePersistance();
             }
            
             
             
         }
         
         return myConsult;
     
     }
     
     public void checkListConsultClient (long idclient) throws Exception{
         Client myclient;
         try {
             myclient= trouverClientparId(idclient);
         }catch(Exception ex){
             System.out.println("Desolé, cette utilisateur n'existe pas");
             throw ex;
         };
         List<Consultation> myconsultList = myclient.getList();
         myconsultList.stream().forEach((consult)-> System.out.println(consult));
         
     }
     private Client trouverClientparId(Long id) throws Exception{
        ClientDAO monClientDAO= new ClientDAO();
        try{
            JpaUtil.creerContextePersistance();
            return monClientDAO.chercherClientparID(id);
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
    }
     
     private Medium trouverMediumparId(Long id) throws Exception{
        MediumDAO monMediumDAO= new MediumDAO();
        Medium mediumtoreturn=null;
        try{
            JpaUtil.creerContextePersistance();
            mediumtoreturn= monMediumDAO.chercherMediumparID(id);
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
        return mediumtoreturn;
    }
     
     //never used (maybe one day?)
      public Client Authentifier(String mail, String mdp) {
        ClientDAO monClientDAO= new ClientDAO();
        try{
            JpaUtil.creerContextePersistance();
            return monClientDAO.authentifierClient(mail, mdp) ;
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            return null;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        
    }
      
      //never used (maybe one day?)
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
    
    
