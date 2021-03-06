package eu.mrndesign.matned.metalserwisproductionrest.model.audit;

import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;
import java.util.Optional;

import static eu.mrndesign.matned.metalserwisproductionrest.Patterns.DATE_TIME_FORMATTER;

public interface AuditInterface {

    Long getId();
    Long getVersion();
    Optional<User> getCreatedBy();
    Optional<LocalDateTime> getCreatedDate();
    Optional<User> getLastModifiedBy();
    Optional<LocalDateTime> getLastModifiedDate();


    static AuditDTO apply(AuditInterface entity){
        return new AuditDTO.AuditBuilder()
                .id(entity.getId())
                .version(entity.getVersion())
                .createdById(entity.getCreatedBy().map(AbstractPersistable::getId).orElse(0L))
                .createdDate(entity.getCreatedDate().map(x->x.format(DATE_TIME_FORMATTER)).orElse(LocalDateTime.now().format(DATE_TIME_FORMATTER)))
                .lastModifiedById(entity.getLastModifiedBy().map(AbstractPersistable::getId).orElse(0L))
                .lastModifiedDate(entity.getLastModifiedDate().map(x->x.format(DATE_TIME_FORMATTER)).orElse(LocalDateTime.now().format(DATE_TIME_FORMATTER)))
                .build();
    }

}

