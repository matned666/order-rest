package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.BaseDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeliveryDTO extends BaseDTO implements DTOEntityDescriptionImplementation {

    public static DeliveryDTO apply(Delivery d) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
            return applyWithAudit(d);
        else return applyWithoutAudit(d);
    }

    private static DeliveryDTO applyWithoutAudit(Delivery entity){
        DeliveryDTO dto = new DeliveryDTO(
                entity.getDeliveryCode(),
                entity.getDeliveryType()!= null? entity.getDeliveryType().name() : null,
                entity.getPickUpTime(),
                entity.getDeliveryTime(),
                entity.getDescription());
        dto.setShipped(entity.isShipped());
        return dto;
    }

    private static DeliveryDTO applyWithAudit(Delivery entity){
        DeliveryDTO dto = apply(entity);
        dto.auditDTO = AuditInterface.apply(entity);
        return dto;
    }

    private String deliveryCode;
    private String deliveryType;
    private LocalDateTime pickUpTime;
    private LocalDateTime deliveryTime;
    private String description;
    private boolean isShipped;

    public DeliveryDTO(String deliveryCode, String description) {
        this.deliveryCode = deliveryCode;
        this.description = description;
    }

    public DeliveryDTO(String deliveryCode, String deliveryType, LocalDateTime pickUpTime, LocalDateTime deliveryTime, String description) {
        this.deliveryCode = deliveryCode;
        this.deliveryType = deliveryType;
        this.pickUpTime = pickUpTime;
        this.deliveryTime = deliveryTime;
        this.description = description;
        this.isShipped = false;
    }

    public void setShipped(boolean shipped) {
        isShipped = shipped;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public LocalDateTime getPickUpTime() {
        return pickUpTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public boolean isShipped() {
        return isShipped;
    }

    @Override
    public String getName() {
        return deliveryCode;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryDTO that = (DeliveryDTO) o;
        return Objects.equals(deliveryCode, that.deliveryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryCode);
    }
}
