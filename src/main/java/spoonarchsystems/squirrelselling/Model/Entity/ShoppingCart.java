package spoonarchsystems.squirrelselling.Model.Entity;

import java.util.List;

public class ShoppingCart {
    private List<ShoppingCartPosition> positions;

    public List<ShoppingCartPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ShoppingCartPosition> positions) {
        this.positions = positions;
    }
}
