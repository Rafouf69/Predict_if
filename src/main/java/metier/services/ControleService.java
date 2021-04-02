/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.services;

import ihm.console.Saisie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Client;
import metier.modele.Employee;

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
    public void testerInscriptionClient() {
        ServicePredictif ServiceClient = new ServicePredictif();
        Client newClient= ImportingClientIHMClient();
        try{
            Client newClientBD= ServiceClient.creerClient(newClient);
            System.out.println("> Succès inscription");
            System.out.println("-> Client: id= " + newClientBD.getId()+ " ;nom= "+ newClientBD.getNom()+" ;mail= "+ newClientBD.getMail()+" ;motDePasse= "+newClientBD.getMotDePasse());
        }catch(Exception Ex){
             System.out.println("> Echec inscription employee: Sorry boy");
        } 
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
    public void initdevversion(){
    
    ServicePredictif ServiceClient = new ServicePredictif();
    //initialize Client
        Client newClient1= new Client("Ducoq","Hubert","HubertDucoq@gmail.com",new Date(), "mdpHubert","0600000001");
        Client newClient2= new Client("Vignon","Raphael","Raphael@gmail.com",new Date(), "mdpRaphael", "0600000002");
        Client newClient3= new Client("Loiseau","Lucas","lucas@gmail.com",new Date(), "mdplucas","0600000003");
        Client newClient4= new Client("Senouci","Alex","Alex@gmail.com",new Date(), "mdpAlex","0600000004");
        Client newClient5= new Client("Lombard","Louis","louis@gmail.com",new Date(), "mdplouis","0600000005");
        Client newClient6= new Client("Rael","Freeze","rael@gmail.com",new Date(), "mdpfreeze","0600000006");
        Client newClient7= new Client("Saumon","René","rené@gmail.com",new Date(), "mdprené","0600000007");
        Client newClient8= new Client("Luflo","Luc","Luc@gmail.com",new Date(), "mdpluc","0600000008");
        Client newClient9= new Client("Dumontel","Albert","albert@gmail.com",new Date(), "mdpalbert","0600000009");
        Client newClient10= new Client("Macron","Emmanuel","Emmanuel@gmail.com",new Date(), "mdpmanu","0600000010");
        
        try{
            ServiceClient.creerClient(newClient1);
            ServiceClient.creerClient(newClient2);
            ServiceClient.creerClient(newClient3);
            ServiceClient.creerClient(newClient4);
            ServiceClient.creerClient(newClient5);
            ServiceClient.creerClient(newClient6);
            ServiceClient.creerClient(newClient7);
            ServiceClient.creerClient(newClient8);
            ServiceClient.creerClient(newClient9);
            ServiceClient.creerClient(newClient10);
        }catch(Exception Ex){
             System.out.println("> Echec inscription employee: Sorry boy");
        } 
        //initialize Employee
        Employee newEmployee1= new Employee("nom1","prenom1","nom1@gmail.com","H", "mdpnom1","0610000001");
        Employee newEmployee2= new Employee("nom2","prenom2","nom2@gmail.com","H", "mdpnom2","0610000002");
        Employee newEmployee3= new Employee("nom3","prenom3","nom3@gmail.com","H", "mdpnom3","0610000003");
        Employee newEmployee4= new Employee("nom4","prenom4","nom4@gmail.com","H", "mdpnom4","0610000004");
        Employee newEmployee5= new Employee("nom5","prenom5","nom5@gmail.com","H", "mdpnom5","0610000005");
        Employee newEmployee6= new Employee("nom6","prenom6","nom6@gmail.com","F", "mdpnom6","061000006");
        Employee newEmployee7= new Employee("nom7","prenom7","nom7@gmail.com","F", "mdpnom7","0610000007");
        Employee newEmployee8= new Employee("nom8","prenom8","nom8@gmail.com","F", "mdpnom8","061000008");
        Employee newEmployee9= new Employee("nom9","prenom9","nom9@gmail.com","F", "mdpnom9","0610000009");
        Employee newEmployee10= new Employee("nom10","prenom10","nom10@gmail.com","F", "mdpnom10","0610000010");
        
        try{
            ServiceClient.creerEmployee(newEmployee1);
            ServiceClient.creerEmployee(newEmployee2);
            ServiceClient.creerEmployee(newEmployee3);
            ServiceClient.creerEmployee(newEmployee4);
            ServiceClient.creerEmployee(newEmployee5);
            ServiceClient.creerEmployee(newEmployee6);
            ServiceClient.creerEmployee(newEmployee7);
            ServiceClient.creerEmployee(newEmployee8);
            ServiceClient.creerEmployee(newEmployee9);
            ServiceClient.creerEmployee(newEmployee10);
        }catch(Exception Ex){
             System.out.println("> Echec inscription employee: Sorry boy");
        }
    }
    public void testerAuthentificationClient() {
        String mail= Saisie.lireChaine("Mail du client :");
        String mdp= Saisie.lireChaine("Mdp du client");
        ServicePredictif ServiceClient = new ServicePredictif();
        Client clientBD= ServiceClient.Authentifier(mail, mdp);
        if (clientBD==null){
             System.out.println("> Authentification failed");
        }
        else{
            System.out.println("> Authentification success");
             System.out.println("-> Bienvenue  "+ clientBD.getPrenom()+" "+ clientBD.getNom());
        }
        
         
    }
    public void testerRechercheClient() {
        long id= Saisie.lireInteger("Id du client :");
        ServicePredictif ServiceClient = new ServicePredictif();
        Client clientBD= ServiceClient.trouverClientparId(id) ;
        if (clientBD==null){
             System.out.println("> Not any client found with this Id");
        }
        else{
            System.out.println("> Client Found");
            System.out.println("-> Client: id= " + clientBD.getId()+ " ;nom= "+ clientBD.getNom()+" ;mail= "+ clientBD.getMail()+" ;motDePasse= "+clientBD.getMotDePasse());
        }
        
         
    }
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
    public void testerdemandesconsult() {
       ServicePredictif ServiceClient = new ServicePredictif();
       
        
         
    }
    
}
