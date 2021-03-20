package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.BaseDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Process;

public class ProcessDTO extends BaseDTO implements DTOEntityDescriptionImplementation {

    public static ProcessDTO apply(Process p) {
        return new ProcessDTO(p.getProcessName(), p.getDescription());
    }

    public static ProcessDTO applyWithAudit(Process p){
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
