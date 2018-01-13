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

import java.util.ArrayList;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/order/{id}")
    public String showOrderDetails(@PathVariable int id, Model model) {
        Order order = orderService.getOrder(id);
        double orderValue = orderService.getOrderValue(order);

        model.addAttribute("order", order);
        model.addAttribute("orderValue", orderValue);
        return "view/order/order_details";
    }

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

    @PostMapping(value="/orderSummary")
    public String orderSummary(@ModelAttribute Order blueprint, @ModelAttribute DateWrapper postponementDateWrapper, @ModelAttribute BooleanWrapper postponementBooleanWrapper, Model model) {
        orderService.setOrderPositions(blueprint, shoppingCartService.getShoppingCart());

        if(!orderService.validateOrder(blueprint)) {
            return "forward:/orderForm";
        }

        System.out.println("##### postponementBoolean: " + postponementBooleanWrapper.getValue());

        if(postponementBooleanWrapper.getValue()) {
            if (!orderService.setPostponement(blueprint, postponementDateWrapper.getDate()))
                return "forward:/orderForm";
        }
        else
            orderService.setPostponement(blueprint, 0);

        model.addAttribute("orderBlueprint", blueprint);
        model.addAttribute("customer", accountService.getCurrentCustomer().getCustomer());

        return "view/order/order_summary";
    }

    @PostMapping(value="/orderConfirmation")
    public String orderConfirmation() {
        return "view/order/order_confirmation";
    }
}
