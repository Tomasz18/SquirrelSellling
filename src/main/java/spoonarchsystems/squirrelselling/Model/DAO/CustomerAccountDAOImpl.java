package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spoonarchsystems.squirrelselling.Model.Entity.CustomerAccount;

@Repository
public class CustomerAccountDAOImpl implements CustomerAccountDAO{

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public CustomerAccount getCustomerAccount(int id) {
        Session session = sessionFactory.getCurrentSession();
        CustomerAccount customerAccount = session.load(CustomerAccount.class, new Integer(id));
        return customerAccount;
    }
}
