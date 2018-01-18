package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

/**
 * Entity describes complaint position contains complained ware and it's quantity.
 * It also have reference to complaint and ordinal number
 */
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
    private Double quantity;

    @Column(name = "numer", nullable = false)
    private Integer number;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
