package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spoonarchsystems.squirrelselling.Model.DAO.OrderDAO;
import spoonarchsystems.squirrelselling.Model.Entity.Order;

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
}
