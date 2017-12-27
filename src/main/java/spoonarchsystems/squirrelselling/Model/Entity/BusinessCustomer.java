package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

// TODO: utworzyć tabelę Przedsiębiorstwa

@Entity
@Table(name = "Przedsiebiorstwa")
public class BusinessCustomer {

    @Id
    @JoinColumn(name = "idKlienta", referencedColumnName = "id")
    private Customer customer;

    @Column(nullable = false)
    private String nip;

    @Column(nullable = false)
    private String regon;

    @Column(name = "nazwa", nullable = false)
    private String name;

    @Column(name = "imieWlasciciela", nullable = false)
    private String ownerName;

    @Column(name = "nazwiskoWlasciciela", nullable = false)
    private String ownerSurname;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname = ownerSurname;
    }
}
