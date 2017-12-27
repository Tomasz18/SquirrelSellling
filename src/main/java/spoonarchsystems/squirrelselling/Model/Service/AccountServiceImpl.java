package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spoonarchsystems.squirrelselling.Model.DAO.CustomerAccountDAO;
import spoonarchsystems.squirrelselling.Model.Entity.CustomerAccount;

@Service
public class AccountServiceImpl implements AccountService {
    static final int CURRENT_CUSTOMER_ID = 0;

    @Autowired
    private CustomerAccountDAO customerAccountDAO;

    @Override
    public CustomerAccount getCurrentCustomer() {
        CustomerAccount account = customerAccountDAO.getCustomerAccount(CURRENT_CUSTOMER_ID);
        return account;
    }
}
