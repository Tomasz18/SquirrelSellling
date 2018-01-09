package spoonarchsystems.squirrelselling.Model.Service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import spoonarchsystems.squirrelselling.Model.DAO.OrderDAO;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Entity.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    public Order getOrder(int id) {
        Order order = orderDAO.getOrder(id);
        return order;
    }

    public boolean validateOrder(Order blueprint) {
        return validatePositions(blueprint) && validateShipmentAddress(blueprint);
    }

    public boolean saveOrder(Order order) {
        Date submissionDate = new Date();
        order.setSubmissionDate(submissionDate);
        order.setNumber(getNextOrderNumber(submissionDate));
        order.setCustomer(accountService.getCurrentCustomer().getCustomer());
        try {
            orderDAO.saveOrder(order);
        }
        catch (HibernateException ex) {
            return false;
        }
        return true;
    }

    public Double calculateDeliveryCost(List<OrderPosition> positions) {
        Double weight = 0.0;
        for(OrderPosition p : positions) {
            weight += p.getWare().getWeight() * p.getQuantity();
        }
        Double cost;
        if(weight <= 1)
            cost = 15.0;
        else if(weight <= 10)
            cost = 20.0;
        else if(weight <= 25)
            cost = 30.0;
        else cost = 50.0;
        return cost;
    }

    private boolean validatePositions(Order blueprint) {
        if(blueprint.getPositions() == null || blueprint.getPositions().size() == 0)
            return false;
        for(OrderPosition op : blueprint.getPositions()) {
            if(op.getQuantity() == null || op.getQuantity() == 0)
                return false;
            if(op.getWare() != null) {
                Ware ware = op.getWare();
                Double validPrice = op.getQuantity() * (op.getQuantity() < op.getWare().getWholesaleThreshold() ? op.getWare().getRetailPrice() : op.getWare().getWholesalePrice());
                if(op.getPrice() != validPrice)
                    return false;
            }
            else
                return false;
        }
        return true;
    }

    private boolean validateShipmentAddress(Order blueprint) {
        Address shipmentAddress = blueprint.getDeliveryAddress();
        if(shipmentAddress == null && !blueprint.isPersonalCollection())
            return false;
        if(shipmentAddress != null) {
            if(shipmentAddress.getCity() == null || shipmentAddress.getCity() == "")
                return false;
            if(shipmentAddress.getBuildingNumber() == null || shipmentAddress.getBuildingNumber() == "")
                return false;
            if(shipmentAddress.getPostalCode() == null || shipmentAddress.getPostalCode() == "")
                return false;
        }
        return true;
    }

    private String getNextOrderNumber(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        ArrayList<Integer> numbers = new ArrayList<>();
        Integer maxNumber = 0;

        List<Order> orders = orderDAO.getOrdersBySubmissionDate(date);
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                numbers.add(Integer.parseInt(order.getNumber().substring(0, 4)));
            }
            maxNumber = Collections.max(numbers);
        }
        return String.format("%04d", maxNumber + 1) + "/" + formattedDate;
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
