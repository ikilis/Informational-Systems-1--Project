/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    
    static ArrayList<RacunZ> noviRacuni = new ArrayList<RacunZ>();
    static ArrayList<TransakcijaZ> noveTransakcije = new ArrayList<TransakcijaZ>();
    
    
    @Resource(lookup="projekatConnectionFactory1")
    static ConnectionFactory myConnFactory;
    
    @Resource(lookup="zahteviKaSub2_1")
    static Queue redKaSub2;
    
    @Resource(lookup="odgovoriOdSub2_1")
    static Queue redOdSub2;
    
    @Resource(lookup="backupQueue")
    static Queue backup;
            
    private static EntityManager em;
    
    private static Sub2Nit thread = new Sub2Nit();
            
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
        em = emf.createEntityManager();
        Destination zahtevi = redKaSub2;
        Destination odgovori = redOdSub2;

        JMSContext context = myConnFactory.createContext();
        JMSConsumer consumer = context.createConsumer(zahtevi);
        JMSProducer producer = context.createProducer();
        
        System.out.println("podsistem2.Main.main()");
        thread.start();

        
        while(true){
                 
            boolean saljeOdgovor = true;
            try {
                // TODO code application logic here
                

                Message message = consumer.receive();

                ObjectMessage msg = (ObjectMessage)message;

                int tip = message.getIntProperty("tip");
                String odgovor = new String();;
                switch (tip){
                    case 3: //  DodajKomitenta
                        KomitentZ komitent = (KomitentZ)msg.getObject();
                        odgovor = kreirajKomitenta(komitent);
                        saljeOdgovor = false;
                        break;
                    case 4: // Azuriraj komitenta
                        KomitentZ aK = (KomitentZ)msg.getObject();
                        odgovor = promeniSediste(aK);
                        saljeOdgovor = false;
                        break;
                    case 5: //  Otvaranje racuna
                        RacunZ rac = (RacunZ)msg.getObject();
                        odgovor = kreirajRacun(rac);
                        break;
                    case 6: //  Zatvaranje Racuna
                        RacunZ racun = (RacunZ)msg.getObject();
                        odgovor = zatvoriRacun(racun);
                        break;
                    case 7: //  Napravi prenos
                        TransakcijaZ prenos = (TransakcijaZ)msg.getObject();
                        odgovor = kreirajTransakciju(prenos);
                        break;
                    case 8: //  Napravi uplatu
                        TransakcijaZ uplata = (TransakcijaZ)msg.getObject();
                        odgovor = kreirajUplatu(uplata);
                        break;
                    case 9: //  Napravi isplatu
                        TransakcijaZ isplata = (TransakcijaZ)msg.getObject();
                        odgovor = kreirajIsplatu(isplata);
                        break;
                    case 13: // Dohvati sve racune za komitenta
                        KomitentZ kr = (KomitentZ)msg.getObject();
                        odgovor = dohvatiSveRacune(kr);
                        break;
                    case 14:
                        RacunZ rT = (RacunZ)msg.getObject();
                        odgovor = dohvatiSveTransakcije(rT);
                        break; 
                    case 16:// razlike
                        
                        String racuni = new String();
                        for(RacunZ r: noviRacuni)
                            racuni += r.getIdRac()+ " " + r.getStanje() + " " + r.getDozvMinus()+ " " + r.getStatus() + " " +
                                   r.getDatum() + " " + r.getVreme() +" "+ r.getBrTransakcija() + '\n';
                        ObjectMessage poruka = context.createObjectMessage();
                        poruka.setObject(racuni);
                        producer.send(odgovori, poruka);
                        
                        for (TransakcijaZ t : noveTransakcije){
                                odgovor = t.getIdTrans()+" "+t.getRacunNa()+" "+t.getRacunSa()+" "+t.getRedniBr()+" "+t.getTip()+" "+t.getIznos()+ " "+ t.getSvrha()+ " "+t.getVreme()+" "+t.getDatum()+" "+t.getIdFil()+'\n';     
                        }
                        
                        break;
                }
                
                if(saljeOdgovor == true){
                    System.out.println("saljemo odgovoe "+tip);
                    System.out.println(odgovor);
                    ObjectMessage odg = context.createObjectMessage();
                    odg.setObject(odgovor);
                    producer.send(odgovori, odg);
                }
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public static String kreirajKomitenta(KomitentZ komitent){

        Komitent k = new Komitent();
        k.setAdresa(komitent.getAdresa());
        k.setNaziv(komitent.getNaziv());
        k.setIdMes(komitent.getIdMes());
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        em.persist(k);
        em.flush();
        em.refresh(k);
        trans.commit();
        return "Uspesno: Kreiran novi Komitent";
    }
    public static String promeniSediste(KomitentZ komitent){
        
        TypedQuery<Komitent> qK = em.createNamedQuery("Komitent.findByIdKom", Komitent.class);
        qK.setParameter("idKom", komitent.getIdKom());
        Komitent k = qK.getResultList().get(0);
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        k.setAdresa(komitent.getAdresa());
        k.setIdMes(komitent.getIdMes());
        em.persist(k);
        em.flush();
        em.refresh(k);
        trans.commit();
        return "Uspesno : promenjeno mesto komitenta";
    
    }
    public static String kreirajRacun(RacunZ racun){

        TypedQuery<Komitent> query = em.createNamedQuery("Komitent.findByIdKom",Komitent.class);
        query.setParameter("idKom", racun.getIdKom());
        List<Komitent> kL = query.getResultList();
        
        if( kL.isEmpty())return "Neuspesno ne postoji komitent";
        
        Racun r = new Racun();
        r.setBrTransakcija(0);
        r.setDozvMinus(racun.getDozvMinus());
        r.setIdMes(racun.getIdMes());
        r.setIdKom(kL.get(0));
        r.setDatum(new java.util.Date());
        r.setVreme(new java.util.Date());
        r.setStatus('A');
        r.setStanje(0);

        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        em.persist(r);
        em.flush();
        em.refresh(r);
        trans.commit();
        TypedQuery<Integer> q = em.createQuery("Select max(r.idRac) from Racun r", Integer.class);
        Integer prim = q.getResultList().get(0);
        
        racun.setBrTransakcija(0);
        racun.setIdRac(prim);
        racun.setDatum(r.getDatum());
        racun.setVreme(r.getVreme());
        racun.setStanje(0);
        racun.setStatus('A');
        
        noviRacuni.add(racun);
        
        return "Uspesno: Kreiran novi Racun";
    }
    public static String zatvoriRacun(RacunZ racun){
 /*       TypedQuery<Komitent> query = em.createNamedQuery("Komitent.findByIdKom",Komitent.class);
        query.setParameter("idKom", racun.getIdKom());
        List<Komitent> kL = query.getResultList();
        if( kL.isEmpty())return "Neuspesno ne postoji komitent";
  */      
        TypedQuery<Racun> q = em.createNamedQuery("Racun.findByIdRac",Racun.class);
        q.setParameter("idRac", racun.getIdRac());
        List<Racun> racuni = q.getResultList();
        if( racuni.isEmpty())return "Neuspesno ne postoji taj racun";
        
        Racun menjani = racuni.get(0);
        if(menjani.getStatus() == 'U')return "Racun je vec ugasen";
        
        if( racun.getIdKom() != menjani.getIdKom().getIdKom())return "Neuspesno ovaj komitent mu nije vlasnik";
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        menjani.setStatus('U');
        em.persist(menjani);
        em.flush();
        em.refresh(menjani);
        trans.commit();
        
        
        racun.setDozvMinus(menjani.getDozvMinus());
        racun.setIdMes(menjani.getIdMes());
        racun.setIdKom(menjani.getIdKom().getIdKom());
        racun.setBrTransakcija(menjani.getBrTransakcija());
        racun.setIdRac(menjani.getIdRac());
        racun.setDatum(menjani.getDatum());
        racun.setVreme(menjani.getVreme());
        racun.setStanje(menjani.getStanje());
        racun.setStatus(menjani.getStatus());
        noviRacuni.add(racun);
        
        return "Racun je sada ugasen";        
    }
    public static String kreirajTransakciju(TransakcijaZ t){
        
        TypedQuery<Racun> q = em.createNamedQuery("Racun.findByIdRac",Racun.class);
        q.setParameter("idRac", t.getRacunNa());
        List<Racun> racuni = q.getResultList();
        if( racuni.isEmpty())return "Neuspesno ne postoji racun na koji prebacujemo";
        TypedQuery<Racun> qq = em.createNamedQuery("Racun.findByIdRac",Racun.class);
        qq.setParameter("idRac", t.getRacunSa());
        List<Racun> rracuni = qq.getResultList();
        if(rracuni.isEmpty())return "Neuspesno ne postoji racun sa kojeg prebacujemo";
        
        Racun racunNa = racuni.get(0);
        Racun racunSa = rracuni.get(0);
        
        if(racunSa.getStatus()!='A')return "Neuspesno Slanje mozemo izvrsiti samo sa aktivnih racuna";
        if( racunNa.getStatus()=='U' ) return "Neuspesno Ne mozemo slati na ugaseni racun";
        
        racunSa.setBrTransakcija(racunSa.getBrTransakcija()+1);
        racunNa.setBrTransakcija(racunNa.getBrTransakcija()+1);
        
        racunSa.setStanje(racunSa.getStanje()- t.getIznos());
        float negativanMinus = 0 - racunSa.getDozvMinus();
        if(racunSa.getStanje() < negativanMinus)racunSa.setStatus('B');
        
        
        racunNa.setStanje(racunNa.getStanje() + t.getIznos());
        if(racunNa.getStatus() == 'B'){
            negativanMinus = 0 - racunNa.getDozvMinus();
            if(racunNa.getStanje()>negativanMinus)racunNa.setStatus('A'); 
        }       
        
        Transakcija transakcija = new Transakcija();
        transakcija.setDatum(new java.util.Date());
        transakcija.setVreme(new java.util.Date());
        transakcija.setIdFil(null);
        transakcija.setIznos(t.getIznos());
        transakcija.setRacunNa(racunNa);
        transakcija.setRacunSa(racunSa);
        transakcija.setSvrha(t.getSvrha());
        transakcija.setTip('P');
        
        int rbr = racunSa.getBrTransakcija();
        transakcija.setRedniBr(rbr);
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        
        em.persist(transakcija);
        em.flush();
        em.refresh(transakcija);
        
        em.persist(racunNa);
        em.flush();
        em.refresh(racunNa);
        em.persist(racunSa);
        em.flush();
        em.refresh(racunSa);
        trans.commit();

        TypedQuery<Integer> kveri = em.createQuery("Select max(t.idTrans) from Transakcija t", Integer.class);
        Integer prim = kveri.getResultList().get(0);
        
        
        
        TransakcijaZ tt = new TransakcijaZ();
        tt.setDatum(new java.util.Date());
        tt.setVreme(new java.util.Date());
        tt.setIdFil(null);
        tt.setIznos(t.getIznos());
        tt.setRacunNa(racunNa.getIdRac());
        tt.setRacunSa(racunSa.getIdRac());
        tt.setSvrha(t.getSvrha());
        tt.setTip('P');
        tt.setIdTrans(prim);
        tt.setRedniBr(rbr);

        
        RacunZ rNa = new RacunZ();
        rNa.setIdRac(racunNa.getIdRac());
        rNa.setDozvMinus(racunNa.getDozvMinus());;
        rNa.setIdKom(racunNa.getIdKom().getIdKom());
        rNa.setBrTransakcija(racunNa.getBrTransakcija());
        rNa.setIdMes(racunNa.getIdRac());
        rNa.setDatum(racunNa.getDatum());
        rNa.setVreme(racunNa.getVreme());
        rNa.setStanje(racunNa.getStanje());
        rNa.setStatus(racunNa.getStatus());
        
        RacunZ rSa = new RacunZ();
        rSa.setIdRac(racunSa.getIdRac());
        rSa.setDozvMinus(racunSa.getDozvMinus());
        rSa.setIdKom(racunSa.getIdKom().getIdKom());
        rSa.setBrTransakcija(racunSa.getBrTransakcija());
        rSa.setIdMes(racunSa.getIdRac());
        rSa.setDatum(racunSa.getDatum());
        rSa.setVreme(racunSa.getVreme());
        rSa.setStanje(racunSa.getStanje());
        rSa.setStatus(racunSa.getStatus());
        
        noviRacuni.add(rSa);
        noviRacuni.add(rNa);
        noveTransakcije.add(tt);
        
        
        
        return "Uspesno kreirana transakcija"; 
    }
    public static String kreirajUplatu(TransakcijaZ uplata){
        TypedQuery<Racun> q = em.createNamedQuery("Racun.findByIdRac",Racun.class);
        q.setParameter("idRac", uplata.getRacunNa());
        System.out.println(uplata.getRacunNa());
        List<Racun> racuni = q.getResultList();
        if( racuni.isEmpty())return "Neuspesno ne postoji racun na koji uplacujemo";
        
        Racun racunNa = racuni.get(0);
        if( racunNa.getStatus()=='U' ) return "Neuspesno Ne mozemo slati na ugaseni racun";

        Transakcija transakcija = new Transakcija();
        transakcija.setDatum(new java.util.Date());
        transakcija.setVreme(new java.util.Date());
        transakcija.setIdFil(uplata.getIdFil());
        transakcija.setIznos(uplata.getIznos());
        transakcija.setRacunNa(racunNa);
        transakcija.setRacunSa(null);
        transakcija.setSvrha(uplata.getSvrha());
        transakcija.setTip('U');
        
        float negativanMinus = 0 - racunNa.getDozvMinus();
        racunNa.setStanje(racunNa.getStanje() + uplata.getIznos());
        
        if(racunNa.getStatus()=='B'){   //  Proveri treba li da se odblokira
           
            if(racunNa.getStanje()>negativanMinus)racunNa.setStatus('A'); 
        }
        racunNa.setBrTransakcija(racunNa.getBrTransakcija()+1);
        int rbr = racunNa.getBrTransakcija();
        transakcija.setRedniBr(rbr);
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        
        em.persist(transakcija);
        em.flush();
        em.refresh(transakcija);     
        em.persist(racunNa);
        em.flush();
        em.refresh(racunNa);
        
        trans.commit();
        
        TypedQuery<Integer> kveri = em.createQuery("Select max(t.idTrans) from Transakcija t", Integer.class);
        Integer prim = kveri.getResultList().get(0);
        
        TransakcijaZ tt = new TransakcijaZ();
        tt.setDatum(new java.util.Date());
        tt.setVreme(new java.util.Date());
        tt.setIdFil(transakcija.getIdFil());
        tt.setIznos(transakcija.getIznos());
        tt.setRacunNa(racunNa.getIdRac());
        tt.setRacunSa(null);
        tt.setSvrha(transakcija.getSvrha());
        tt.setTip('U');
        tt.setIdTrans(prim);
        tt.setRedniBr(rbr);
        
        RacunZ rNa = new RacunZ();
        rNa.setIdRac(racunNa.getIdRac());
        rNa.setDozvMinus(racunNa.getDozvMinus());
        rNa.setIdMes(racunNa.getIdMes());
        rNa.setIdKom(racunNa.getIdKom().getIdKom());
        rNa.setBrTransakcija(racunNa.getBrTransakcija());
        rNa.setIdMes(racunNa.getIdRac());
        rNa.setDatum(racunNa.getDatum());
        rNa.setVreme(racunNa.getVreme());
        rNa.setStanje(racunNa.getStanje());
        rNa.setStatus(racunNa.getStatus());
        
        noveTransakcije.add(tt);
        noviRacuni.add(rNa);
        
        return "Uspesno kreirana uplata";
  
    }
    public static String kreirajIsplatu(TransakcijaZ isplata){
        TypedQuery<Racun> q = em.createNamedQuery("Racun.findByIdRac",Racun.class);
        q.setParameter("idRac", isplata.getRacunSa());
        List<Racun> racuni = q.getResultList();
        if( racuni.isEmpty())return "Neuspesno ne postoji racun sa kojeg isplacujemo";
        
        Racun racunSa = racuni.get(0);
        if( racunSa.getStatus()!='A' ) return "Neuspesno Ne mozemo slati sa racuna koji su ugaseni ili blokirani";

        Transakcija transakcija = new Transakcija();
        transakcija.setDatum(new java.util.Date());
        transakcija.setVreme(new java.util.Date());
        transakcija.setIdFil(isplata.getIdFil());
        transakcija.setIznos(isplata.getIznos());
        transakcija.setRacunNa(null);
        transakcija.setRacunSa(racunSa);
        transakcija.setSvrha(isplata.getSvrha());
        transakcija.setTip('I');
        
        racunSa.setBrTransakcija(racunSa.getBrTransakcija()+1);
        
        int rbr = racunSa.getBrTransakcija();
        transakcija.setRedniBr(rbr);
                
        racunSa.setStanje(racunSa.getStanje()- isplata.getIznos());
        float negativanMinus = 0 - racunSa.getDozvMinus();
        if(racunSa.getStanje() < negativanMinus)racunSa.setStatus('B');
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        
        em.persist(transakcija);
        em.flush();
        em.refresh(transakcija);     
        em.persist(racunSa);
        em.flush();
        em.refresh(racunSa);
        
        trans.commit();
        
        TypedQuery<Integer> kveri = em.createQuery("Select max(t.idTrans) from Transakcija t", Integer.class);
        Integer prim = kveri.getResultList().get(0);
        
        TransakcijaZ tt = new TransakcijaZ();
        tt.setDatum(new java.util.Date());
        tt.setVreme(new java.util.Date());
        tt.setIdFil(transakcija.getIdFil());
        tt.setIznos(transakcija.getIznos());
        tt.setRacunNa(null);
        tt.setRacunSa(racunSa.getIdRac());
        tt.setSvrha(transakcija.getSvrha());
        tt.setTip('I');
        tt.setIdTrans(prim);
        tt.setRedniBr(rbr);
        
        RacunZ rSa = new RacunZ();
        rSa.setIdRac(racunSa.getIdRac());
        rSa.setDozvMinus(racunSa.getDozvMinus());
        rSa.setIdMes(racunSa.getIdMes());
        rSa.setIdKom(racunSa.getIdKom().getIdKom());
        rSa.setBrTransakcija(racunSa.getBrTransakcija());
        rSa.setIdMes(racunSa.getIdRac());
        rSa.setDatum(racunSa.getDatum());
        rSa.setVreme(racunSa.getVreme());
        rSa.setStanje(racunSa.getStanje());
        rSa.setStatus(racunSa.getStatus());
        
        noveTransakcije.add(tt);
        noviRacuni.add(rSa);
        
        return "Uspesno kreirana isplata";
        
        
    }
    public static String dohvatiSveRacune(KomitentZ komitent) {
        TypedQuery<Komitent> qK = em.createNamedQuery("Komitent.findByIdKom",Komitent.class);
        qK.setParameter("idKom", komitent.getIdKom());
        if( qK.getResultList().size() < 1){
            return "Neuspesno : komitent ne postoji";
        }
        TypedQuery<Racun> qT = em.createNamedQuery("Racun.findAll",Racun.class);
        if( qT.getResultList().size() < 1){
            return "Ne postoje racuni u bazi";
        }
        
        String ispis = new String();
        for (Racun r : qT.getResultList()){  
            // Integer je klasa pa se radi po ref retardu
            if(r.getIdKom().getIdKom().equals(komitent.getIdKom()))
            ispis += r.getIdRac()+ " " + r.getStanje() + " " + r.getDozvMinus()+ " " + r.getStatus() + " " +
                    r.getDatum().getDate()+"."+r.getDatum().getMonth()+" "+ r.getVreme().getHours()+":"+r.getVreme().getMinutes()+":"+r.getVreme().getSeconds() +" "+ r.getBrTransakcija() + '\n';

        }
        return ispis;
        
    }
    public static String dohvatiSveTransakcije(RacunZ racun){
        TypedQuery<Transakcija> qR = em.createNamedQuery("Racun.findByIdRac",Transakcija.class);
        qR.setParameter("idRac", racun.getIdRac());
        if( qR.getResultList().size() < 1){
            return "Neuspesno : racun ne postoji";
        }
        
        TypedQuery<Transakcija> qT = em.createNamedQuery("Transakcija.findAll",Transakcija.class);
        if( qT.getResultList().size() < 1){
            return "Ne postoje transakcije u bazi";
        }
        
        String ispis = new String();
        for (Transakcija t : qT.getResultList()){
            Integer idSa = null;
            Integer idNa = null;
            if(t.getRacunNa()!=null)idNa = t.getRacunNa().getIdRac();
            if(t.getRacunSa()!=null)idSa = t.getRacunSa().getIdRac();
            
            if((t.getRacunSa() !=null && t.getRacunSa().getIdRac().equals(racun.getIdRac()) ) || (t.getRacunNa()!=null && t.getRacunNa().getIdRac().equals(racun.getIdRac())))
                ispis += t.getIdTrans()+" "+idNa+" "+idSa+" "+t.getRedniBr()+" "+t.getTip()+" "+t.getIznos()+ " "+ t.getSvrha()+ " "
                        +t.getDatum().getDate()+"."+t.getDatum().getMonth()+" "+ t.getVreme().getHours()+":"+t.getVreme().getMinutes()+":"+t.getVreme().getSeconds() +" "+" "+t.getIdFil()+'\n';     
        }
        if(ispis.length()==0)return "Nema transakcija  za ovaj racun";
        System.out.println(ispis);
        return ispis; 
    }
    
}
