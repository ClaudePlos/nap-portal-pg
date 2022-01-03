package pl.kskowronski.data.entity.egeria.css;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "css_stanowiska_kosztow")
public class SK {

    @Id
    @Column(name = "SK_ID", nullable = false)
    private Integer skId;

    @Column(name = "SK_KOD", nullable = false)
    private String skKod;

    @Column(name = "SK_OPIS", nullable = false)
    private String skOpis;

    public SK() {
    }

    public SK(Integer skId, String skKod, String skOpis) {
        this.skId = skId;
        this.skKod = skKod;
        this.skOpis = skOpis;
    }

    public Integer getSkId() {
        return skId;
    }

    public void setSkId(Integer skId) {
        this.skId = skId;
    }

    public String getSkKod() {
        return skKod;
    }

    public void setSkKod(String skKod) {
        this.skKod = skKod;
    }

    public String getSkOpis() {
        return skOpis;
    }

    public void setSkOpis(String skOpis) {
        this.skOpis = skOpis;
    }
}
