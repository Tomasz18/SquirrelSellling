package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import spoonarchsystems.squirrelselling.Model.DAO.WareDAO;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCartPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for ShoppingCart.
 * Scoped for session.
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartServiceImpl implements ShoppingCartService {

    /**
     * Data Access Object for ware (of type: {@link WareDAO}).
     */
    @Autowired
    private WareDAO wareDAO;

    /**
     * Shopping cart object.
     */
    private ShoppingCart shoppingCart = new ShoppingCart();

    /**
     * Getter for existing shopping cart.
     *
     * @return shopping cart (of type: {@link ShoppingCart})
     */
    @Override
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Method that updates quantities of shopping cart positions.
     * Validates if new position quantities are correct.
     *
     * @param cart  shopping cart to update (of type: {@link ShoppingCart})
     * @return update success or failure (of type: boolean)
     */
    @Override
    public boolean updateQuantity(ShoppingCart cart) {
        List<ShoppingCartPosition> positions = shoppingCart.getPositions();
        List<ShoppingCartPosition> quantities = cart.getPositions();
        if(!validateQuantity(quantities, positions))
            return false;
        for(int i = 0; i < positions.size(); i++) {
            ShoppingCartPosition p = positions.get(i);
            double q = quantities.get(i).getQuantity();
            p.setQuantity(q);
            Ware w = p.getWare();
            p.setPrice(q * (q < w.getWholesaleThreshold() ? w.getRetailPrice() : w.getWholesalePrice()));
        }
        return true;
    }

    /**
     * Method that removes shopping cart position for given number.
     *
     * @param number    number of position to remove (of type: int)
     * @return removal success or failure (of type: boolean)
     */
    @Override
    public boolean remove(int number) {
        List<ShoppingCartPosition> positions = shoppingCart.getPositions();
        boolean removed = false;
        for(int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNumber() == number) {
                positions.remove(i);
                removed = true;
            }
            if(i < positions.size() && removed)
                positions.get(i).setNumber(i);
        }
        return removed;
    }

    /**
     * Method that clears shopping cart position list.
     */
    @Override
    public void clear() {
        shoppingCart.getPositions().clear();
    }

    /**
     * Method that gets total price of shopping cart.
     *
     * @return shopping cart total (of type: double)
     */
    @Override
    public double getSum() {
        double sum = 0;
        for(ShoppingCartPosition p : shoppingCart.getPositions())
            sum += p.getPrice();
        return sum;
    }

    /**
     * Getter for shopping cart position list size.
     *
     * @return shopping cart size (of type: int)
     */
    @Override
    public int getSize() {
        return shoppingCart.getPositions().size();
    }

    /**
     * Method that initializes test data for shopping cart.
     * Mock, for demonstration purposes.
     */
    @Override
    public void initTestData() {
        List<ShoppingCartPosition> posList = new ArrayList<>();
        ShoppingCartPosition pos;
        Ware ware;
        for(int i = 1; i < 4; i++) {
            pos = new ShoppingCartPosition();
            ware = wareDAO.getWare(i);
            pos.setNumber(i);
            pos.setWare(ware);
            pos.setQuantity(i * 10);
            pos.setPrice(i * 10 * (i * 10 < ware.getWholesaleThreshold() ? ware.getRetailPrice() : ware.getWholesalePrice()));
            posList.add(pos);
        }
        shoppingCart.setPositions(posList);
    }

    /**
     * Method that validates shopping cart position quantities.
     *
     * @param quantities    list of shopping cart positions, qunatites source (of type: List<ShoppingCartPosition>)
     * @param positions     list of shopping cart positions, to compare (of type: List<ShoppingCartPosition>)
     * @return validation success or failure (of type: boolean)
     */
    private boolean validateQuantity(List<ShoppingCartPosition> quantities, List<ShoppingCartPosition> positions) {
        for(int i = 0; i < positions.size(); i++)
            if(quantities.get(i).getQuantity() > positions.get(i).getWare().getDisposableState())
                return false;
        return true;
    }
}
