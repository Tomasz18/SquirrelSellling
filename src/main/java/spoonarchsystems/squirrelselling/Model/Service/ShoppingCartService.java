package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCart();
    boolean updateQuantity(ShoppingCart cart);
    boolean remove(int number);
    void clear();
    double getSum();
    int getSize();
    void initTestData();
}
