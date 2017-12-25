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
}
