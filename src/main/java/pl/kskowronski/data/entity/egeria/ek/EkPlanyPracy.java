package pl.kskowronski.data.entity.egeria.ek;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ek_systemy_pracy")
public class EkPlanyPracy {

    @Id
    @Column(name = "pp_id")
    private Integer ppId;

    @Column(name = "pp_sp_id")
    private Integer ppSpId;

    @Column(name = "pp_kod")
    private String ppKod;

    @Column(name = "pp_nazwa")
    private String ppNazwa;

    @Column(name = "pp_punkt_startowy")
    private Integer ppPunktStartowy;

    public EkPlanyPracy() {
    }

    public Integer getPpId() {
        return ppId;
    }

    public void setPpId(Integer ppId) {
        this.ppId = ppId;
    }

    public Integer getPpSpId() {
        return ppSpId;
    }

    public void setPpSpId(Integer ppSpId) {
        this.ppSpId = ppSpId;
    }

    public String getPpKod() {
        return ppKod;
    }

    public void setPpKod(String ppKod) {
        this.ppKod = ppKod;
    }

    public String getPpNazwa() {
        return ppNazwa;
    }

    public void setPpNazwa(String ppNazwa) {
        this.ppNazwa = ppNazwa;
    }

    public Integer getPpPunktStartowy() {
        return ppPunktStartowy;
    }

    public void setPpPunktStartowy(Integer ppPunktStartowy) {
        this.ppPunktStartowy = ppPunktStartowy;
    }
}
