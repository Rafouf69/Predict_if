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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.*;


/**
 *
 * @author louislombard
 */
public class ServicePredictif {
    //inscritclient
    public Client creerClient(Client client) throws Exception{
        
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
            Logger.getAnonymousLogger().log(Level.INFO, "[Service predictif:Log] " + ex);
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
            JpaUtil.annulerTransaction();
            throw ex;
        }
        
        finally { // dans tous les cas, on ferme l'entity manager
        JpaUtil.fermerContextePersistance();
        }     
        return newMed;
    }

    public Consultation DemandeDeConsultation(Client myClient,Long idmedium, Date date) throws Exception{
           
        //Etape 1: Retrouver le client demandé
         
        //Etape 2:Vérifier que le client demandé n'a pas déja une consultation en attente
        if (!myClient.getStatus().equals("free")){
            throw new Exception("Sorry, " + myClient.getPrenom() +" "+ myClient.getNom() + " already have a consultation reserved");
        }
        
         
        //Etape 3: Retrouver le medium demandée
        Medium mymedium;
        try {
            mymedium= trouverMediumParId(idmedium);
        }catch(Exception ex){
            throw ex;
        }
         
         
         //Etape 4: Trouver la liste des employée pouvant executer le role
         List<Employee> MatchingEmployees;
         Consultation myConsult;
         try{
             JpaUtil.creerContextePersistance();
             EmployeDAO myDAOemp= new EmployeDAO();
             MatchingEmployees=myDAOemp.chercherEmployeDispo(mymedium.getGender());
         }catch(Exception Ex){
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
                 
                 
                 //changer etat employee dans la classe de service 
                 myConsult= new Consultation(employeChose,date,myClient, mymedium);
                 JpaUtil.creerContextePersistance();
                 JpaUtil.ouvrirTransaction();
                 ConsultationDAO myConsultationDAO= new ConsultationDAO();
                 myConsultationDAO.creer(myConsult);
                 JpaUtil.validerTransaction();
                 
            }catch(Exception ex){
                JpaUtil.annulerTransaction();
                throw ex;
            }
            finally{
                JpaUtil.fermerContextePersistance();
            }
            
             
             
        }
         
        return myConsult;
     
     }
    public String checkWork(Employee myEmp) throws Exception{
         
        String returningString;
        
        //checkstatus
        if (myEmp.getStatus().equals("free")){
            returningString="This is great: You have no work to do";
        }
        else if (myEmp.getStatus().equals("consulting")){
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
    public String BegginingConsult(Employee myEmp) throws Exception{
        
        //check employee is phoning
        if (!myEmp.getStatus().equals("Waiting")){
            throw new Exception("You cannot begin a conversation because they are not conversation waitings for you");
        }
        //should never happen. just incase.
        if (!myEmp.getList().get(myEmp.getList().size()-1).getStatus().equals("Waiting")){
            throw new Exception("Hmmm An error occured. please call us.");
        }   
          
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            ConsultationDAO myConsultationDAO= new ConsultationDAO();
            myConsultationDAO.beginConsult(myEmp.getList().get(myEmp.getList().size()-1));
            JpaUtil.validerTransaction();      
        }catch(Exception Ex){
            JpaUtil.annulerTransaction();
            throw Ex;
        }finally{
            JpaUtil.fermerContextePersistance();
        }

        String returningString="Conversation started!";

        return returningString;
     }
    public String EndingConsult(Employee myEmp, String message) throws Exception{
        
        //check employee is phoning
        if (!myEmp.getStatus().equals("Conversing")){
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
            myConsultationDAO.endConsult(myEmp.getList().get(myEmp.getList().size()-1), message);
            JpaUtil.validerTransaction();      
        }catch(Exception ex){
            JpaUtil.annulerTransaction();
            throw ex;
        }finally{
            JpaUtil.fermerContextePersistance();
        }
         
         
        String returningString="Conversation ended!";

        return returningString;
     }
     
    public List<String> AskingHelp(Employee myEmp, int amour, int sante, int travail) throws Exception{
        List<String> result=null;
        
        //check currently conversing
        if (!"Conversing".equals(myEmp.getStatus())){
           throw new Exception("This feature can only be called when conversing. Sorry.");
        }
        try{
            AstroNetApi astroNetApi = new AstroNetApi();
            result=astroNetApi.getPredictions(myEmp.getList().get(myEmp.getList().size()-1).getClient().getCouleur(), myEmp.getList().get(myEmp.getList().size()-1).getClient().getAnimalTotem(), amour, sante, travail);       
        }catch(Exception ex){
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
    
    public void ClientInfos(Client myClient)
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

    public String EmployeeStats(Employee myEmp) throws Exception
    {
        
        List<Consultation> empListConsult= myEmp.getList();
        TreeMap <Client, Integer> mapClient = new TreeMap<Client,  Integer>();
        TreeMap <Medium, Integer> mapMedium = new TreeMap<Medium,  Integer>();
        
        for (Consultation consult : empListConsult){
            //recuperer les mediums et client
            Client client= consult.getClient();
            Medium medium= consult.getMedium();
            
            //stocker dans une map avec l'objetc comme clefs et le nombre de consultation comme valeurs
            mapMedium.put(medium,( (mapMedium.get(medium)==null) ? 1 : (mapMedium.get(medium)+1)));
            mapClient.put(client,( (mapClient.get(client)==null) ? 1 : (mapClient.get(client)+1)));
   
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
    
    
