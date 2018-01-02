package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.stereotype.Service;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.ComplaintPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;


import java.util.ArrayList;

@Service
public class ComplaintServiceImpl implements ComplaintService {

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
        return complaintForm;
    }
}
