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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import zastupnici.*;

/**
 *
 * @author Ivan Savic
 */

// SAMO SALJEM UPITE KA PODSISTEMIMA I VRACAM ODGOVOR, PROVERE IDU U PODSISTEM
@Path("sub1")
public class Podsistem1 {
    
    
    @Resource(lookup="projekatConnectionFactory1")
    ConnectionFactory myConnFactory;
    
    @Resource(lookup="zahteviKaSub1_1")
    Queue redKaSub1;
    
    @Resource(lookup="odgovoriOdSub1_1")
    Queue redOdSub1;
    
    @Resource(lookup="zahteviKaSub2_1")
    Queue redKaSub2;
    
    @Resource(lookup="odgovoriOdSub2_1")
    Queue redOdSub2;
    
    @Resource(lookup="zahteviKaSub3_1")
    Queue redKaSub3;
    
    @Resource(lookup="odgovoriOdSub3_1")
    Queue redOdSub3;
    @Resource(lookup="backupQueue")
    Queue bekap;
    
    @GET
    @Path("isprazniRedove")
    public Response isprazniRedove(){
        Destination ka1 = redKaSub1;
        Destination od1 = redOdSub1;
        Destination ka2 = redKaSub2;
        Destination od2 = redOdSub2;
        Destination ka3 = redKaSub3;
        Destination od3 = redOdSub3;
        Destination bek = bekap;
            
            
        JMSContext context = myConnFactory.createContext();
        JMSConsumer c1 = context.createConsumer(ka1);
        JMSConsumer c2 = context.createConsumer(ka2);
        JMSConsumer c3 = context.createConsumer(od1);
        JMSConsumer c4 = context.createConsumer(od2);
        JMSConsumer c5 = context.createConsumer(ka3);
        JMSConsumer c6 = context.createConsumer(od3);
        JMSConsumer c7 = context.createConsumer(bek);
        
        while(true){
            Message m1 = c1.receiveNoWait();
            Message m2 = c2.receiveNoWait();
            Message m3 = c3.receiveNoWait();
            Message m4 = c4.receiveNoWait();
            Message m5 = c5.receiveNoWait();
            Message m6 = c6.receiveNoWait();
            Message m7 = c7.receiveNoWait();
            if(m1==null && m2==null && m3==null && m4==null && m5==null && m6==null && m7==null)break;         
        }
        return Response.status(Response.Status.OK).entity("Ispraznili smo sve redove").build();
    }
    
    
    @POST //1
    @Path("kreirajMesto/{zip}/{naziv}")
    public Response kreirajMesto(@PathParam("zip")int zip, @PathParam("naziv") String naziv){
        
        Destination redZahteva = redKaSub1;
        Destination redOdgovora = redOdSub1;
            
            
        JMSContext context = myConnFactory.createContext();
        JMSConsumer consumer = context.createConsumer(redOdgovora);
        JMSProducer producer = context.createProducer();
        
 
 
        String ret = null;
        try {
           

            
            MestoZ mesto = new MestoZ();
            mesto.setIdMes(0);
            mesto.setPostanskiBroj(zip);
            mesto.setNaziv(naziv);
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(mesto);
            oM.setIntProperty("tip", 1);
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
    
    @POST //2
    @Path("kreirajFilijalu/{naziv}/{zip}/{adresa}")
    public Response kreirajFilijalu(@PathParam("naziv")String naziv, @PathParam("zip")int idMes,  @PathParam("adresa")String adresa){
        
        // U PODSISTEM 1
        // Stvori Filijalu 
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub1;
           Destination redOdgovora = redOdSub1;
           
            
            FilijalaZ filijala = new FilijalaZ();
            filijala.setAdresa(adresa);
            filijala.setIdFil(0);
            filijala.setIdMes(idMes);
            filijala.setNaziv(naziv);
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(filijala);
            oM.setIntProperty("tip", 2);
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
    @POST //3
    @Path("kreirajKomitenta/{naziv}/{mesto}/{adresa}")
    public Response kreirajKomitenta(@PathParam("naziv") String naziv, @PathParam("mesto")int idMes, @PathParam("adresa") String adresa){
        
         
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub1;
           Destination redOdgovora = redOdSub1;
           Destination javiSub2 = redKaSub2;
            
            KomitentZ komitent = new KomitentZ();
            komitent.setAdresa(adresa);
            komitent.setIdKom(0);
            komitent.setIdMes(idMes);
            komitent.setNaziv(naziv);
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(komitent);
            oM.setIntProperty("tip", 3);
            producer.send(redZahteva,oM);
            
            Message odg = consumer.receive();
            ObjectMessage msg = (ObjectMessage)odg;
            ret = (String) msg.getObject();
            String cao = ret;
            
            if ( (ret.split(" ")[0]).equals("Uspesno:") ){       
                  producer.send(javiSub2,oM);
                  System.out.println(cao);
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.status(Response.Status.OK).entity(ret).build();
    }
    @POST //4
    @Path("promeniSediste/{komitent}/{novoMesto}/{novaAdresa}")
    public Response promeniSediste(@PathParam("komitent")int idKom, @PathParam("novoMesto")int mestoId, @PathParam("novaAdresa")String novaAdresa){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub1;
           Destination redOdgovora = redOdSub1;
           Destination javiSub2 = redKaSub2;
            
            KomitentZ komitent = new KomitentZ();
            komitent.setAdresa(novaAdresa);
            komitent.setIdKom(idKom);
            komitent.setIdMes(mestoId);
            komitent.setNaziv(null);
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(komitent);
            oM.setIntProperty("tip", 4);
            producer.send(redZahteva,oM);
            
            Message odg = consumer.receive();
            ObjectMessage msg = (ObjectMessage)odg;
            ret = (String) msg.getObject();
            
            if ( ret.split(" ")[0].equals("Uspesno")){
                  producer.send(javiSub2, oM);   // POSALJJI U SUB 2 DA SE Azurira
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.status(Response.Status.OK).entity(ret).build();
        
    }
    @GET //10
    @Path("svaMesta")
    public Response dohvSvaMesta(){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub1;
           Destination redOdgovora = redOdSub1;
            
            KomitentZ komitent = new KomitentZ();
            
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(komitent);
            oM.setIntProperty("tip", 10);
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
    
    @GET // 12
    @Path("sviKomitenti")
    public Response dohvSveKomitente(){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub1;
           Destination redOdgovora = redOdSub1;
            
            KomitentZ komitent = new KomitentZ();
            
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(komitent);
            oM.setIntProperty("tip", 12);
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
    
    @GET // 11
    @Path("sveFilijale")
    public Response dohvSveFilijale(){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub1;
           Destination redOdgovora = redOdSub1;
            
            KomitentZ komitent = new KomitentZ();
            
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(komitent);
            oM.setIntProperty("tip", 11);
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
    
    
}
