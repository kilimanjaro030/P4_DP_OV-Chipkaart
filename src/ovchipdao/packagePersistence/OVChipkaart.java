package ovchipdao.packagePersistence;

import ovchipdao.packageDomain.Reiziger;

import java.sql.Date;

public class OVChipkaart {
    private int kaart_nummer;
    private java.sql.Date geldig_tot;
    private int klasse;
    private int saldo;
    private Reiziger reiziger;

    public OVChipkaart(int kaart_nummer, java.sql.Date geldig_tot, int klasse, int saldo) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String toString() {
        if(reiziger != null){
            return reiziger.toString() + " kaartnummer: " + kaart_nummer + " geldig tot: " + geldig_tot + " klasse: " + klasse + " saldo: " + saldo;
        }else{
            return " kaartnummer: " + kaart_nummer + " geldig tot: " + geldig_tot + " klasse: " + klasse + " saldo: " + saldo;
        }

    }
}
