package spoonarchsystems.squirrelselling.Model.Entity;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "zamowienia")
public class Order {

    public enum OrderStatus {
        submitted, rejected, waitingForRealization, sent, readyToCollect, realized, canceled
    }

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

    @OneToMany(mappedBy = "order")
    private Set<Complaint> complaints;

    @ManyToOne
    @JoinColumn(name = "adresDostawy", referencedColumnName = "id")
    private Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "adresNabywcy", referencedColumnName = "id")
    private Address byuerAddress;

    @Column(name = "numer", columnDefinition = "char(15)", nullable = false, unique = true)
    private String number;

    @Column(name = "status", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "dataZlozenia", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date submissionDate;

    @Column(name = "czasRealizacji")
    private Integer realizationTime;

    @Column(name = "czasOdroczenia", nullable = false)
    private Integer postponementTime;

    @Column(name = "dataSprzedazy")
    @Temporal(TemporalType.DATE)
    private Date saleDate;

    @Column(name = "reklamacyjne", nullable = false)
    private Boolean complaining;

    @Column(name = "dataSkompletowania")
    @Temporal(TemporalType.DATE)
    private Date completionDate;

    @Column(name = "czasDostawy")
    private Integer deliveryTime;

    @Column(name = "odbiorOsobisty", nullable = false)
    private Boolean personalCollection;

    @Column(name = "kosztDostawy", nullable = false)
    private Double deliveryCost;

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Integer getRealizationTime() {
        return realizationTime;
    }

    public void setRealizationTime(Integer realizationTime) {
        this.realizationTime = realizationTime;
    }

    public Integer getPostponementTime() {
        return postponementTime;
    }

    public void setPostponementTime(Integer postponementTime) {
        this.postponementTime = postponementTime;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Boolean isComplaining() {
        return complaining;
    }

    public void setComplaining(Boolean complaining) {
        this.complaining = complaining;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Boolean isPersonalCollection() {
        return personalCollection;
    }

    public void setPersonalCollection(Boolean personalCollection) {
        this.personalCollection = personalCollection;
    }

    public Double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Set<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Set<Complaint> complaints) {
        this.complaints = complaints;
    }
}
