package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Order getOrder(int id) {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.load(Order.class, new Integer(id));
        return order;
    }

    @Override
    public List<Order> getOrdersBySubmissionDate(Date date) {
        Session session = sessionFactory.getCurrentSession();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = session.createQuery("FROM Order WHERE data_zlozenia = :submissionDate");
        query.setParameter("submissionDate", dateFormat.format(new Date()));
        return (ArrayList<Order>) query.list();
    }

    @Override
    public void saveOrder(Order order) {
        sessionFactory.getCurrentSession().persist(order);
    }
}
