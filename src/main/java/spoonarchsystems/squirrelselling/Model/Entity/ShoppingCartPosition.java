package spoonarchsystems.squirrelselling.Model.Entity;

/**
 * Represents position in shopping cart contains ware reference, its quantity,
 * price with it is selling and ordinal number
 */
public class ShoppingCartPosition {

    private int number;

    private Ware ware;

    private double quantity;

    private double price;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Ware getWare() {
        return ware;
    }

    public void setWare(Ware ware) {
        this.ware = ware;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
