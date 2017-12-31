package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

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
}
