package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
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

    @Transactional
    @Override
    public List<Order> getOrdersBySubmissionDate(Date date) {
        Session session = sessionFactory.getCurrentSession();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = session.createQuery("FROM Order WHERE data_zlozenia = :submissionDate");
        query.setParameter("submissionDate", dateFormat.format(new Date()));
        return (ArrayList<Order>) query.list();
    }

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
            else {
                System.out.println("##### else");
            }
        }

        if(!order.getPersonalCollection()) {
            Address deliveryAddress = order.getDeliveryAddress();
            Address address = session.get(Address.class, deliveryAddress.getId());
            if(address == null) {
                session.persist(deliveryAddress);
            }
            else {
                System.out.println("##### else");
            }
        }

//        List<Order> orderList = order.getCustomer().getOrders();
//        if(orderList == null)
//            orderList = new ArrayList<>();
//        orderList.add(order);
//        order.getCustomer().setOrders(orderList);

        System.out.println("##### order.customer.id = " + order.getCustomer().getId());
        System.out.println("##### order.customer.type = " + order.getCustomer().getCustomerType());
        System.out.println("##### order.customer.business = " + order.getCustomer().getBusinessCustomer());
        System.out.println("##### order.customer.individual = " + order.getCustomer().getIndividualCustomer());
        System.out.println("##### order.customer.individual.name = " + order.getCustomer().getIndividualCustomer().getName());
        System.out.println("##### order.invoiceAddress = " + order.getInvoiceBuyerAddress());
        System.out.println("##### order.deliveryAddress = " + order.getDeliveryAddress());

        session.persist(order);
    }
}
