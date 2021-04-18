/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import metier.modele.*;
import java.util.List;
import javax.persistence.*;
/**
 *
 * @author louislombard
 */
public class ClientDAO {
    
    public Client creer(Client client) {
        
        JpaUtil.obtenirContextePersistance().persist(client);
        return client;
    }
    
    public List<Client> chercherTous() {
        String s = "select e from Client e";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(s,Client.class);
        return query.getResultList();
    }
   
    
     public Client authentifierClient(String mail) {
        String s = "select e from Client e where e.mail = :unmail";
        Query query = JpaUtil.obtenirContextePersistance().createQuery(s);
        query.setParameter("unmail", mail);
        return (Client) query.getSingleResult();
        
    }
    public Client chercherClientparID(Long Id) {
        return JpaUtil.obtenirContextePersistance().find(Client.class, Id);
        
    }
    
    
}
