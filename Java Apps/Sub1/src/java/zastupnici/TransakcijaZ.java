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
public class TransakcijaZ  implements Serializable {
    
    
    private Integer idTrans;
    private Date datum;
    private Date vreme;
    private float iznos;
    private Character tip;
    private int redniBr;
    private String svrha;
    private Integer idFil;
    private Integer racunSa;
    private Integer racunNa;

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
    
}
