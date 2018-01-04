package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.ComplaintPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplaintServiceImpl implements ComplaintService {

    public static int INVALID_POSITION = 0;
    public static int BAD_AMOUNT = 1;

    private Complaint complaint;

    @Override
    public Complaint getComplaintForm(Order order) {
        Complaint complaintForm = new Complaint();
        ArrayList<ComplaintPosition> positions = new ArrayList<>();
        for (OrderPosition orderPosition : order.getPositions()) {
            ComplaintPosition complaintPosition = new ComplaintPosition();
            complaintPosition.setNumber(orderPosition.getNumber());
            complaintPosition.setQuantity(orderPosition.getQuantity());
            positions.add(complaintPosition);
        }
        complaintForm.setPositions(positions);
        complaintForm.setOrder(order);
        return complaintForm;
    }

    @Override
    public Set<Integer> validateComplaint(Complaint complaint, Order order) {
        HashSet<Integer> errors = new HashSet<>();
        boolean allNulls = true;
        for (ComplaintPosition complaintPosition : complaint.getPositions()) {
            if (complaintPosition.getNumber() != null) {
                allNulls = false;
                OrderPosition orderPosition = findOrderPosition(complaintPosition.getNumber(), order.getPositions());
                if (orderPosition == null) {
                    errors.add(INVALID_POSITION);
                }
                if (complaintPosition.getQuantity() <= 0 || complaintPosition.getQuantity() > orderPosition.getQuantity()) {
                    errors.add(BAD_AMOUNT);
                }
            }
        }
        if (allNulls) {
            errors.add(BAD_AMOUNT);
        }
        return  errors;
    }

    @Override
    public Complaint prepareComplaint(Complaint complaint, Order order) {
        int newNumber = 1;
        ArrayList<ComplaintPosition> positions = new ArrayList<>();
        for (ComplaintPosition complaintPosition : complaint.getPositions()) {
            if (complaintPosition.getNumber() != null) {
                OrderPosition orderPosition = findOrderPosition(complaintPosition.getNumber(), order.getPositions());
                complaintPosition.setWare(orderPosition.getWare());
                complaintPosition.setNumber(newNumber);
                complaintPosition.setComplaint(complaint);
                newNumber++;
                positions.add(complaintPosition);
            }
        }
        complaint.setPositions(positions);
        complaint.setOrder(order);
        this.complaint = complaint;
        return this.complaint;
    }

    private OrderPosition findOrderPosition(Integer number, List<OrderPosition> positions) {
        for (OrderPosition orderPosition : positions) {
            if (orderPosition.getNumber().equals(number)) {
                return orderPosition;
            }
        }
        return null;
    }
}
