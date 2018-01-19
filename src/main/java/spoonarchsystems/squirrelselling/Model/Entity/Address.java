package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity describes address
 */
@Entity
@Table(name = "adresy")
public class Address {

    @Id
    @Column(name = "id", columnDefinition = "char(128)")
    private String id;

    @Column(name = "miasto", nullable = false, length = 80)
    private String city;

    @Column(name = "ulica", length = 80)
    private String street;

    @Column(name = "nrBudynku", nullable = false, length = 5)
    private String buildingNumber;

    @Column(name = "nrLokalu", length = 5)
    private String localNumber;

    @Column(name = "kodPocztowy", nullable = false, length = 6)
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
