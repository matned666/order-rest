package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query("select c from ClientEntity c where lower(c.clientName) = lower(?1) ")
    Optional<ClientEntity> findByClientName(String clientName);

    @Query("select case when count(c)> 0 then true else false end from ClientEntity c where lower(c.clientName) like lower(?1)")
    boolean existsByClientName(String name);

    @Query("select c from ClientEntity c join Order o on o.client.id = c.id where o.isActive = true")
    Page<ClientEntity> findAllWithActiveOrders(Pageable pageable);
}
