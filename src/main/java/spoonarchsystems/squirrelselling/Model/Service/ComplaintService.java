package spoonarchsystems.squirrelselling.Model.Service;

import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.Order;

public interface ComplaintService {
    Complaint getComplaintForm(Order order);
}
