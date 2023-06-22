package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ek_systemy_pracy")
public class EkSystemyPracy {

    @Id
    @Column(name = "sp_id")
    private Integer spId;

    @Column(name = "sp_kod")
    private Integer spKod;

    @Column(name = "sp_nazwa")
    private String spNazwa;

    @Column(name = "sp_d_czas_wartosc")
    private Double spDCzasWartosc;

    @Column(name = "sp_t_czas_wartosc")
    private Double spTCzasWartosc;

    public EkSystemyPracy() {
    }

    public Integer getSpId() {
        return spId;
    }

    public void setSpId(Integer spId) {
        this.spId = spId;
    }

    public Integer getSpKod() {
        return spKod;
    }

    public void setSpKod(Integer spKod) {
        this.spKod = spKod;
    }

    public String getSpNazwa() {
        return spNazwa;
    }

    public void setSpNazwa(String spNazwa) {
        this.spNazwa = spNazwa;
    }

    public Double getSpDCzasWartosc() {
        return spDCzasWartosc;
    }

    public void setSpDCzasWartosc(Double spDCzasWartosc) {
        this.spDCzasWartosc = spDCzasWartosc;
    }

    public Double getSpTCzasWartosc() {
        return spTCzasWartosc;
    }

    public void setSpTCzasWartosc(Double spTCzasWartosc) {
        this.spTCzasWartosc = spTCzasWartosc;
    }
}
