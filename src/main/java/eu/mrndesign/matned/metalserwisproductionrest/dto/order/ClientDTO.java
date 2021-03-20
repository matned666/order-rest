package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.BaseDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;

public class ClientDTO  extends BaseDTO implements DTOEntityDescriptionImplementation {

    public static ClientDTO apply(ClientEntity entity){
        return new ClientDTO(entity.getClientName(), entity.getClientDescription());
    }

    public static ClientDTO applyWithAudit(ClientEntity entity){
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
