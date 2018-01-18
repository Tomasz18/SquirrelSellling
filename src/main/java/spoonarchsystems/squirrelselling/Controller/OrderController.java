package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spoonarchsystems.squirrelselling.Model.Entity.CustomerAccount;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Service.AccountService;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;
import spoonarchsystems.squirrelselling.Utils.BooleanWrapper;
import spoonarchsystems.squirrelselling.Utils.DateWrapper;

import javax.mail.MessagingException;
import java.util.ArrayList;

/**
 * A controller handles order submission process and order presentation for user
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Fills model with requested {@link Order} and its value to present order details
     * @param id an ID of order to show
     * @param model container for order and its value needed by view
     * @return order details view name
     */
    @GetMapping("/order/{id}")
    public String showOrderDetails(@PathVariable int id, Model model) {
        Order order = orderService.getOrder(id);
        double orderValue = orderService.getOrderValue(order);

        model.addAttribute("order", order);
        model.addAttribute("orderValue", orderValue);
        return "view/order/order_details";
    }

    /**
     * Prepares orders to show submitted by currently logged in user. Adds prepared orders
     * to model container.
     * @param model container for orders to show
     * @return order list view name
     */
    @GetMapping("/orders")
    public String showOrders(Model model) {
        CustomerAccount customerAccount = accountService.getCurrentCustomer();
        ArrayList<Double> ordersValues = new ArrayList<>();
        for (Order order : customerAccount.getCustomer().getOrders()) {
            ordersValues.add(orderService.getOrderValue(order));
        }

        model.addAttribute("orders", customerAccount.getCustomer().getOrders());
        model.addAttribute("ordersValues", ordersValues);
        return "view/order/order_list";
    }

    /**
     * Prepares {@link Order} to fill with wares from shopping cart and options chosen by user.
     * Function returns order form view name.
     * @param shoppingCart contains wares added by user during shopping
     * @param model container for option objects to fill in oder form
     * @return order form view name
     */
    @PostMapping(value="/orderForm")
    public String placeOrder(@ModelAttribute ShoppingCart shoppingCart, Model model) {
        if(shoppingCart != null && shoppingCart.getPositions() != null && !shoppingCart.getPositions().isEmpty()) {
            shoppingCartService.updateQuantity(shoppingCart);
        }
        model.addAttribute("shoppingCart", shoppingCartService.getShoppingCart());
        model.addAttribute("orderBlueprint", orderService.getOrderBlueprint(shoppingCartService.getShoppingCart()));
        BooleanWrapper postponementWrapper = new BooleanWrapper();
        postponementWrapper.setValue(false);
        model.addAttribute("postponement", postponementWrapper);
        model.addAttribute("postponementDateWrapper", new DateWrapper());
        return "view/order/order_form";
    }

    /**
     * Gets {@link Order} parameters such as order positions, postponement date, addresses, and bill type
     * and prepares {@link Order} to save. It also validates retrieved data and forwards back to order form
     * if necessary.
     * @param blueprint an incomplete order object with part of data
     * @param postponementDateWrapper a wrapper containing postponement date
     * @param postponementBooleanWrapper a wrapper containing user choice to delay order realization or not
     * @param model container for order summary data
     * @return forwarding to order form in case of validation fail or order summary view name in opposite case
     */
    @PostMapping(value="/orderSummary")
    public String orderSummary(@ModelAttribute Order blueprint,
                               @ModelAttribute DateWrapper postponementDateWrapper,
                               @ModelAttribute BooleanWrapper postponementBooleanWrapper,
                               Model model) {
        orderService.setOrderPositions(blueprint, shoppingCartService.getShoppingCart());

        if(!orderService.validateOrder(blueprint)) {
            return "forward:/orderForm";
        }

        if(postponementBooleanWrapper.getValue()) {
            if (!orderService.setPostponement(blueprint, postponementDateWrapper.getDate()))
                return "forward:/orderForm";
        }
        else
            orderService.setPostponement(blueprint, 0);

        model.addAttribute("orderBlueprint", blueprint);
        model.addAttribute("customer", accountService.getCurrentCustomer().getCustomer());

        orderService.prepareOrder(blueprint);

        return "view/order/order_summary";
    }

    /**
     * Saves order to database and sends confirmation e-mail with order details to customer
     * @return order confirmation view name
     */
    @PostMapping(value="/orderConfirmation")
    public String orderConfirmation() {
        Order order = orderService.getPreparedOrder();
        boolean saved = orderService.saveOrder(order);
        if (saved) {
            try {
                orderService.sendOrderConfirmationEmail(order);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return "view/order/order_confirmation";
    }
}
