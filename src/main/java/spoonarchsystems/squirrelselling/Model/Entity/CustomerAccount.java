package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

/**
 * Entity describes customer account with reference to customer related with
 */
@Entity
@Table(name = "kontaklientow")
public class CustomerAccount {

    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "idKlienta", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(name = "login", length = 30, unique = true, nullable = false)
    private String login;

    @Column(name = "haslo", columnDefinition = "char(128)", nullable = false)
    private String password;

    @Column(name = "status", nullable = false, length = 15)
    private String status;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "typKlienta", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private Customer.CustomerType customerType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Customer.CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Customer.CustomerType customerType) {
        this.customerType = customerType;
    }
}
