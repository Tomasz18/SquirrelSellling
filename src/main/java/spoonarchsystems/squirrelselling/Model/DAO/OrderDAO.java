package spoonarchsystems.squirrelselling.Model.DAO;

import spoonarchsystems.squirrelselling.Model.Entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderDAO {
    Order getOrder(int id);
    List<Order> getOrdersBySubmissionDate(Date date);
    void saveOrder(Order order);
}
