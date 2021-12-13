package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ek_zwolnienia")
public class Zwolnienie {

    @Id
    @Column(name = "zwol_id")
    private Integer zwolId;

    @Column(name = "zwol_frm_id")
    private Integer zwolFrmId;

    public Zwolnienie() {
    }

    public Integer getZwolId() {
        return zwolId;
    }

    public void setZwolId(Integer zwolId) {
        this.zwolId = zwolId;
    }

    public Integer getZwolFrmId() {
        return zwolFrmId;
    }

    public void setZwolFrmId(Integer zwolFrmId) {
        this.zwolFrmId = zwolFrmId;
    }
}
