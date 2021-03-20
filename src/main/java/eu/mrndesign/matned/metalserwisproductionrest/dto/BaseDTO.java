package eu.mrndesign.matned.metalserwisproductionrest.dto;

import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;

public class BaseDTO {

    protected AuditDTO auditDTO;

    public BaseDTO() {
        auditDTO = new AuditDTO();
    }
}
