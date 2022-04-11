/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zastupnici;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Ivan Savic
 */
public class RacunZ  implements Serializable {
    
    
    private Integer idRac;
    private float stanje;
    private float dozvMinus;
    private Character status;
    private Date datum;
    private Date vreme;
    private int brTransakcija;
    private int idKom;
    private int idMes;
    
    
    
    public int getIdMes() {
        return idMes;
    }
    public void setIdMes(int idMes) {
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
    
}
