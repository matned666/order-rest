package eu.mrndesign.matned.metalserwisproductionrest.model.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ProcessDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Objects;

@Entity()
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PROCESS")
public class Process extends BaseEntity<ProcessDTO> implements AuditInterface {

    private String processName;
    private String description;

    public Process() {
    }

    public Process(String processName, String description) {
        this.processName = processName;
        this.description = description;
    }

    public String getProcessName() {
        return processName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Process process = (Process) o;
        return Objects.equals(processName, process.processName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), processName);
    }

    @Override
    public void applyNew(ProcessDTO editedData) {
        if (editedData.getName() != null)
            if (!editedData.getName().isEmpty())
                this.processName = editedData.getName();
        if (editedData.getDescription() != null)
            if (!editedData.getDescription().isEmpty())
                this.description = editedData.getDescription();
    }
}
