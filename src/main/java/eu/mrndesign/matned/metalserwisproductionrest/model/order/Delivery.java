package eu.mrndesign.matned.metalserwisproductionrest.model.order;

import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DELIVERY")
public class Delivery extends BaseEntity implements AuditInterface {

    @Enumerated
    private DeliveryType deliveryType;

    private LocalDateTime deliveryTime;
    private String description;

    private boolean isShipped;

    public Delivery() {
    }

    public Delivery(DeliveryType deliveryType, LocalDateTime deliveryTime, String description) {
        this.deliveryType = deliveryType;
        this.deliveryTime = deliveryTime;
        this.description = description;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
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
        if (!super.equals(o)) return false;
        Delivery delivery = (Delivery) o;
        return deliveryType == delivery.deliveryType &&
                Objects.equals(deliveryTime, delivery.deliveryTime) &&
                Objects.equals(description, delivery.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deliveryType, deliveryTime, description);
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryType=" + deliveryType +
                ", deliveryTime=" + deliveryTime +
                ", description='" + description + '\'' +
                '}';
    }

    public enum DeliveryType{
        CARRIER,
        SHIPPING,
        OWN,
        CLIENT

    }
}
