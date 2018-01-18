package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity describes individual customer
 * contains reference to {@link Customer} connected with
 */
@Entity
@Table(name = "klienciindywidualni")
public class IndividualCustomer implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "idKlienta", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "imie", nullable = false, length = 20)
    private String name;

    @Column(name = "nazwisko", nullable = false, length = 30)
    private String surname;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
