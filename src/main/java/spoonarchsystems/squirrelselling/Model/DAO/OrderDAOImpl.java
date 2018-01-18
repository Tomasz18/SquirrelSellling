package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spoonarchsystems.squirrelselling.Model.Entity.Address;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Entity.OrderPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object provides {@link Order} database operations
 */
@Repository
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Fetches and returns {@link Order} with given id from database
     * @param id order id to fetch
     * @return order with given id
     */
    @Override
    public Order getOrder(int id) {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.load(Order.class, new Integer(id));
        return order;
    }

    /**
     * Return {@link Order} list with given submission date from database
     * @param date submission date with which orders are fetched
     * @return order lists with given submission date
     */
    @Transactional
    @Override
    public List<Order> getOrdersBySubmissionDate(Date date) {
        Session session = sessionFactory.getCurrentSession();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = session.createQuery("FROM Order WHERE data_zlozenia = :submissionDate");
        query.setParameter("submissionDate", dateFormat.format(new Date()));
        return (ArrayList<Order>) query.list();
    }

    /**
     * Persists given {@link Order}, its positions and set addresses in database
     * @param order to save in database
     */
    @Transactional
    @Override
    public void saveOrder(Order order) {
        Session session = sessionFactory.getCurrentSession();
        for (OrderPosition position : order.getPositions()) {
            position.setWare((Ware) session.merge(position.getWare()));
        }

        if(order.getInvoice()) {
            Address invoiceAddress = order.getInvoiceBuyerAddress();
            Address address = session.get(Address.class, invoiceAddress.getId());
            if(address == null) {
                session.persist(invoiceAddress);
            }
        }

        if(!order.getPersonalCollection()) {
            Address deliveryAddress = order.getDeliveryAddress();
            Address address = session.get(Address.class, deliveryAddress.getId());
            if(address == null) {
                session.persist(deliveryAddress);
            }
        }

        session.persist(order);
    }
}
