package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spoonarchsystems.squirrelselling.Model.DAO.OrderDAO;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    @Transactional
    public Order getOrder(int id) {
        Order order = orderDAO.getOrder(id);
        return order;
    }

    @Override
    public double getOrderValue(Order order) {
        double orderValue = 0;
        for (OrderPosition position : order.getPositions()) {
            orderValue += position.getPrice() * position.getQuantity();
        }
        return orderValue;
    }
}
