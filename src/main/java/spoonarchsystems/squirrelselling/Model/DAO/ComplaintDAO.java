package spoonarchsystems.squirrelselling.Model.DAO;

import spoonarchsystems.squirrelselling.Model.Entity.Complaint;

import java.util.Date;
import java.util.List;

public interface ComplaintDAO {
    void saveComplaint(Complaint complaint);
    List<Complaint> getComplaintsBySubmissionDate(Date submissionDate);
}
