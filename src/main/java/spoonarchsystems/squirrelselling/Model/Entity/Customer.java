package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entity describes a customer contains submitted order list and
 * reference to related business or individual customer
 */
@Entity
@Table(name = "klienci")
public class Customer implements Serializable{

    /**
     * Represents customer type
     */
    public enum CustomerType {
        /**
         * Individual customer
         */
        I,
        /**
         * Business customer
         */
        P }

    @Id
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "customer")
    @OrderBy("dataZlozenia")
    private List<Order> orders;

    @OneToOne(mappedBy = "customer")
    private IndividualCustomer individualCustomer;

    @OneToOne(mappedBy = "customer")
    private BusinessCustomer businessCustomer;

    @Column(name = "numer", length = 19, unique = true, nullable = false)
    private String number;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon", length = 15, nullable = false)
    private String phone;

    @Column(name = "typKlienta", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public IndividualCustomer getIndividualCustomer() {
        return individualCustomer;
    }

    public void setIndividualCustomer(IndividualCustomer individualCustomer) {
        this.individualCustomer = individualCustomer;
    }

    public BusinessCustomer getBusinessCustomer() {
        return businessCustomer;
    }

    public void setBusinessCustomer(BusinessCustomer businessCustomer) {
        this.businessCustomer = businessCustomer;
    }
}
