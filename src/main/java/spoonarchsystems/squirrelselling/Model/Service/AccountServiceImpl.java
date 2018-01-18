package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spoonarchsystems.squirrelselling.Model.DAO.CustomerAccountDAO;
import spoonarchsystems.squirrelselling.Model.Entity.CustomerAccount;

/**
 * Service class for Account
 * Implements the AccountService interface
 */
@Service
public class AccountServiceImpl implements AccountService {
    /**
     * Mock, current customer id (of type: int)
     */
    static final int CURRENT_CUSTOMER_ID = 1;

    /**
     * Data Access Object for customer account (of type: CustomerAccountDAO)
     */
    @Autowired
    private CustomerAccountDAO customerAccountDAO;

    /**
     * Method that gets currently logged in customer account
     * Mock, for demonstration purposes only
     *
     * @return currently logged in customer account (of type: CustomerAccount)
     */
    @Override
    public CustomerAccount getCurrentCustomer() {
        CustomerAccount account = customerAccountDAO.getCustomerAccount(CURRENT_CUSTOMER_ID);
        return account;
    }
}
