/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Medium;

/**
 *
 * @author Lucas
 */
public class MediumDAO {
    public Medium creer(Medium newMedium)
    {
        JpaUtil.obtenirContextePersistance().persist(newMedium);
        return newMedium;
    }
    
    public Medium modifier(Medium medium)
    {
        return JpaUtil.obtenirContextePersistance().merge(medium);
    }
    
    public Medium chercherMediumParID(Long Id) {
        return JpaUtil.obtenirContextePersistance().find(Medium.class, Id);   
    }
   
    public List<Medium> chercherTous()
    {
        String s = "select m from Medium m order by m.denomination asc";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(s, Medium.class);
        return query.getResultList();
    }
}
