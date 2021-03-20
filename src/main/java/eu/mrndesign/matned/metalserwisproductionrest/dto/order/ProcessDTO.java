package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.BaseDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Process;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ProcessDTO extends BaseDTO implements DTOEntityDescriptionImplementation {

    public static ProcessDTO apply(Process p) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
            return applyWithAudit(p);
        else return applyWithoutAudit(p);
    }

    private static ProcessDTO applyWithoutAudit(Process p) {
        return new ProcessDTO(p.getProcessName(), p.getDescription());
    }

    private static ProcessDTO applyWithAudit(Process p) {
        ProcessDTO dto = apply(p);
        dto.auditDTO = AuditInterface.apply(p);
        return dto;
    }


    private String processName;
    private String description;

    public ProcessDTO(String processName, String description) {
        this.processName = processName;
        this.description = description;
    }


    @Override
    public String getName() {
        return processName;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
