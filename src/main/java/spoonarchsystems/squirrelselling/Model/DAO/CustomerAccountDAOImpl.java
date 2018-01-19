package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spoonarchsystems.squirrelselling.Model.Entity.CustomerAccount;
import spoonarchsystems.squirrelselling.Model.Entity.Order;

import java.util.ArrayList;

/**
 * Data Access Object provides {@link CustomerAccount} database operations
 */
@Repository
public class CustomerAccountDAOImpl implements CustomerAccountDAO{

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Returns {@link CustomerAccount} with set customer and its orders
     * @param id ID of customer account to fetch
     * @return customer account with given id
     */
    @Transactional
    @Override
    public CustomerAccount getCustomerAccount(int id) {
        Session session = sessionFactory.getCurrentSession();
        CustomerAccount customerAccount = session.load(CustomerAccount.class, new Integer(id));
        Criteria criteria = session.createCriteria(Order.class).add(Restrictions.eq("customer.id", customerAccount.getCustomer().getId()));
        ArrayList<Order> orders = (ArrayList<Order>) criteria.list();
        customerAccount.getCustomer().setOrders(orders);
        return customerAccount;
    }
}
