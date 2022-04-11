/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.*;
import java.util.ArrayList;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ivan Savic
 */
public class Main {
    
    @Resource(lookup="projekatConnectionFactory1")
    static ConnectionFactory myConnFactory;
    
    @Resource(lookup="zahteviKaSub3_1")
    static Queue redKaSub3;
    
    @Resource(lookup="odgovoriOdSub3_1")
    static Queue redOdSub3;
    
    @Resource(lookup="backupQueue")
    static Queue backup;

    static EntityManager em;
    
    private static Sub3Nit thread = new Sub3Nit();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");
        em = emf.createEntityManager();
        Destination zahtevi = redKaSub3;
        Destination odgovori = redOdSub3;

        JMSContext context = myConnFactory.createContext();
        JMSConsumer consumer = context.createConsumer(zahtevi);
        JMSProducer producer = context.createProducer();
        
        thread.start();
         
        while(true){
            try {
                Message message = consumer.receive();
                String odgovor = null;
                odgovor = dohvatiSve();
                ObjectMessage odg = context.createObjectMessage();
                odg.setObject(odgovor);
                producer.send(odgovori, odg);
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public static String dohvatiSve(){
        TypedQuery<Mesto> qM = em.createNamedQuery("Mesto.findAll",Mesto.class);
        ArrayList<Mesto> mesta = new ArrayList<Mesto>(qM.getResultList());
        
        
        String svaMesta = new String();
        if(mesta.size() == 0)svaMesta= "Nema mesta u bazi";
        for (Mesto m : mesta){
            svaMesta += " " + m.getIdMes() + " " + m.getPostanskiBroj()+ " " + m.getNaziv() + "\n";
        }
        TypedQuery<Komitent> qK = em.createNamedQuery("Komitent.findAll",Komitent.class);
        ArrayList<Komitent> komitenti = new ArrayList<Komitent>(qK.getResultList());
        
        
        String sviKomitenti = new String();
        if(komitenti.size() == 0)sviKomitenti =  "Nema mesta u bazi";
        for (Komitent k : komitenti){
            sviKomitenti += " " + k.getIdKom()+ " " + k.getNaziv()+ " " + k.getIdMes()+ "\n";
        }
        
        TypedQuery<Filijala> qF = em.createNamedQuery("Filijala.findAll",Filijala.class);
        ArrayList<Filijala> filijale = new ArrayList<Filijala>(qF.getResultList());
        String sveFilijale = new String();
        if(filijale.size() == 0)sveFilijale = "Nema filijala u bazi";
        for (Filijala f : filijale){
            sveFilijale += " " + f.getIdFil()+ " " + f.getNaziv()+ " " + f.getIdMes()+ "\n";
        }
        TypedQuery<Racun> qR = em.createNamedQuery("Racun.findAll",Racun.class);
        ArrayList<Racun> racuni = new ArrayList<Racun>(qR.getResultList());
        String sviRacuni = new String();
        if(racuni.size() == 0)sveFilijale = "Nema racuna u bazi";
        for (Racun r : racuni){
            sviRacuni += r.getIdRac()+ " " + r.getStanje() + " " + r.getDozvMinus()+ " " + r.getStatus() + " " +
                                   r.getDatum() + " " + r.getVreme() +" "+ r.getBrTransakcija() + '\n';
        }
        TypedQuery<Transakcija> qT = em.createNamedQuery("Transakcija.findAll",Transakcija.class);
        ArrayList<Transakcija> transakcije = new ArrayList<Transakcija>(qT.getResultList());
        String sveTransakcije = new String();
        if(transakcije.size() == 0)sveFilijale = "Nema transakcija u bazi";
        for (Transakcija t : transakcije){
            Integer idSa = null;
            Integer idNa = null;
            if(t.getRacunNa()!=null)idNa = t.getRacunNa();
            if(t.getRacunSa()!=null)idSa = t.getRacunSa();
           sveTransakcije += t.getIdTrans()+" "+idNa+" "+idSa+" "+t.getRedniBr()+" "+t.getTip()+" "+t.getIznos()+ " "+ t.getSvrha()+ " "+t.getVreme()+" "+t.getDatum()+" "+t.getIdFil()+'\n';
        }
        
        String ret = "Mesta \n"+svaMesta + "\n Komitenti \n"+sviKomitenti+"\n Filijale \n"+sveFilijale+"\n Racuni \n"+sviRacuni+"\n Transakcije \n"+sveTransakcije;
        return ret;
    
    }
    
}
