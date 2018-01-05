package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reklamacje")
public class Complaint {

    public enum ComplaintStatus {
        submitted, accepted, rejected
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "idZamowienia", referencedColumnName = "id")
    private Order order;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL)
    @OrderBy("numer")
    private List<ComplaintPosition> positions;

    @Column(name = "numer", columnDefinition = "char(15)", nullable = false, unique = true)
    private String number;

    @Column(name = "status", length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    @Column(name = "dataZlozenia", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date submissionDate;

    @Column(name = "opis", nullable = false)
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ComplaintPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ComplaintPosition> positions) {
        this.positions = positions;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
