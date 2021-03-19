package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("select c from ClientEntity c where lower(c.clientName) = lower(?1) ")
    Optional<ClientEntity> findByClientName(String clientName);
}
