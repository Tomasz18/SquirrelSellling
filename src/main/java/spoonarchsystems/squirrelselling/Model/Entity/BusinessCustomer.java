package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;
import java.io.Serializable;

// TODO: utworzyć tabelę Przedsiębiorstwa

@Entity
@Table(name = "przedsiebiorstwa")
public class BusinessCustomer  implements Serializable{

    @Id
    @OneToOne
    @JoinColumn(name = "idKlienta", referencedColumnName = "id")
    private Customer customer;

    @Column(nullable = false, columnDefinition = "char(9)")
    private String nip;

    @Column(nullable = false, length = 14)
    private String regon;

    @Column(name = "nazwa", nullable = false, length = 128)
    private String name;

    @Column(name = "imieWlasciciela", nullable = false, length = 20)
    private String ownerName;

    @Column(name = "nazwiskoWlasciciela", nullable = false, length = 30)
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
