package spoonarchsystems.squirrelselling.Model.Service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import spoonarchsystems.squirrelselling.Model.DAO.OrderDAO;
import spoonarchsystems.squirrelselling.Model.Entity.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderServiceImpl implements OrderService {

    private static final String EMPTY_DELIVERY_ADDRESS = "Nie określono adresu dostawy! Uzupełnij pola.";
    private static final String EMPTY_INVOICE_ADDRESS = "Nie określono adresu nabywcy dla faktury! Uzupełnij pola.";
    private static final String EMPTY_POSTPONEMENT_DATE = "Nie określono daty odroczenia! Uzupełnij pole.";
    private static final String INVALID_POSTPONEMENT_DATE = "Wprowadzono niepoprawną datę odroczenia! Musi mieścić się w zakresie 7 - 30 dni licząc od dnia dzisiejszego.";
    private static final String INVALID_SHOPPING_CART = "Wystąpił błąd z koszykiem! Sprawdź, czy dodałeś towary do koszyka przed złożeniem zamówienia.";
    private static final String SAVE_ERROR = "Wystąpił błąd przy zapisie zamówienia! Proszę, spróbuj złożyć je ponownie, a jeśli błąd będzie nadal występował, skontaktuj się z obsługą hurtowni.";

    private List<String> errors = new ArrayList<>();

    private Order order;

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

    @Override
    public Order getOrderBlueprint(ShoppingCart shoppingCart) {
        Order blueprint = new Order();
        setOrderPositions(blueprint, shoppingCart);
        blueprint.setPersonalCollection(true);
        blueprint.setInvoice(false);
        return blueprint;
    }

    @Override
    public void setOrderPositions(Order blueprint, ShoppingCart shoppingCart) {
        List<OrderPosition> positions = new ArrayList<>();
        for(ShoppingCartPosition scp : shoppingCart.getPositions()) {
            OrderPosition p = new OrderPosition();
            p.setNumber(scp.getNumber());
            p.setWare(scp.getWare());
            p.setQuantity(scp.getQuantity());
            p.setPrice(scp.getPrice());
            p.setOrder(blueprint);
            positions.add(p);
        }
        blueprint.setPositions(positions);
    }

    @Override
    public boolean validateOrder(Order blueprint) {
        errors.clear();
        return validatePositions(blueprint) && validateShipmentAddress(blueprint);
    }

    @Override
    public boolean setPostponement(Order blueprint, Date date) {
        if(date == null) {
            errors.add(EMPTY_POSTPONEMENT_DATE);
            return false;
        }
        int days = calculatePostponement(date);
        if(days < 7 || days > 30) {
            errors.add(INVALID_POSTPONEMENT_DATE);
            return false;
        }
        errors.clear();
        blueprint.setPostponementTime(days);
        return true;
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public void prepareOrder(Order blueprint) {
        order = new Order();

        order.setPositions(blueprint.getPositions());

        order.setInvoice(blueprint.getInvoice());
        if(order.getInvoice())
            order.setInvoiceBuyerAddress(blueprint.getInvoiceBuyerAddress());

        order.setPersonalCollection(blueprint.getPersonalCollection());
        if(!order.getPersonalCollection())
            order.setDeliveryAddress(blueprint.getDeliveryAddress());

        order.setPostponementTime(blueprint.getPostponementTime());

        order.setDeliveryCost(calculateDeliveryCost(blueprint.getPositions()));
    }

    @Override
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
        if(blueprint.getPositions() == null || blueprint.getPositions().size() == 0) {
            errors.add(INVALID_SHOPPING_CART);
            return false;
        }
        for(OrderPosition op : blueprint.getPositions()) {
            if(op.getQuantity() == null || op.getQuantity() == 0)
                return false;
            if(op.getWare() != null) {
                Ware ware = op.getWare();
                Double validPrice = op.getQuantity() * (op.getQuantity() < ware.getWholesaleThreshold() ? ware.getRetailPrice() : ware.getWholesalePrice());
                if(!op.getPrice().equals(validPrice)) {
                    errors.add(INVALID_SHOPPING_CART);
                    return false;
                }
            }
            else {
                errors.add(INVALID_SHOPPING_CART);
                return false;
            }
        }
        return true;
    }

    private boolean validateShipmentAddress(Order blueprint) {
        Address shipmentAddress = blueprint.getDeliveryAddress();
        if(!blueprint.getPersonalCollection()) {
            if(shipmentAddress == null) {
                errors.add(EMPTY_DELIVERY_ADDRESS);
                return false;
            }
            if(shipmentAddress.getCity() == null || shipmentAddress.getCity().isEmpty()) {
                errors.add(EMPTY_DELIVERY_ADDRESS);
                return false;
            }
            if(shipmentAddress.getBuildingNumber() == null || shipmentAddress.getBuildingNumber().isEmpty()) {
                errors.add(EMPTY_DELIVERY_ADDRESS);
                return false;
            }
            if(shipmentAddress.getPostalCode() == null || shipmentAddress.getPostalCode().isEmpty()) {
                errors.add(EMPTY_DELIVERY_ADDRESS);
                return false;
            }
        }
        return true;
    }

    private int calculatePostponement(Date date) {
        Date now = new Date();
        long diff = date.getTime() - now.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
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
}
