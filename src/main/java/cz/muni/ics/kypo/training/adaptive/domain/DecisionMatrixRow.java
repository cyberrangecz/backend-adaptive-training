package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DecisionMatrixRow {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "order_in_phase", nullable = false)
    int order;
    double AA;
    double KU;
    double CiT;
    double SD;
    double WF;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingPhase trainingPhase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getAA() {
        return AA;
    }

    public void setAA(double AA) {
        this.AA = AA;
    }

    public double getKU() {
        return KU;
    }

    public void setKU(double KU) {
        this.KU = KU;
    }

    public double getCiT() {
        return CiT;
    }

    public void setCiT(double ciT) {
        CiT = ciT;
    }

    public double getSD() {
        return SD;
    }

    public void setSD(double SD) {
        this.SD = SD;
    }

    public double getWF() {
        return WF;
    }

    public void setWF(double WF) {
        this.WF = WF;
    }

    public TrainingPhase getTrainingPhase() {
        return trainingPhase;
    }

    public void setTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhase = trainingPhase;
    }

    @Override
    public String toString() {
        return "DecisionMatrixRow{" +
                "id=" + id +
                ", order=" + order +
                ", AA=" + AA +
                ", KU=" + KU +
                ", CiT=" + CiT +
                ", SD=" + SD +
                ", WF=" + WF +
                '}';
    }
}
