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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ivan Savic
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findByIdRac", query = "SELECT r FROM Racun r WHERE r.idRac = :idRac"),
    @NamedQuery(name = "Racun.findByStanje", query = "SELECT r FROM Racun r WHERE r.stanje = :stanje"),
    @NamedQuery(name = "Racun.findByDozvMinus", query = "SELECT r FROM Racun r WHERE r.dozvMinus = :dozvMinus"),
    @NamedQuery(name = "Racun.findByStatus", query = "SELECT r FROM Racun r WHERE r.status = :status"),
    @NamedQuery(name = "Racun.findByDatum", query = "SELECT r FROM Racun r WHERE r.datum = :datum"),
    @NamedQuery(name = "Racun.findByVreme", query = "SELECT r FROM Racun r WHERE r.vreme = :vreme"),
    @NamedQuery(name = "Racun.findByBrTransakcija", query = "SELECT r FROM Racun r WHERE r.brTransakcija = :brTransakcija"),
    @NamedQuery(name = "Racun.findByIdKom", query = "SELECT r FROM Racun r WHERE r.idKom = :idKom"),
    @NamedQuery(name = "Racun.findByIdMes", query = "SELECT r FROM Racun r WHERE r.idMes = :idMes")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdRac")
    private Integer idRac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Stanje")
    private float stanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DozvMinus")
    private float dozvMinus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    private Character status;
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
    @Column(name = "BrTransakcija")
    private int brTransakcija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKom")
    private int idKom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdMes")
    private int idMes;

    public Racun() {
    }

    public Racun(Integer idRac) {
        this.idRac = idRac;
    }

    public Racun(Integer idRac, float stanje, float dozvMinus, Character status, Date datum, Date vreme, int brTransakcija, int idKom, int idMes) {
        this.idRac = idRac;
        this.stanje = stanje;
        this.dozvMinus = dozvMinus;
        this.status = status;
        this.datum = datum;
        this.vreme = vreme;
        this.brTransakcija = brTransakcija;
        this.idKom = idKom;
        this.idMes = idMes;
    }

    public Integer getIdRac() {
        return idRac;
    }

    public void setIdRac(Integer idRac) {
        this.idRac = idRac;
    }

    public float getStanje() {
        return stanje;
    }

    public void setStanje(float stanje) {
        this.stanje = stanje;
    }

    public float getDozvMinus() {
        return dozvMinus;
    }

    public void setDozvMinus(float dozvMinus) {
        this.dozvMinus = dozvMinus;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
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

    public int getBrTransakcija() {
        return brTransakcija;
    }

    public void setBrTransakcija(int brTransakcija) {
        this.brTransakcija = brTransakcija;
    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }

    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRac != null ? idRac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.idRac == null && other.idRac != null) || (this.idRac != null && !this.idRac.equals(other.idRac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Racun[ idRac=" + idRac + " ]";
    }
    
}
