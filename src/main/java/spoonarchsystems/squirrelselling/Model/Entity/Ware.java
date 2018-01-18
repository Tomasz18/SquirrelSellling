package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;

/**
 * Entity describes ware with reference to category
 */
@Entity
@Table(name = "towary")
public class Ware {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "nazwaKategori", referencedColumnName = "nazwa", nullable = false)
    private Category category;

    @Column(name = "kod", length = 10, nullable = false)
    private String code;

    @Column(name = "jednostka", length = 10, nullable = false)
    private String unit;

    @Column(name = "nazwa", length = 64, unique = true, nullable = false)
    private String name;

    @Column(name = "stanDyspozycyjny", nullable = false)
    private double disposableState;

    @Column(name = "progHurtowy", nullable = false)
    private double wholesaleThreshold;

    @Column(name = "opis", nullable = false)
    private String description;

    @Column(name = "waga", nullable = false)
    private double weight;

    @Column(name = "cenaHurtowa", nullable = false)
    private double wholesalePrice;

    @Column(name = "cenaDetaliczna", nullable = false)
    private double retailPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDisposableState() {
        return disposableState;
    }

    public void setDisposableState(double disposableState) {
        this.disposableState = disposableState;
    }

    public double getWholesaleThreshold() {
        return wholesaleThreshold;
    }

    public void setWholesaleThreshold(double wholesaleThreshold) {
        this.wholesaleThreshold = wholesaleThreshold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
