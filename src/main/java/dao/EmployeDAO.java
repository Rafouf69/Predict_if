/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.IOException;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    
    public List<Employee> chercherEmployeDispo(String genre) throws Exception
    {
        // Ne retenir que les employés free et dont le genre est compatible
        String q = "select e from Employee e where e.genre = :ungenre and e.status = 'free'";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(q, Employee.class);
        query.setParameter("ungenre", genre);
        return query.getResultList();
    }
    public Employee chercherEmployeeparID(Long Id) {
        return JpaUtil.obtenirContextePersistance().find(Employee.class, Id);
        
    }
    public Employee authentifierEmp(String mail) {
        String s = "select e from Employee e where e.mail = :unmail";
        Query query = JpaUtil.obtenirContextePersistance().createQuery(s);
        query.setParameter("unmail", mail);
        return (Employee) query.getSingleResult();
        
    }
    
}
