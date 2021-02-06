package cz.muni.ics.kypo.training.adaptive.dto;

public class DecisionMatrixRowDto {
    private long id;
    private int order;
    private double AA;
    private double KU;
    private double CiT;
    private double SD;
    private double WF;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
