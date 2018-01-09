package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;

public interface OrderService {
    Order getOrder(int id);
    Order getOrderBlueprint(ShoppingCart shoppingCart);
    boolean validateOrder(Order blueprint);
    boolean saveOrder(Order blueprint);
}
