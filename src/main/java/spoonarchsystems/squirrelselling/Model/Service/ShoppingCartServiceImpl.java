package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.stereotype.Service;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCartPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private ShoppingCart shoppingCart = new ShoppingCart();

    @Override
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @Override
    public boolean update(ShoppingCart cart) {
        if(!validateQuantity(cart) || !validatePrice(cart))
            return false;
        shoppingCart.setPositions(cart.getPositions());
        return true;
    }

    @Override
    public boolean remove(ShoppingCartPosition cartPosition) {
        List<ShoppingCartPosition> positions = shoppingCart.getPositions();
        if(!positions.contains(cartPosition))
            return false;
        positions.remove(cartPosition);
        return true;
    }

    @Override
    public void clear() {
        shoppingCart.getPositions().clear();
    }

    private boolean validateQuantity(ShoppingCart cart) {
        for(ShoppingCartPosition p : cart.getPositions()) {
            if(p.getQuantity() > p.getWare().getDisposableState())
                return false;
        }
        return true;
    }

    private boolean validatePrice(ShoppingCart cart) {
        for(ShoppingCartPosition p : cart.getPositions()) {
            double q = p.getQuantity();
            Ware w = p.getWare();
            double price = q * (q < w.getWholesaleThreshold() ? w.getRetailPrice() : w.getWholesalePrice());
            if(price != p.getPrice())
                return false;
        }
        return true;
    }
}
