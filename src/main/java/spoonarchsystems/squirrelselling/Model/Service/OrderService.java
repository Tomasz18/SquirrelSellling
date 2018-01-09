package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.Order;

public interface OrderService {
    Order getOrder(int id);
    boolean validateOrder(Order blueprint);
    boolean saveOrder(Order blueprint);
}
