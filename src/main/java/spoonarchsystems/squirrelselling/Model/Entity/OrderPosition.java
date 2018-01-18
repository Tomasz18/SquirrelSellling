package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

/**
 * Entity describes order position contains {@link Ware}, its quantity and price with it was sell.
 * It also have reference to related {@link Order} and ordinal number
 */
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
    private Double quantity;

    @Column(name = "cena", nullable = false)
    private Double price;

    @Column(name = "numer", nullable = false)
    private Integer number;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
