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
        Date date = new Date();
        Client newClient= new Client(nom,prenom,mail, date,motDePasse, "0606060606");
        ArrayList<String> listeAstrale;
        AstroNetApi astroNetApi = new AstroNetApi();
        try {
            listeAstrale = (ArrayList<String>) astroNetApi.getProfil(newClient.getPrenom(), newClient.getDate());
            newClient.setZodiaque(listeAstrale.get(0));
            newClient.setSigneChinois(listeAstrale.get(1));
            newClient.setCouleur(listeAstrale.get(2));
            newClient.setAnimalTotem(listeAstrale.get(3));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newClient;
         
    }
    public void testerInscriptionClient() {
        ServiceClient ServiceClient = new ServiceClient();
        Client newClient= ImportingClientIHMClient();
        Client newClientBD= ServiceClient.creer(newClient);
        
        if (newClientBD==null){
            System.out.println("> Echec inscription");
            
        }
        else{
           System.out.println("> Succès inscription");
           System.out.println("-> Client: id= " + newClientBD.getId()+ " ;nom= "+ newClientBD.getNom()+" ;mail= "+ newClientBD.getMail()+" ;motDePasse= "+newClientBD.getMotDePasse());
        }
        
         
    }
    public void testerAuthentificationClient() {
        String mail= Saisie.lireChaine("Mail du client :");
        String mdp= Saisie.lireChaine("Mdp du client");
        ServiceClient ServiceClient = new ServiceClient();
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
        ServiceClient ServiceClient = new ServiceClient();
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
        ServiceClient ServiceClient = new ServiceClient();
        List <Client> clientsBD = ServiceClient.ListeClients() ;
        if (clientsBD==null){
             System.out.println(">Ooops. An error occurred");
        }
        else{
             System.out.println("> Clients Found");
            clientsBD.stream().forEach((clientBD) ->  System.out.println("-> Client: id= " + clientBD.getId()+ " ;nom= "+ clientBD.getNom()+" ;mail= "+ clientBD.getMail()+" ;motDePasse= "+clientBD.getMotDePasse()));
        }
        
         
    }
    
    
}
