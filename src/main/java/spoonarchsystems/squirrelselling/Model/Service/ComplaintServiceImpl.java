package spoonarchsystems.squirrelselling.Model.Service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import spoonarchsystems.squirrelselling.Model.DAO.ComplaintDAO;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.ComplaintPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service class for Complaint.
 * Implements ComplaintService interface.
 * Scoped for session.
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplaintServiceImpl implements ComplaintService {

    /**
     * Error code for invalid position (of type: int).
     */
    public static int INVALID_POSITION = 0;
    /**
     * Error code for bad position amount (of type: int).
     */
    public static int BAD_AMOUNT = 1;

    /**
     * Prepared complaint (of type: {@link Complaint}).
     * Null when no complaint in preparation.
     */
    private Complaint complaint;

    /**
     * Data Access Object for complaint (of type: {@link ComplaintDAO}).
     */
    @Autowired
    private ComplaintDAO complaintDAO;

    /**
     * Method that gets form data for complaint (of type: {@link Complaint}),
     * Filled data:     complaint positions (of type: {@link ArrayList}),
     *                  source order (of Type: Order).
     *
     * @param order     order, source for complaint (of type: {@link Order})
     * @return partially filled complaint (of type: {@link Complaint})
     */
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

    /**
     * Method for complaint validation based on source order.
     * Handles errors:  position is null (code: INVALID_POSITION),
     *                  amount is null or less than 0 or complaint position quantity is greater than order position quantity (code: BAD_AMOUNT).
     *
     * @param complaint complaint to validate (of type: {@link Complaint})
     * @param order     source order (of type: Order)
     * @return set of errors, empty when successfully validated (of type: {@link Set})
     */
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
                } else if (complaintPosition.getQuantity() == null || complaintPosition.getQuantity() <= 0 || complaintPosition.getQuantity() > orderPosition.getQuantity()) {
                    errors.add(BAD_AMOUNT);
                }
            }
        }
        if (allNulls) {
            errors.add(BAD_AMOUNT);
        }
        return  errors;
    }

    /**
     * Method that prepares complaint to be submitted.
     * Filled data:     complaint positions with correct numbers (of type: {@link ArrayList}),
     *                  source order (of type: {@link Order}).
     *
     * @param complaint complaint object to fill (of type: {@link Complaint})
     * @param order     source order (of type: {@link Order})
     * @return prepared complaint (of type: {@link Complaint})
     */
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

    /**
     * Getter for prepared complaint.
     * Returns null if prepared complaint is not set.
     *
     * @return prepared complaint (of type: {@link Complaint})
     */
    @Override
    public Complaint getCurrentComplaint() {
        return this.complaint;
    }

    /**
     * Method that saves complaint to database.
     *
     * @param complaint complaint to save (of type: {@link Complaint})
     * @return save success or failure (of type: boolean)
     */
    @Transactional
    @Override
    public boolean saveComplaint(Complaint complaint) {
        Date submissionDate = new Date();
        String complaintNumber = getNextComplaintNumber(submissionDate);
        if (complaintNumber.length() > 15) {
            return false;
        }
        this.complaint.setDescription(complaint.getDescription());
        this.complaint.setNumber(complaintNumber);
        this.complaint.setStatus(Complaint.ComplaintStatus.submitted);
        this.complaint.setSubmissionDate(submissionDate);
        try {
            complaintDAO.saveComplaint(this.complaint);
        } catch (HibernateException ex) {
            return false;
        }
            return true;
    }

    /**
     * Method that finds order position by its number in given order positions list.
     * Returns null if position not found.
     *
     * @param number    position number to find (of type: Integer)
     * @param positions list of positions, search source (of type: {@link List})
     * @return found position or null (of type: {@link OrderPosition})
     */
    private OrderPosition findOrderPosition(Integer number, List<OrderPosition> positions) {
        for (OrderPosition orderPosition : positions) {
            if (orderPosition.getNumber().equals(number)) {
                return orderPosition;
            }
        }
        return null;
    }

    /**
     * Method that gets next complaint number.
     *
     * @param date complaint submission date (of type: Date)
     * @return next complaint number (of type: String)
     */
    private String getNextComplaintNumber(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        ArrayList<Integer> numbers = new ArrayList<>();
        Integer maxNumber = 0;

        List<Complaint> complaints = complaintDAO.getComplaintsBySubmissionDate(date);
        if (!complaints.isEmpty()) {
            for (Complaint complaint : complaints) {
                numbers.add(Integer.parseInt(complaint.getNumber().substring(0, 4)));
            }
            maxNumber = Collections.max(numbers);
        }
        return String.format("%04d", maxNumber + 1) + "/" + formattedDate;
    }
}
