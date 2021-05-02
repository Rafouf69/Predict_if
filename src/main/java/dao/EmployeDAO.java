/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import metier.modele.Employee;

/**
 *
 * @author Lucas
 */
public class EmployeDAO {
    public Employee creer(Employee employee)
    {
        JpaUtil.obtenirContextePersistance().persist(employee);
        return employee;
    }
    
    public Employee modifier(Employee employee)
    {
        return JpaUtil.obtenirContextePersistance().merge(employee);
    }
    
    public List<Employee> chercherEmployeDispo(String genre)
    {
        // Ne retenir que les employés free et dont le genre est compatible
        String q = "select e from Employee e where e.genre = :ungenre and e.status = 0";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(q, Employee.class);
        query.setParameter("ungenre", genre);
        return query.getResultList();
    }
    
    public Employee chercherEmployeeparID(Long Id) {
        return JpaUtil.obtenirContextePersistance().find(Employee.class, Id);
        
    }
    
    public List<Employee> chercherTous() throws Exception
    {
        String q = "select e from Employee e";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(q, Employee.class);
        return query.getResultList();
    }
    
    public Employee authentifierEmp(String mail) {
        String s = "select e from Employee e where e.mail = :unmail";
        Query query = JpaUtil.obtenirContextePersistance().createQuery(s);
        query.setParameter("unmail", mail);
        return (Employee) query.getSingleResult();
        
    }
    
}
