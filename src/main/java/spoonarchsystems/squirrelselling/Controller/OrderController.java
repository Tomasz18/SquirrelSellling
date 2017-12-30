package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;
import spoonarchsystems.squirrelselling.Model.Entity.Order;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/{id}")
    public String showOrderDetails(@PathVariable int id, Model model) {
        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);
        return "view/order/order_details";
    }
}
