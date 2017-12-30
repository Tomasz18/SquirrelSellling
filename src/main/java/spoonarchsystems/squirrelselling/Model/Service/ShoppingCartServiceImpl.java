package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spoonarchsystems.squirrelselling.Model.DAO.WareDAO;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCartPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private WareDAO wareDAO;

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

    @Override
    public void clear() {
        shoppingCart.getPositions().clear();
    }

    @Override
    public double getSum() {
        double sum = 0;
        for(ShoppingCartPosition p : shoppingCart.getPositions())
            sum += p.getPrice();
        return sum;
    }

    public void initTestData() {
        ShoppingCart cart = new ShoppingCart();
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
        cart.setPositions(posList);
        update(cart);
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
