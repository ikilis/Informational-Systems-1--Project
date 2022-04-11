/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ivan Savic
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTrans", query = "SELECT t FROM Transakcija t WHERE t.idTrans = :idTrans"),
    @NamedQuery(name = "Transakcija.findByDatum", query = "SELECT t FROM Transakcija t WHERE t.datum = :datum"),
    @NamedQuery(name = "Transakcija.findByVreme", query = "SELECT t FROM Transakcija t WHERE t.vreme = :vreme"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findByTip", query = "SELECT t FROM Transakcija t WHERE t.tip = :tip"),
    @NamedQuery(name = "Transakcija.findByRedniBr", query = "SELECT t FROM Transakcija t WHERE t.redniBr = :redniBr"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha"),
    @NamedQuery(name = "Transakcija.findByIdFil", query = "SELECT t FROM Transakcija t WHERE t.idFil = :idFil"),
    @NamedQuery(name = "Transakcija.findByRacunSa", query = "SELECT t FROM Transakcija t WHERE t.racunSa = :racunSa"),
    @NamedQuery(name = "Transakcija.findByRacunNa", query = "SELECT t FROM Transakcija t WHERE t.racunNa = :racunNa")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTrans")
    private Integer idTrans;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
    @Temporal(TemporalType.TIME)
    private Date vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Iznos")
    private float iznos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tip")
    private Character tip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RedniBr")
    private int redniBr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Svrha")
    private String svrha;
    @Column(name = "IdFil")
    private Integer idFil;
    @Column(name = "RacunSa")
    private Integer racunSa;
    @Column(name = "RacunNa")
    private Integer racunNa;

    public Transakcija() {
    }

    public Transakcija(Integer idTrans) {
        this.idTrans = idTrans;
    }

    public Transakcija(Integer idTrans, Date datum, Date vreme, float iznos, Character tip, int redniBr, String svrha) {
        this.idTrans = idTrans;
        this.datum = datum;
        this.vreme = vreme;
        this.iznos = iznos;
        this.tip = tip;
        this.redniBr = redniBr;
        this.svrha = svrha;
    }

    public Integer getIdTrans() {
        return idTrans;
    }

    public void setIdTrans(Integer idTrans) {
        this.idTrans = idTrans;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public float getIznos() {
        return iznos;
    }

    public void setIznos(float iznos) {
        this.iznos = iznos;
    }

    public Character getTip() {
        return tip;
    }

    public void setTip(Character tip) {
        this.tip = tip;
    }

    public int getRedniBr() {
        return redniBr;
    }

    public void setRedniBr(int redniBr) {
        this.redniBr = redniBr;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Integer getIdFil() {
        return idFil;
    }

    public void setIdFil(Integer idFil) {
        this.idFil = idFil;
    }

    public Integer getRacunSa() {
        return racunSa;
    }

    public void setRacunSa(Integer racunSa) {
        this.racunSa = racunSa;
    }

    public Integer getRacunNa() {
        return racunNa;
    }

    public void setRacunNa(Integer racunNa) {
        this.racunNa = racunNa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTrans != null ? idTrans.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTrans == null && other.idTrans != null) || (this.idTrans != null && !this.idTrans.equals(other.idTrans))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ idTrans=" + idTrans + " ]";
    }
    
}
