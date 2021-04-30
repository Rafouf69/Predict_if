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
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
            Message.envoyerMail("contact@predict.if", newClient.getMail(), "Bienvenue chez PREDICT’IF", "Bonjour "+ newClient.getPrenom()+", nous vous confirmons votre inscription au service PREDICT’IF.Rendez-vous  vite  sur  notre  site  pour  consulter  votre profil  astrologique  et  profiter  des  dons incroyables de nos mediums.");
        }
        catch(Exception ex){
            //System.out.println("ERREUR: " + ex);
            JpaUtil.annulerTransaction();
            Message.envoyerMail("contact@predict.if", client.getMail(), "Echec de l’inscription chez PREDICT’IF", "Bonjour "+ client.getPrenom()+", votre inscription au service PREDICT’IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
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
            JpaUtil.annulerTransaction();
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
            JpaUtil.annulerTransaction();
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }     
        return newmed;
    }
     public Consultation DemandedeConsultation(long idclient,String mdp,long idmedium, Date date) throws Exception{
        
       
         //Etape 1: Retrouver le client demandé
         Client myclient;
         try {
               myclient=checkClientIdentity(idclient,mdp);
         }catch(Exception ex){
             throw ex;
         }
         
         //Etape 2:Vérifier que le client demandé n'a pas déja une consultation en attente
         if (!myclient.getStatus().equals("free")){
             throw new Exception("Sorry, " + myclient.getPrenom() +" "+ myclient.getNom() + " already have a consultation reserved");
         }
         
         
         //Etape 3: Retrouver le medium demandée
         Medium mymedium;
         try {
             mymedium= trouverMediumparId(idmedium);
         }catch(Exception ex){
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
                 //A faire: Algorithm pour trouver l'employee qui est le moins prix et pas le premier de la liste pour essayer de rééquilibrer le nombre d'apparition des demployee
                 Employee employeChose = Collections.min(MatchingEmployees);
                 
                 myConsult= new Consultation(employeChose,date,myclient, mymedium);
                 JpaUtil.creerContextePersistance();
                 JpaUtil.ouvrirTransaction();
                 ConsultationDAO myConsultationDAO= new ConsultationDAO();
                 myConsultationDAO.creer(myConsult);
                 JpaUtil.validerTransaction();
                 
             }catch(Exception Ex){
                  JpaUtil.annulerTransaction();
                  System.out.println("ERREUR creating consultation: " + Ex);
                 throw Ex;
             }
             finally{
                 JpaUtil.fermerContextePersistance();
             }
            
             
             
         }
         
         return myConsult;
     
     }
     public String checkWork(long EmpId, String mdp) throws Exception{
         
         String returningString=null;
         //Find Employeee concern
        
         
         Employee myemp=null;
         try {
             myemp=checkEmpIdentity(EmpId,mdp);
         }catch(Exception Ex){
             throw Ex;
         }
         
         //checkstatus
         if (myemp.getStatus().equals("free")){
             returningString="This is great: You have no work to do";
         }
         else if (myemp.getStatus().equals("consulting")){
             returningString="You cannot use this feature while you're with a client. Sorry. PLease end your consutation";
         }
         else{
             //get Lastelementof the list (should be a consultation waiting)
             Consultation waitingconsult= myemp.getList().get(myemp.getList().size()-1);
             
             //This should never happen with our logic. Just in case.
             if (!"Waiting".equals(waitingconsult.getStatus())){
                 throw new Exception("Hmmm An error Occurred. PLease contact Predictif");
             }
             returningString="It seems that you have one client waiting for you. Please begin the consultation n° " +waitingconsult.getId() +" with the client n° "+ waitingconsult.getClient().getId()+". You shoul incarn Medium "+waitingconsult.getMedium().getDenomination();
         }
         return returningString;
     }
     public String BegginingConsult(long EmpId, String mdp) throws Exception{
         
         String returningString=null;
         
         //Find Employeee concern
         Employee myemp=null;
         try {
             myemp=checkEmpIdentity(EmpId,mdp);
         }catch(Exception Ex){
             throw Ex;
         }
         //check employee is phoning
         if (!myemp.getStatus().equals("Waiting")){
             throw new Exception("You cannot begin a conversation because they are not conversation waitings for you");
         }
         //should never happen. just incase.
         if (!myemp.getList().get(myemp.getList().size()-1).getStatus().equals("Waiting")){
          throw new Exception("Hmmm An error occured. please call us.");
         }
         
          
         try{
             JpaUtil.creerContextePersistance();
             JpaUtil.ouvrirTransaction();
             ConsultationDAO myConsultationDAO= new ConsultationDAO();
             myConsultationDAO.beginconsult(myemp.getList().get(myemp.getList().size()-1));
             JpaUtil.validerTransaction();      
         }catch(Exception Ex){
             JpaUtil.annulerTransaction();
             System.out.println("ERREUR updating consultation: " + Ex);
             throw Ex;
         }finally{
             JpaUtil.fermerContextePersistance();
         }
         
         
         returningString="Conversation started!";

         return returningString;
     }
     public String EndingConsult(long EmpId, String mdp, String message) throws Exception{
         
         String returningString=null;
         
         //Find Employeee concern
        Employee myemp=null;
         try {
             myemp=checkEmpIdentity(EmpId,mdp);
         }catch(Exception Ex){
             throw Ex;
         }
         
         //check employee is phoning
         if (!myemp.getStatus().equals("Conversing")){
             throw new Exception("You cannot en a conversation if you are not consultating");
         }
         //should never happen. just incase.
         if (!myemp.getList().get(myemp.getList().size()-1).getStatus().equals("Running")){
          throw new Exception("Hmmm An error occured. please call us.");
         }
          
         try{
             JpaUtil.creerContextePersistance();
             JpaUtil.ouvrirTransaction();
             ConsultationDAO myConsultationDAO= new ConsultationDAO();
             myConsultationDAO.endconsult(myemp.getList().get(myemp.getList().size()-1), message);
             JpaUtil.validerTransaction();      
         }catch(Exception Ex){
             JpaUtil.annulerTransaction();
             System.out.println("ERREUR updating consultation: " + Ex);
             throw Ex;
         }finally{
             JpaUtil.fermerContextePersistance();
         }
         
         
         returningString="Conversation ended!";

         return returningString;
     }
     public List<String> AskingHelp(long Idemp, String mdp, int Amour, int Sante, int Travail) throws Exception{
         List<String> result=null;
         
         //Find Employeee concern
         Employee myemp=null;
         try {
             myemp=checkEmpIdentity(Idemp,mdp);
         }catch(Exception Ex){
             throw Ex;
         }
         //check currently conversing
         if (!"Conversing".equals(myemp.getStatus())){
            throw new Exception("This feature can only be called when conversing. Sorry.");
         }
         try{
             AstroNetApi astroNetApi = new AstroNetApi();
             System.out.println(myemp.getList().get(myemp.getList().size()-1).getClient().getCouleur());
             System.out.println(myemp.getList().get(myemp.getList().size()-1).getClient().getAnimalTotem());
             result=astroNetApi.getPredictions(myemp.getList().get(myemp.getList().size()-1).getClient().getCouleur(), myemp.getList().get(myemp.getList().size()-1).getClient().getAnimalTotem(), Amour, Sante, Travail);
             
         }catch(Exception Ex){
            throw Ex;
         }
         return result;
         
     }
     
    public void companyStats(long EmpId, String mdp) throws Exception
    {
        Employee myEmp=null;
        try {
            myEmp=checkEmpIdentity(EmpId,mdp);
        }catch(Exception Ex){
            throw Ex;
        }
        
        //top 3 des mediums choisis par les clients
        MediumDAO monMediumDAO= new MediumDAO();
        List<Medium> listeMedium=null;
        
        try{
            JpaUtil.creerContextePersistance();
            listeMedium = monMediumDAO.chercherTous();
            Collections.sort(listeMedium, new Medium());
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        System.out.println("-----Top 3 des mediums hoisis par les clients-----");
        System.out.println("medium numéro 1 : " + listeMedium.get(0).getDenomination());
        System.out.println("medium numéro 2 : " + listeMedium.get(1).getDenomination());
        System.out.println("medium numéro 3 : " + listeMedium.get(2).getDenomination());
        System.out.println("----------");
        
        //nombre de consultations par medium
        System.out.println("-----Nombre de consultation par medium-----");
        for (Medium listeMedium1 : listeMedium) {
            System.out.println(listeMedium1.getDenomination()+ " a " + listeMedium1.getConsultNumber() + " consultations");
        }
        System.out.println("----------");
        
        //répartition des clients par employé
        System.out.println("-----répartition des clients par employé-----");
        
        EmployeDAO monEmployeDAO= new EmployeDAO();
        List<Employee> listeEmployee=null;
        
        try{
            JpaUtil.creerContextePersistance();
            listeEmployee = monEmployeDAO.chercherTous();
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        
        for (Employee listeEmployee1 : listeEmployee) {
            List<Consultation> listeConsultation = listeEmployee1.getList();
            Set<Client> setClient = new HashSet<>();
            for(Consultation listeConsultation1 : listeConsultation)
            {
                setClient.add(listeConsultation1.getClient());
            }
            System.out.println(listeEmployee1.getNom() + " " +listeEmployee1.getPrenom() + " a " + setClient.size() + " clients uniques");
        }
    }
    public String EmployeeStats(long EmpId, String mdp) throws Exception
    {
        Employee myEmp=null;
        try {
            myEmp=checkEmpIdentity(EmpId,mdp);
        }catch(Exception Ex){
            throw Ex;
        }
        List<Consultation> emplistconsult= myEmp.getList();
        TreeMap <Client, Integer> mapclient = new TreeMap<Client,  Integer>();
        TreeMap <Medium, Integer> mapMedium = new TreeMap<Medium,  Integer>();
        
        for (Consultation consult : emplistconsult){
            //recuperer les mediums et client
            Client client= consult.getClient();
            Medium medium= consult.getMedium();
            
            //stocker dans une map avec l'objetc comme clefs et le nombre de consultation comme valeurs
            mapMedium.put(medium,( (mapMedium.get(medium)==null) ? 1 : (mapMedium.get(medium)+1)));
            mapclient.put(client,( (mapclient.get(client)==null) ? 1 : (mapclient.get(client)+1)));
   
        }
        //TEST
        Medium medium1= new Medium("A","B","C");
        Medium medium2= new Medium("B","C","D");
        mapMedium.put(medium1,((mapMedium.get(medium1)==null) ? 1 : (mapMedium.get(medium1)+1)));
        mapMedium.put(medium1,((mapMedium.get(medium1)==null) ? 1 : (mapMedium.get(medium1)+1)));
        mapMedium.put(medium1,((mapMedium.get(medium1)==null) ? 1 : (mapMedium.get(medium1)+1)));
        mapMedium.put(medium1,((mapMedium.get(medium1)==null) ? 1 : (mapMedium.get(medium1)+1)));
        mapMedium.put(medium2,((mapMedium.get(medium2)==null) ? 1 : (mapMedium.get(medium2)+1)));
        mapMedium.put(medium2,((mapMedium.get(medium2)==null) ? 1 : (mapMedium.get(medium2)+1)));
        mapMedium.put(medium2,((mapMedium.get(medium2)==null) ? 1 : (mapMedium.get(medium2)+1)));
        
       
        System.out.println(mapMedium);
        
        
        final Map<Medium, Integer> sortedByCount = mapMedium.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(sortedByCount);
        return sortedByCount.toString();
        
    }
     public void checkListConsultClient (long idclient) throws Exception{
         Client myclient;
         try {
             myclient= trouverClientparId(idclient);
         }catch(Exception ex){
             throw ex;
         };
         List<Consultation> myconsultList = myclient.getList();
         myconsultList.stream().forEach((consult)-> System.out.println(consult));
         
     }
     private Client trouverClientparId(Long id) throws Exception{
        ClientDAO monClientDAO= new ClientDAO();
        Client returningClient=null;
        try{
            JpaUtil.creerContextePersistance();
            returningClient= monClientDAO.chercherClientparID(id);
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }   
        if (returningClient==null){
            throw new Exception("Sorry that id does not match with any ClientId");
        }
        return returningClient;
     }
     private Employee trouverEmpparId(Long id) throws Exception{
        EmployeDAO monEmpDAO= new EmployeDAO();
        Employee emptoreturn= null;
        try{
            JpaUtil.creerContextePersistance();
            emptoreturn= monEmpDAO.chercherEmployeeparID(id);
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }   
        if (emptoreturn==null){
            throw new Exception("Sorry that id does not match with any EmployeeId");
        }
        return emptoreturn;
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
        if (mediumtoreturn==null){
            throw new Exception("Sorry that id does not match with any MediumId");
        }
        return mediumtoreturn;
    }
     
     private Consultation trouverConsultparId(Long id) throws Exception{
        ConsultationDAO monConsultDAO= new ConsultationDAO();
        Consultation consulttorretrun=null;
        try{
            JpaUtil.creerContextePersistance();
            consulttorretrun= monConsultDAO.chercherConsultparID(id);
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
        if (consulttorretrun==null){
            throw new Exception("Sorry that id does not match with any ConsultationId");
        }
        return consulttorretrun;
    }
     
      public Client AuthentifierClient(String mail, String mdp) throws Exception {
          
        ClientDAO monClientDAO= new ClientDAO();
        Client monclient;
        try{
            JpaUtil.creerContextePersistance();
            monclient= monClientDAO.authentifierClient(mail) ;
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
            
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        //Cas exceptionnele si client null
        if (monclient==null){
            throw new Exception("Aucun client n'est incrit avec cet email. Veuillez vérifier vos credential");
        }
        //verification de mot de passe
        if (!monclient.getMotDePasse().equals(mdp)){
             throw new Exception("Mauvais mot de passe. Veuillez vérifier vos credential");
        }
        
        return monclient;
        
        
    }
       public Employee AuthentifierEmployee(String mail, String mdp) throws Exception {
          
        EmployeDAO monEmpDAO= new EmployeDAO();
        Employee monEmp;
        try{
            JpaUtil.creerContextePersistance();
            monEmp= monEmpDAO.authentifierEmp(mail) ;
        }
        catch(Exception ex){
            System.out.println("ERREUR: " + ex);
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        //Cas exceptionnele si client null
        if (monEmp==null){
            throw new Exception("Aucun client n'est incrit avec cet email. Veuillez vérifier vos credential");
        }
        //verification de mot de passe
        if (!monEmp.getMotDePasse().equals(mdp)){
             throw new Exception("Mauvais mot de passe. Veuillez vérifier vos credential");
        }
        
        return monEmp;
        
        
    }
      private Client checkClientIdentity(long idclient, String mdp)throws Exception{
        Client myclient;
        //Etape 1: on récupère le client
         try {
             myclient= trouverClientparId(idclient);
         }catch(Exception ex){
             System.out.println("Ooops An error Ocured");
             throw ex;
         };
         
         //Etape 2: Verifier que le client existe: si on recherche un id qui existe pas cela ne renvoie pas une erreur: Nous devons la créer
         if (myclient==null){
             throw new Exception("This client do not exist. Please check Id");
         }
         //Etape 3: on vérifie le mdp
         if (!myclient.getMotDePasse().equals(mdp)){
            throw new Exception("Wrong Credentials. please make sure password is good");
         }
         //Etape 4: on vérifie le mdp
         return myclient;
      }
       private Employee checkEmpIdentity(long idEmp, String mdp)throws Exception{
        Employee myemp;
        //Etape 1: on récupère le client
         try {
             myemp= trouverEmpparId(idEmp);
         }catch(Exception ex){
             System.out.println("Ooops An error Ocured");
             throw ex;
         };
         
         //Etape 2: Verifier que le client existe: si on recherche un id qui existe pas cela ne renvoie pas une erreur: Nous devons la créer
         if (myemp==null){
             throw new Exception("This employee do not exist. Please check Id");
         }
         //Etape 3: on vérifie le mdp
         if (!myemp.getMotDePasse().equals(mdp)){
            throw new Exception("Wrong Credentials. please make sure password is good");
         }
         //Etape 4: on vérifie le mdp
         return myemp;
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
    
    
