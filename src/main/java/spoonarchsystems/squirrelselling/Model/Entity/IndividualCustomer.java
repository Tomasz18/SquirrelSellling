package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

// TODO: utworzyć tabelę KlienciIndywidualni

@Entity
@Table(name = "KlienciIndywidualni")
public class IndividualCustomer {

    @Id
    @JoinColumn(name = "idKlienta", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "imie", nullable = false)
    private String name;

    @Column(name = "nazwisko", nullable = false)
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
