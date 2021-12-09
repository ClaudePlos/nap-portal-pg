package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.*;

@Entity
@Table(name="napf_sl_typ_etatu_tmp")
public class WymiarEtatu {

    @Id
    @Column(name="id")
    private Integer id;

    private float etat;
    private String opis;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getEtat() {
        return etat;
    }

    public void setEtat(float etat) {
        this.etat = etat;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}

