/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import static podsistem2.Main.backup;
import static podsistem2.Main.myConnFactory;

/**
 *
 * @author Ivan Savic
 */
public class Sub2Nit extends Thread{

    @Override
    public void run() {
       Destination red = backup;
        
        JMSContext context = myConnFactory.createContext();
        JMSConsumer consumer = context.createConsumer(red, "id = 2");
        JMSProducer producer = context.createProducer();
        
        while (true){
            try {
                System.out.println("Cekam");
                Message m = consumer.receive();
                
                System.out.println("Uzimam racune");
                ObjectMessage racuni = context.createObjectMessage();
                racuni.setIntProperty("id", 6);
                racuni.setObject(Main.noviRacuni);
                producer.send(red, racuni);
                Main.noviRacuni.clear();
                
                System.out.println("Uzimam transakcije");
                ObjectMessage transakcije = context.createObjectMessage();
                transakcije.setIntProperty("id", 7);
                transakcije.setObject(Main.noveTransakcije);
                producer.send(red, transakcije);
                Main.noveTransakcije.clear();

   
            } catch (JMSException ex) {
                
            }
            
        }
    }
    
    
}
