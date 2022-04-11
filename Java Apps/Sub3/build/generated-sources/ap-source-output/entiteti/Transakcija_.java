package entiteti;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-24T18:11:13")
@StaticMetamodel(Transakcija.class)
public class Transakcija_ { 

    public static volatile SingularAttribute<Transakcija, Date> datum;
    public static volatile SingularAttribute<Transakcija, Float> iznos;
    public static volatile SingularAttribute<Transakcija, String> svrha;
    public static volatile SingularAttribute<Transakcija, Date> vreme;
    public static volatile SingularAttribute<Transakcija, Integer> racunNa;
    public static volatile SingularAttribute<Transakcija, Integer> redniBr;
    public static volatile SingularAttribute<Transakcija, Integer> idFil;
    public static volatile SingularAttribute<Transakcija, Integer> idTrans;
    public static volatile SingularAttribute<Transakcija, Character> tip;
    public static volatile SingularAttribute<Transakcija, Integer> racunSa;

}