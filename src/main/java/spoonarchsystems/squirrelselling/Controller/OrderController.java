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
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Service.AccountService;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

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
        shoppingCartService.updateQuantity(shoppingCart);
        model.addAttribute("shoppingCart", shoppingCartService.getShoppingCart());
        model.addAttribute("shoppingCartSum", shoppingCartService.getSum());
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

    @GetMapping(value="/orderSummary")
    public String orderSummary() {
        return "view/order/order_summary";
    }
}
