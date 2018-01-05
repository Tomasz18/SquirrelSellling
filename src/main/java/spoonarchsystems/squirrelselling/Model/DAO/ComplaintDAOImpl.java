package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.ComplaintPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ComplaintDAOImpl implements ComplaintDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void saveComplaint(Complaint complaint) {
        Session session = sessionFactory.getCurrentSession();
        for (ComplaintPosition complaintPosition : complaint.getPositions()) {
            complaintPosition.setWare((Ware) session.merge(complaintPosition.getWare()));
        }
        session.persist(complaint);
    }

    @Override
    public List<Complaint> getComplaintsBySubmissionDate(Date submissionDate) {
        Session session = sessionFactory.getCurrentSession();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = session.createQuery("FROM Complaint WHERE data_zlozenia = :submissionDate");
        query.setParameter("submissionDate", dateFormat.format(new Date()));
        return (ArrayList<Complaint>) query.list();
    }
}
