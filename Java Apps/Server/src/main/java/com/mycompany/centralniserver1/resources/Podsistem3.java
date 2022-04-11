/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.centralniserver1.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import zastupnici.RacunZ;

/**
 *
 * @author Ivan Savic
 */
@Path("sub3")
public class Podsistem3 {
    
    @Resource(lookup="projekatConnectionFactory1")
    ConnectionFactory myConnFactory;
    
    
    @Resource(lookup="zahteviKaSub3_1")
    Queue redKaSub3;
    @Resource(lookup="odgovoriOdSub3_1")
    Queue redOdSub3;
    @Resource(lookup="zahteviKaSub2_1")
    Queue redKaSub2;
    @Resource(lookup="odgovoriOdSub2_1")
    Queue redOdSub2;
    @Resource(lookup="zahteviKaSub1_1")
    Queue redKaSub1;
    @Resource(lookup="odgovoriOdSub1_1")
    Queue redOdSub1;
    

    @GET
    @Path("sve")
    public Response dohvSve(){
        
        String ret = null;
        try {
            
            Destination redZahteva = redKaSub3;
            Destination redOdgovora = redOdSub3;
            RacunZ racun = new RacunZ();
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();                      
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(racun);
            oM.setIntProperty("tip", 15);
            producer.send(redZahteva,oM);            
            Message odg = consumer.receive();
            ObjectMessage msg = (ObjectMessage)odg;
            ret = (String) msg.getObject();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.status(Response.Status.OK).entity(ret).build(); 
    }
    @GET
    @Path("razlike")
    public Response razlike(){
        
        String ret = null;
        try {
            
            Destination zahtev1 = redKaSub1;
            Destination zahtev2 = redKaSub2;
            Destination odgovor1 = redOdSub1;
            Destination odgovor2 = redOdSub2;
            RacunZ racun = new RacunZ();
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer1 = context.createConsumer(odgovor1);
            JMSConsumer consumer2 = context.createConsumer(odgovor2);
            JMSProducer producer = context.createProducer();                      
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(racun);
            oM.setIntProperty("tip", 16);
            producer.send(zahtev1,oM);
            
            Message mM = consumer1.receive();
            ObjectMessage mm = (ObjectMessage)mM;
            String mesta = (String) mm.getObject();
            Message mK = consumer1.receive();
            ObjectMessage mk = (ObjectMessage)mK;
            String komitenti = (String) mk.getObject();
            Message mF = consumer1.receive();
            ObjectMessage mf = (ObjectMessage)mF;
            String filijale = (String) mf.getObject();
            
            producer.send(zahtev2,oM);
            Message mR = consumer2.receive();
            ObjectMessage mr = (ObjectMessage)mR;
            String racuni = (String) mr.getObject();
            Message mT = consumer2.receive();
            ObjectMessage mt = (ObjectMessage)mT;
            String transakcije = (String) mt.getObject();
            
            ret = "Razlike\n" +"Mesta \n"+mesta + "\n Komitenti \n"+komitenti+"\n Filijale \n"+filijale+"\n Racuni \n"+racuni+"\n Transakcije \n"+transakcije;
            
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.status(Response.Status.OK).entity(ret).build(); 
    }
}
