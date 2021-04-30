/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.console;

import dao.EmployeDAO;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import metier.modele.Client;
import metier.modele.Employee;
import metier.modele.Medium;
import metier.services.ServicePredictif;

/**
 *
 * @author louislombard
 */
public class ControleService {
    
    private Client ImportingClientIHMClient() {
        
        String nom= Saisie.lireChaine("Nom du client :");
        String prenom= Saisie.lireChaine("Prenom du client");
        String mail= Saisie.lireChaine("Mail du client :");
        String motDePasse= Saisie.lireChaine("MotDePasse du client");
        String phonenumber=Saisie.lireChaine("Numéro de téléphone du client: ");
        Date birthdate = new Date();
        Client newClient= new Client(nom,prenom,mail, birthdate,motDePasse, phonenumber);
        return newClient;
         
    }
     private Employee ImportingClientIHMEmployee() {
        
        String nom= Saisie.lireChaine("Nom de l'employé :");
        String prenom= Saisie.lireChaine("Prenom de l'employé");
        String mail= Saisie.lireChaine("Mail de l'employé :");
        String genre= Saisie.lireChaine("Genre de l'employé (H/F) :");
        String motDePasse= Saisie.lireChaine("MotDePasse de l'employé: ");
        String phonenumber=Saisie.lireChaine("Numéro de téléphonede l'employée: ");
        Employee myemp= new Employee(nom,prenom,mail,genre,motDePasse, phonenumber);
        return myemp;
         
    }
      private Medium ImportingClientIHMMedium() {
        Medium newMedium=null;
        String denomination= Saisie.lireChaine("Dénomination du médium :");
        String genre= Saisie.lireChaine("Genre du medium (H/F) :");
        String presentation=Saisie.lireChaine("Présentation du médium :");
        
        int i=0;
        while (i!=1 && i!=2 && i!=3){
            i= Saisie.lireInteger("Type de medium: Cartomancien(1)/Spirite(2)/Astrologue(3)");
        }
        if (i==1){ //Create Catromancien
            newMedium = new Medium(denomination,genre,presentation);
        }
        else if (i==2){ //Create Spirite
            String support=Saisie.lireChaine("Support du medium: ");
            newMedium = new Medium(denomination,genre,presentation,support);
        }
        else if (i==3){ //Create Astrologue
            String formation=Saisie.lireChaine("Formation du medium: ");
            String promotion=Saisie.lireChaine("Promotion du medium: ");
            newMedium = new Medium(denomination,genre,presentation,formation,promotion);
        }
        return newMedium;
         
    }
    public Client testerInscriptionClient() throws Exception{
        ServicePredictif ServiceClient = new ServicePredictif();
        Client newClient= ImportingClientIHMClient();
        Client newClientBD=null;
        try{
            newClientBD= ServiceClient.creerClient(newClient);
            System.out.println("> Succès inscription");
            System.out.println("-> Client: id= " + newClientBD.getId()+ " ;nom= "+ newClientBD.getNom()+" ;mail= "+ newClientBD.getMail()+" ;motDePasse= "+newClientBD.getMotDePasse());
        }catch(Exception Ex){
             System.out.println("> Echec inscription employee: Sorry boy");
             throw Ex;
        } 
        return newClientBD;
    }
    
    public void testerInscriptionEmployee(){
        ServicePredictif Servicepred = new ServicePredictif();
        Employee newEmployee= ImportingClientIHMEmployee();
        try{ 
            Employee newEmptBD= Servicepred.creerEmployee(newEmployee);
            System.out.println("-> Succès inscription");
            System.out.println("-> Bienvenue à l'Employée: id= " + newEmptBD.getId()+ " ;nom= "+ newEmptBD.getNom()+" ;mail= "+ newEmptBD.getMail()+" ;motDePasse= "+newEmptBD.getMotDePasse());
        }catch(Exception ex){
         System.out.println("> Echec inscription: Sorry boy");
        }
    }
    
    public void testerInscriptionMedium(){
      ServicePredictif Servicepred = new ServicePredictif();
      Medium newMedium= ImportingClientIHMMedium();
      try{ 
            Medium newMediumBD= Servicepred.creerMedium(newMedium);
            System.out.println("-> Succès inscription");
            System.out.println("-> Bienvenue au Medium: id= " + newMedium.getId()+ " ;denomination= "+ newMedium.getDenomination()+" ;mail= ");
        }catch(Exception ex){
         System.out.println("> Echec inscription: Sorry boy");
        }
     }
    public void initdevversion(int nbclient, int nbemployee, int nbmedium){
    
        ServicePredictif serviceClient = new ServicePredictif();
        //initialize Client
        for(int i=0;i<nbclient;i++){
            String nom= "NomClient"+i;
            String prenom= "PrenomClient"+i;
            String mail="MailClient"+i+"@gmail.com";
            String mdp = "mdpClient"+i;
            String phonenumber="060000000"+i;
            Client newClient= new Client(nom,prenom,mail,new Date(),mdp,phonenumber);
            try{
            serviceClient.creerClient(newClient);
            }catch (Exception Ex){
             System.out.println("> Echec initialization: ta base de données est sans doute déja remplie garçon");
            }
            
        }
        
        //initialize Employee
        for(int i=0;i<nbemployee;i++){
            String nom= "NomEployee"+i;
            String prenom= "PrenomEmployee"+i;
            String mail="MailEmployee"+i+"@gmail.com";
            String mdp = "mdpEmployee"+i;
            String phonenumber="061100000"+i;
            
            String genre= (i%2==1?"H":"F");
            Employee newEmployee= new Employee(nom,prenom,mail,genre,mdp,phonenumber);
            try{
            serviceClient.creerEmployee(newEmployee);
            }catch (Exception Ex){
             System.out.println("> Echec initialization: ta base de données est sans doute déja remplie garçon");
            }
            
        }
        
        
        //initialize Medium
        for(int i=0;i<nbmedium;i++){
           
            String nom= "denominationMedium"+i;
            String genre= (i%2==1?"H":"F");
            String presentation= "presentationMedium"+i;
            if (i%3==0){
                Medium newMed= new Medium(nom,genre,presentation);
                try{
                    serviceClient.creerMedium(newMed);
                }catch (Exception Ex){
                    System.out.println("> Echec initialization: ta base de données est sans doute déja remplie garçon");
                }
            }
            else if (i%3==1){
                
                String support ="supportMedium"+i;
                Medium newMed= new Medium(nom,genre,presentation,support);
                try{
                    serviceClient.creerMedium(newMed);
                }catch (Exception Ex){
                    System.out.println("> Echec initialization: ta base de données est sans doute déja remplie garçon");
                }
            }
            else {
                String formation ="formationMedium"+i;
                String promotion ="200"+i;
                Medium newMed= new Medium(nom,genre,presentation,formation,promotion);
                try{
                    serviceClient.creerMedium(newMed);
                }catch (Exception Ex){
                    System.out.println("> Echec initialization: ta base de données est sans doute déja remplie garçon");
                }
            }   
            
            
        }

    }

    public Client testerAuthentificationClient() throws Exception{
        String mail= Saisie.lireChaine("Mail du client :");
        String mdp= Saisie.lireChaine("Mdp du client");
        ServicePredictif ServiceClient = new ServicePredictif();
        Client clientBD;
        try{
            clientBD= ServiceClient.AuthentifierClient(mail,mdp);  
        }catch(Exception err){
            System.out.println("> Authentification Failed : " + err.getMessage());
            throw err;
        }
      
        return clientBD;
    }
    public Employee testerAuthentificationEmployee() throws Exception{
        String mail= Saisie.lireChaine("Mail de l'employe :");
        String mdp= Saisie.lireChaine("Mdp de l'employe");
        ServicePredictif ServiceClient = new ServicePredictif();
        Employee myEmp;
        try{
            myEmp= ServiceClient.AuthentifierEmployee(mail,mdp);  
        }catch(Exception err){
            System.out.println("> Authentification Failed : " + err.getMessage());
            throw err;
        }
      
        return myEmp;
        
        
         
    }
//    public void testerRechercheClient() {
//        long id= Saisie.lireInteger("Id du client :");
//        ServicePredictif serviceClient = new ServicePredictif();
//        Client clientBD= serviceClient.trouverClientparId(id) ;
//        if (clientBD==null){
//             System.out.println("> Not any client found with this Id");
//        }
//        else{
//            System.out.println("> Client Found");
//            System.out.println("-> Client: id= " + clientBD.getId()+ " ;nom= "+ clientBD.getNom()+" ;mail= "+ clientBD.getMail()+" ;motDePasse= "+clientBD.getMotDePasse());
//        }
//        
//         
//    }
    public void testerListeClients() {
        ServicePredictif ServiceClient = new ServicePredictif();
        List <Client> clientsBD = ServiceClient.ListeClients() ;
        if (clientsBD==null){
             System.out.println(">Ooops. An error occurred");
        }
        else{
            System.out.println("> Clients Found");
            clientsBD.stream().forEach((clientBD) ->  System.out.println("-> Client: id= " + clientBD.getId()+ " ;nom= "+ clientBD.getNom()+" ;mail= "+ clientBD.getMail()+" ;motDePasse= "+clientBD.getMotDePasse()));
        }
        
         
    }
    public void testerdemandesconsult(Client myClient) {
       ServicePredictif Servicepredictif = new ServicePredictif();
      
       long idmedium= Saisie.lireInteger("Id du medium :");
       
       try {
            Servicepredictif.DemandedeConsultation(myClient.getId(),myClient.getMotDePasse(),idmedium, new Date());
       }catch(Exception Ex){
            System.out.println(Ex);
       }
       
       try {
           Servicepredictif.checkListConsultClient(myClient.getId());
       }catch(Exception ex)
       {
           System.out.println("Fail : " + ex.getMessage());
       }
       
       ;
    }
    public void testercheckwork(Employee myEmp) {
       ServicePredictif Servicepredictif = new ServicePredictif();
       try {
            String Result= Servicepredictif.checkWork(myEmp.getId(), myEmp.getMotDePasse());
            System.out.println(Result);
       }catch(Exception Ex){
            System.out.println(Ex);
       }
       
    }
    public void testerbeginconsult(Employee myEmp) {
       ServicePredictif Servicepredictif = new ServicePredictif();
       try {
            String Result= Servicepredictif.BegginingConsult(myEmp.getId(),myEmp.getMotDePasse());
            System.out.println(Result);
       }catch(Exception Ex){
            System.out.println(Ex);
       }
       
    }
    public void testerendconsult(Employee myEmp) {
       ServicePredictif Servicepredictif = new ServicePredictif();
       String Comment= Saisie.lireChaine("Commentaire : ");
       try {
            String Result= Servicepredictif.EndingConsult(myEmp.getId(), myEmp.getMotDePasse(), Comment);
            System.out.println(Result);
       }catch(Exception Ex){
            System.out.println(Ex);
       }
    }
    public void testerAskingHelp(Employee myEmp) {
       List<Integer> listvalue = Arrays.asList(new Integer[]{1,2,3,4});
       int niveauAmour= Saisie.lireInteger("Niveau d'Amour : ",listvalue);
       int niveauSante= Saisie.lireInteger("Niveau de Santé : ",listvalue);
       int niveauTravail= Saisie.lireInteger("Niveau de travail : ",listvalue);
       try {
            ServicePredictif Servicepredictif = new ServicePredictif();
            List<String> Result= Servicepredictif.AskingHelp(myEmp.getId(), myEmp.getMotDePasse(), niveauAmour, niveauSante, niveauTravail);
            System.out.println(Result);
       }catch(Exception Ex){
            System.out.println(Ex);
       }
    
    }
    
    public void testerCompanyStats(Employee myEmp)
    {
        try {
            ServicePredictif servicePredictif = new ServicePredictif();
            servicePredictif.companyStats(myEmp.getId(),myEmp.getMotDePasse());
        }catch(Exception Ex){
            System.out.println(Ex);
        }
    }
    
    private void testerGetListAllMedium() {
        try {
            ServicePredictif servicePredictif = new ServicePredictif();
            servicePredictif.getListAllMedium();
        }catch(Exception Ex){
            System.out.println(Ex);
        }
    }
    
    public int runningserviceEmployee(Employee myEmp) {
            System.out.println("***************************************************************");
            System.out.println("******************** Welcome to Predict'if ********************");
            System.out.println("*********************** Console Version ***********************");
            System.out.println("************************** Employee ***************************");
            System.out.println("---------------------------------------------------------------");
            System.out.println("***************************   |||   ***************************");
            System.out.println("********** Client *********   |||   ******** Employee *********");
            System.out.println("***************************   |||   ***************************");
            System.out.println("                              |||                              ");
            System.out.println(" • Check Work            (1)  |||   • Begining Consultation (2)");
            System.out.println("                              |||                              ");
            System.out.println(" • End Consultation      (3)  |||   • Ask for help          (4)");
            System.out.println("                              |||                              ");
            System.out.println(" • Check Company Stats   (5)  |||   • Check Employee Info   (6)");
            System.out.println("                              |||                              ");
            System.out.println("---------------------------------------------------------------");
            System.out.println("***************************************************************");
            System.out.println("***************************************************************");
            System.out.println("*************************   Exit (0)  *************************");
            System.out.println("***************************************************************");
            System.out.println("***************************************************************");
            int integ= Saisie.lireInteger("Choisir un chiffre entre 0 et 6 : ");
        switch (integ) {
            case 1:
                testercheckwork(myEmp);
                break;
            case 2:
                testerbeginconsult(myEmp);
                break;
            case 3:
                testerendconsult(myEmp);
                break;
            case 4:
                testerAskingHelp(myEmp);
                break;
            case 5:
                testerCompanyStats(myEmp);
                break;
            case 6:
                //testerEmployeeInfos();
                break;
            default:
                break;
        }
            return integ;
       
    }
    public int runningserviceClient(Client myClient) {
            System.out.println("***************************************************************");
            System.out.println("******************** Welcome to Predict'if ********************");
            System.out.println("*********************** Console Version ***********************");
            System.out.println("*************************** Clien t****************************");
            System.out.println("---------------------------------------------------------------");
            System.out.println(" • Demander Consultation (1)  |||   • Voir liste Mediums    (2)");
            System.out.println("                              |||                              ");
            System.out.println(" • Check Client infos    (3)  |||   •                       ( )");
            System.out.println("                              |||                              ");
        
            System.out.println("---------------------------------------------------------------");
            System.out.println("***************************************************************");
            System.out.println("***************************************************************");
            System.out.println("*************************   Exit (0)  *************************");
            System.out.println("***************************************************************");
            System.out.println("***************************************************************");
            int integ= Saisie.lireInteger("Choisir un chiffre entre 0 et 3 : ");
        switch (integ) {
            case 1:
                testerdemandesconsult(myClient);
                break;     
            case 2:
                testerGetListAllMedium();
                break;
            case 3:
                //testClient();
                break;
            default:
                break;
        }
            return integ;
       
    }

}
