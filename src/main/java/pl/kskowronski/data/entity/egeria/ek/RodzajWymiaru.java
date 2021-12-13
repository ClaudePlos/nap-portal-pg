package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EK_RODZAJE_WYMIAROW")
public class RodzajWymiaru {

    @Id
    @Column(name = "rwy_kod")
    private String rwyKod;

    @Column(name = "rwy_dg_kod")
    private String rwyDgKod;

    @Column(name = "rwy_nazwa")
    private String rwyNazwa;

    public RodzajWymiaru() {
    }

    public RodzajWymiaru(String rwyKod) {
        this.rwyKod = rwyKod;
    }

    public String getRwyKod() {
        return rwyKod;
    }

    public void setRwyKod(String rwyKod) {
        this.rwyKod = rwyKod;
    }

    public String getRwyNazwa() {
        return rwyNazwa;
    }

    public void setRwyNazwa(String rwyNazwa) {
        this.rwyNazwa = rwyNazwa;
    }

    public String getRwyDgKod() {
        return rwyDgKod;
    }

    public void setRwyDgKod(String rwyDgKod) {
        this.rwyDgKod = rwyDgKod;
    }
}
