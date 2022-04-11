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
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import static podsistem3.Main.backup;
import static podsistem3.Main.em;
import static podsistem3.Main.myConnFactory;
import zastupnici.*;

/**
 *
 * @author Ivan Savic
 */
public class Sub3Nit extends Thread {
    
    
    
    @Override
    public void run() {
        
        Destination red = backup;
        
        JMSContext context = myConnFactory.createContext();
        JMSConsumer mesta = context.createConsumer(red, "id = 4");
        JMSConsumer komitenti = context.createConsumer(red, "id = 3");
        JMSConsumer filijale = context.createConsumer(red, "id = 5");
        JMSConsumer racuni = context.createConsumer(red, "id = 6");
        JMSConsumer transakcije = context.createConsumer(red, "id = 7");
        
        JMSProducer producer = context.createProducer();
        while(true){
            
            try {
                System.out.println("Spavam");
                Thread.sleep(30000);
                
                System.out.println("Trazim od sub1");
                
                ObjectMessage om = context.createObjectMessage();
                om.setIntProperty("id", 1);
                producer.send(red, om);
                
                System.out.println("Trazim od sub2");
                ObjectMessage o = context.createObjectMessage();
                o.setIntProperty("id", 2);
                producer.send(red, o);
                
                
                Message nM = mesta.receive();
                System.out.println("Dobio mesta");
                ObjectMessage nm = (ObjectMessage)nM;
                Message nK = komitenti.receive();
                System.out.println("Dobio komitente");
                ObjectMessage nk = (ObjectMessage)nK;
                Message nF = filijale.receive();
                System.out.println("Dobio filijale");
                ObjectMessage nf = (ObjectMessage)nF;
                Message nR = racuni.receive();
                System.out.println("Dobio racune");
                ObjectMessage nr = (ObjectMessage)nR;
                Message nT = transakcije.receive();
                System.out.println("Dobio transakcije");
                ObjectMessage nt = (ObjectMessage)nT;
                
                ArrayList<MestoZ> novaMesta =  (ArrayList<MestoZ>)nm.getObject();
                ArrayList<KomitentZ> noviKomitenti =  (ArrayList<KomitentZ>)nk.getObject();
                ArrayList<FilijalaZ> noveFilijale =  (ArrayList<FilijalaZ>)nf.getObject();
                ArrayList<RacunZ> noviRacuni =  (ArrayList<RacunZ>)nr.getObject();
                ArrayList<TransakcijaZ> noveTransakcije =  (ArrayList<TransakcijaZ>)nt.getObject();
                
                System.out.println(novaMesta);
                System.out.println(noviKomitenti);
                System.out.println(noveFilijale);
                System.out.println(noviRacuni);
                System.out.println(noveTransakcije);
               
                dodajMesta(novaMesta);
                dodajKomitente(noviKomitenti);
                dodajFilijale(noveFilijale);
                dodajRacune(noviRacuni);
                dodajTransakcije(noveTransakcije);
   
            } catch (JMSException ex) {
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Sub3Nit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void dodajMesta(ArrayList<MestoZ> mesta){
        
        for( MestoZ m : mesta){
            Mesto mm = em.find(Mesto.class, m.getIdMes());
            if(mm==null){
                mm = new Mesto();
                mm.setNaziv(m.getNaziv());
                mm.setPostanskiBroj(m.getPostanskiBroj());
                mm.setIdMes(m.getIdMes());
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(mm);
                em.flush();
                em.refresh(mm);
                trans.commit();
            }
            
        }
    }
    
    public void dodajKomitente(ArrayList<KomitentZ> komitenti){
        for(KomitentZ k : komitenti){
            Komitent kk = null;
            TypedQuery<Komitent> qM = em.createNamedQuery("Komitent.findByIdKom", Komitent.class);
            qM.setParameter("idKom", k.getIdKom());
            if (qM.getResultList().size() < 1){
                kk = new Komitent();
                kk.setIdKom(k.getIdKom());
                kk.setIdMes(k.getIdMes());
                kk.setAdresa(k.getAdresa());
                kk.setNaziv(k.getNaziv());
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(kk);
                em.flush();
                em.refresh(kk);
                trans.commit();
            }
            else{
                kk = qM.getResultList().get(0);
                kk.setAdresa(k.getAdresa());
                kk.setIdMes(k.getIdMes());
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(kk);
                em.flush();
                em.refresh(kk);
                trans.commit();
            }
            
        }
    }
    public void dodajFilijale(ArrayList<FilijalaZ> filijale ){
        for(FilijalaZ f : filijale){
            Filijala ff = null;
            TypedQuery<Filijala> qM = em.createNamedQuery("Filijala.findByIdFil", Filijala.class);
            qM.setParameter("idFil", f.getIdFil());
            if (qM.getResultList().size() < 1){
                ff = new Filijala();
                ff.setIdFil(f.getIdFil());
                ff.setNaziv(f.getNaziv());
                ff.setIdMes(f.getIdMes());
                ff.setAdresa(f.getAdresa());
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(ff);
                em.flush();
                em.refresh(ff);
                trans.commit();
            }
            
        }
    }
    
    public void dodajRacune(ArrayList<RacunZ> racuni){
        for(RacunZ r : racuni){
            Racun rr = null;
            TypedQuery<Racun> qM = em.createNamedQuery("Racun.findByIdRac", Racun.class);
            qM.setParameter("idRac", r.getIdRac());
            if (qM.getResultList().size() < 1){
                rr = new Racun();
                rr.setIdRac(r.getIdRac());
                rr.setStanje(r.getStanje());
                rr.setDozvMinus(r.getDozvMinus());
                rr.setStatus(r.getStatus());
                rr.setDatum(r.getDatum());
                rr.setVreme(r.getVreme());
                rr.setBrTransakcija(r.getBrTransakcija());
                rr.setIdKom(r.getIdKom());
                rr.setIdMes(r.getIdMes());
                
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(rr);
                em.flush();
                em.refresh(rr);
                trans.commit();
            }
            else{
                rr = qM.getResultList().get(0);
                rr.setStanje(r.getStanje());
                rr.setStatus(r.getStatus());
                rr.setBrTransakcija(r.getBrTransakcija());
                
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(rr);
                em.flush();
                em.refresh(rr);
                trans.commit();
            }
            
        }
    }
    
    public void dodajTransakcije(ArrayList<TransakcijaZ> transakcije){
        for(TransakcijaZ t : transakcije){
            Transakcija tt = new Transakcija();
            
                tt.setIdTrans(t.getIdTrans());
                tt.setDatum(t.getDatum());
                tt.setVreme(t.getVreme());
                tt.setTip(t.getTip());
                tt.setRedniBr(t.getRedniBr());
                tt.setSvrha(t.getSvrha());
                tt.setIdFil(t.getIdFil());
                tt.setRacunSa(t.getRacunSa());
                tt.setRacunNa(t.getRacunNa());
                tt.setIznos(t.getIznos());
                
                EntityTransaction trans = em.getTransaction();
                trans.begin();
                em.persist(tt);
                em.flush();
                em.refresh(tt);
                trans.commit();
            
            
        }
    }
}
