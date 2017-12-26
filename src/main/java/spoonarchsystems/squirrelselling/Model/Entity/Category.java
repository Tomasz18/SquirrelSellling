package spoonarchsystems.squirrelselling.Model.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "kategorie")
public class Category {

    @Id
    @Column(name = "nazwa", length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "nadkategoria", referencedColumnName = "nazwa")
    private Category overCategory;

    @OneToMany(mappedBy = "overCategory")
    private Set<Category> subcategories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getOverCategory() {
        return overCategory;
    }

    public void setOverCategory(Category overCategory) {
        this.overCategory = overCategory;
    }

    public Set<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
