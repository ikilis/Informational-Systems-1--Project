/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;


import entiteti.*;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import zastupnici.*;

/**
 *
 * @author Ivan Savic
 */
public class Main {
    
    static ArrayList<KomitentZ> noviKomitenti = new ArrayList<KomitentZ>();
    static ArrayList<MestoZ> novaMesta = new ArrayList<MestoZ>();
    static ArrayList<FilijalaZ> noveFilijale = new ArrayList<FilijalaZ>();
    
    @Resource(lookup="projekatConnectionFactory1")
    static ConnectionFactory myConnFactory;
    
    @Resource(lookup="zahteviKaSub1_1")
    static Queue redKaSub1;
    
    @Resource(lookup="odgovoriOdSub1_1")
    static Queue redOdSub1;
    
    @Resource(lookup="backupQueue")
    static Queue backup;

    private static EntityManager em;
    
    private static Sub1Nit thread = new Sub1Nit();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
        em = emf.createEntityManager();
        Destination zahtevi = redKaSub1;
        Destination odgovori = redOdSub1;

        JMSContext context = myConnFactory.createContext();
        JMSConsumer consumer = context.createConsumer(zahtevi);
        JMSProducer producer = context.createProducer();
        
        thread.start();
        
        while(true){
            try {
                // TODO code application logic here
                

                Message message = consumer.receive();

                ObjectMessage msg = (ObjectMessage)message;

                int tip = message.getIntProperty("tip");
                String odgovor = new String();
                switch (tip){
                    case 1: // Kreiraj Mesto
                        MestoZ mesto = (MestoZ)msg.getObject();
                        odgovor = kreirajMesto(mesto);
                        break;
                    case 2: //  Kreiraj Filijalu
                        FilijalaZ filijala = (FilijalaZ)msg.getObject();
                        odgovor = kreirajFilijalu(filijala);
                        break;
                    case 3: // Kreiraj Komitenta
                        System.out.println("podsistem1.Main.main() kreiramo komitenta");
                        KomitentZ komitent = (KomitentZ)msg.getObject();
                        odgovor = kreirajKomitenta(komitent);
                        break;
                    case 4: // Promeni sediste
                        KomitentZ aK = (KomitentZ)msg.getObject();
                        odgovor = promeniSediste(aK);
                        break;
                    case 10: //  Dohvatanje svih mesta
                        odgovor = dohvatiSvaMesta();
                        break;
                    case 11: //  Dohvatanje svih filijala  
                        odgovor = dohvatiSveFilijale();
                        break;
                    case 12: //  Dohvatanje svih komitenata  
                        odgovor = dohvatiSveKomitente();
                        break;
                    case 5:
                        RacunZ racun = (RacunZ)msg.getObject();
                        odgovor = proveraZaRacun(racun);
                        break;
                    case 16:// razlike
                        
                        String mesta = new String();
                        for(MestoZ m: novaMesta)
                             mesta += " " + m.getIdMes() + " " + m.getPostanskiBroj()+ " " + m.getNaziv() + "\n";
                        ObjectMessage odg = context.createObjectMessage();
                        odg.setObject(mesta);
                        producer.send(odgovori, odg);
                        
                        String komitenti = new String();
                        for (KomitentZ k : noviKomitenti){
                                komitenti += " "+ k.getIdKom()+" "+k.getNaziv()+" "+k.getAdresa()+ " "+k.getIdMes()+"\n";
                        }
                        ObjectMessage o = context.createObjectMessage();
                        o.setObject(komitenti);
                        producer.send(odgovori, o);
                        for (FilijalaZ f : noveFilijale){
                                odgovor += " " + f.getIdFil()+ " " + f.getNaziv()+ " " + f.getAdresa()+" "+f.getIdMes() + "\n";
                        }
                        
                        break;
                        
                }
                System.out.println("podsistem1.Main.main() saljemo odgovor");
                ObjectMessage odg = context.createObjectMessage();
                odg.setObject(odgovor);
                producer.send(odgovori, odg);
                System.out.println("podsistem1.Main.main() poslali smo odgovor "+tip);
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static String kreirajMesto(MestoZ mesto){
        
       TypedQuery<Mesto> query = em.createNamedQuery("Mesto.findByPostanskiBroj",Mesto.class);
       query.setParameter("postanskiBroj", mesto.getPostanskiBroj());
       List<Mesto> mestaList = query.getResultList();
       if (mestaList.size() > 0){
           return "Neuspesno : takvo mesto vec postoji u bazi";
       }
       Mesto m = new Mesto();
       m.setNaziv(mesto.getNaziv());
       m.setPostanskiBroj(mesto.getPostanskiBroj());
       EntityTransaction trans = em.getTransaction();
       trans.begin();
       em.persist(m);
       em.flush();
       em.refresh(m);
       trans.commit();
       
       TypedQuery<Mesto> q = em.createNamedQuery("Mesto.findByPostanskiBroj",Mesto.class);
       q.setParameter("postanskiBroj", mesto.getPostanskiBroj());
       
       mesto.setIdMes(q.getResultList().get(0).getIdMes());
       novaMesta.add(mesto);

       return "Uspesno : Novo mesto kreirano";
        
    }   
    public static String kreirajFilijalu(FilijalaZ filijala){
        TypedQuery<Mesto> qF = em.createNamedQuery("Mesto.findByIdMes", Mesto.class);
        qF.setParameter("idMes", filijala.getIdMes());
        if (qF.getResultList().size() < 1){
            return "Neuspesno : mesto ne postoji";
        }
        Filijala f = new Filijala();
        f.setAdresa(filijala.getAdresa());
        f.setIdMes(qF.getResultList().get(0));
        f.setNaziv(filijala.getNaziv());
        
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        em.persist(f);
        em.flush();
        em.refresh(f);
        trans.commit();
        
       TypedQuery<Integer> q = em.createQuery("Select max(f.idFil) from Filijala f", Integer.class);
       Integer prim = q.getResultList().get(0);
       
       filijala.setIdFil(prim);
       noveFilijale.add(filijala);
        
        
        return "Uspesno : Nova filijala kreirana";
        
    }
    public static String kreirajKomitenta(KomitentZ komitent){
        TypedQuery<Mesto> qK = em.createNamedQuery("Mesto.findByIdMes", Mesto.class);
        qK.setParameter("idMes", komitent.getIdMes());
        if( qK.getResultList().size() < 1){
            return "Neuspesno : mesto ne postoji";
        }
        Komitent k = new Komitent();
        k.setAdresa(komitent.getAdresa());
        k.setNaziv(komitent.getNaziv());
        k.setIdMes(qK.getResultList().get(0));
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        em.persist(k);
        em.flush();
        em.refresh(k);
        trans.commit();
        
        TypedQuery<Integer> q = em.createQuery("Select max(k.idKom) from Komitent k", Integer.class);
        Integer prim = q.getResultList().get(0);
       
        komitent.setIdKom(prim);
        noviKomitenti.add(komitent);
        
        
        return "Uspesno: Kreiran novi Komitent";
    }
    public static String promeniSediste(KomitentZ komitent){
        
        TypedQuery<Mesto> qM = em.createNamedQuery("Mesto.findByIdMes", Mesto.class);
        qM.setParameter("idMes", komitent.getIdMes());
        if (qM.getResultList().size() < 1){
            return "Neuspesno : takvo mesto ne postoji";
        }
        TypedQuery<Komitent> qK = em.createNamedQuery("Komitent.findByIdKom", Komitent.class);
        qK.setParameter("idKom", komitent.getIdKom());
        if (qK.getResultList().size() < 1){
            return "Neuspesno : takav komitent ne postoji";
        }
        Komitent k = qK.getResultList().get(0);
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        k.setAdresa(komitent.getAdresa());
        k.setIdMes(qM.getResultList().get(0));
        em.persist(k);
        em.flush();
        em.refresh(k);
        trans.commit();
        
        komitent.setIdKom(k.getIdKom());
        komitent.setAdresa(k.getAdresa());
        komitent.setIdMes(k.getIdMes().getIdMes());
        noviKomitenti.add(komitent);
        
        return "Uspesno : promenjeno mesto komitenta";
    
    }
    public static String dohvatiSvaMesta(){
        TypedQuery<Mesto> qM = em.createNamedQuery("Mesto.findAll",Mesto.class);
        ArrayList<Mesto> mesta = new ArrayList<Mesto>(qM.getResultList());
        
        
        String svaMesta = new String();
        if(mesta.size() == 0)return "Nema mesta u bazi";
        for (Mesto m : mesta){
            svaMesta += " " + m.getIdMes() + " " + m.getPostanskiBroj()+ " " + m.getNaziv() + "\n";
        }
        return svaMesta;
    }
    public static String dohvatiSveFilijale(){
        TypedQuery<Filijala> qF = em.createNamedQuery("Filijala.findAll",Filijala.class);
        ArrayList<Filijala> filijale = new ArrayList<Filijala>(qF.getResultList());
        
        
        String sveFilijale = new String();
        if(filijale.size() == 0)return "Nema filijala u bazi";
        for (Filijala f : filijale){
            
            sveFilijale += " " + f.getIdFil()+ " " + f.getNaziv()+ " " + f.getAdresa()+" "+f.getIdMes().getIdMes() + "\n";
        }
        return sveFilijale;
    }
    public static String dohvatiSveKomitente(){
        TypedQuery<Komitent> qK = em.createNamedQuery("Komitent.findAll",Komitent.class);
        ArrayList<Komitent> komitenti = new ArrayList<Komitent>(qK.getResultList());
        
        
        String sviKomitenti = new String();
        if(komitenti.size() == 0)return "Nema komitenata u bazi";
        for (Komitent k : komitenti){
            
           sviKomitenti += " "+ k.getIdKom()+" "+k.getNaziv()+" "+k.getAdresa()+ " "+k.getIdMes().getIdMes()+"\n";
        }
        return sviKomitenti;
        
    }
    public static String proveraZaRacun(RacunZ racun){
        TypedQuery<Mesto> qM = em.createNamedQuery("Mesto.findByIdMes", Mesto.class);
        qM.setParameter("idMes", racun.getIdMes());
        if (qM.getResultList().size() < 1){
            return "Neuspesno: takvo mesto ne postoji";
        }
        TypedQuery<Komitent> qK = em.createNamedQuery("Komitent.findByIdKom", Komitent.class);
        qK.setParameter("idKom", racun.getIdKom());
        if (qK.getResultList().size() < 1){
            return "Neuspesno: takav komitent ne postoji";
        }

        return "Uspesno : promenjeno mesto komitenta";
    }
    
}
