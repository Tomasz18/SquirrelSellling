package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

@Entity
@Table(name = "pozycjereklamacji")
@IdClass(ComplaintPositionPrimaryKey.class)
public class ComplaintPosition {

    @Id
    @ManyToOne
    @JoinColumn(name = "idTowaru", referencedColumnName = "id")
    private Ware ware;

    @Id
    @ManyToOne
    @JoinColumn(name = "idReklamacji", referencedColumnName = "id")
    private Complaint complaint;

    @Column(name = "ilosc", nullable = false)
    private double quantity;

    @Column(name = "numer", nullable = false)
    private int number;

    public Ware getWare() {
        return ware;
    }

    public void setWare(Ware ware) {
        this.ware = ware;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
