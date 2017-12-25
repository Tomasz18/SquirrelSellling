package spoonarchsystems.squirrelselling.Model.Entity;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "zamowienia")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @OrderBy("number")
    private List<OrderPosition> positions;

    @ManyToOne
    @JoinColumn(name = "idKlienta", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(name = "numer", columnDefinition = "char(15)", nullable = false, unique = true)
    private String number;

    @Column(name = "status", nullable = false, length = 25)
    private String status;

    @Column(name = "dataZlozenia", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date submissionDate;

    @Column(name = "czasRealizacji")
    private int realizationTime;

    @Column(name = "czasOdroczenia", nullable = false)
    private int postponementTime;

    @Column(name = "dataSprzedazy")
    @Temporal(TemporalType.DATE)
    private Date saleDate;

    @Column(name = "reklamacyjne", nullable = false)
    private boolean complaining;

    @Column(name = "dataSkompletowania")
    @Temporal(TemporalType.DATE)
    private Date completionDate;

    @Column(name = "czasDostawy")
    private int deliveryTime;

    @Column(name = "odbiorOsobisty", nullable = false)
    private boolean personalCollection;

    @Column(name = "kosztDostawy", nullable = false)
    private double deliveryCost;

    @Column(name = "dataOdbioru")
    @Temporal(TemporalType.DATE)
    private Date collectionDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<OrderPosition> positions) {
        this.positions = positions;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getRealizationTime() {
        return realizationTime;
    }

    public void setRealizationTime(int realizationTime) {
        this.realizationTime = realizationTime;
    }

    public int getPostponementTime() {
        return postponementTime;
    }

    public void setPostponementTime(int postponementTime) {
        this.postponementTime = postponementTime;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public boolean isComplaining() {
        return complaining;
    }

    public void setComplaining(boolean complaining) {
        this.complaining = complaining;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isPersonalCollection() {
        return personalCollection;
    }

    public void setPersonalCollection(boolean personalCollection) {
        this.personalCollection = personalCollection;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
}
