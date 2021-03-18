package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.order.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProcessRepository extends JpaRepository<Process, Long> {

    @Query("select p from Process p where lower(p.processName) = lower(?1)")
    Optional<Process> findByProcessName(String processName);

    @Query("select case when count(p)> 0 then true else false end from Process p where lower(p.processName) like lower(?1)")
    boolean existsByName(String process);
}
