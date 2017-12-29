package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCartPosition;

public interface ShoppingCartService {
    ShoppingCart getShoppingCart();
    boolean update(ShoppingCart cart);
    boolean remove(ShoppingCartPosition cartPosition);
    void clear();
    double getSum();
}
