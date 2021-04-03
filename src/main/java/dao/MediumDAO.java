/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.IOException;
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
    
   
    
}
