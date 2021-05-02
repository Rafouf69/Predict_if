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
import java.util.Arrays;   
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.*;


/**
 *
 * @author louislombard
 */
public class ServicePredictif {
   
    public Client inscrireClient(Client client) throws Exception{
        
        ClientDAO monClientDAO= new ClientDAO();
        Client newClient;
        AstroNetApi astroNetApi = new AstroNetApi();
  
        try{
            ArrayList<String>  listeAstrale = (ArrayList<String>) astroNetApi.getProfil(client.getPrenom(), client.getDate());
            client.setZodiaque(listeAstrale.get(0));
            client.setSigneChinois(listeAstrale.get(1));
            client.setCouleur(listeAstrale.get(2));
            client.setAnimalTotem(listeAstrale.get(3));
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newClient= monClientDAO.creer(client);
            JpaUtil.validerTransaction();
            Message.envoyerMail("contact@predict.if", newClient.getMail(), "Bienvenue chez PREDICT’IF", "Bonjour "+ newClient.getPrenom()+", nous vous confirmons votre inscription au service PREDICT’IF.Rendez-vous  vite  sur  notre  site  pour  consulter  votre profil  astrologique  et  profiter  des  dons incroyables de nos mediums.");
        }
        catch(Exception ex){
            //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            JpaUtil.annulerTransaction();
            Message.envoyerMail("contact@predict.if", client.getMail(), "Echec de l’inscription chez PREDICT’IF", "Bonjour "+ client.getPrenom()+", votre inscription au service PREDICT’IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }     
        
        return newClient;
    }
    
    public Employee creerEmployee(Employee employee) throws Exception{
        
        EmployeDAO monEmpDAO= new EmployeDAO();
        Employee newEmp;
        
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newEmp= monEmpDAO.creer(employee);
            JpaUtil.validerTransaction();
            Message.envoyerMail("contact@predict.if", employee.getMail(), "Bienvenue chez PREDICT’IF", "Bonjour "+ employee.getPrenom()+", nous vous confirmons votre inscription au service PREDICT’IF.Rendez-vous  vite  sur  notre  site  pour  consulter  votre profil  astrologique  et  profiter  des  dons incroyables de nos mediums.");
        }
        catch(Exception ex){
             //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            JpaUtil.annulerTransaction();
            Message.envoyerMail("contact@predict.if", employee.getMail(), "Echec de l’inscription chez PREDICT’IF", "Bonjour "+ employee.getPrenom()+", votre inscription au service PREDICT’IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }    
        
        return newEmp;
    }
    public Medium creerMedium(Medium newMedium) throws Exception{
        MediumDAO monMedDAO= new MediumDAO();
        Medium newMed;
        
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            newMed = monMedDAO.creer(newMedium);
            JpaUtil.validerTransaction();
            
        }
        catch(Exception ex){
             //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            JpaUtil.annulerTransaction();
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }     
        
        return newMed;
    }

    public Consultation DemandeDeConsultation(Client myClient,Long idmedium, Date date) throws Exception{
             
        List<Employee> matchingEmployees;
        Employee employeChoisi ;
        Consultation myConsult;
        Medium mymedium;
       
        
        //Etape 1: Vérifier que le client demandé n'a pas déja une consultation en attente
        if (!myClient.getStatus().equals(Status.FREE)){
            throw new Exception("Sorry, " + myClient.getPrenom() +" "+ myClient.getNom() + " already have a consultation reserved");
        }
        
         
        //Etape 2: Retrouver le medium demandée
        try {
            mymedium= trouverMediumParId(idmedium);
        }catch(Exception ex){
             //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            throw ex;
        }
         
         
         //Un seul try catch ou lieu de plusieurs ????
         
         try{
             //Etape 3: Assigner le bon employé
             JpaUtil.creerContextePersistance();
             JpaUtil.ouvrirTransaction();
             
             EmployeDAO myDAOemp= new EmployeDAO();
             ConsultationDAO myConsultationDAO= new ConsultationDAO();
             ClientDAO myClientDAO= new ClientDAO();
             MediumDAO myMediumDAO= new MediumDAO();
             
             matchingEmployees=myDAOemp.chercherEmployeDispo(mymedium.getGender()); //trouver les potentiels employées correspondant
             
             if (matchingEmployees.isEmpty()){
                 
                throw new Exception("Sorry, " + mymedium.getDenomination() + " is unavailable for the moment, please come back later"); //Dans le cas ou aucun employé est possible
                
             }else{
                 
                employeChoisi = Collections.min(matchingEmployees); //prendre l'employée le plus faible
           
                myConsult= new Consultation(employeChoisi,date,myClient, mymedium);
                 
                //changer le status de objets concernés et ajouter objet concernés
                employeChoisi.setStatus(Status.WAITING);
                myClient.setStatus(Status.WAITING);
                employeChoisi.addNewConsult(myConsult);
                myClient.addNewConsult(myConsult);
                mymedium.addnewconsult(myConsult);
                
                //Sauvegarder changement et modifications dans BD
                myMediumDAO.modifier(mymedium);
                myClientDAO.modifier(myClient);
                myDAOemp.modifier(employeChoisi);
                
                JpaUtil.validerTransaction();
             }     
         }catch(Exception ex){
            //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            JpaUtil.annulerTransaction();
            throw ex;   
         }
         finally{
            JpaUtil.fermerContextePersistance();
         }
         
       
         
        return myConsult;
     
     }
    
    public String checkWork(Employee myEmp) throws Exception{
         
        String returningString;
        
        //checkstatus
        if (myEmp.getStatus().equals(Status.FREE)){
            returningString="This is great: You have no work to do";
        }
        else if (myEmp.getStatus().equals(Status.CONVERSING)){
            returningString="You cannot use this feature while you're with a client. Sorry. PLease end your consutation";
        }
        else{
            //get Lastelementof the list (should be a consultation waiting)
            Consultation waitingconsult= myEmp.getList().get(myEmp.getList().size()-1);
            
            //This should never happen with our logic. Just in case.
            if (!"Waiting".equals(waitingconsult.getStatus())){
                throw new Exception("Hmmm An error Occurred. PLease contact Predictif");
            }
            returningString="It seems that you have one client waiting for you. Please begin the consultation n° " +waitingconsult.getId() +" with the client n° "+ waitingconsult.getClient().getId()+". You shoul incarn Medium "+waitingconsult.getMedium().getDenomination();
        }
        return returningString;
    }
    
    public String begginingConsult(Employee myEmp) throws Exception{
        
        //check employee is phoning
        if (!myEmp.getStatus().equals(Status.WAITING)){
            throw new Exception("You cannot begin a conversation because they are not conversation waitings for you");
        }
        
        //should never happen. just incase.
        if (!myEmp.getList().get(myEmp.getList().size()-1).getStatus().equals(Status.WAITING)){
            throw new Exception("Hmmm An error occured. please call us.");
        }   
          
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            ConsultationDAO myConsultationDAO= new ConsultationDAO();
            EmployeDAO myEmpDAO= new EmployeDAO();
            ClientDAO myClientDAO= new ClientDAO();
            
            Consultation consultToChange= myEmp.getList().get(myEmp.getList().size()-1);
            Client clientToChange=consultToChange.getClient();
            
            myEmp.setStatus(Status.CONVERSING);
            clientToChange.setStatus(Status.CONVERSING);
            consultToChange.setStatus(ConsultationStatus.RUNNING);
            consultToChange.setDateBegin(new Date());
            
            myConsultationDAO.modifier(consultToChange);
            myEmpDAO.modifier(myEmp);
            myClientDAO.modifier(clientToChange);
            
            JpaUtil.validerTransaction();      
        }catch(Exception Ex){
            //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            JpaUtil.annulerTransaction();
            throw Ex;
        }finally{
            JpaUtil.fermerContextePersistance();
        }

        String returningString="Conversation started!";
        return returningString;
     }
    public String endingConsult(Employee myEmp, String message) throws Exception{
        
        //check employee is phoning
        if (!myEmp.getStatus().equals(Status.CONVERSING)){
            throw new Exception("You cannot en a conversation if you are not consultating");
        }
        //should never happen. just incase.
        if (!myEmp.getList().get(myEmp.getList().size()-1).getStatus().equals("Running")){
            throw new Exception("Hmmm An error occured. please call us.");
        }
         
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            ConsultationDAO myConsultationDAO= new ConsultationDAO();
            EmployeDAO myEmpDAO= new EmployeDAO();
            ClientDAO myClientDAO= new ClientDAO();
            
            Consultation consultToChange= myEmp.getList().get(myEmp.getList().size()-1);
            Client clientToChange=consultToChange.getClient();
            
            myEmp.setStatus(Status.FREE);
            clientToChange.setStatus(Status.FREE);
            consultToChange.setStatus(ConsultationStatus.DONE);
            consultToChange.setDateEnd(new Date());
            consultToChange.setCommentaire(message);
            
            myConsultationDAO.modifier(consultToChange);
            myEmpDAO.modifier(myEmp);
            myClientDAO.modifier(clientToChange);
            
            JpaUtil.validerTransaction();      
        }catch(Exception ex){
            //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
            JpaUtil.annulerTransaction();
            throw ex;
        }finally{
            JpaUtil.fermerContextePersistance();
        }
         
         
        String returningString="Conversation ended!";

        return returningString;
     }
     
    public List<String> askingHelp(Employee myEmp, int amour, int sante, int travail) throws Exception{
        
        List<String> result=null;
        
        //check currently conversing
        if (!Status.CONVERSING.equals(myEmp.getStatus())){
           throw new Exception("This feature can only be called when conversing. Sorry.");
        }
        
        try{
            AstroNetApi astroNetApi = new AstroNetApi();
            result=astroNetApi.getPredictions(myEmp.getList().get(myEmp.getList().size()-1).getClient().getCouleur(), myEmp.getList().get(myEmp.getList().size()-1).getClient().getAnimalTotem(), amour, sante, travail);       
        }catch(Exception ex){
            //Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex); //in debug mode
           throw ex;
        }
        return result;
         
    }
     
    public ArrayList<List> companyStats() throws Exception
    {    
        MediumDAO monMediumDAO= new MediumDAO();
        List<Medium> listeMedium=new ArrayList<>();
        
        EmployeDAO monEmployeDAO= new EmployeDAO();
        List<Employee> listeEmployee= new ArrayList<>();
        
        try{
            JpaUtil.creerContextePersistance();
            listeMedium = monMediumDAO.chercherTous();
            
            Collections.sort(listeMedium, new Medium());
            listeEmployee = monEmployeDAO.chercherTous();
        }
        catch(Exception ex){
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        
        ArrayList<List> array = new ArrayList<>();
        
        array.add(listeMedium);
        array.add(listeEmployee);
        
        return array;
    }
    
    public void clientInfos(Client myClient)
    {
        System.out.println(myClient);
    }
    
    public void checkListConsultClient (Long idClient) throws Exception{
        Client myClient;
        try {
            myClient= trouverClientparId(idClient);
        }catch(Exception ex){
            throw ex;
        }
        List<Consultation> myConsultList = myClient.getList();
        myConsultList.stream().forEach((consult)-> System.out.println(consult));
         
    }

    public ArrayList EmployeeStats(Employee myEmp) throws Exception
    {
       
        List<Consultation> empListConsult= myEmp.getList();
        HashMap <Client, Integer> mapClient = new HashMap<Client,  Integer>();
        HashMap <Medium, Integer> mapMedium = new HashMap<Medium,  Integer>();
        
        
        for (Consultation consult : empListConsult){
            //recuperer les mediums et client
            Client client= consult.getClient();
            Medium medium= consult.getMedium();
            
            //stocker dans une map avec l'objetc comme clefs et le nombre de consultation comme valeurs
            mapMedium.put(medium,( (mapMedium.get(medium)==null) ? 1 : (mapMedium.get(medium)+1)));
            mapClient.put(client,( (mapClient.get(client)==null) ? 1 : (mapClient.get(client)+1)));
   
        }
       
        ArrayList<HashMap> array = new ArrayList<>();
        
        array.add(mapMedium);
        array.add(mapClient);
        
        return array;
        
        
    }
    
     private Client trouverClientparId(Long id) throws Exception{
        ClientDAO monClientDAO= new ClientDAO();
        Client returningClient=null;
        try{
            JpaUtil.creerContextePersistance();
            returningClient= monClientDAO.chercherClientparID(id);
        }
        catch(Exception ex){
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
        Employee empToReturn= null;
        try{
            JpaUtil.creerContextePersistance();
            empToReturn= monEmpDAO.chercherEmployeeparID(id);
        }
        catch(Exception ex){
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }   
        if (empToReturn==null){
            throw new Exception("Sorry that id does not match with any EmployeeId");
        }
        return empToReturn;
     }
     
     private Medium trouverMediumParId(Long id) throws Exception{
        MediumDAO monMediumDAO= new MediumDAO();
        Medium mediumToReturn=null;
        try{
            JpaUtil.creerContextePersistance();
            mediumToReturn= monMediumDAO.chercherMediumparID(id);
        }
        catch(Exception ex){
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
        if (mediumToReturn==null){
            throw new Exception("Sorry that id does not match with any MediumId");
        }
        return mediumToReturn;
    }
     
     private Consultation trouverConsultparId(Long id) throws Exception{
        ConsultationDAO monConsultDAO= new ConsultationDAO();
        Consultation consultToReturn=null;
        try{
            JpaUtil.creerContextePersistance();
            consultToReturn= monConsultDAO.chercherConsultparID(id);
        }
        catch(Exception ex){
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }
        if (consultToReturn==null){
            throw new Exception("Sorry that id does not match with any ConsultationId");
        }
        return consultToReturn;
    }
     
      public Client AuthentifierClient(String mail, String mdp) throws Exception {
          
        ClientDAO monClientDAO= new ClientDAO();
        Client monClient;
        try{
            JpaUtil.creerContextePersistance();
            monClient= monClientDAO.authentifierClient(mail) ;
        }
        catch(Exception ex){
            throw ex;
            
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        //Cas exceptionnele si client null
        if (monClient==null){
            throw new Exception("Aucun client n'est incrit avec cet email. Veuillez vérifier vos credential");
        }
        //verification de mot de passe
        if (!monClient.getMotDePasse().equals(mdp)){
             throw new Exception("Mauvais mot de passe. Veuillez vérifier vos credential");
        }
        
        return monClient;
        
        
    }
       public Employee AuthentifierEmployee(String mail, String mdp) throws Exception {
          
        EmployeDAO monEmpDAO= new EmployeDAO();
        Employee monEmp;
        try{
            JpaUtil.creerContextePersistance();
            monEmp= monEmpDAO.authentifierEmp(mail) ;
        }
        catch(Exception ex){
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
             throw ex;
         }
         
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
             throw ex;
         }
         
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
            return null;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
      }

    public List<Medium> getListAllMedium() throws Exception{
        MediumDAO mediumDAO= new MediumDAO();
        List<Medium> listeMedium=null;
        try{
            JpaUtil.creerContextePersistance();
            listeMedium = mediumDAO.chercherTous();
        }
        catch(Exception ex){
            throw ex;
        }
        finally { // dans tous les cas, on ferme l'entity manager
            JpaUtil.fermerContextePersistance();
        }
        return listeMedium;
    }

    
}
    
    
