package spoonarchsystems.squirrelselling.Model.Service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import spoonarchsystems.squirrelselling.Model.DAO.OrderDAO;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Entity.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AddressService addressService;

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
        int days = calculatePostponement(date) + 1;
        if(days < 7 || days > 30) {
            errors.add(INVALID_POSTPONEMENT_DATE);
            return false;
        }
        errors.clear();
        blueprint.setPostponementTime(days);
        return true;
    }

    @Override
    public boolean setPostponement(Order blueprint, Integer value) {
        if(value < 0)
            return false;
        blueprint.setPostponementTime(value);
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
        if(order.getInvoice()) {
            Address invoiceAddress = blueprint.getInvoiceBuyerAddress();
            invoiceAddress.setId(addressService.getHashedId(invoiceAddress));
            order.setInvoiceBuyerAddress(invoiceAddress);
        }
        else order.setInvoiceBuyerAddress(null);

        order.setDeliveryCost(0.0);
        order.setPersonalCollection(blueprint.getPersonalCollection());
        if(!order.getPersonalCollection()) {
            Address deliveryAddress = blueprint.getDeliveryAddress();
            deliveryAddress.setId(addressService.getHashedId(deliveryAddress));
            order.setDeliveryAddress(deliveryAddress);
            order.setDeliveryCost(calculateDeliveryCost(blueprint));
        }
        else order.setDeliveryAddress(null);

        order.setPostponementTime(blueprint.getPostponementTime());
        order.setComplaining(false);
    }

    @Override
    public Order getPreparedOrder() {
        return order;
    }

    @Override
    public boolean saveOrder(Order order) {
        Date submissionDate = new Date();
        order.setSubmissionDate(submissionDate);
        order.setNumber(getNextOrderNumber(submissionDate));
        order.setStatus(Order.OrderStatus.submitted);
        order.setCustomer(accountService.getCurrentCustomer().getCustomer());
        System.out.println("##### account.id = " + String.valueOf(accountService.getCurrentCustomer().getId()));
        System.out.println("##### order.customer = " + order.getCustomer());
        for(OrderPosition op : order.getPositions()) {
            op.setOrder(order);
        }
        try {
            orderDAO.saveOrder(order);
        }
        catch (HibernateException ex) {
            return false;
        }
        return true;
    }

    @Override
    public Double calculateDeliveryCost(Order order) {
        Double weight = 0.0;
        for(OrderPosition p : order.getPositions()) {
            weight += p.getWare().getWeight() * p.getQuantity();
        }

        Double cost = 0.0;
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

    @Override
    public double getOrderValue(Order order) {
        double orderValue = 0;
        for (OrderPosition position : order.getPositions()) {
            orderValue += position.getPrice();
        }
        if(order.getPersonalCollection() != null && !order.getPersonalCollection())
            orderValue += calculateDeliveryCost(order);
        return orderValue;
    }

    @Override
    public void sendOrderConfirmationEmail(Order order) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("orderValue", getOrderValue(order));
        String content = templateEngine.process("view/general/emails/order_confirmation", context);

        messageHelper.setTo(order.getCustomer().getEmail());
        messageHelper.setText(content, true);
        messageHelper.setSubject("Zamówienie " + order.getNumber());

        javaMailSender.send(message);
    }
}
