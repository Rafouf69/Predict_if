/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Objects;
import java.lang.Math;
import metier.modele.Client;
import metier.modele.Consultation;
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
        String phoneNumber=Saisie.lireChaine("Numéro de téléphone du client: ");
        Date birthdate = new Date();
        Client newClient= new Client(nom,prenom,mail, birthdate,motDePasse, phoneNumber);
        return newClient;
         
    }
     private Employee ImportingClientIHMEmployee() {
        
        String nom= Saisie.lireChaine("Nom de l'employé :");
        String prenom= Saisie.lireChaine("Prenom de l'employé");
        String mail= Saisie.lireChaine("Mail de l'employé :");
        String genre= Saisie.lireChaine("Genre de l'employé (H/F) :");
        String motDePasse= Saisie.lireChaine("MotDePasse de l'employé: ");
        String phoneNumber=Saisie.lireChaine("Numéro de téléphonede l'employée: ");
        Employee myemp= new Employee(nom,prenom,mail,genre,motDePasse, phoneNumber);
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
        if (i==1){ //Create Cartomancien
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
        ServicePredictif serviceClient = new ServicePredictif();
        Client newClient= ImportingClientIHMClient();
        Client newClientBD=null;
        try{
            newClientBD= serviceClient.inscrireClient(newClient);
            System.out.println("> Succès inscription");
            System.out.println("-> Client: id= " + newClientBD.getId()+ " ;nom= "+ newClientBD.getNom()+" ;mail= "+ newClientBD.getMail()+" ;motDePasse= "+newClientBD.getMotDePasse());
        }catch(Exception ex){
             System.out.println("> Echec inscription employee: Sorry boy");
             throw ex;
        } 
        return newClientBD;
    }
    
    public void testerInscriptionEmployee(){
        ServicePredictif servicePred = new ServicePredictif();
        Employee newEmployee= ImportingClientIHMEmployee();
        try{ 
            Employee newEmptBD= servicePred.creerEmployee(newEmployee);
            System.out.println("-> Succès inscription");
            System.out.println("-> Bienvenue à l'Employée: id= " + newEmptBD.getId()+ " ;nom= "+ newEmptBD.getNom()+" ;mail= "+ newEmptBD.getMail()+" ;motDePasse= "+newEmptBD.getMotDePasse());
        }catch(Exception ex){
         System.out.println("> Echec inscription: Sorry boy");
        }
    }
    
    public void testerInscriptionMedium(){
      ServicePredictif servicePredictif = new ServicePredictif();
      Medium newMedium= ImportingClientIHMMedium();
      try{ 
            Medium newMediumBD= servicePredictif.creerMedium(newMedium);
            System.out.println("-> Succès inscription");
            System.out.println("-> Bienvenue au Medium: id= " + newMedium.getId()+ " ;denomination= "+ newMedium.getDenomination()+" ;mail= ");
        }catch(Exception ex){
         System.out.println("> Echec inscription: Sorry boy");
        }
     }
    public void initDevVersion(int nbclient, int nbemployee, int nbmedium){
    
        ServicePredictif serviceClient = new ServicePredictif();
        //initialize Client
        for(int i=0;i<nbclient;i++){
            String nom= "NomClient"+i;
            String prenom= "PrenomClient"+i;
            String mail="MailClient"+i+"@gmail.com";
            String mdp = "mdpClient"+i;
            String phoneNumber="060000000"+i;
            Client newClient= new Client(nom,prenom,mail,new Date(),mdp,phoneNumber);
            try{
            serviceClient.inscrireClient(newClient);
            }catch (Exception ex){
             System.out.println("> Echec initialization: ta base de données est sans doute déja remplie garçon");
            }
            
        }
        
        //initialize Employee
        for(int i=0;i<nbemployee;i++){
            String nom= "NomEployee"+i;
            String prenom= "PrenomEmployee"+i;
            String mail="MailEmployee"+i+"@gmail.com";
            String mdp = "mdpEmployee"+i;
            String phoneNumber="061100000"+i;
            
            String genre= (i%2==1?"H":"F");
            Employee newEmployee= new Employee(nom,prenom,mail,genre,mdp,phoneNumber);
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
        ServicePredictif service = new ServicePredictif();
        Client clientBD;
        try{
            clientBD= service.AuthentifierClient(mail,mdp);  
        }catch(Exception err){
            System.out.println("> Authentification Failed : " + err.getMessage());
            throw err;
        }
      
        return clientBD;
    }
    public Employee testerAuthentificationEmployee() throws Exception{
        String mail= Saisie.lireChaine("Mail de l'employe :");
        String mdp= Saisie.lireChaine("Mdp de l'employe");
        ServicePredictif service = new ServicePredictif();
        Employee myEmp;
        try{
            myEmp= service.AuthentifierEmployee(mail,mdp);  
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
        ServicePredictif service = new ServicePredictif();
        List <Client> clientsBD = service.ListeClients() ;
        if (clientsBD==null){
             System.out.println(">Ooops. An error occurred");
        }
        else{
            System.out.println("> Clients Found");
            clientsBD.stream().forEach((clientBD) ->  System.out.println("-> Client: id= " + clientBD.getId()+ " ;nom= "+ clientBD.getNom()+" ;mail= "+ clientBD.getMail()+" ;motDePasse= "+clientBD.getMotDePasse()));
        }
        
         
    }
    public void testerDemandeConsult(Client myClient) {
       ServicePredictif service = new ServicePredictif();
      
       long idMedium= Saisie.lireInteger("Id du medium :");
       //enlever les mdp
       try {
            service.demandeDeConsultation(myClient,idMedium, new Date());
       }catch(Exception ex){
            System.out.println(ex);
       }
       
       try {
           service.checkListConsultClient(myClient.getId());
       }catch(Exception ex)
       {
           System.out.println("Fail : " + ex.getMessage());
       }
    }
    public void testerCheckWork(Employee myEmp) {
       ServicePredictif service = new ServicePredictif();
       try {
            String Result= service.checkWork(myEmp);
            System.out.println(Result);
       }catch(Exception Ex){
            System.out.println(Ex);
       }
       
    }
    public void testerEmployeeInfos(Employee myEmp) {
       ServicePredictif service = new ServicePredictif();
       try {
            ArrayList<HashMap> result= service.EmployeeStats(myEmp);
            HashMap<Medium,Integer> mapMedium =result.get(0);
            HashMap<Client,Integer> mapClient =result.get(1);
            List<Integer> nbOfClientList= new ArrayList(mapClient.values());
            List<Integer> nbOfTimesSorted= new ArrayList(mapMedium.values());
            Collections.sort(nbOfTimesSorted,Collections.reverseOrder());
            Collections.sort(nbOfClientList,Collections.reverseOrder());
            
            int tailleClient= Math.min(nbOfClientList.size(),3);
            int tailleMedium= Math.min(nbOfTimesSorted.size(),3);
            
            System.out.println("BBB "+nbOfTimesSorted);
            System.out.println("BBB "+nbOfClientList);
            
            
            Client[] mostClient = new  Client[tailleClient];
            int[] mostClientTime = new int [tailleClient];
            
            for (int i= 0;i<tailleClient;i++){
                mostClientTime[i]=nbOfClientList.get(i);
            }
            
            
            Medium[] mostMedium = new  Medium[tailleMedium];
            int[] mostMediumTime = new int [tailleMedium];
            
            for (int i= 0;i<tailleMedium;i++){
                mostMediumTime[i]=nbOfTimesSorted.get(i);
            }
            
  

            mapMedium.entrySet().forEach(entry -> {
                boolean insert= false;
                for (int i= 0;i<tailleMedium;i++){
                    if (Objects.equals(entry.getValue(), mostMediumTime[i]) && mostMedium[i]==null && !insert){
                        mostMedium[i]=entry.getKey();
                    }
                }
            });
            mapClient.entrySet().forEach(entry -> {
                boolean insert= false;
                for (int i= 0;i<tailleClient;i++){
                    if (Objects.equals(entry.getValue(), mostClientTime[i]) && mostClient[i]==null && !insert){
                        mostClient[i]=entry.getKey();
                    }
                }
            });

            System.out.println("-----Top 3 des mediums incarnés par "+myEmp.getPrenom()+" "+myEmp.getNom()+" -----");
            for(int i=0;i<tailleMedium;i++){
                System.out.println("medium numéro "+ (i+1)+ " : " + mostMedium[i].getDenomination()+" incarné "+mostMediumTime[i]+ "fois");
            }
            System.out.println("-----Top 3 des clients consulté par "+myEmp.getPrenom()+" "+myEmp.getNom()+" -----");
             for(int i=0;i<tailleClient;i++){
                 System.out.println("client numéro "+(i+1) + " : " + mostClient[i].getPrenom()+" "+ mostClient[i].getNom()+" vous à consulté "+mostClientTime[i]+ "fois");
            }
        

       }catch(Exception ex){
            System.out.println(ex);
       }
       
    }
    public void testerBeginConsult(Employee myEmp) {
       ServicePredictif service = new ServicePredictif();
       try {
            String Result= service.begginingConsult(myEmp);
            System.out.println(Result);
       }catch(Exception ex){
            System.out.println(ex);
       }
       
    }
    public void testeRendConsult(Employee myEmp) {
       ServicePredictif service = new ServicePredictif();
       String comment= Saisie.lireChaine("Commentaire : ");
       try {
            String result= service.endingConsult(myEmp, comment);
            System.out.println(result);
       }catch(Exception ex){
            System.out.println(ex);
       }
    }
    public void testerAskingHelp(Employee myEmp) {
       List<Integer> listvalue = Arrays.asList(new Integer[]{1,2,3,4});
       int niveauAmour= Saisie.lireInteger("Niveau d'Amour : ",listvalue);
       int niveauSante= Saisie.lireInteger("Niveau de Santé : ",listvalue);
       int niveauTravail= Saisie.lireInteger("Niveau de travail : ",listvalue);
       try {
            ServicePredictif service = new ServicePredictif();
            List<String> result= service.askingHelp(myEmp, niveauAmour, niveauSante, niveauTravail);
            System.out.println(result);
       }catch(Exception ex){
            System.out.println(ex);
       }
    
    }
    
    public void testerCompanyStats()
    {
        ArrayList<List> array = new ArrayList<>();
        try {
            ServicePredictif servicePredictif = new ServicePredictif();
            array = servicePredictif.companyStats();
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        List<Medium> listeMedium = array.get(0);
        List<Employee> listeEmployee = array.get(1);
        
        //top 3 des medium
        
        System.out.println("-----Top 3 des mediums choisis par les clients-----");
        System.out.println("medium numéro 1 : " + listeMedium.get(0).getDenomination());
        System.out.println("medium numéro 2 : " + listeMedium.get(1).getDenomination());
        System.out.println("medium numéro 3 : " + listeMedium.get(2).getDenomination());
        System.out.println("");
        
        //nombre de consultations par medium
        System.out.println("-----Nombre de consultation par medium-----");
        for (Medium listeMedium1 : listeMedium) {
            System.out.println(listeMedium1.getDenomination()+ " a réalisé" + listeMedium1.getConsultNumber() + " consultations");
        }
        System.out.println("");
        
        //repartition des clients par employee
        
        System.out.println("-----répartition des clients par employée-----");
        for (Employee listeEmployee1 : listeEmployee) {
            List<Consultation> listeConsultation = listeEmployee1.getList();
            Set<Client> setClient = new HashSet<>();
            for(Consultation listeConsultation1 : listeConsultation)
            {
                setClient.add(listeConsultation1.getClient());
            }
            System.out.println(listeEmployee1.getNom() + " " +listeEmployee1.getPrenom() + " a " + setClient.size() + " clients uniques");
        }
        System.out.println("");
    }
    
    private void testerGetListAllMedium() {
        List<Medium> listeMedium=null;
        try {
            ServicePredictif servicePredictif = new ServicePredictif();
            listeMedium = servicePredictif.getListAllMedium();
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        System.out.println("Les medium disponibles sont les suivants : ");
        for(Medium listeMedium1 : listeMedium)
        {
            System.out.println(listeMedium1.toString());
        }
    }
    
    public void testerClientInfos(Client myClient)
    {
        System.out.println(myClient);
    }
    
    public int runningServiceEmployee(Employee myEmp) {
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
                testerCheckWork(myEmp);
                break;
            case 2:
                testerBeginConsult(myEmp);
                break;
            case 3:
                testeRendConsult(myEmp);
                break;
            case 4:
                testerAskingHelp(myEmp);
                break;
            case 5:
                testerCompanyStats();
                break;
            case 6:
                testerEmployeeInfos(myEmp);
                break;
            default:
                break;
        }
            return integ;
       
    }
    public int runningServiceClient(Client myClient) {
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
                testerDemandeConsult(myClient);
                break;     
            case 2:
                testerGetListAllMedium();
                break;
            case 3:

                testerClientInfos(myClient);

                break;
            default:
                break;
        }
            return integ;
       
    }

}
