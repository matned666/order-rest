package eu.mrndesign.matned.metalserwisproductionrest.model.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ClientDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "CLIENT_ENTITY")
public class ClientEntity extends BaseEntity<ClientDTO> implements AuditInterface {

    private String clientName;
    private String clientDescription;


    public ClientEntity() {
    }

    public ClientEntity(String clientName, String clientDescription) {
        this.clientName = clientName;
        this.clientDescription = clientDescription;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientDescription() {
        return clientDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientEntity that = (ClientEntity) o;
        return Objects.equals(clientName, that.clientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientName);
    }

    @Override
    public void applyNew(ClientDTO editedData) {
        this.clientName = editedData.getName();
        this.clientDescription = editedData.getDescription();
    }
}
