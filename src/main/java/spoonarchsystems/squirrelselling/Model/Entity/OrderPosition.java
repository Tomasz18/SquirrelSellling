package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

@Entity
@Table(name = "pozycjezamowien")
@IdClass(OrderPositionPrimaryKey.class)
public class OrderPosition {

    @Id
    @ManyToOne
    @JoinColumn(name = "idZamowienia", referencedColumnName = "id")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "idTowaru", referencedColumnName = "id")
    private Ware ware;

    @Column(name = "ilosc", nullable = false)
    private double quantity;

    @Column(name = "cena", nullable = false)
    private double price;

    @Column(name = "numer", nullable = false)
    private int number;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Ware getWare() {
        return ware;
    }

    public void setWare(Ware ware) {
        this.ware = ware;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
