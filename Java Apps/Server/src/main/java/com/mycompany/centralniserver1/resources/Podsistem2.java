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
import zastupnici.KomitentZ;
import zastupnici.*;

/**
 *
 * @author Ivan Savic
 */

@Path("sub2")
public class Podsistem2 {
    
    
    @Resource(lookup="projekatConnectionFactory1")
    ConnectionFactory myConnFactory;
    
    
    @Resource(lookup="zahteviKaSub2_1")
    Queue redKaSub2;
    
    @Resource(lookup="odgovoriOdSub2_1")
    Queue redOdSub2;
    
    @Resource(lookup="zahteviKaSub1_1")
    Queue redKaSub1;
    
    @Resource(lookup="odgovoriOdSub1_1")
    Queue redOdSub1;
    
    
    @POST
    @Path("otvaranjeRacuna/{komitent}/{mesto}/{dozvMinus}")
    public Response otvaranjeRacuna(@PathParam("komitent") int idK, @PathParam("mesto")int idMes, @PathParam("dozvMinus")float dozvoljeniMinus){
        //DATUM I VREME STAVIMO U PODSISTEMU JER TEK TAD SE STVARNO KREIRA
            
        // ubaci ono za ciscenje poruka iz kreijraj mesto

        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub2;
           Destination redOdgovora = redOdSub2;
           
           Destination proveraZ = redKaSub1;
           Destination odgovorProvere = redOdSub1;
           
           
           
            RacunZ racun = new RacunZ();
            racun.setBrTransakcija(0);
            racun.setDozvMinus(dozvoljeniMinus);
            racun.setIdKom(idK);
            racun.setIdMes(idMes);
            racun.setStanje(0);
            racun.setStatus('A');
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();  
            JMSConsumer p = context.createConsumer(odgovorProvere);
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(racun);
            oM.setIntProperty("tip", 5);
            
            
            
            producer.send(proveraZ,oM);
            Message o = p.receive();
            ObjectMessage oo = (ObjectMessage)o;
            ret = (String) oo.getObject();
            if ( (ret.split(" ")[0]).equals("Neuspesno:") ){         
                return Response.status(Response.Status.OK).entity(ret).build();    
            }
                    
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


    @POST
    @Path("zatvaranjeRacuna/{komitent}/{racun}")
    public Response zatvaranjeRacuna(@PathParam("komitent") int idK, @PathParam("racun") int idRac){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub2;
           Destination redOdgovora = redOdSub2;
    
            RacunZ racun = new RacunZ();
            racun.setIdRac(idRac);
            racun.setIdKom(idK);
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(racun);
            oM.setIntProperty("tip", 6);
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
    
    @POST
    @Path("prenos/{racun1}/{racun2}/{suma}/{svrha}")
    public Response prenosNovca(@PathParam("racun1")int idRac1, @PathParam("racun2")int idRac2, @PathParam("suma")float suma, @PathParam("svrha")String svrha){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub2;
           Destination redOdgovora = redOdSub2;
    
            TransakcijaZ transakcija = new TransakcijaZ();
            transakcija.setRacunSa(idRac1);
            transakcija.setRacunNa(idRac2);
            transakcija.setIznos(suma);
            transakcija.setTip('P');
            transakcija.setSvrha(svrha);
            
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(transakcija);
            oM.setIntProperty("tip", 7);
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
    
    @POST
    @Path("uplata/{racun}/{suma}/{svrha}/{idFil}")
    public Response uplata(@PathParam("racun")int idRac, @PathParam("suma")float suma, @PathParam("svrha")String svrha, @PathParam("idFil")int idFil){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub2;
           Destination redOdgovora = redOdSub2;
    
            TransakcijaZ transakcija = new TransakcijaZ();
            transakcija.setRacunNa(idRac);
            transakcija.setIznos(suma);
            transakcija.setTip('U');
            transakcija.setSvrha(svrha);
            transakcija.setIdFil(idFil);
            
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(transakcija);
            oM.setIntProperty("tip", 8);
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
    @POST
    @Path("isplata/{racun}/{suma}/{svrha}/{idFil}")
    public Response isplata(@PathParam("racun")int idRac, @PathParam("suma")float suma, @PathParam("svrha")String svrha, @PathParam("idFil")int idFil){
        
        String ret = null;
        try {
           
            
           Destination redZahteva = redKaSub2;
           Destination redOdgovora = redOdSub2;
    
            TransakcijaZ transakcija = new TransakcijaZ();
            transakcija.setRacunSa(idRac);
            transakcija.setRacunNa(null);
            transakcija.setIznos(suma);
            transakcija.setTip('I');
            transakcija.setSvrha(svrha);
            transakcija.setIdFil(idFil);
            
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(transakcija);
            oM.setIntProperty("tip", 9);
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
    @Path("racuni/{komitent}")
    public Response dohvRacune(@PathParam("komitent")int idKom){
        
        String ret = null;
        try {
           
            
            Destination redZahteva = redKaSub2;
            Destination redOdgovora = redOdSub2;
    
            KomitentZ komitent = new KomitentZ();
            komitent.setIdKom(idKom);
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(komitent);
            oM.setIntProperty("tip", 13);
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
    @Path("transakcije/{racun}")
    public Response dohvTransakcije(@PathParam("racun")int idRac){
        
        String ret = null;
        try {
           
            
            Destination redZahteva = redKaSub2;
            Destination redOdgovora = redOdSub2;
    
            RacunZ racun = new RacunZ();
            racun.setIdRac(idRac);
            
            JMSContext context = myConnFactory.createContext();
            JMSConsumer consumer = context.createConsumer(redOdgovora);
            JMSProducer producer = context.createProducer();          
            
            ObjectMessage oM = context.createObjectMessage();
            oM.setObject(racun);
            oM.setIntProperty("tip", 14);
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
