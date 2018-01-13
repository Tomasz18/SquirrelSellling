package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;

import java.util.Date;
import java.util.List;

public interface OrderService {
    Order getOrder(int id);
    Order getOrderBlueprint(ShoppingCart shoppingCart);
    void setOrderPositions(Order blueprint, ShoppingCart shoppingCart);
    boolean validateOrder(Order blueprint);
    boolean setPostponement(Order blueprint, Date date);
    boolean setPostponement(Order blueprint, Integer value);
    List<String> getErrors();
    void prepareOrder(Order blueprint);
    boolean saveOrder(Order blueprint);
    double getOrderValue(Order order);
}
