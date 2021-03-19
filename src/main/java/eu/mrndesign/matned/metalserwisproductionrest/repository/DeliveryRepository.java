package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from Delivery d where d.deliveryCode = ?1")
    Optional<Delivery> findByDeliveryCode(String deliveryCode);

}
