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

/**
 * Service class for Order
 * Implements OrderService interface
 * Scoped for session
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderServiceImpl implements OrderService {

    /**
     * Message for empty delivery address error (of type: String)
     */
    private static final String EMPTY_DELIVERY_ADDRESS = "Nie określono adresu dostawy! Uzupełnij pola.";
    /**
     * Message for empty postponement date error (of type: String)
     */
    private static final String EMPTY_POSTPONEMENT_DATE = "Nie określono daty odroczenia! Uzupełnij pole.";
    /**
     * Message for invalid postponement date error (of type: String)
     */
    private static final String INVALID_POSTPONEMENT_DATE = "Wprowadzono niepoprawną datę odroczenia! Musi mieścić się w zakresie 7 - 30 dni licząc od dnia dzisiejszego.";
    /**
     * Message for invalid shopping cart error (of type: String)
     */
    private static final String INVALID_SHOPPING_CART = "Wystąpił błąd z koszykiem! Sprawdź, czy dodałeś towary do koszyka przed złożeniem zamówienia.";

    /**
     * List of errors (of type: String)
     */
    private List<String> errors = new ArrayList<>();

    /**
     * Prepared order (of type: Order)
     * Null if not set
     */
    private Order order;

    /**
     * Data Access Object for order (of type: OrderDAO)
     */
    @Autowired
    private OrderDAO orderDAO;

    /**
     * Account service object (of type: AccountService)
     */
    @Autowired
    private AccountService accountService;

    /**
     * Mail sender object (of type: JavaMailSender)
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Template engine for mail (of type: TemplateEngine)
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Address service object (of type: AddressService)
     */
    @Autowired
    private AddressService addressService;

    /**
     * Method that gets order by its id
     *
     * @param id    order id (of type: int)
     * @return order object for given id (of type: Order)
     */
    @Override
    @Transactional
    public Order getOrder(int id) {
        Order order = orderDAO.getOrder(id);
        return order;
    }

    /**
     * Method that gets blueprint for order for given shopping cart (of type: Order)
     *
     * @param shoppingCart  shopping cart, data source for blueprint (of type: ShoppingCart)
     * @return partially filled order object (of type: Order)
     */
    @Override
    public Order getOrderBlueprint(ShoppingCart shoppingCart) {
        Order blueprint = new Order();
        setOrderPositions(blueprint, shoppingCart);
        blueprint.setPersonalCollection(true);
        blueprint.setInvoice(false);
        return blueprint;
    }

    /**
     * Method that translates shopping cart positions to order positions and sets them for order object
     *
     * @param blueprint     order blueprint to fill (of type: Order)
     * @param shoppingCart  shopping cart, data source (of type: ShoppingCart)
     */
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

    /**
     * Method that validates order blueprint
     *
     * @param blueprint     order blueprint to validate (of type: Order)
     * @return validation success or failure (od type: boolean)
     */
    @Override
    public boolean validateOrder(Order blueprint) {
        errors.clear();
        return validatePositions(blueprint) && validateShipmentAddress(blueprint);
    }

    /**
     * Method that sets order postponement time based on given date
     * Validates postponement time - must be in range <7, 30>
     *
     * @param blueprint     order blueprint (of type: Order)
     * @param date          postponement date (of type: Date)
     * @return validation success or failure (of type: boolean)
     */
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

    /**
     * Method that sets postponement time for order blueprint
     * Postponement value must be greater than 0
     *
     * @param blueprint     order blueprint (of type: Order)
     * @param value         postponement time value (of type: Integer)
     * @return success or failure to set postponement (of type: boolean)
     */
    @Override
    public boolean setPostponement(Order blueprint, Integer value) {
        if(value < 0)
            return false;
        blueprint.setPostponementTime(value);
        return true;
    }

    /**
     * Getter for errors
     *
     * @return errors (of type: List<String>)
     */
    @Override
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Method that prepares order
     * Filled data:     order positions (of type: List<OrderPosition>)
     *                  invoice (of type: Boolean)
     *                  invoice address, null if invoice is false (of type: Address)
     *                  personal collection (of type: Boolean)
     *                  delivery address, null if personal collection is true (of type: Address)
     *                  delivery cost, 0.0 if personal collection is true (of type: Double)
     *                  postponement time (of type: Integer)
     *                  complaining, set to false (of type: Boolean)
     *
     * @param blueprint
     */
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

    /**
     * Getter for prepared order
     * Null if not set
     *
     * @return prepared order (of type: Order)
     */
    @Override
    public Order getPreparedOrder() {
        return order;
    }

    /**
     * Method that saves order to database
     *
     * @param order     order to save (of type: Order)
     * @return save success or failuer (of type: Boolean)
     */
    @Override
    public boolean saveOrder(Order order) {
        Date submissionDate = new Date();
        order.setSubmissionDate(submissionDate);
        order.setNumber(getNextOrderNumber(submissionDate));
        order.setStatus(Order.OrderStatus.submitted);
        order.setCustomer(accountService.getCurrentCustomer().getCustomer());
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

    /**
     * Method that calculates delivery cost for order
     *
     * @param order     order (of type: Order)
     * @return delivery cost (of type: Double)
     */
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

    /**
     * Method that validates order positions
     * Adds error message to errors if invalid
     *
     * @param blueprint     order blueprint to validate (of type: Order)
     * @return validation success or failuer (of type: boolean)
     */
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

    /**
     * Method that validates shipment address
     * Adds error message to errors if invalid
     *
     * @param blueprint     order blueprint to validate (of type: Order)
     * @return validation success or failure (of type: boolean)
     */
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

    /**
     * Method that calculates postponement time based on given date
     *
     * @param date      date, postponement source (of type: Date)
     * @return postponement time in days (of type: int)
     */
    private int calculatePostponement(Date date) {
        Date now = new Date();
        long diff = date.getTime() - now.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * Method that gets next order number based on given submission date
     *
     * @param date      order submission date (of type: Date)
     * @return next order number (of type: String)
     */
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

    /**
     * Method that gets order value
     *
     * @param order     order, data source (of type: Order)
     * @return order value (of type: double)
     */
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

    /**
     * Method that sends order confirmation email
     * May throw MessagingException
     *
     * @param order     placed order (of type: Order)
     * @throws MessagingException
     */
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
