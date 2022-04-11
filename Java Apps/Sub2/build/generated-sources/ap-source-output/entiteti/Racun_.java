package entiteti;

import entiteti.Komitent;
import entiteti.Transakcija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-24T18:10:52")
@StaticMetamodel(Racun.class)
public class Racun_ { 

    public static volatile SingularAttribute<Racun, Date> datum;
    public static volatile SingularAttribute<Racun, Float> stanje;
    public static volatile SingularAttribute<Racun, Date> vreme;
    public static volatile SingularAttribute<Racun, Integer> idMes;
    public static volatile SingularAttribute<Racun, Integer> brTransakcija;
    public static volatile SingularAttribute<Racun, Komitent> idKom;
    public static volatile SingularAttribute<Racun, Float> dozvMinus;
    public static volatile ListAttribute<Racun, Transakcija> transakcijaList;
    public static volatile ListAttribute<Racun, Transakcija> transakcijaList1;
    public static volatile SingularAttribute<Racun, Integer> idRac;
    public static volatile SingularAttribute<Racun, Character> status;

}