package spoonarchsystems.squirrelselling.Model.Entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderPositionPrimaryKey implements Serializable {
    private Order order;
    private Ware ware;

    public OrderPositionPrimaryKey() {}

    public OrderPositionPrimaryKey(Order order, Ware ware) {
        this.order = order;
        this.ware = ware;
    }

    @Override
    public boolean equals(Object obj) {
        return this.order.getNumber().equals(((OrderPositionPrimaryKey)obj).order.getNumber())
                && this.ware.getId() == ((OrderPositionPrimaryKey) obj).ware.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(order.getNumber(), ware.getName());
    }
}
