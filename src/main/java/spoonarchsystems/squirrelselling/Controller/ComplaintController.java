package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Service.ComplaintService;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/complaintForm", params = {"id"})
    public String showComplaintForm(final HttpServletRequest req, Model model) {
        int orderId = Integer.valueOf(req.getParameter("id"));
        Order order = orderService.getOrder(orderId);
        Complaint complaintForm = complaintService.getComplaintForm(order);

        double orderValue = 0;
        for (OrderPosition position : order.getPositions()) {
            orderValue += position.getPrice() * position.getQuantity();
        }

        model.addAttribute("complaintForm", complaintForm);
        model.addAttribute("order", order);
        model.addAttribute("orderValue", orderValue);
        return "view/complaint/complaint_form";
    }
}