package pl.kskowronski.data.entity.egeria.css;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "css_obiekty_w_przedsieb")
public class JO {

    @Id
    @Column(name = "OB_ID", nullable = false)
    private Integer obId;

    @Column(name = "OB_STAN_DEFINICJI", nullable = false)
    private String obStanDefinicji;

    @Column(name = "OB_PELNY_KOD", nullable = false)
    private String obPelnyKod;

    @Column(name = "OB_NAZWA", nullable = false)
    private String obNazwa;

    public Integer getObId() {
        return obId;
    }

    public void setObId(Integer obId) {
        this.obId = obId;
    }

    public String getObStanDefinicji() {
        return obStanDefinicji;
    }

    public void setObStanDefinicji(String obStanDefinicji) {
        this.obStanDefinicji = obStanDefinicji;
    }

    public String getObPelnyKod() {
        return obPelnyKod;
    }

    public void setObPelnyKod(String obPelnyKod) {
        this.obPelnyKod = obPelnyKod;
    }

    public String getObNazwa() {
        return obNazwa;
    }

    public void setObNazwa(String obNazwa) {
        this.obNazwa = obNazwa;
    }
}
