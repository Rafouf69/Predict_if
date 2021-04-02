/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import metier.modele.Employee;

/**
 *
 * @author Lucas
 */
public class EmployeDAO {
    public Employee creer(Employee employe)
    {
        JpaUtil.obtenirContextePersistance().persist(employe);
        return employe;
    }
    
}
