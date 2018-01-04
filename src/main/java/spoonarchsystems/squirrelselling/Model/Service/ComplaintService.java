package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.Order;

import java.util.Set;

public interface ComplaintService {
    Complaint getComplaintForm(Order order);
    Set<Integer> validateComplaint(Complaint complaint, Order order);
    Complaint prepareComplaint(Complaint complaint, Order order);
}
