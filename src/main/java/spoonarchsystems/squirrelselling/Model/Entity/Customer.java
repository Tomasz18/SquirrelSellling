package spoonarchsystems.squirrelselling.Model.Entity;

import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "klienci")
public class Customer implements Serializable{

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
    private Character customerType;

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

    public Character getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Character customerType) {
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
