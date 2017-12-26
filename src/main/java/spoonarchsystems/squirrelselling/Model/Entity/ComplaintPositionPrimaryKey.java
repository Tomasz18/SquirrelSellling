package spoonarchsystems.squirrelselling.Model.Entity;

import java.io.Serializable;
import java.util.Objects;

public class ComplaintPositionPrimaryKey implements Serializable {
    private Complaint complaint;
    private Ware ware;

    public ComplaintPositionPrimaryKey() {}

    public ComplaintPositionPrimaryKey(Complaint complaint, Ware ware) {
        this.complaint = complaint;
        this.ware = ware;
    }

    @Override
    public boolean equals(Object obj) {
        return this.complaint.getNumber().equals(((ComplaintPositionPrimaryKey)obj).complaint.getNumber())
                && this.ware.getId() == ((ComplaintPositionPrimaryKey) obj).ware.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(complaint.getNumber(), ware.getName());
    }
}
