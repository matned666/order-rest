package eu.mrndesign.matned.metalserwisproductionrest.model.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.DeliveryDTO;
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
public class Delivery extends BaseEntity<DeliveryDTO> implements AuditInterface {

    private String deliveryCode;

    @Enumerated
    private DeliveryType deliveryType;

    private LocalDateTime pickUpTime;
    private LocalDateTime deliveryTime;
    private String description;

    private boolean isShipped;

    public Delivery() {
    }


    public Delivery(String deliveryCode,
                    String description) {
        this.deliveryCode = deliveryCode;
        this.description = description;
    }

    public Delivery(String deliveryCode,
                    DeliveryType deliveryType,
                    LocalDateTime pickUpTime,
                    LocalDateTime deliveryTime,
                    String description) {
        this.deliveryCode = deliveryCode;
        this.deliveryType = deliveryType;
        this.pickUpTime = pickUpTime;
        this.deliveryTime = deliveryTime;
        this.description = description;
        this.isShipped = false;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public boolean isShipped() {
        return isShipped;
    }

    public void setShipped() {
        isShipped = !isShipped;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public LocalDateTime getPickUpTime() {
        return pickUpTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(deliveryCode, delivery.deliveryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deliveryCode);
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryType=" + deliveryType +
                ", deliveryTime=" + deliveryTime +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public void applyNew(DeliveryDTO editedData) {
        if (editedData.getName() != null)
            if (!editedData.getName().isEmpty())
                this.deliveryCode = editedData.getName();
        if (editedData.getDeliveryType() != null)
            if (!editedData.getDeliveryType().isEmpty())
                this.deliveryType = Delivery.DeliveryType.valueOf(editedData.getDeliveryType());
        if (editedData.getDeliveryTime() != null)
            this.deliveryTime = editedData.getDeliveryTime();
        if (editedData.getPickUpTime() != null)
            this.pickUpTime = editedData.getPickUpTime();
        if (editedData.getDescription() != null)
            if (!editedData.getDescription().isEmpty())
                this.description = editedData.getDescription();
    }

    public enum DeliveryType {
        NOT_SPECIFIED,
        CARRIER,
        SHIPPING,
        OWN,
        CLIENT

    }
}
