package ihm.console;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author louislombard
 */
import dao.JpaUtil;
import java.util.Arrays;
import java.util.List;
import metier.modele.Client;
import metier.modele.Employee;

public class Main {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        JpaUtil.init();
        ControleService Controle= new ControleService();
        Controle.initdevversion(20,20,20);
        
        //Definir quelle version de l'app
        List<Integer> listvalue = Arrays.asList(new Integer[]{0,1,2,3});
        Client UserClient=null;
        Employee UserEmp=null;
        
        int role=-1;
      
        while (role!=0){
           
            role= Saisie.lireInteger("EMPLOYEE (1) ou CLIENT-INSCRIT (2) ou NOUVEAU-CLIENT(3) ou QUITTER(0) : ",listvalue);

          if (role==2){    
              while(UserClient==null){
                  try {
                      UserClient=Controle.testerAuthentificationClient();
                  }catch(Exception Ex){
                      System.out.println(Ex);
                  } 
          }
          }else if (role==1){
              while(UserEmp==null){
                  try {
                      UserEmp=Controle.testerAuthentificationEmployee();
                  }catch(Exception Ex){
                      System.out.println(Ex);
                  } 
              }
          } else if (role==3){
              while(UserClient==null){
                  try {
                      UserClient=Controle.testerInscriptionClient();
                  }catch(Exception Ex){
                      System.out.println(Ex);
                  } 
              }
          } 

          //rentrer dans la boucle des actions
          int i=-1;
          while(i!=0 && role!=0){
              if (role==1){
                   i=Controle.runningserviceEmployee(UserEmp);

              }else if (role==2){
                  i=Controle.runningserviceClient(UserClient);
              }
          }
        }
        
        
        
        JpaUtil.destroy();
    }

    
    
    
    
    
}
