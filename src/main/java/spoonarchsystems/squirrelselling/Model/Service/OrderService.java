package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.Order;

public interface OrderService {
    Order getOrder(int id);
    double getOrderValue(Order order);
}
