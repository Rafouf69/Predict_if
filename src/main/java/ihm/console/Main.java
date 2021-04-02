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
import metier.services.*;

public class Main {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        JpaUtil.init();
        ControleService Controle= new ControleService();
        Controle.initdevversion();
        
       
        //Controle.testerRechercheClient();
        //Controle.testerAuthentificationClient();
        JpaUtil.destroy();
    }

    
    
    
    
    
}
