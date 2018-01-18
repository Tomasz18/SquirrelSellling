package spoonarchsystems.squirrelselling.Model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds position with wares chosen by customer to buy
 */
public class ShoppingCart {
    private List<ShoppingCartPosition> positions;

    public List<ShoppingCartPosition> getPositions() {
        if(positions == null)
            positions = new ArrayList<>();
        return positions;
    }

    public void setPositions(List<ShoppingCartPosition> positions) {
        this.positions = positions;
    }
}
