/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import static podsistem1.Main.backup;
import static podsistem1.Main.myConnFactory;
import static podsistem1.Main.novaMesta;
import static podsistem1.Main.noviKomitenti;
import zastupnici.*;

/**
 *
 * @author Ivan Savic
 */
public class Sub1Nit extends Thread{

    
    
    @Override
    public void run() {
       
        Destination red = backup;
        
        JMSContext context = myConnFactory.createContext();
        JMSConsumer consumer = context.createConsumer(red, "id = 1");
        JMSProducer producer = context.createProducer();
        
        while (true){
            try {
                System.out.println("Cekam");
                Message m = consumer.receive();
                
                System.out.println("Uzimam mesta");
                ObjectMessage mesta = context.createObjectMessage();
                mesta.setIntProperty("id", 4);
                mesta.setObject(novaMesta);
                producer.send(red, mesta);
                
                Main.novaMesta.clear();
                
                System.out.println("Uzimam komitente");
                ObjectMessage komitenti = context.createObjectMessage();
                komitenti.setIntProperty("id", 3);
                komitenti.setObject(noviKomitenti);
                producer.send(red, komitenti);
                Main.noviKomitenti.clear();
                
                System.out.println("uzimam filijale");
                ObjectMessage filijale = context.createObjectMessage();
                filijale.setIntProperty("id", 5);
                filijale.setObject(Main.noveFilijale);
                producer.send(red, filijale);
                Main.noveFilijale.clear();
   
            } catch (JMSException ex) {
                
            }
            
        }
        
    }

    
}
