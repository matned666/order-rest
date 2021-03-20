package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.BaseDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ClientDTO  extends BaseDTO implements DTOEntityDescriptionImplementation {

    public static ClientDTO apply(ClientEntity c) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
            return applyWithAudit(c);
        else return applyWithoutAudit(c);
    }

    private static ClientDTO applyWithoutAudit(ClientEntity entity){
        return new ClientDTO(entity.getClientName(), entity.getClientDescription());
    }

    private static ClientDTO applyWithAudit(ClientEntity entity){
        ClientDTO dto = new ClientDTO(entity.getClientName(), entity.getClientDescription());
        dto.auditDTO = AuditInterface.apply(entity);
        return dto;
    }

    private String clientName;
    private String clientDescription;


    public ClientDTO(String clientName, String clientDescription) {
        this.clientName = clientName;
        this.clientDescription = clientDescription;
    }

    @Override
    public String getName() {
        return clientName;
    }

    @Override
    public String getDescription() {
        return clientDescription;
    }

    public AuditDTO getAuditDTO() {
        return auditDTO;
    }
}
