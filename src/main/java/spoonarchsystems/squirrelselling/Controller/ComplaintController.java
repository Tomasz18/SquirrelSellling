package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Service.ComplaintService;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * A controller handles complaint submission process.
 * It manages complaint form showing and submission, also saves complaint into database.
 */
@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private OrderService orderService;

    /**
     * Prepares complaint form for order specified in request and adds
     * it to model for returned view. It also adds {@link Order} specified
     * in request and its value.
     * @param req a request contains {@link Order} id to complaint
     * @param model model container for complaint from, complaining {@link Order} and its value
     * @return complaint form view name
     */
    @PostMapping(value = "/complaintForm", params = {"id"})
    public String showComplaintForm(final HttpServletRequest req, Model model) {
        int orderId = Integer.valueOf(req.getParameter("id"));
        Order order = orderService.getOrder(orderId);
        Complaint complaint = complaintService.getComplaintForm(order);

        double orderValue = orderService.getOrderValue(order);

        model.addAttribute("complaintForm", complaint);
        model.addAttribute("order", order);
        model.addAttribute("orderValue", orderValue);
        return "view/complaint/complaint_form";
    }

    /**
     * Prepares complaint positions based on {@link Complaint} received from complaint form and then
     * fills model with {@link Complaint} for description complement.
     * In case of error detects through validating complaint positions fills model with complaint form,
     * {@link Order} which complaint is based on, its value and errors detected through complaint validation.
     * And then returns complaint form view name.
     * @param complaint complaint contains positions received from complaint form
     * @param model model container for complaint needed to fill complaint description
     * @return complaint summary view name or complaint form view name in case of validation fails
     */
    @PostMapping("/complaintSummary")
    public String prepareComplaintPositions(@ModelAttribute Complaint complaint, Model model) {
        Order order = orderService.getOrder(complaint.getOrder().getId());
        Set<Integer> errors = complaintService.validateComplaint(complaint,order);

        if (!errors.isEmpty()) {
            double orderValue = orderService.getOrderValue(order);

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

    /**
     * Validates complaint description. If there is no validation errors saves complaint int database and returns
     * complaint confirmation view name.
     * If validation or saving fails returns complaint summary view name.
     * @param complaint a complaint contains description received from from
     * @param model container for errors sends in case of validation or saving fail
     * @return complaint summary or complaint confirmation view name
     */
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
