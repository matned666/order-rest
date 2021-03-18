package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
