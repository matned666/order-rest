package eu.mrndesign.matned.metalserwisproductionrest.model.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class EntityDescription {

    private String name;
    @Column(length = 2000)
    private String description;

    public EntityDescription() {
    }

    public EntityDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDescription that = (EntityDescription) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "EntityDescription{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
