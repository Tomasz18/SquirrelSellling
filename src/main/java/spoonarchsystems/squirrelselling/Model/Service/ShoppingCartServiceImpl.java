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

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private WareDAO wareDAO;

    private ShoppingCart shoppingCart = new ShoppingCart();

    @Override
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

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

    @Override
    public int getSize() {
        return shoppingCart.getPositions().size();
    }

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

    private boolean validateQuantity(List<ShoppingCartPosition> quantities, List<ShoppingCartPosition> positions) {
        for(int i = 0; i < positions.size(); i++)
            if(quantities.get(i).getQuantity() > positions.get(i).getWare().getDisposableState())
                return false;
        return true;
    }
}
