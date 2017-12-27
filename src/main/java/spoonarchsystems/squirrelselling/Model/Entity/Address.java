package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

// TODO: utowrzyć tabelę Adresy

@Entity
@Table(name = "Adresy")
public class Address {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "miasto", nullable = false)
    private String city;

    @Column(name = "ulica")
    private String street;

    @Column(name = "nrBudynku", nullable = false)
    private String buildingNumber;

    @Column(name = "nrLokalu")
    private String localNumber;

    @Column(name = "kodPocztowy", nullable = false)
    private String postalCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(String localNumber) {
        this.localNumber = localNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
