package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.BaseDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;

import java.time.LocalDateTime;

public class DeliveryDTO extends BaseDTO implements DTOEntityDescriptionImplementation {

    public static DeliveryDTO apply(Delivery entity){
        DeliveryDTO dto = new DeliveryDTO(
                entity.getDeliveryCode(),
                entity.getDeliveryType().name(),
                entity.getPickUpTime(),
                entity.getDeliveryTime(),
                entity.getDescription());
        dto.setShipped(entity.isShipped());
        return dto;
    }

    public static DeliveryDTO applyWithAudit(Delivery entity){
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
}
