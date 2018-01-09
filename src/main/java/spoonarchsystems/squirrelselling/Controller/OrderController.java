package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;
import spoonarchsystems.squirrelselling.Utils.DateWrapper;

import java.util.Calendar;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/order/{id}")
    public String showOrderDetails(@PathVariable int id, Model model) {
        Order order = orderService.getOrder(id);
        double orderValue = 0;
        for (OrderPosition position : order.getPositions()) {
            orderValue += position.getPrice() * position.getQuantity();
        }

        model.addAttribute("order", order);
        model.addAttribute("orderValue", orderValue);
        return "view/order/order_details";
    }

    @PostMapping(value="/orderForm")
    public String placeOrder(@ModelAttribute ShoppingCart shoppingCart, Model model) {
        shoppingCartService.updateQuantity(shoppingCart);
        model.addAttribute("shoppingCart", shoppingCartService.getShoppingCart());
        model.addAttribute("orderBlueprint", orderService.getOrderBlueprint(shoppingCartService.getShoppingCart()));
        model.addAttribute("postponementDateWrapper", new DateWrapper());
        return "view/order/order_form";
    }

    @PostMapping(value="/orderSummary")
    public String orderSummary(@ModelAttribute Order order, @ModelAttribute DateWrapper postponementDateWrapper) {
        System.out.println("##### DeliveryAddress:" + order.getDeliveryAddress().getStreet() + " " + order.getDeliveryAddress().getBuildingNumber() + "/" + order.getDeliveryAddress().getLocalNumber());
        System.out.println("#####                 " + order.getDeliveryAddress().getCity() + " " + order.getDeliveryAddress().getPostalCode());
        System.out.println("##### InvoiceAddress:" + order.getInvoiceBuyerAddress().getStreet() + " " + order.getInvoiceBuyerAddress().getBuildingNumber() + "/" + order.getInvoiceBuyerAddress().getLocalNumber());
        System.out.println("#####                 " + order.getInvoiceBuyerAddress().getCity() + " " + order.getInvoiceBuyerAddress().getPostalCode());
        System.out.println("##### PostponementDate: " + postponementDateWrapper.getDate());
        return "view/order/order_summary";
    }
}
