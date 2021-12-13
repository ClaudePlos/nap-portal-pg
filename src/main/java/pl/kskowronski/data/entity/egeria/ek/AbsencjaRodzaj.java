package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ek_rodzaje_absencji")
public class AbsencjaRodzaj {

    @Id
    @Column(name = "rda_id")
    private Integer rdaId;

    @Column(name = "rda_kod")
    private String rdaKod;

    @Column(name = "rda_nazwa")
    private String rdaNazwa;

    public AbsencjaRodzaj() {
    }

    public Integer getRdaId() {
        return rdaId;
    }

    public void setRdaId(Integer rdaId) {
        this.rdaId = rdaId;
    }

    public String getRdaKod() {
        return rdaKod;
    }

    public void setRdaKod(String rdaKod) {
        this.rdaKod = rdaKod;
    }

    public String getRdaNazwa() {
        return rdaNazwa;
    }

    public void setRdaNazwa(String rdaNazwa) {
        this.rdaNazwa = rdaNazwa;
    }
}
