package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Service.ComplaintService;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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
        Complaint complaint = complaintService.getComplaintForm(order);

        double orderValue = 0;
        for (OrderPosition position : order.getPositions()) {
            orderValue += position.getPrice() * position.getQuantity();
        }

        model.addAttribute("complaintForm", complaint);
        model.addAttribute("order", order);
        model.addAttribute("orderValue", orderValue);
        return "view/complaint/complaint_form";
    }

    @PostMapping("/complaintSummary")
    public String prepareComplaintPositions(@ModelAttribute Complaint complaint, Model model) {
        Order order = orderService.getOrder(complaint.getOrder().getId());
        Set<Integer> errors = complaintService.validateComplaint(complaint,order);

        if (!errors.isEmpty()) {
            double orderValue = 0;
            for (OrderPosition position : order.getPositions()) {
                orderValue += position.getPrice() * position.getQuantity();
            }

            model.addAttribute("complaintForm", complaint);
            model.addAttribute("order", order);
            model.addAttribute("orderValue", orderValue);
            model.addAttribute("errors", errors);
            return "view/complaint/complaint_form";
        }

        Complaint preparedComplaint = complaintService.prepareComplaint(complaint, order);

        model.addAttribute("complaint", preparedComplaint);
        return "view/complaint/complaint_summary";
    }

    @PostMapping("/makeComplaint")
    public String makeComplaint(@ModelAttribute Complaint complaint, Model model) {
        if (complaint.getDescription().isEmpty()) {
            model.addAttribute("emptyDescription", true);
            return "view/complaint/complaint_summary";
        }

        boolean isSaved = complaintService.saveComplaint(complaint);

        if (!isSaved) {
            model.addAttribute("connectionError", true);
            return "view/complaint/complaint_summary";
        }

        return "view/complaint/complaint_confirmation";
    }
}
